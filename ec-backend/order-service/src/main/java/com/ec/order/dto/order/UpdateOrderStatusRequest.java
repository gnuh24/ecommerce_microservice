package com.ec.order.dto.order;

import com.ec.order.entity.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    	private OrderStatus.OrderStatusEnum status;
}
