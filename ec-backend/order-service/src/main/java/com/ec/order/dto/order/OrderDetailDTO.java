package com.ec.order.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
	private String productVariantId;
	private String productName;
	private String productThumbnail;
	private Integer productVolume;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal totalPrice;
	
	public static OrderDetailDTO fromEntity(com.ec.order.entity.OrderDetail entity) {
		if (entity == null) return null;
		
		return OrderDetailDTO.builder()
		    .productVariantId(entity.getProductVariantId())
		    .productName(entity.getProductName())
		    .productThumbnail(entity.getProductThumbnail())
		    .productVolume(entity.getProductVolume())
		    .unitPrice(entity.getUnitPrice())
		    .quantity(entity.getQuantity())
		    .totalPrice(entity.getTotalPrice())
		    .build();
	}
	
}
