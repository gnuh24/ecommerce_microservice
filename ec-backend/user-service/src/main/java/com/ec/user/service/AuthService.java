package com.ec.user.service;

//import com.sgu.backend.dto.request.auth.LoginRequestForm;
//import com.sgu.backend.dto.request.auth.UserRegistrationForm;
//import com.sgu.backend.dto.response.auth.AuthResponseDTO;

import com.ec.user.dto.auth.AuthResponseDTO;
import com.ec.user.dto.auth.LoginRequestForm;

public interface AuthService {

  AuthResponseDTO login(LoginRequestForm request);
//  boolean registration(UserRegistrationForm userRegistrationForm);
//  boolean verifiOTP( String email,String otp);
//
//    AuthResponseDTO staffLogin(LoginRequestForm request);
//
//    AuthResponseDTO refreshToken(String oldToken, String refreshToken);
}
