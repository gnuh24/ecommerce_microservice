package com.ec.order.exceptions.business.order;

public class OrderCannotBeCancelledException extends OrderException {
    public OrderCannotBeCancelledException(String currentStatus) {
        super("Không thể hủy đơn hàng ở trạng thái: " + currentStatus);
    }
}
