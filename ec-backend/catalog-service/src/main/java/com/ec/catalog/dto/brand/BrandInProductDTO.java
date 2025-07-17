package com.ec.catalog.dto.brand;

import com.ec.catalog.entity.Brand;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandInProductDTO {
	
	private String id;
	private String brandName;
	
	public static BrandInProductDTO fromEntity(Brand brand) {
		if (brand == null) return null;
		BrandInProductDTO dto = new BrandInProductDTO();
		dto.setId(brand.getId());
		dto.setBrandName(brand.getBrandName());
		return dto;
	}
}
