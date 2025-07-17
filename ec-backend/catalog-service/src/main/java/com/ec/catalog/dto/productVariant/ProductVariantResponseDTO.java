package com.ec.catalog.dto.productVariant;

import com.ec.catalog.entity.ProductVariant;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantResponseDTO {
	private String id;
	private Integer volume;
	private BigDecimal price;
	
	public static ProductVariantResponseDTO fromEntity(ProductVariant variant) {
		if (variant == null) return null;
		ProductVariantResponseDTO dto = new ProductVariantResponseDTO();
		dto.setId(variant.getId());
		dto.setVolume(variant.getVolume());
		dto.setPrice(variant.getPrice());
		return dto;
	}
}
