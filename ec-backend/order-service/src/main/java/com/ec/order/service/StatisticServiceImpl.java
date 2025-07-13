package com.ec.order.service;

import com.ec.order.dto.statistic.BestSellingProductDto;
import com.ec.order.dto.statistic.BestSellingVariantDto;
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
	
	
}
