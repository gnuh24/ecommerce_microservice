package com.ec.order.controller;

import com.ec.order.api.ApiResponse;
import com.ec.order.dto.order.OrderAdminDetailDto;
import com.ec.order.dto.order.OrderAdminListDto;
import com.ec.order.entity.Order;
import com.ec.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
	
	private final OrderService orderService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<OrderAdminListDto>>> getAllOrders(
	    @RequestParam(required = false) String search,
	    @RequestParam(required = false) String status,
	    @RequestParam(required = false) String paymentMethod,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
	    Pageable pageable
	) {
		Page<OrderAdminListDto> result = orderService.getAllOrders(search, status, paymentMethod, fromDate, toDate, pageable);
		ApiResponse<Page<OrderAdminListDto>> response = new ApiResponse<>(200, "Lấy danh sách đơn hàng thành công", result);
		return ResponseEntity.ok(response);
	}

	
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderAdminDetailDto>> getOrderDetail(@PathVariable String orderId) {
		Order entity = orderService.getOrderDetailById(orderId);
		OrderAdminDetailDto detail = OrderAdminDetailDto.fromEntity(entity);
		
		ApiResponse<OrderAdminDetailDto> response = new ApiResponse<>(
		    200,
		    "Lấy chi tiết đơn hàng thành công",
		    detail
		);
		
		return ResponseEntity.ok(response);
	}

	
}
