package com.ec.user.exceptions;

import com.ec.user.api.ApiResponse;
import com.ec.user.utils.EnvironmentUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private EnvironmentUtils environmentUtils;
	
	private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, Exception exception, Object errors, int code) {
		if (environmentUtils.isDevMode()) {
			return new ResponseEntity<>(new ErrorResponseForDevMode(status.value(), message, exception.toString(), errors, code), status);
		} else {
			return new ResponseEntity<>(new ApiResponse<>(status.value(), message, null), status);
		}
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAll(Exception exception) {
		String message = exception.getLocalizedMessage();
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exception, null, 1);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		String message = "URL Không hợp lệ !!!";
		return buildErrorResponse(HttpStatus.valueOf(status.value()), message, exception, null, 2);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		String message = "Server không hỗ trợ thao tác " + exception.getMethod() + ". Chỉ hỗ trợ: " + exception.getSupportedHttpMethods();
		return buildErrorResponse(HttpStatus.valueOf(status.value()), message, exception, null, 3);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		String message = "Không hỗ trợ định dạng file: " + exception.getContentType();
		return buildErrorResponse(HttpStatus.valueOf(status.value()), message, exception, null, 4);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		Map<String, String> errors = new HashMap<>();
		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		}
		
		String message = "Tham số truyền xuống BackEnd có vấn đề !! (Hãy kiểm tra lại các điều kiện)";
		return buildErrorResponse(HttpStatus.valueOf(status.value()), message, exception, errors, 5);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			String fieldName = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(fieldName, errorMessage);
		}
		
		String message = "Lỗi Bean Validation API !!! (Sai ràng buộc dưới Database)";
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exception, errors, 5);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		String message = "Thiếu các tham số bắt buộc trong API !!";
		return buildErrorResponse(HttpStatus.valueOf(status.value()), message, exception, null, 6);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		String message = "Đã có một hoặc nhiều tham số không đúng kiểu dữ liệu !!";
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exception, null, 7);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException exception) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), exception, null, 8);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<Object> handleFileNotFound(FileNotFoundException exception) {
		String message = "Không tìm thấy ảnh !!";
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exception, null, 9);
	}
}
