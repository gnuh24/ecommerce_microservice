package com.ec.catalog.exceptions.business.wishlist;

import com.ec.catalog.exceptions.business.BusinessException;

public abstract class WishlistException extends BusinessException {
	public WishlistException(String message) {
		super(message);
	}
}

