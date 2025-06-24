package com.ec.catalog.service;

import com.ec.catalog.dto.product.ProductFilterForm;
import com.ec.catalog.entity.Product;
import com.ec.catalog.repository.ProductRepository;
import com.ec.catalog.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

//    @Autowired
//    @Lazy
//    private StockLotService stockLotService;
	
	@Override
	public Page<Product> getAllProduct(Pageable pageable, String search, ProductFilterForm form) {
		Specification<Product> where = ProductSpecification.buildWhere(search, form);
		return productRepository.findAll(where, pageable);
	}
	
	@Override
	public Product getProductBySlug(String slug) {
		return productRepository.findBySlugAndIsDeletedFalseAndIsPublishedTrue(slug)
		    .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
	}

//
//	@Override
//	public Product getProductById(Integer productId) {
//		return productRepository.findById(productId)
//		    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
//	}
//
//	@Override
//	public List<Product> getProductsByIds(List<Integer> ids) {
//		return productRepository.findAllById(ids);
//	}
	
	
//	@Override
//	public int updateDefaultBrandOfProduct(Integer brandId) {
//		return productRepository.updateBrandToDefault(brandId);
//	}
//
//	@Override
//	public int updateDefaultCategoryOfProduct(Integer categoryId) {
//		return productRepository.updateCategoryToDefault(categoryId);
//	}
//
//	@Override
//	@Transactional
//	public Product createProduct(ProductCreateForm form) {
//
//		Product entity = new Product();
//
//		entity.setProductName(form.getProductName());
//		entity.setAbv(form.getAbv());
//		entity.setOrigin(form.getOrigin());
//		entity.setCapacity(form.getCapacity());
//		entity.setDescription(form.getDescription());
//
//		Brand brand = brandService.getBrandById(form.getBrandId());
//		entity.setBrand(brand);
//
//		Category category = categoryService.getCategoryById(form.getCategoryId());
//		entity.setCategory(category);
//
//		entity.setImage(form.getImage());
//
//		entity = productRepository.save(entity);
//
//		StockLotCreateForm stockLotCreateForm = new StockLotCreateForm();
//		stockLotCreateForm.setUnitPrice(0);
//		stockLotCreateForm.setProductId(entity.getId());
//		stockLotCreateForm.setQuantity(0);
//		stockLotCreateForm.setMaxQuantity(0);
//
//		stockLotService.createStockLot(stockLotCreateForm);
//
//
//		return entity;
//	}
//
//	@Override
//	@Transactional
//	public Product updateProduct(Integer productId,
//				     ProductUpdateForm form) {
//		// Fetch the existing product from the repository
//		Product oldProduct = getProductById(productId);
//
//		// Update fields if they are not null
//		if (form.getStatus() != null) {
//			oldProduct.setStatus(form.getStatus());
//		}
//		if (form.getImage() != null && !form.getImage().isEmpty()) {
//			oldProduct.setImage(form.getImage());
//		}
//
//		if (form.getOrigin() != null) {
//			oldProduct.setOrigin(form.getOrigin());
//		}
//		if (form.getCapacity() != null) {
//			oldProduct.setCapacity(form.getCapacity());
//		}
//		if (form.getAbv() != null) {
//			oldProduct.setAbv(form.getAbv());
//		}
//		if (form.getDescription() != null) {
//			oldProduct.setDescription(form.getDescription());
//		}
//		if (form.getBrandId() != null) {
//			Brand newBrand = brandService.getBrandById(form.getBrandId());
//			oldProduct.setBrand(newBrand);
//		}
//		if (form.getCategoryId() != null) {
//			Category newCategory = categoryService.getCategoryById(form.getCategoryId());
//			oldProduct.setCategory(newCategory);
//		}
//
//		// Save the updated product
//		return productRepository.save(oldProduct);
//	}
//
	
}

