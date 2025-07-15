package com.ec.order.controller;

import com.ec.order.api.ApiResponse;
import com.ec.order.dto.statistic.*;
import com.ec.order.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class StatisticController {
	
	private final StatisticService statisticService;
	
	@GetMapping("/best-selling")
	public ResponseEntity<ApiResponse<List<BestSellingProductDto>>> getBestSellingProducts(
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
	    @RequestParam(required = false) String brandId,
	    @RequestParam(required = false) String categoryId,
	    @RequestParam(defaultValue = "5") int top
	) {
		List<BestSellingProductDto> result = statisticService.getBestSellingProducts(fromDate, toDate, brandId, categoryId, top);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách sản phẩm bán chạy thành công", result));
	}
	
	@GetMapping("/best-selling/{productId}/variants")
	public ResponseEntity<ApiResponse<List<BestSellingVariantDto>>> getBestSellingVariantsByProduct(
	    @PathVariable String productId,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
	) {
		List<BestSellingVariantDto> result = statisticService.getBestSellingVariantsByProductId(productId, fromDate, toDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách variant bán chạy thành công", result));
	}
	
	@GetMapping("/revenue-by-date")
	public ResponseEntity<ApiResponse<List<RevenueByDateDto>>> getRevenueByDate(
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
	    @RequestParam(defaultValue = "DAY") String groupBy
	) {
		List<RevenueByDateDto> result = statisticService.getRevenueByDate(fromDate, toDate, groupBy);
		return ResponseEntity.ok(new ApiResponse<>(200, "Thống kê doanh thu theo thời gian thành công", result));
	}
	
	@GetMapping("/order-status-daily")
	public ResponseEntity<ApiResponse<List<OrderStatusDailyDto>>> getOrderStatusDaily(
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
	    @RequestParam(defaultValue = "%Y-%m-%d") String format
	) {
		List<OrderStatusDailyDto> result = statisticService.getOrderStatusDaily(fromDate, toDate, format);
		return ResponseEntity.ok(new ApiResponse<>(200, "Thống kê đơn hàng theo trạng thái theo ngày thành công", result));
	}
	
	@GetMapping("/order-status-summary")
	public ResponseEntity<ApiResponse<List<OrderStatusSummaryDto>>> getOrderStatusSummary(
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
	) {
		List<OrderStatusSummaryDto> result = statisticService.getOrderStatusSummary(fromDate, toDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Tổng số đơn theo trạng thái thành công", result));
	}
	
	@GetMapping("/dashboard-overview")
	public ResponseEntity<ApiResponse<DashboardOverviewDto>> getDashboardOverview(
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
	) {
		DashboardOverviewDto result = statisticService.getDashboardOverview(fromDate, toDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thống kê tổng quan dashboard thành công", result));
	}
	
	
	
}
