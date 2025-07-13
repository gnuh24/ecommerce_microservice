package com.ec.order.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public interface BestSellingProductDto {
	String getProductId();
	String getProductName();
	String getProductThumbnail();
	
	String getBrandId();
	String getBrandName();
	
	String getCategoryId();
	String getCategoryName();
	
	Integer getTotalQuantitySold();
	BigDecimal getTotalRevenue();
}



