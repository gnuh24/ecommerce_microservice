// ProductService.java
package com.ec.catalog.service;

import com.ec.catalog.dto.product.ProductFilterForm;
import com.ec.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
	
	Page<Product> getAllProduct(Pageable pageable, String search, ProductFilterForm form);
	
//	Product getProductById(Integer productId);
//
//	List<Product> getProductsByIds(List<Integer> ids);
	
//	int updateDefaultBrandOfProduct(Integer brandId);
//
//	int updateDefaultCategoryOfProduct(Integer categoryId);
//
//	Product createProduct(ProductCreateForm form);
//
//	Product updateProduct(Integer productId, ProductUpdateForm form);
	
}