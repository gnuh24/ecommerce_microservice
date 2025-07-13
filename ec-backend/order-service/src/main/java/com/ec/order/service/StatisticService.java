package com.ec.order.service;

import com.ec.order.dto.statistic.*;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
	List<BestSellingProductDto> getBestSellingProducts(LocalDateTime fromDate, LocalDateTime toDate, String brandId, String categoryId, int top);
	List<BestSellingVariantDto> getBestSellingVariantsByProductId(String productId, LocalDateTime fromDate, LocalDateTime toDate);
	List<RevenueByDateDto> getRevenueByDate(LocalDateTime fromDate, LocalDateTime toDate, String groupBy);
	List<OrderStatusDailyDto> getOrderStatusDaily(LocalDateTime fromDate, LocalDateTime toDate, String format);
	List<OrderStatusSummaryDto> getOrderStatusSummary(LocalDateTime fromDate, LocalDateTime toDate);
	DashboardOverviewDto getDashboardOverview(
	    LocalDateTime fromDate, LocalDateTime toDate
	);
}
