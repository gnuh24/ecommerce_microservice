// ProductService.java
package com.ec.catalog.service;

import com.ec.catalog.dto.product.ProductCreateForm;
import com.ec.catalog.dto.product.ProductFilterForm;
import com.ec.catalog.dto.product.ProductUpdateForm;
import com.ec.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
	
	Page<Product> getAllProduct(Pageable pageable, String search, ProductFilterForm form);
	
	Product getProductById(String productId);
	
	Product getProductBySlug(String slug);
	
	Page<Product> filterProductsForAdmin(String search, Boolean isPublished, String categoryId, String brandId, Pageable pageable);
	Product createProduct(ProductCreateForm form);
	Product updateProduct(String productId, ProductUpdateForm form);

	
}