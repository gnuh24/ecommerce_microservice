package com.ec.order.service;

import com.ec.order.dto.order.CheckoutRequest;
import com.ec.order.dto.order.OrderAdminListDto;
import com.ec.order.dto.order.VnPayPaymentCreateForm;
import com.ec.order.entity.Order;
import com.ec.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderService {
	Page<Order> getMyOrders(String accountId, Pageable pageable);
	
	Order getOrderDetailById(String orderId, String accountId);
	
	Order getOrderDetailById(String orderId);
	
	Page<OrderAdminListDto> getAllOrders(
	    String keyword,
	    String status,
	    String paymentMethod,
	    LocalDate fromDate,
	    LocalDate toDate,
	    Pageable pageable
	);
	
	String createOrderWithCOD(String accountId, CheckoutRequest request);
	
	Order createOrderAndGenerateVnPayUrl(String accountId, CheckoutRequest request);
	
	Order processVnPayReturn(VnPayPaymentCreateForm form);
	
	void updateOrderStatus(String orderId, OrderStatus.OrderStatusEnum newStatus);
	void cancelOrder(String orderId);
}
