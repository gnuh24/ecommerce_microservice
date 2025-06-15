package com.ec.user.dto.auth;

import lombok.Data;

@Data
public class UpdateEmailForm {
	
	private String otp;
	
	private String newEmail;
	
}
