package com.ec.order.exceptions.business.order;

import com.ec.order.entity.Order;

public class OrderStatusTransitionNotAllowedException extends OrderException {
    public OrderStatusTransitionNotAllowedException(String from, String to) {
        super("Không thể chuyển từ trạng thái " + from + " sang " + to);
    }
}
