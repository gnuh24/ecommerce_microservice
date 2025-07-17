package com.ec.catalog.exceptions.business.brand;

public class CannotDeleteDefaultBrandException extends RuntimeException {
	public CannotDeleteDefaultBrandException(String id) {
		super("Không thể xóa thương hiệu mặc định với mã '" + id + "'");
	}
}