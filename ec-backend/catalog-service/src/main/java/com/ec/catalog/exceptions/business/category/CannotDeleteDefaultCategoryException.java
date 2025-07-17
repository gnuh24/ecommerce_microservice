package com.ec.catalog.exceptions.business.category;

public class CannotDeleteDefaultCategoryException extends RuntimeException {
	public CannotDeleteDefaultCategoryException(String id) {
		super("Không thể xóa danh mục mặc định với mã '" + id + "'");
	}
}