package com.ec.user.exceptions.AuthException;

import com.ec.user.api.ApiResponse;
import com.ec.user.exceptions.ErrorResponseForDevMode;
import com.ec.user.utils.EnvironmentUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
	
	@Autowired
	private EnvironmentUtils environmentUtils;
	
	@Autowired
	private MessageSource messageSource;
	
	private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	
	private void writeJsonResponse(HttpServletResponse response, int status, String message, String detailMessage, int code) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json;charset=UTF-8");
		
		if (environmentUtils.isDevMode()) {
			ErrorResponseForDevMode devResponse = new ErrorResponseForDevMode(status, message, detailMessage, null, code);
			response.getWriter().write(objectWriter.writeValueAsString(devResponse));
		} else {
			ApiResponse<Object> prodResponse = new ApiResponse<>(status, message, null);
			response.getWriter().write(objectWriter.writeValueAsString(prodResponse));
		}
	}
	
	// 401 Unauthorized
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String message = authException.getLocalizedMessage();
		String detailMessage = authException.toString();
		int code = 8;
		
		writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), message, detailMessage, code);
	}
	
	// 403 Forbidden
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
		String message = "Bạn không có đủ quyền để thực hiện chức năng này !!";
		String detailMessage = exception.toString();
		int code = 9;
		
		writeJsonResponse(response, HttpStatus.FORBIDDEN.value(), message, detailMessage, code);
	}
}
