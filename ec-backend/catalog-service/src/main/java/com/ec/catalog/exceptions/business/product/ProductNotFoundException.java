package com.ec.catalog.exceptions.business.product;

// 4. ProductNotFoundException: lỗi cụ thể
public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(Long productId) {
        super("Không tìm thấy sản phẩm với ID: " + productId);
    }
}
