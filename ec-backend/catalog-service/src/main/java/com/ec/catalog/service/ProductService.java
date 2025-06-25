// ProductService.java
package com.ec.catalog.service;

import com.ec.catalog.dto.product.ProductFilterForm;
import com.ec.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
	
	Page<Product> getAllProduct(Pageable pageable, String search, ProductFilterForm form);
	
	Product getProductById(String productId);
	
	Product getProductBySlug(String slug);
	
//	List<Product> getProductsByIds(List<Integer> ids);
	
//	int updateDefaultBrandOfProduct(Integer brandId);
//
//	int updateDefaultCategoryOfProduct(Integer categoryId);
//
//	Product createProduct(ProductCreateForm form);
//
//	Product updateProduct(Integer productId, ProductUpdateForm form);
	
}