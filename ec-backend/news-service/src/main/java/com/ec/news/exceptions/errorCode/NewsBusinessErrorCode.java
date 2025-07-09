package com.ec.news.exceptions.errorCode;

public final class NewsBusinessErrorCode {
	
	private NewsBusinessErrorCode() {}
	
	// ======= NEWS =======
	public static final String NEWS_NOT_FOUND             			= "NEWS-001"; // Không tìm thấy bài viết
	public static final String NEWS_ALREADY_DELETED       		= "NEWS-002"; // Bài viết đã bị xóa
	public static final String NEWS_INVALID_ID            			= "NEWS-003"; // ID bài viết không hợp lệ
	public static final String NEWS_TITLE_ALREADY_EXISTS  	= "NEWS-004"; // Tiêu đề bài viết đã tồn tại
	public static final String NEWS_UNAUTHORIZED_ACCESS   	= "NEWS-005"; // Không có quyền truy cập bài viết
	
	// ======= NEWS IMAGE =======
	public static final String NEWS_IMAGE_NOT_FOUND       		= "NEWS-IMG-001"; // Không tìm thấy ảnh của bài viết
	public static final String NEWS_IMAGE_UPLOAD_FAILED   	= "NEWS-IMG-002"; // Tải ảnh lên thất bại
	public static final String NEWS_IMAGE_DELETE_FAILED   	= "NEWS-IMG-003"; // Xóa ảnh thất bại
	public static final String NEWS_IMAGE_TOO_LARGE       		= "NEWS-IMG-004"; // Ảnh vượt quá dung lượng cho phép
	public static final String NEWS_IMAGE_INVALID_FORMAT  	= "NEWS-IMG-005"; // Định dạng ảnh không hợp lệ
}
