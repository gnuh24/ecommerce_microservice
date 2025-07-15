package com.ec.order.dto.statistic;

import java.math.BigDecimal;

public interface BestSellingVariantDto {
	String getProductVariantId();
	Integer getVolume(); // ml
	Integer getTotalQuantitySold();
	BigDecimal getTotalRevenue();
}
