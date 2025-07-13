package com.ec.order.service;

import com.ec.order.dto.statistic.BestSellingProductDto;
import com.ec.order.dto.statistic.BestSellingVariantDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
	List<BestSellingProductDto> getBestSellingProducts(LocalDateTime fromDate, LocalDateTime toDate, String brandId, String categoryId, int top);
	List<BestSellingVariantDto> getBestSellingVariantsByProductId(String productId, LocalDateTime fromDate, LocalDateTime toDate);
	
}
