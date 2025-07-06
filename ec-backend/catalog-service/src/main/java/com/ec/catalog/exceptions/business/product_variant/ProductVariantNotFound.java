package com.ec.catalog.exceptions.business.product_variant;

public class ProductVariantNotFound extends ProductVariantException {
	public ProductVariantNotFound(String variantId) {
		super("Không tìm thấy phiên bản sản phẩm với ID: " + variantId);
	}
}
