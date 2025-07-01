package com.ec.catalog.service;

import java.math.BigDecimal;
import java.util.List;

import com.ec.catalog.entity.ProductVariant;

public interface ProductVariantService {
	
	List<ProductVariant> getVariantsByIds(List<String> productIds);
	
	List<ProductVariant> getVariantsByProductId(String productId);
	
	BigDecimal getMinPriceByProductId(String productId);
	
	BigDecimal getMaxPriceByProductId(String productId);
}
