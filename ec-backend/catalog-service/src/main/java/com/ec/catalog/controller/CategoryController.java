package com.ec.catalog.controller;

import com.ec.catalog.api.ApiResponse;
import com.ec.catalog.dto.category.CategoryCreateForm;
import com.ec.catalog.dto.category.CategoryResponseDTO;
import com.ec.catalog.dto.category.CategoryUpdateForm;
import com.ec.catalog.entity.Category;
import com.ec.catalog.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value = "/no-paging")
	public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategoryNoPaging() {
		List<Category> list = categoryService.getAllCategoryNoPaging();
		List<CategoryResponseDTO> dtos = modelMapper.map(list, new TypeToken<List<CategoryResponseDTO>>() {
		}.getType());
		
		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dtos));
	}
	
}
