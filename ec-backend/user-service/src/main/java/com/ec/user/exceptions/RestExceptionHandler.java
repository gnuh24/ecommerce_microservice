package com.ec.user.exceptions;


import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ec.user.exceptions.AuthException.AuthExceptionHandler;
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

//Annotaion này dùng để đánh dấu đây là 1 Bean Global chuyên dùng để xử lý các lỗi  !!!
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	
	// Lỗi mặc định
	// Annotaion này dùng để đánh dấu phương thức này sẽ xử lý tất cả các lỗi phát sinh từ các Controller
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAll(Exception exception) {
		
		String message = exception.getLocalizedMessage();
		String detailMessage = Arrays.toString(exception.getStackTrace());
		int code = 1;
		
		if (exception instanceof EntityNotFoundException) {
			ErrorResponse response = new ErrorResponse(404, message, detailMessage, null, code);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		if (exception instanceof FileNotFoundException) {
			ErrorResponse response = new ErrorResponse(400, "Không tìm thấy ảnh !!", detailMessage, null, code);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		ErrorResponse response = new ErrorResponse(400, message, detailMessage, null, code);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	// URL không hợp lệ
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
	    NoHandlerFoundException exception,
	    @NonNull HttpHeaders headers,
	    @NonNull HttpStatusCode status,
	    @NonNull WebRequest request) {
		
		String message = "URL Không hợp lệ !!!";
		String detailMessage = exception.getLocalizedMessage();
		int code = 2;
		
		ErrorResponse response = new ErrorResponse(status.value(), message, detailMessage, null, code);
		
		return new ResponseEntity<>(response, status);
	}
	
	// Lỗi liên quan tới các phương thức HTTP
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
	    HttpRequestMethodNotSupportedException exception,
	    @NonNull HttpHeaders headers,
	    @NonNull HttpStatusCode status,
	    @NonNull WebRequest request) {
		
		String message = "Server không hỗ trợ thao tác " + exception.getMethod() +
		    " Chỉ hỗ trợ các thao tác: " + exception.getSupportedHttpMethods();
		String detailMessage = exception.getLocalizedMessage();
		int code = 3;
		
		ErrorResponse response = new ErrorResponse(status.value(), message, detailMessage, null, code);
		
		return new ResponseEntity<>(response, status);
	}
	
	// Không hỗ trợ các loại file đặc biệt VD: Ảnh, word, .sql v.v
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
	    HttpMediaTypeNotSupportedException exception,
	    @NonNull HttpHeaders headers,
	    @NonNull HttpStatusCode status,
	    @NonNull WebRequest request) {
		
		String message = "Không hỗ trợ đnh dạng cho file " + exception.getContentType().getType();
		String detailMessage = exception.getLocalizedMessage();
		int code = 4;
		
		ErrorResponse response = new ErrorResponse(status.value(), message, detailMessage, null, code);
		
		return new ResponseEntity<>(response, status);
	}
	
	
	// BindException: This exception is thrown when fatal binding errors occur.
	//Ex: Chuyển đổi dữ liệu thất bại từ Entity -> DTO, Form -> ENtity ....
	// Liên quan tới Model Mapper
	
	// MethodArgumentNotValidException: This exception is thrown when argument
	// annotated with @Valid failed validation:
	//Ex: Truyển thiếu tham số, sai điều kiện vào form.
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	    MethodArgumentNotValidException exception,
	    @NonNull HttpHeaders headers,
	    @NonNull HttpStatusCode status,
	    @NonNull WebRequest request) {
		
		String message = "Tham số truyền xuống BackEnd có vấn đề !! (Hãy kiểm tra lại các điều kiện)";
		
		String detailMessage = exception.getLocalizedMessage();
		// error
		Map<String, String> errors = new HashMap<>();
		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		}
		
		int code = 5;
		
		ErrorResponse response = new ErrorResponse(status.value(), message, detailMessage, errors, code);
		
		return new ResponseEntity<>(response, status);
	}
	
	//Bean Validation API Error.
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
		
		String message = "Lỗi Bean Validation API !!! (Sai rèn buộc dưới Database)";
		String detailMessage = exception.getLocalizedMessage();
		// error
		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation violation : exception.getConstraintViolations()) {
			String fieldName = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(fieldName, errorMessage);
		}
		int code = 5;
		
		ErrorResponse response = new ErrorResponse(400, message, detailMessage, errors, code);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	// MissingServletRequestPartException: This exception is thrown when when the part of a multipart request not found
	//VD trong trường hợp ta truyền cả dữ liệu chuỗi và file nhưng một trong 2 hoặc cả 2 không được tìm thấy thì lỗi này xuất hiện
	
	// MissingServletRequestParameterException: This exception is thrown when request missing parameter:
	//VD: Tham số bắt buộc ở API như search v.v
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
	    MissingServletRequestParameterException exception,
	    @NonNull HttpHeaders headers,
	    @NonNull HttpStatusCode status,
	    @NonNull WebRequest request) {
		String message = "Thiếu các tham số bắt buộc trong API !!";
		String detailMessage = exception.getLocalizedMessage();
		int code = 6;
		
		ErrorResponse response = new ErrorResponse(status.value(), message, detailMessage, null, code);
		
		return new ResponseEntity<>(response, status);
	}
	
	// TypeMismatchException: This exception is thrown when try to set bean property with wrong type.
	
	
	// MethodArgumentTypeMismatchException: This exception is thrown when method argument is not the expected type:
	//VD: Ta cần 1 biến Int trả về nhưng nó lại trả về String
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		String message = "Đã có 1 (một số) tham số trả về không đúng kiểu dữ liệu !!";
		String detailMessage = exception.getLocalizedMessage();
		int code = 7;
		
		ErrorResponse response = new ErrorResponse(400, message, detailMessage, null, code);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
}

