package com.ec.order.dto.statistic;

public interface OrderStatusSummaryDto {
    String getStatus();  // COMPLETE, PENDING, ...
    Long getCount();     // Số lượng đơn ở trạng thái đó
}
