package com.ec.order.service;

import com.ec.order.dto.order.CheckoutCODRequest;
import com.ec.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
	Page<Order> getMyOrders(String accountId, Pageable pageable);
	Order getOrderDetailById(String orderId, String accountId);
	String createOrderWithCOD(String accountId, CheckoutCODRequest request);
	
}
