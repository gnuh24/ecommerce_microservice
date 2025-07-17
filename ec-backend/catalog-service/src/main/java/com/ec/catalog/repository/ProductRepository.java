package com.ec.catalog.repository;

import com.ec.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
	
	Optional<Product> findBySlugAndIsDeletedFalseAndIsPublishedTrue(String slug);
	boolean existsByProductNameIgnoreCaseAndIsDeletedFalse(String productName);
	boolean existsBySlug(String slug);
	List<Product> findByBrandId(String brandId);
	List<Product> findByCategoryId(String categoryId);
}
