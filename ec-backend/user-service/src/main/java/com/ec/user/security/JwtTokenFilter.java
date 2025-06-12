package com.ec.user.security;

import com.ec.user.exceptions.AuthException.AuthExceptionHandler;
import com.ec.user.exceptions.JwtException.InvalidJWTSignatureException;
import com.ec.user.exceptions.JwtException.TokenExpiredException;
import com.ec.user.exceptions.JwtException.UsernameNotFound;
import com.ec.user.service.AccountService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	@Lazy
	private AccountService accountService;
	
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	
	// ✅ Danh sách các đường dẫn public bắt đầu bằng /api/user
	private boolean isPublicPath(String path) {
		return path.startsWith("/api/user/auth/login")
		    || path.startsWith("/api/user/auth/register")
		    || path.startsWith("/api/user/auth/check-username")
		    || path.startsWith("/api/user/auth/staff-login")
		    || path.startsWith("/api/user/auth/active-account")
		    || path.startsWith("/api/user/auth/send-reset-password-otp")
		    || path.startsWith("/api/user/auth/reset-password")
		    || path.startsWith("/api/user/swagger")
		    || path.startsWith("/api/user/v3/api-docs");
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
					@NonNull HttpServletResponse response,
					@NonNull FilterChain filterChain) throws ServletException, IOException {
		
		final String path = request.getRequestURI();
		// ✅ Bỏ qua filter nếu là API public
		if (isPublicPath(path)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;
		
		// Kiểm tra token
		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
			jwtToken = authHeader.substring(7);
			try {
				userEmail = jwtTokenProvider.getUsername(jwtToken);
				
				if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = accountService.loadUserByUsername(userEmail);
					
					// Tạo SecurityContext và Authen Token
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					    userDetails, null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					securityContext.setAuthentication(token);
					SecurityContextHolder.setContext(securityContext);
				}
			} catch (ExpiredJwtException e1) {
				authExceptionHandler.commence(request, response, new TokenExpiredException("Access Token đã hết hạn sử dụng."));
				return;
			} catch (SignatureException e2) {
				authExceptionHandler.commence(request, response, new InvalidJWTSignatureException("Access Token chứa signature không hợp lệ."));
				return;
			} catch (UsernameNotFoundException e3) {
				authExceptionHandler.commence(request, response, new UsernameNotFound("Access Token chứa thông tin không tồn tại trong hệ thống."));
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
