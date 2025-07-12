package com.ec.order.dto.order;

import com.ec.order.entity.Order;
import com.ec.order.entity.OrderDetail;
import com.ec.order.entity.OrderStatus;
import com.ec.order.entity.Payment;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderAdminDetailDto {
	private String id;
	private String receiverName;
	private String receiverPhone;
	private String receiverAddress;
	private String note;
	private LocalDateTime orderTime;
	private BigDecimal totalAmount;
	private String accountId;
	
	private List<OrderDetailDTO> orderDetails;
	private PaymentDTO payment;
	private List<OrderStatusDTO> statuses;
	
	public static OrderAdminDetailDto fromEntity(Order order) {
		return OrderAdminDetailDto.builder()
		    .id(order.getId())
		    .receiverName(order.getReceiverName())
		    .receiverPhone(order.getReceiverPhone())
		    .receiverAddress(order.getReceiverAddress())
		    .note(order.getNote())
		    .totalAmount(order.getTotalAmount())
		    .orderTime(order.getOrderTime())
		    .accountId(order.getAccountId())
		    .orderDetails(order.getOrderDetails() != null
			? order.getOrderDetails().stream().map(OrderDetailDTO::fromEntity).collect(Collectors.toList())
			: null)
		    .payment(order.getPayment() != null
			? PaymentDTO.fromEntity(order.getPayment())
			: null)
		    .statuses(order.getStatuses() != null
			? order.getStatuses().stream().map(OrderStatusDTO::fromEntity).collect(Collectors.toList())
			: null)
		    .build();
	}
}
