package com.ec.catalog.exceptions.business.wishlist;

public class ProductAlreadyInWishlistException extends WishlistException {
	public ProductAlreadyInWishlistException(String productId) {
		super("Sản phẩm với ID [" + productId + "] đã tồn tại trong danh sách yêu thích.");
	}
}
