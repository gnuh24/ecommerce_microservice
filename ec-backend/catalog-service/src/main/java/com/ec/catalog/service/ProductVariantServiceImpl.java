package com.ec.catalog.service;

import com.ec.catalog.entity.ProductVariant;
import com.ec.catalog.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Override
	public List<ProductVariant> getVariantsByProductId(String productId) {
		return productVariantRepository.findByProductIdAndIsDeletedFalse(productId);
	}
	
	@Override
	public BigDecimal getMinPriceByProductId(String productId) {
		return productVariantRepository.findMinPriceByProductId(productId);
	}
	
	@Override
	public BigDecimal getMaxPriceByProductId(String productId) {
		return productVariantRepository.findMaxPriceByProductId(productId);
	}
	
	@Override
	public ProductVariant getThumbnailVariant(String productId) {
		// Optional: Chỉ nếu bạn có trường thumbnail trong variant
		return null;
	}
}
