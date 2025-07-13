package com.ec.order.dto.statistic;

import java.math.BigDecimal;

public interface DashboardOverviewDto {
    Long getTotalOrder();
    Long getCompleteOrder();
    Long getCanceledOrder();
    Long getProcessingOrder();
    Long getPendingOrder();        // 👈 THÊM DÒNG NÀY
    BigDecimal getTotalRevenue();
}
