package com.ec.order.service;

import com.ec.order.client.CatalogClient;
import com.ec.order.dto.order.CheckoutCODRequest;
import com.ec.order.dto.order.CheckoutItem;
import com.ec.order.dto.order.ProductVariantForOrderDTO;
import com.ec.order.entity.Order;
import com.ec.order.entity.OrderDetail;
import com.ec.order.entity.OrderStatus;
import com.ec.order.entity.Payment;
import com.ec.order.exceptions.business.order.OrderNotFoundException;
import com.ec.order.exceptions.business.order.OrderOutOfStockException;
import com.ec.order.repository.OrderRepository;
import com.ec.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CatalogClient catalogClient;
	
	@Override
	public Page<Order> getMyOrders(String accountId, Pageable pageable) {
		return orderRepository.findByAccountId(accountId, pageable);
	}
	
	@Override
	public Order getOrderDetailById(String orderId, String accountId) {
		return orderRepository.findByIdAndAccountId(orderId, accountId)
		    .orElseThrow(() -> new OrderNotFoundException(orderId));
	}
	
	@Override
	@Transactional
	public String createOrderWithCOD(String accountId, CheckoutCODRequest request) {
		// 1. Chuẩn hóa input
		List<CheckoutItem> rawItems = request.getProductVariantList();
		
		// 2. Lấy thông tin variant
		List<ProductVariantForOrderDTO> variantInfos = catalogClient.getVariantDetails(rawItems);
		// 3. Map số lượng từ client
		Map<String, Integer> quantityMap = rawItems.stream()
		    .collect(Collectors.toMap(CheckoutItem::getProductVariantId, CheckoutItem::getQuantity));
		
		// 4. Tính tiền & validate kho
		List<OrderDetail> orderDetails = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (ProductVariantForOrderDTO variant : variantInfos) {
			String variantId = variant.getProductVariantId();
			int quantity = quantityMap.get(variantId);
			if (quantity > variant.getQuantity()) {
				throw new OrderOutOfStockException(variant.getProductName(), variant.getQuantity(), quantity);
			}
			
			BigDecimal total = variant.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
			totalAmount = totalAmount.add(total);
			
			OrderDetail detail = OrderDetail.builder()
			    .productVariantId(variant.getProductVariantId())
			    .productName(variant.getProductName())
			    .productThumbnail(variant.getThumbnail())
			    .productVolume(variant.getVolume())
			    .unitPrice(variant.getUnitPrice())
			    .quantity(quantity)
			    .totalPrice(total)
			    .build();
			
			orderDetails.add(detail);
		}
		
		// 5–9. Tạo Order, Payment, Status, save DB, giảm tồn kho (giữ nguyên logic cũ)
		Order order = Order.builder()
		    .accountId(accountId)
		    .receiverName(request.getReceiverName())
		    .receiverPhone(request.getReceiverPhone())
		    .receiverAddress(request.getReceiverAddress())
		    .note(request.getNote())
		    .orderTime(LocalDateTime.now())
		    .totalAmount(totalAmount)
		    .orderDetails(orderDetails)
		    .build();
		
		orderDetails.forEach(detail -> detail.setOrder(order));
		
		Payment payment = Payment.builder()
		    .paymentMethod(Payment.PaymentMethod.COD)
		    .paymentStatus(Payment.PaymentStatus.PENDING)
		    .order(order)
		    .build();
		
		order.setPayments(List.of(payment));
		
		OrderStatus status = OrderStatus.builder()
		    .status(OrderStatus.OrderStatusEnum.PENDING)
		    .order(order)
		    .build();
		
		order.setStatuses(List.of(status));
		
		orderRepository.save(order);
		
		catalogClient.reduceQuantities(rawItems);
		
		return order.getId();
	}
	
	
	
}
