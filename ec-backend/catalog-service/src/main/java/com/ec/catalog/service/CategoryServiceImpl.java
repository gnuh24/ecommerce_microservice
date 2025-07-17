package com.ec.catalog.service;

import com.ec.catalog.dto.category.CategoryCreateForm;
import com.ec.catalog.dto.category.CategoryUpdateForm;
import com.ec.catalog.entity.Category;
import com.ec.catalog.entity.Product;
import com.ec.catalog.exceptions.business.category.CannotDeleteDefaultCategoryException;
import com.ec.catalog.exceptions.business.category.CategoryAlreadyExistsException;
import com.ec.catalog.exceptions.business.category.CategoryNotFoundException;
import com.ec.catalog.repository.CategoryRepository;
import com.ec.catalog.repository.ProductRepository;
import com.ec.catalog.specification.CategorySpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	@Lazy
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<Category> getAllCategoryNoPaging() {
		return categoryRepository.findAllByIsDeletedFalse();
	}
	
	@Override
	public Page<Category> getAllCategories(Pageable pageable, String search) {
		return categoryRepository.findAll(CategorySpecification.searchByName(search), pageable);
	}
	
	
	@Override
	public Category getCategoryById(String id) {
		return categoryRepository.findByIdAndIsDeletedFalse(id)
		    .orElseThrow(() -> new CategoryNotFoundException(id));
	}
	
	@Override
	public Category createCategory(CategoryCreateForm form) {
		if (categoryRepository.existsByCategoryNameIgnoreCaseAndIsDeletedFalse(form.getCategoryName())) {
			throw new CategoryAlreadyExistsException(form.getCategoryName());
		}
		
		Category category = Category.builder()
		    .categoryName(form.getCategoryName().trim())
		    .build();
		
		return categoryRepository.save(category);
	}
	
	@Override
	public Category updateCategory(String categoryId, CategoryUpdateForm form) {
		Category category = this.getCategoryById(categoryId);
		
		String newName = form.getCategoryName().trim();
		
		if (!category.getCategoryName().equalsIgnoreCase(newName) &&
		    categoryRepository.existsByCategoryNameIgnoreCaseAndIsDeletedFalse(newName)) {
			throw new CategoryAlreadyExistsException(newName);
		}
		
		category.setCategoryName(newName);
		return categoryRepository.save(category);
	}
	
	
	@Override
	@Transactional
	public void deleteCategory(String categoryId) {
		if ("C001".equals(categoryId)) {
			throw new CannotDeleteDefaultCategoryException(categoryId);
		}
		
		Category category = this.getCategoryById(categoryId);
		
		category.setIsDeleted(true);
		category.setDeletedAt(LocalDateTime.now());
		categoryRepository.save(category);
		
		// Cập nhật toàn bộ Product có categoryId bị xóa sang C001
		List<Product> affectedProducts = productRepository.findByCategoryId(categoryId);
		Category defaultCategory = this.getCategoryById("C001");
		for (Product product : affectedProducts) {
			product.setCategory(defaultCategory);
		}
		productRepository.saveAll(affectedProducts);
	}
	
	
}
