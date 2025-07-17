package com.ec.catalog.exceptions.business.wishlist;

public class ProductNotInWishlistException extends WishlistException {
	public ProductNotInWishlistException(String productId) {
		super("Không tìm thấy sản phẩm với ID [" + productId + "] trong danh sách yêu thích.");
	}
}
