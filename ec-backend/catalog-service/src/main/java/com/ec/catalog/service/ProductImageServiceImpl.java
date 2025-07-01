package com.ec.catalog.service;

import com.ec.catalog.entity.ProductImage;
import com.ec.catalog.repository.ProductImageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Override
	public List<ProductImage> getAllByProductId(String productId) {
		return productImageRepository.findByProductIdAndIsDeletedFalse(productId);
	}
	
	@Override
	public Optional<ProductImage> getThumbnailByProductId(String productId) {
		Optional<ProductImage> optionalThumbnail = productImageRepository.findByProductIdAndIsDeletedFalse(productId)
		    .stream()
		    .filter(ProductImage::getIsThumbnail)
		    .findFirst();
		
		if (optionalThumbnail.isEmpty()) {
			throw new EntityNotFoundException("Không tìm thấy thumbnail cho productId: " + productId);
		}
		
		return optionalThumbnail;
	}

	

	
}
