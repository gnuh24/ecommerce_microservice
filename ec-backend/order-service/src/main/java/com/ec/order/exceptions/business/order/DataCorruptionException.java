package com.ec.order.exceptions.business.order;

public class DataCorruptionException extends OrderException {
	
	// ✅ Constructor tiện dụng: chỉ cần truyền orderId, thông báo sẽ là tiếng Việt
	public DataCorruptionException(String orderId) {
		super("Đơn hàng với mã '" + orderId + "' không có trạng thái. Có thể dữ liệu bị sai lệch do thao tác thủ công.");
	}
	
}
