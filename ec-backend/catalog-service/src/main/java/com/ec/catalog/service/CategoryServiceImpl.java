package com.ec.catalog.service;

import com.ec.catalog.dto.category.CategoryCreateForm;
import com.ec.catalog.dto.category.CategoryUpdateForm;
import com.ec.catalog.entity.Category;
import com.ec.catalog.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    @Lazy
//    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Category> getAllCategoryNoPaging() {
	return categoryRepository.findAllByIsDeletedFalse();
    }

//    @Override
//    public Page<Category> getAllCategory(Pageable pageable, String search) {
//	Specification<Category> specification = CategorySpecification.buildWhere(search);
//	return categoryRepository.findAll(specification, pageable);
//    }
//
//    @Override
//    public Category getCategoryById(Integer id) {
//	return categoryRepository.findById(id)
//		.orElseThrow(() -> new EntityNotFoundException("Category ID: " + id + " không tồn tại!"));
//    }
//
//    @Override
//    public Category createCategory(CategoryCreateForm form) throws Exception {
//	validateCategoryName(form.getCategoryName(), null);
//
//	Category entity = modelMapper.map(form, Category.class);
//	return categoryRepository.save(entity);
//    }
//
//    @Override
//    public Category updateCategory(Integer id, CategoryUpdateForm form) throws Exception {
//	Category oldCategory = getCategoryById(id);
//
//	validateCategoryName(form.getCategoryName(), oldCategory.getId());
//
//	oldCategory.setCategoryName(form.getCategoryName());
//	return categoryRepository.save(oldCategory);
//    }
//
//    @Override
//    public void deleteCategory(Integer categoryId) {
//	getCategoryById(categoryId);
//	productService.updateDefaultCategoryOfProduct(categoryId);
//	categoryRepository.deleteById(categoryId);
//    }

//    /**
//     * Validates if the category name already exists.
//     * If an ID is provided, it ensures the name is not used by another category.
//     *
//     * @param categoryName the category name to validate
//     * @param currentId the ID of the current category (for updates)
//     * @throws Exception if the category name is invalid
//     */
//    private void validateCategoryName(String categoryName, Integer currentId) throws Exception {
//	Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryName);
//
//	if (existingCategory.isPresent() && !existingCategory.get().getId().equals(currentId)) {
//	    throw new Exception("Tên danh mục '" + categoryName + "' đã tồn tại, vui lòng chọn tên khác!");
//	}
//    }
}
