package com.ec.catalog.exceptions.errorCode;

public final class CatalogBusinessErrorCode {

    private CatalogBusinessErrorCode() {}

    // ======= PRODUCT =======
    public static final String CAT_PRODUCT_NOT_FOUND           = "CAT-PRODUCT-001"; // Không tìm thấy sản phẩm
    public static final String CAT_PRODUCT_INACTIVE            = "CAT-PRODUCT-002"; // Sản phẩm đã bị vô hiệu hóa

    // ======= PRODUCT VARIANT =======
    public static final String CAT_VARIANT_NOT_FOUND           = "CAT-VARIANT-001"; // Không tìm thấy phiên bản sản phẩm
    public static final String CAT_VARIANT_QUANTITY_NOT_ENOUGH = "CAT-VARIANT-002"; // Không đủ số lượng trong kho
    public static final String CAT_VARIANT_INVALID_REQUEST     = "CAT-VARIANT-003"; // Dữ liệu yêu cầu không hợp lệ
    public static final String CAT_VARIANT_ALREADY_DISABLED    = "CAT-VARIANT-004"; // Phiên bản đã ngưng kinh doanh
}
