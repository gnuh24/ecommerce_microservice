package com.ec.order.service;

import com.ec.order.entity.Order;
import com.ec.order.repository.OrderRepository;
import com.ec.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@Override
	public Page<Order> getMyOrders(String accountId, Pageable pageable) {
		return orderRepository.findByAccountId(accountId, pageable);
	}
	
	@Override
	public Order getOrderDetailById(String orderId, String accountId) {
		return orderRepository.findByIdAndAccountId(orderId, accountId)
		    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));
		
	
	}
}
