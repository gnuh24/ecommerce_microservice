package com.ec.order.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ec.order.entity.Order;
import com.ec.order.entity.OrderStatus;
import com.ec.order.entity.Payment;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@Builder
public class OrderAdminListDto {
	private String id;
	
	private String receiverName;
	private String receiverPhone;
	private String receiverAddress;
	
	private BigDecimal totalAmount;
	private LocalDateTime orderTime;
	
	private String accountId;
	private String latestStatus;      // PENDING, PROCESSING, etc
	private String paymentMethod;     // COD, VNPAY, MOMO
	
	// ✅ Nhét trực tiếp fromEntity vào đây
	public static OrderAdminListDto fromEntity(Order order) {
		OrderStatus.OrderStatusEnum latestStatus = order.getStatuses().stream()
		    .max(Comparator.comparing(OrderStatus::getUpdateTime))
		    .map(OrderStatus::getStatus)
		    .orElse(null);
		
		String paymentMethod = String.valueOf(order.getPayment().getPaymentMethod());
		
		return OrderAdminListDto.builder()
		    .id(order.getId())
		    .receiverName(order.getReceiverName())
		    .receiverPhone(order.getReceiverPhone())
		    .receiverAddress(order.getReceiverAddress())
		    .totalAmount(order.getTotalAmount())
		    .orderTime(order.getOrderTime())
		    .accountId(order.getAccountId())
		    .latestStatus(String.valueOf(latestStatus))
		    .paymentMethod(paymentMethod)
		    .build();
	}
}

