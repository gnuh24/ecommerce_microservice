package com.ec.order.service;

import com.ec.order.dto.statistic.*;
import com.ec.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
	
	private final OrderRepository orderRepository;
	
	@Override
	public List<BestSellingProductDto> getBestSellingProducts(LocalDateTime fromDate, LocalDateTime toDate, String brandId, String categoryId, int top) {
		return orderRepository.getBestSellingProducts(fromDate, toDate, brandId, categoryId, top);
	}
	
	@Override
	public List<BestSellingVariantDto> getBestSellingVariantsByProductId(String productId, LocalDateTime fromDate, LocalDateTime toDate) {
		return orderRepository.getBestSellingVariantsByProductId(productId, fromDate, toDate);
	}
	
	@Override
	public List<RevenueByDateDto> getRevenueByDate(LocalDateTime fromDate, LocalDateTime toDate, String groupBy) {
		String format;
		switch (groupBy.toUpperCase()) {
			case "MONTH" -> format = "%Y-%m";
			case "WEEK" -> format = "%Y-%u";
			case "DAY" -> format = "%Y-%m-%d";
			default -> throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
		}
		return orderRepository.getRevenueByDate(fromDate, toDate, format);
	}
	
	@Override
	public List<OrderStatusDailyDto> getOrderStatusDaily(LocalDateTime fromDate, LocalDateTime toDate, String format) {
		return orderRepository.getOrderStatusDaily(fromDate, toDate, format);
	}
	
	@Override
	public List<OrderStatusSummaryDto> getOrderStatusSummary(LocalDateTime fromDate, LocalDateTime toDate) {
		return orderRepository.getOrderStatusSummary(fromDate, toDate);
	}
	
	@Override
	public DashboardOverviewDto getDashboardOverview(
	    LocalDateTime fromDate,
	    LocalDateTime toDate) {
		return orderRepository.getDashboardOverview(fromDate, toDate);
	}
	
	
}
