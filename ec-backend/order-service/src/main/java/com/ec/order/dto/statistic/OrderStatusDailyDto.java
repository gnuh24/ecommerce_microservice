package com.ec.order.dto.statistic;

public interface OrderStatusDailyDto {
    String getTimeUnit();  // ví dụ: 2025-07-13
    String getStatus();    // ví dụ: COMPLETE, CANCELED,...
    Long getCount();       // số lượng đơn
}
