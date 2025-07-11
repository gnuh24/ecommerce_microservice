package com.ec.catalog.controller;

import com.ec.catalog.api.ApiResponse;
import com.ec.catalog.dto.product.ProductCreateForm;
import com.ec.catalog.dto.product.ProductListDTOForAdmin;
import com.ec.catalog.dto.product.ProductDetailDTOForAdmin;
import com.ec.catalog.dto.product.ProductUpdateForm;
import com.ec.catalog.entity.Product;
import com.ec.catalog.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {
	
	@Autowired
	private ProductService productService;
	
	// === GET LIST PRODUCTS ===
	@GetMapping
	public ResponseEntity<ApiResponse<Page<ProductListDTOForAdmin>>> getAllProductsForAdmin(
	    @RequestParam(required = false) String search,
	    @RequestParam(required = false) Boolean isPublished,
	    @RequestParam(required = false) String categoryId,
	    @RequestParam(required = false) String brandId,
	    Pageable pageable
	) {
		Page<Product> entities = productService.filterProductsForAdmin(search, isPublished, categoryId, brandId, pageable);
		Page<ProductListDTOForAdmin> result = entities.map(ProductListDTOForAdmin::fromEntity);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách sản phẩm thành công", result));
	}
	
	// === GET DETAIL PRODUCT ===
	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductDetailDTOForAdmin>> getProductDetailForAdmin(
	    @PathVariable String productId
	) {
		Product product = productService.getProductById(productId);
		ProductDetailDTOForAdmin dto = ProductDetailDTOForAdmin.fromEntity(product);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy chi tiết sản phẩm thành công", dto));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ProductDetailDTOForAdmin>> createProduct(
	    @Valid @RequestBody ProductCreateForm form
	) {
		Product product = productService.createProduct(form);
		ProductDetailDTOForAdmin result = ProductDetailDTOForAdmin.fromEntity(product);
		return ResponseEntity.ok(new ApiResponse<>(200, "Tạo sản phẩm thành công", result));
	}
	
	@PatchMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductDetailDTOForAdmin>> updateProduct(
	    @PathVariable String productId,
	    @Valid @RequestBody ProductUpdateForm form
	) {
		Product updated = productService.updateProduct(productId, form);
		ProductDetailDTOForAdmin result = ProductDetailDTOForAdmin.fromEntity(updated);
		return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật sản phẩm thành công", result));
	}
	
}
