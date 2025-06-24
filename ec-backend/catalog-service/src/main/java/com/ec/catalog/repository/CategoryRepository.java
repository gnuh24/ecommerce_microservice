package com.ec.catalog.repository;

import com.ec.catalog.entity.Brand;
import com.ec.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,  Integer>, JpaSpecificationExecutor<Category> {
    /**
     * Finds a category by its name.
     *
     * @param categoryName the name of the category to find.
     * @return an Optional containing the Category if found, otherwise empty.
     */
    Optional<Category> findByCategoryName(String categoryName);
	
	List<Category> findAllByIsDeletedFalse();
	
}

