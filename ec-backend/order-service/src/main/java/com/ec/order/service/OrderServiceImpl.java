package com.ec.order.service;

import com.ec.order.client.CatalogClient;
import com.ec.order.dto.order.*;
import com.ec.order.entity.*;
import com.ec.order.entity.OrderStatus.OrderStatusEnum;
import com.ec.order.exceptions.business.order.*;
import com.ec.order.exceptions.business.payment.PaymentNotFoundException;
import com.ec.order.integration.redis.RedisService;
import com.ec.order.repository.OrderRepository;
import com.ec.order.repository.OrderStatusRepository;
import com.ec.order.repository.PaymentRepository;
import com.ec.order.repository.VnPayPaymentRepository;
import com.ec.order.specification.OrderSpecification;
import com.ec.order.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CatalogClient catalogClient;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private VnPayPaymentRepository vnPayPaymentRepository;
	
	@Autowired
	private OrderStatusRepository orderStatusRepository;
	
	@Override
	public Page<Order> getMyOrders(String accountId, Pageable pageable) {
		return orderRepository.findByAccountIdAndIsTempFalse(accountId, pageable);
	}
	
	@Override
	public Order getOrderDetailById(String orderId, String accountId) {
		return orderRepository.findByIdAndAccountId(orderId, accountId)
		    .orElseThrow(() -> new OrderNotFoundException(orderId));
	}
	
	@Override
	public Order getOrderDetailById(String orderId) {
		return orderRepository.findById(orderId)
		    .orElseThrow(() -> new OrderNotFoundException(orderId));
	}
	
	@Override
	public Page<OrderAdminListDto> getAllOrders(
	    String keyword,
	    String status,
	    String paymentMethod,
	    LocalDate fromDate,
	    LocalDate toDate,
	    Pageable pageable) {
		Specification<Order> spec = Specification.where(null);
		
		if (keyword != null && !keyword.isBlank()) {
			spec = spec.and(OrderSpecification.keywordMatch(keyword));
		}
		
		if (status != null) {
			spec = spec.and(OrderSpecification.hasLatestStatus(status));
		}
		
		if (paymentMethod != null) {
			spec = spec.and(OrderSpecification.hasPaymentMethod(paymentMethod));
		}
		
		if (fromDate != null) {
			spec = spec.and(OrderSpecification.fromDate(fromDate));
		}
		
		if (toDate != null) {
			spec = spec.and(OrderSpecification.toDate(toDate));
		}
		
		Page<Order> orders = orderRepository.findAll(spec, pageable);
		return orders.map(OrderAdminListDto::fromEntity);
	}
	
	
	@Override
	@Transactional
	public String createOrderWithCOD(String accountId, CheckoutRequest request) {
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
			    .productVariantId(variantId)
			    .productName(variant.getProductName())
			    .productThumbnail(variant.getThumbnail())
			    .productVolume(variant.getVolume())
			    .unitPrice(variant.getUnitPrice())
			    .quantity(quantity)
			    .totalPrice(total)
			    .build();
			
			orderDetails.add(detail);
		}
		
		// 5. Tạo Order entity
		Order order = Order.builder()
		    .accountId(accountId)
		    .receiverName(request.getReceiverName())
		    .receiverPhone(request.getReceiverPhone())
		    .receiverAddress(request.getReceiverAddress())
		    .note(request.getNote())
		    .orderTime(LocalDateTime.now())
		    .totalAmount(totalAmount)
		    .isTemp(false)
		    .build();
		
		// 6. Gắn order vào các chi tiết
		orderDetails.forEach(detail -> detail.setOrder(order));
		order.setOrderDetails(orderDetails);
		
		// 7. Tạo Payment (OneToOne)
		Payment payment = Payment.builder()
		    .id(IdGenerator.generateId()) // nếu bạn cần ID riêng
		    .paymentMethod(Payment.PaymentMethod.COD)
		    .paymentStatus(Payment.PaymentStatus.PENDING)
		    .order(order)
		    .build();
		order.setPayment(payment); // OneToOne
		
		// 8. Tạo OrderStatus
		OrderStatus status = OrderStatus.builder()
		    .status(OrderStatus.OrderStatusEnum.PENDING)
		    .order(order)
		    .build();
		order.setStatuses(List.of(status));
		
		// 9. Lưu tất cả vào DB (Cascade sẽ tự lưu orderDetails, payment, statuses)
		orderRepository.save(order);
		
		// 10. Gọi catalogClient giảm tồn kho
		catalogClient.reduceQuantities(rawItems);
		
		return order.getId();
	}
	
	
	@Override
	@Transactional
	public Order createOrderAndGenerateVnPayUrl(String accountId, CheckoutRequest request) {
		// 1. Tính tổng tiền
		BigDecimal totalAmount = calculateTotalAmount(request.getProductVariantList());
		String orderId = IdGenerator.generateId();
		
		// 2. Lấy thông tin biến thể sản phẩm từ Catalog Service
		List<ProductVariantForOrderDTO> variants = catalogClient.getVariantDetails(request.getProductVariantList());
		Map<String, Integer> quantityMap = request.getProductVariantList().stream()
		    .collect(Collectors.toMap(CheckoutItem::getProductVariantId, CheckoutItem::getQuantity));
		
		// 3. Tạo chi tiết đơn hàng
		List<OrderDetail> orderDetails = variants.stream().map(variant -> {
			int quantity = quantityMap.get(variant.getProductVariantId());
			BigDecimal total = variant.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
			return OrderDetail.builder()
			    .productVariantId(variant.getProductVariantId())
			    .productName(variant.getProductName())
			    .productThumbnail(variant.getThumbnail())
			    .productVolume(variant.getVolume())
			    .unitPrice(variant.getUnitPrice())
			    .quantity(quantity)
			    .totalPrice(total)
			    .order(null) // gán sau
			    .build();
		}).toList();
		
		// 4. Tạo Order entity
		Order order = Order.builder()
		    .id(orderId)
		    .accountId(accountId)
		    .note(request.getNote())
		    .receiverName(request.getReceiverName())
		    .receiverPhone(request.getReceiverPhone())
		    .receiverAddress(request.getReceiverAddress())
		    .totalAmount(totalAmount)
		    .isTemp(true) // đơn hàng tạm thời
		    .orderTime(LocalDateTime.now())
		    .build();
		
		// 5. Gắn chi tiết vào đơn hàng
		orderDetails.forEach(detail -> detail.setOrder(order));
		order.setOrderDetails(orderDetails);
		
		// 6. Tạo Payment ban đầu
		Payment payment = Payment.builder()
		    .id(IdGenerator.generateId()) // nếu bạn cần ID riêng
		    .paymentMethod(Payment.PaymentMethod.VNPAY)
		    .paymentStatus(Payment.PaymentStatus.PENDING)
		    .order(order)
		    .build();
		
		// 7. Gắn payment vào order (OneToOne)
		order.setPayment(payment);
		
		// 8. Lưu vào DB (cascade tự lưu hết)
		orderRepository.save(order);
		
		return order;
	}
	
	
	@Override
	@Transactional
	public Order processVnPayReturn(VnPayPaymentCreateForm form) {
		// 1. Tìm Order
		Order order = orderRepository.findById(form.getOrderId())
		    .orElseThrow(() -> new OrderNotFoundException("Order không tồn tại"));
		
		// 2. Tìm Payment liên kết với đơn hàng
		Payment payment = paymentRepository.findByOrder(order)
		    .orElseThrow(() -> new PaymentNotFoundException("Payment không tồn tại"));
		
		// 3. Cập nhật trạng thái thanh toán
		payment.setPaymentStatus(form.getPaymentStatus());
		paymentRepository.save(payment);
		
		// 4. Tạo bản ghi VnPayPayment (phiên bản mới dùng ENUM và ghi rõ description)
		VnPayPayment vnPayPayment = VnPayPayment.builder()
		    .payment(payment)
		    .vnpResponseCode(form.getVnpResponseCode())
		    .vnpTransactionStatusCode(form.getVnpTransactionStatusCode())
		    .transactionId(form.getTransactionId())
		    .paymentTime(form.getPaymentTime())
		    .vnpSecureHash(form.getVnpSecureHash())
		    .bankCode(form.getBankCode())
		    .cardType(form.getCardType())
		    .vnpResponseStatus(form.getVnpResponseStatus())
		    .vnpTransactionStatus(form.getVnpTransactionStatus())
		    .build();
		
		vnPayPaymentRepository.save(vnPayPayment);
		
		// 5. Nếu thanh toán thành công
		if (form.getPaymentStatus() == Payment.PaymentStatus.SUCCESS) {
			// 5.1 Thêm trạng thái PROCESSING vào OrderStatus
			OrderStatus status = OrderStatus.builder()
			    .order(order)
			    .status(OrderStatus.OrderStatusEnum.PROCESSING)
			    .updateTime(LocalDateTime.now())
			    .build();
			orderStatusRepository.save(status);
			
			// 5.2 Tạo danh sách CheckoutItem để trừ tồn kho
			List<CheckoutItem> checkoutItems = order.getOrderDetails().stream()
			    .map(detail -> CheckoutItem.builder()
				.productVariantId(detail.getProductVariantId())
				.quantity(detail.getQuantity())
				.build())
			    .collect(Collectors.toList());
			
			// 5.3 Gọi sang Catalog để trừ tồn kho
			catalogClient.reduceQuantities(checkoutItems);
			
			// 5.4 Xóa cờ đơn hàng tạm
			order.setTemp(false);
			orderRepository.save(order);
		}
		
		return order;
	}
	
	@Override
	@Transactional
	public void updateOrderStatus(String orderId, OrderStatusEnum newStatus) {
		Order order = this.getOrderDetailById(orderId);
		
		OrderStatusEnum currentStatus = order.getStatuses().stream()
		    .max(Comparator.comparing(OrderStatus::getUpdateTime))
		    .map(OrderStatus::getStatus)
		    .orElseThrow(() -> new DataCorruptionException(orderId));
		
		List<OrderStatusEnum> allowedNext = allowedTransitions.getOrDefault(currentStatus, List.of());
		if (!allowedNext.contains(newStatus)) {
			throw new OrderStatusTransitionNotAllowedException(currentStatus.name(), newStatus.name());
		}
		
		OrderStatus newOrderStatus = OrderStatus.builder()
		    .status(newStatus)
		    .order(order)
		    .build();
		
		order.getStatuses().add(newOrderStatus);
		orderRepository.save(order);
	}
	
	@Override
	@Transactional
	public void cancelOrder(String orderId) {
		Order order = getOrderDetailById(orderId);
		
		OrderStatusEnum currentStatus = order.getStatuses().stream()
		    .max(Comparator.comparing(OrderStatus::getUpdateTime))
		    .map(OrderStatus::getStatus)
		    .orElseThrow(() -> new DataCorruptionException(orderId));
		
		if (!(currentStatus == OrderStatusEnum.PENDING || currentStatus == OrderStatusEnum.PROCESSING)) {
			throw new OrderCannotBeCancelledException(currentStatus.name());
		}
		
		// ✅ Chuyển OrderDetail -> List<CheckoutItem> để gọi tăng số lượng
		List<CheckoutItem> restoreItems = order.getOrderDetails().stream()
		    .map(detail -> CheckoutItem.builder()
			.productVariantId(detail.getProductVariantId())
			.quantity(detail.getQuantity())
			.build())
		    .toList();
		
		
		// ✅ Gọi CatalogClient để tăng lại số lượng
		catalogClient.increaseQuantities(restoreItems);
		
		// ✅ Thêm trạng thái hủy
		OrderStatus cancelStatus = OrderStatus.builder()
		    .order(order)
		    .status(OrderStatusEnum.CANCELED)
		    .build();
		
		order.getStatuses().add(cancelStatus);
		orderRepository.save(order);
	}
	
	
	private BigDecimal calculateTotalAmount(List<CheckoutItem> items) {
		List<ProductVariantForOrderDTO> variants = catalogClient.getVariantDetails(items);
		Map<String, Integer> quantityMap = items.stream()
		    .collect(Collectors.toMap(CheckoutItem::getProductVariantId, CheckoutItem::getQuantity));
		
		return variants.stream()
		    .map(variant -> variant.getUnitPrice().multiply(BigDecimal.valueOf(quantityMap.get(variant.getProductVariantId()))))
		    .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	
	private static final Map<OrderStatus.OrderStatusEnum, List<OrderStatus.OrderStatusEnum>> allowedTransitions = Map.of(
	    OrderStatusEnum.PENDING, List.of(OrderStatusEnum.PROCESSING, OrderStatusEnum.CANCELED),
	    OrderStatusEnum.PROCESSING, List.of(OrderStatusEnum.SHIPPING, OrderStatusEnum.CANCELED),
	    OrderStatusEnum.SHIPPING, List.of(OrderStatusEnum.COMPLETE)
	);
	
	
}
