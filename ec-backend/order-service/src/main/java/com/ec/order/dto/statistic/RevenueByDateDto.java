package com.ec.order.dto.statistic;

import java.math.BigDecimal;

public interface RevenueByDateDto {
    String getTimeUnit(); // yyyy-MM-dd hoặc yyyy-MM hoặc yyyy-ww
    BigDecimal getTotalRevenue();
}
