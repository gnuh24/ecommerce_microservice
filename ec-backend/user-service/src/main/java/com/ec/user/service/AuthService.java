package com.ec.user.service;

//import com.sgu.backend.dto.request.auth.LoginRequestForm;
//import com.sgu.backend.dto.request.auth.UserRegistrationForm;
//import com.sgu.backend.dto.response.auth.AuthResponseDTO;

import com.ec.user.dto.account.AccountRedisDTO;
import com.ec.user.dto.auth.AuthResponseDTO;
import com.ec.user.dto.auth.LoginRequestForm;
import com.ec.user.dto.auth.UserRegistrationForm;
import com.ec.user.entity.Account;

public interface AuthService {
	
	Account activeAccount(String otp);
	
	boolean isUsernameExists(String username);
	
	AuthResponseDTO login(LoginRequestForm request);
	
	AuthResponseDTO staffLogin(LoginRequestForm request);
	
	AccountRedisDTO register(UserRegistrationForm userRegistrationForm);
//
//
//    AuthResponseDTO refreshToken(String oldToken, String refreshToken);
}
