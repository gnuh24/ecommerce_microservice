package com.ec.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsGlobalConfig {
	
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		
		// ✅ Chỉ định các origin cụ thể cần cho phép
		config.setAllowedOriginPatterns(List.of(
		    "http://localhost:4200",
		    "http://127.0.0.1:5500"
		));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(List.of("*"));
		config.setAllowedMethods(List.of("*"));
		config.setMaxAge(3600L); // cache CORS preflight trong 1h
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return new CorsWebFilter(source);
	}
}
