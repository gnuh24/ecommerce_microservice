package com.ec.catalog.exceptions.errorCode;

public final class CatalogBusinessErrorCode {
	
	private CatalogBusinessErrorCode() {
	}
	
	// ======= PRODUCT =======
	public static final String CAT_PRODUCT_NOT_FOUND = "CAT-PRODUCT-001"; // Không tìm thấy sản phẩm
	public static final String CAT_PRODUCT_INACTIVE = "CAT-PRODUCT-002"; // Sản phẩm đã bị vô hiệu hóa
	
	// ======= PRODUCT VARIANT =======
	public static final String CAT_VARIANT_NOT_FOUND = "CAT-VARIANT-001"; // Không tìm thấy phiên bản sản phẩm
	public static final String CAT_VARIANT_QUANTITY_NOT_ENOUGH = "CAT-VARIANT-002"; // Không đủ số lượng trong kho
	public static final String CAT_VARIANT_INVALID_REQUEST = "CAT-VARIANT-003"; // Dữ liệu yêu cầu không hợp lệ
	public static final String CAT_VARIANT_ALREADY_DISABLED = "CAT-VARIANT-004"; // Phiên bản đã ngưng kinh doanh
	
	// ======= CATEGORY =======
	public static final String CAT_CATEGORY_NOT_FOUND = "CAT-CATEGORY-001"; // Không tìm thấy danh mục
	public static final String CAT_CATEGORY_ALREADY_EXISTS = "CAT-CATEGORY-002"; // Tên danh mục đã tồn tại
	public static final String CAT_CATEGORY_INVALID_ID = "CAT-CATEGORY-003"; // ID danh mục không hợp lệ
	public static final String CAT_CATEGORY_ALREADY_DELETED = "CAT-CATEGORY-004"; // Danh mục đã bị xóa
	
}
