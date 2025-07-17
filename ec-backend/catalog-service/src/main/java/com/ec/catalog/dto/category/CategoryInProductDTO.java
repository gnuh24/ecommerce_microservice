package com.ec.catalog.dto.category;

import com.ec.catalog.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryInProductDTO {
	
	private String id;
	private String categoryName;
	
	public static CategoryInProductDTO fromEntity(Category category) {
		if (category == null) return null;
		CategoryInProductDTO dto = new CategoryInProductDTO();
		dto.setId(category.getId());
		dto.setCategoryName(category.getCategoryName());
		return dto;
	}
}
