package com.ec.user.integration.redis;

public class RedisConstants {
	
	// 1. Email tồn tại
	public static final String USERNAME_EXIST = "username_exist"; // dùng kèm với :<email>
	
	// 2. OTP xác thực tài khoản
	public static final String OTP_VERIFY_ACCOUNT = "otp_verify_account"; // dùng kèm với :<email>
	
	// 3. OTP quên mật khẩu
	public static final String OTP_FORGOT_PASSWORD = "otp_forgot_password"; // dùng kèm với :<email>
	
	// 4. OTP đổi email
	public static final String OTP_CHANGE_EMAIL = "otp_change_email"; // dùng kèm với :<email>
	
	// 5. Đơn hàng ảo
	public static final String TEMP_ORDER = "temp_order"; // dùng kèm với :<orderId> hoặc <userId>
}
