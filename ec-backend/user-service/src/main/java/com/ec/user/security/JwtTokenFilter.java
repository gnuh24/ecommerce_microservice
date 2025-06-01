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
	
	@Override
	// Xác thực Token khi login và call API (Chạy đầu tiên)
	protected void doFilterInternal(HttpServletRequest request,
					@NonNull HttpServletResponse response,
					@NonNull FilterChain filterChain) throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;
		
		// Kiểm tra token
		if (authHeader != null && !authHeader.isBlank()) {
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

