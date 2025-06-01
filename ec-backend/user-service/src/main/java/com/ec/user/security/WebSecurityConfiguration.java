package com.ec.user.security;

import com.ec.user.exceptions.AuthException.AuthExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	
//	@Autowired
//	@Lazy
//	private AccountService accountService;
	
	@Autowired
	@Lazy
	private AuthExceptionHandler authExceptionHandler;
	
	@Autowired
	private JwtTokenFilter jwtAuthFIlter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
					       CorsConfigurationSource corsConfigurationSource) throws Exception {
		http
		    // Loại bỏ bảo vệ CSRF
		    .csrf(AbstractHttpConfigurer::disable)
//		    .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Bật CORS
		    
		    
		    // Configure các luồng truy cập
		    .authorizeHttpRequests(auth -> auth
			    
			    // Xác thực tất cả các request
			    .anyRequest().permitAll()


//			.requestMatchers(HttpMethod.GET, "/api/redis/get/name")                                         .permitAll()
//
//			.requestMatchers(HttpMethod.GET, "/accounts/{Id}")                                                .permitAll()
//			.requestMatchers(HttpMethod.GET, "/accounts/email")                                             .permitAll()
//
//			.requestMatchers(HttpMethod.POST, "/accounts")                                                    .permitAll()
//			.requestMatchers(HttpMethod.POST, "/accounts/activate-account")                         .permitAll()
//			.requestMatchers(HttpMethod.POST, "/accounts/{accountId}/account-activity-logs").hasAnyAuthority("USER")
//
//
//			.requestMatchers(HttpMethod.PATCH, "/accounts/{id}")                                            .hasAnyAuthority("USER")
//			.requestMatchers(HttpMethod.PATCH, "/accounts/{id}/update-password")                .hasAnyAuthority("USER")
//			.requestMatchers(HttpMethod.PATCH, "/accounts/{id}/update-email")                       .hasAnyAuthority("USER")
//
//
//			.requestMatchers( HttpMethod.GET, "/media")                                                           .permitAll()
//			.requestMatchers( HttpMethod.POST, "/media/upload")                                             .permitAll()
//
//
//			.requestMatchers(HttpMethod.POST, "/auth/send-otp-update-email")                        .hasAnyAuthority("USER")
//			.requestMatchers(HttpMethod.POST, "/auth/send-otp-reset-password")                    .permitAll()
//			.requestMatchers(HttpMethod.PATCH, "/auth/{id}/update-role")                                 .hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/auth/{id}/update-status")                              .hasAnyAuthority("ADMIN")
//
//
//
//			.requestMatchers(HttpMethod.GET, "/auth/check-email")                                           .permitAll()
//			.requestMatchers(HttpMethod.POST, "/auth/login")                                                    .permitAll()
//			.requestMatchers(HttpMethod.POST, "/auth/staff-login")                                            .permitAll()
//			.requestMatchers(HttpMethod.POST, "/auth/register")                                                .permitAll()
//			.requestMatchers(HttpMethod.POST, "/auth/refresh-token")                                       .hasAnyAuthority("USER", "ADMIN")
//
//			.requestMatchers(HttpMethod.GET, "/accounts")                                                        .hasAnyAuthority("ADMIN")
//
//			.requestMatchers(HttpMethod.GET, "/profiles/{profileId}")                                           .hasAnyAuthority("USER", "ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/profiles/{profileId}")                                           .hasAnyAuthority("USER")
//
//			.requestMatchers(HttpMethod.GET, "/brands/{brandId}")                                             .permitAll()
//			.requestMatchers(HttpMethod.GET, "/brands")                                                            .permitAll()
//			.requestMatchers(HttpMethod.GET, "/brands/no-paging")                                           .permitAll()
//			.requestMatchers(HttpMethod.POST, "/brands")                                                          .hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/brands/{brandId}")                                        .hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.DELETE, "/brands/{brandId}")                                       .hasAnyAuthority("ADMIN")
//
//
//			.requestMatchers(HttpMethod.GET, "/categories/{categoryId}").permitAll()
//			.requestMatchers(HttpMethod.GET, "/categories") .hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.GET, "/categories/no-paging").permitAll()
//			.requestMatchers(HttpMethod.POST, "/categories").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/categories/{categoryId}").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.DELETE, "/categories/{categoryId}").hasAnyAuthority("ADMIN")
//
//			.requestMatchers(HttpMethod.GET, "/products/public").permitAll()
//			.requestMatchers(HttpMethod.GET, "/products/public/{productId}").permitAll()
//
//			.requestMatchers(HttpMethod.GET, "/products/management").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.GET, "/products/management/{productId}").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.POST, "/products").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/products/{productId}").hasAnyAuthority("ADMIN")
//
//
//			.requestMatchers(HttpMethod.GET, "/comments/product/{productId}").permitAll()
//			.requestMatchers(HttpMethod.GET, "/comments/{commentId}").permitAll()
//			.requestMatchers(HttpMethod.POST, "/comments").hasAnyAuthority("USER")
//			.requestMatchers(HttpMethod.PATCH, "/comments/{commentId}/content").hasAnyAuthority("USER")
//			.requestMatchers(HttpMethod.PATCH, "/comments/{commentId}/status").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.DELETE, "/comments/{commentId}").hasAnyAuthority("USER")
//
//			.requestMatchers(HttpMethod.GET, "/cart-item/{accountId}").hasAuthority("USER")
//			.requestMatchers(HttpMethod.POST, "/cart-item").hasAuthority("USER")
//			.requestMatchers(HttpMethod.PATCH, "/cart-item").hasAuthority("USER")
//			.requestMatchers(HttpMethod.DELETE, "/cart-item").hasAuthority("USER")
//			.requestMatchers(HttpMethod.DELETE, "/cart-item/{accountId}").hasAuthority("USER")
//
//			.requestMatchers(HttpMethod.GET, "/reports").hasAuthority("ADMIN")
//			.requestMatchers(HttpMethod.GET, "/reports/{reportId}").hasAuthority("ADMIN")
//			.requestMatchers(HttpMethod.POST, "/reports").hasAuthority("USER")
//
//			.requestMatchers(HttpMethod.GET, "/orders/management").hasAuthority("ADMIN")
//			.requestMatchers(HttpMethod.GET, "/orders/management/{orderId}").hasAuthority("ADMIN")
//
//
//			.requestMatchers(HttpMethod.GET, "/orders/my-order").hasAuthority("USER")
//			.requestMatchers(HttpMethod.GET, "/orders/my-order/{orderId}").hasAuthority("USER")
//			.requestMatchers(HttpMethod.GET, "/orders/vnpay-payment-return").permitAll()
//
//			.requestMatchers(HttpMethod.POST, "/orders").hasAuthority("USER")
//			.requestMatchers(HttpMethod.POST, "/orders/{orderId}/status").hasAnyAuthority("USER", "ADMIN")
//
//
//			.requestMatchers(HttpMethod.GET, "/vouchers/management").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.GET, "/vouchers/public").permitAll()
//			.requestMatchers(HttpMethod.POST, "/vouchers").hasAnyAuthority("ADMIN")
//			.requestMatchers(HttpMethod.PATCH, "/vouchers/{voucherId}").hasAnyAuthority("ADMIN")
//
//			.requestMatchers(HttpMethod.GET, "/media/{mediaId}").permitAll()
//			.requestMatchers(HttpMethod.POST , "/media/upload").hasAnyAuthority("ADMIN")
		    
		    
		    ).httpBasic(Customizer.withDefaults())
		    
		    // Add JWT vào chuỗi lọc và ưu tiên loc theo JWT
		    .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//		.authenticationProvider(authenticationProvider())
		    
		    .addFilterBefore(jwtAuthFIlter, UsernamePasswordAuthenticationFilter.class)
		    
		    .exceptionHandling((exceptionHandling) -> exceptionHandling
			
			// Cấu hình xử lý ngoại lệ cho trường hợp không xác thực (Login sai ^^)
			.authenticationEntryPoint(authExceptionHandler)
			
			// Cấu hình xử lý ngoại lệ cho trường hợp truy cập bị từ chối (Không đủ quyền)
			.accessDeniedHandler(authExceptionHandler)
		    
		    );
		
		return http.build();
	}
	

	
	
}
