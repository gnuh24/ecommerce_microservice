package com.ec.catalog.service;

import com.ec.catalog.dto.productVariant.QuantityReduceRequest;
import com.ec.catalog.entity.ProductVariant;
import com.ec.catalog.exceptions.business.product_variant.ProductVariantNotFound;
import com.ec.catalog.exceptions.business.product_variant.ProductVariantQuantityNotEnough;
import com.ec.catalog.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Override
	public List<ProductVariant> getVariantsByIds(List<String> variantIds) {
		return productVariantRepository.findByIdIn(variantIds);
	}
	
	@Override
	public List<ProductVariant> getVariantsByProductId(String productId) {
		return productVariantRepository.findByProductIdAndIsDeletedFalse(productId);
	}
	
	@Override
	@Transactional
	public void reduceQuantities(List<QuantityReduceRequest> requests) {
		for (QuantityReduceRequest req : requests) {
			ProductVariant variant = productVariantRepository.findById(req.getProductVariantId())
			    .orElseThrow(() -> new ProductVariantNotFound(req.getProductVariantId()));
			
			int available = variant.getQuantity();
			int requested = req.getQuantity();
			if (available < requested) {
				throw new ProductVariantQuantityNotEnough(req.getProductVariantId(), requested, available);
			}
			
			variant.setQuantity(available - requested);
			// Có thể bỏ dòng dưới nếu bạn dùng JPA + dirty checking
			productVariantRepository.save(variant);
		}
	}


	
	@Override
	public BigDecimal getMinPriceByProductId(String productId) {
		return productVariantRepository.findMinPriceByProductId(productId);
	}
	
	@Override
	public BigDecimal getMaxPriceByProductId(String productId) {
		return productVariantRepository.findMaxPriceByProductId(productId);
	}
	

}
