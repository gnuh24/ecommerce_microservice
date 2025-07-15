package com.ec.order.dto.order;

import com.ec.order.entity.OrderStatus.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    private OrderStatusEnum status;
    private LocalDateTime updateTime;
	
	public static OrderStatusDTO fromEntity(com.ec.order.entity.OrderStatus entity) {
		if (entity == null) return null;
		
		return OrderStatusDTO.builder()
		    .status(entity.getStatus())
		    .updateTime(entity.getUpdateTime())
		    .build();
	}
	
}
