package com.ec.catalog.service;

import com.ec.catalog.dto.category.CategoryCreateForm;
import com.ec.catalog.dto.category.CategoryUpdateForm;
import com.ec.catalog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
	
    List<Category> getAllCategoryNoPaging();

//    Page<Category> getAllCategory(Pageable pageable, String search);
//
//    Category getCategoryById(Integer id);
//
//    Category createCategory(CategoryCreateForm form) throws Exception;
//
//    Category updateCategory(Integer id, CategoryUpdateForm form) throws Exception;
//
//    void deleteCategory(Integer categoryId) throws Exception;
    
}
