package com.ec.catalog.controller;

import com.ec.catalog.api.ApiResponse;
import com.ec.catalog.dto.brand.BrandInProductDTO;
import com.ec.catalog.dto.category.CategoryInProductDTO;
import com.ec.catalog.dto.product.ProductDetailPublicDTO;
import com.ec.catalog.dto.product.ProductFilterForm;
import com.ec.catalog.dto.product.ProductListPublicDTO;
import com.ec.catalog.dto.productImage.ProductImageResponseDTO;
import com.ec.catalog.dto.productVariant.ProductVariantResponseDTO;
import com.ec.catalog.entity.Account;
import com.ec.catalog.entity.Product;
import com.ec.catalog.entity.ProductImage;
import com.ec.catalog.service.ProductImageService;
import com.ec.catalog.service.ProductService;
import com.ec.catalog.service.ProductVariantService;
import com.ec.catalog.service.WishlistService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private ProductVariantService productVariantService;
	
	@Autowired
	private WishlistService wishlistService;
	

//	@GetMapping(value = "/management")
//	public ResponseEntity<ApiResponse<Page<ProductListManagementDTO>>> getAllProductsForManagement(
//	    Pageable pageable,
//	    @RequestParam(name = "search", required = false) String search,
//	    ProductFilterForm form) {
//
//		Page<Product> entities = productService.getAllProduct(pageable, search, form);
//		List<ProductListManagementDTO> dtos = modelMapper.map(
//		    entities.getContent(), new TypeToken<List<ProductListManagementDTO>>() {
//		    }.getType()
//		);
//
//		for (ProductListManagementDTO dto : dtos) {
//			StockLot stockLot = stockLotService.getTheValidStockLot(dto.getId());
//
//			if (stockLot == null) {
//				stockLot = stockLotService.getTheValidStockLotBackup(dto.getId());
//			}
//
//			if (stockLot != null) {
//				dto.setPrice(stockLot.getUnitPrice());
//				dto.setQuantity(stockLot.getQuantity());
//			}
//		}
//
//		Page<ProductListManagementDTO> dtoPage = new PageImpl<>(dtos, pageable, entities.getTotalElements());
//		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dtoPage));
//	}


//	@GetMapping(value = "/management/{productId}")
//	public ResponseEntity<ApiResponse<ProductDetailManagementDTO>> getProductInDetailForManagement(@PathVariable Integer productId) {
//		Product entity = productService.getProductById(productId);
//		ProductDetailManagementDTO dto = modelMapper.map(entity, ProductDetailManagementDTO.class);
//		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dto));
//	}

//	@GetMapping("/list-by-ids")
//	public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getVariantsByIds(@RequestParam List<Integer> ids) {
//		List<Product> products = productService.getVariantsByIds(ids);
//		List<ProductResponseDTO> dto = modelMapper.map(products, new TypeToken<List<ProductResponseDTO>>() {
//		}.getType());
//		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dto));
//	}
	
	@GetMapping(value = "/public")
	public ResponseEntity<ApiResponse<Page<ProductListPublicDTO>>> getAllProductsForPublic(
	    Pageable pageable,
	    @RequestParam(name = "search", required = false) String search,
	    ProductFilterForm form) {
		
		form.setIsPublished(true);
		Page<Product> entities = productService.getAllProduct(pageable, search, form);
		
		List<ProductListPublicDTO> dtos = entities.getContent().stream().map(product -> {
			String thumbnailUrl = productImageService
			    .getThumbnailByProductId(product.getId())
			    .map(ProductImage::getImageUrl)
			    .orElse(null);
			
			BigDecimal minPrice = productVariantService.getMinPriceByProductId(product.getId());
			BigDecimal maxPrice = productVariantService.getMaxPriceByProductId(product.getId());
			
			return ProductListPublicDTO.builder()
			    .id(product.getId())
			    .productName(product.getProductName())
			    .slug(product.getSlug())
			    .thumbnailUrl(thumbnailUrl)
			    .minPrice(minPrice)
			    .maxPrice(maxPrice)
			    .build();
		}).collect(Collectors.toList());
		
		Page<ProductListPublicDTO> dtoPage = new PageImpl<>(dtos, pageable, entities.getTotalElements());
		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dtoPage));
	}
	
	
	@GetMapping(value = "/public/{slug}")
	public ResponseEntity<ApiResponse<ProductDetailPublicDTO>> getProductDetailForPublic(
	    @PathVariable String slug) {
		
		Product entity = productService.getProductBySlug(slug);
		
		ProductDetailPublicDTO dto = ProductDetailPublicDTO.builder()
		    .id(entity.getId())
		    .productName(entity.getProductName())
		    .slug(entity.getSlug())
		    .description(entity.getDescription())
		    .vintage(entity.getVintage())
		    .alcohol(entity.getAlcohol())
		    .region(entity.getRegion())
		    .brand(BrandInProductDTO.fromEntity(entity.getBrand())) // bạn cần đảm bảo có static method fromEntity
		    .category(CategoryInProductDTO.fromEntity(entity.getCategory()))
		    .images(entity.getImages()
			.stream()
			.map(ProductImageResponseDTO::fromEntity)
			.toList())
		    .variants(entity.getVariants()
			.stream()
			.map(ProductVariantResponseDTO::fromEntity)
			.toList())
		    .build();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
		    && authentication.getPrincipal() instanceof Account account) {
			boolean isInWishlist = wishlistService.isProductInWishlist(account.getId(), entity.getId());
			dto.setIsInWishlist(isInWishlist);
		}
		
		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", dto));
	}




//	@PostMapping()
//	public ResponseEntity<ApiResponse<ProductListManagementDTO>> createProduct(@RequestBody @Valid ProductCreateForm form) {
//		Product entity = productService.createProduct(form);
//		ProductListManagementDTO dto = modelMapper.map(entity, ProductListManagementDTO.class);
//		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(HttpStatus.CREATED.value(), "Product created successfully", dto));
//	}
//
//	@PatchMapping(value = "/{productId}")
//	public ResponseEntity<ApiResponse<ProductListManagementDTO>> updateProduct(@PathVariable Integer productId,
//										   @RequestBody @Valid ProductUpdateForm form) {
//		Product entity = productService.updateProduct(productId, form);
//		ProductListManagementDTO dto = modelMapper.map(entity, ProductListManagementDTO.class);
//		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product updated successfully", dto));
//	}
}

