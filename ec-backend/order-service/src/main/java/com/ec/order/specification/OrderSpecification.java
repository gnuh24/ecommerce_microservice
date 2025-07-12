package com.ec.order.specification;

import com.ec.order.entity.Order;
import com.ec.order.entity.OrderStatus;
import com.ec.order.entity.Payment;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderSpecification {
	
	public static Specification<Order> keywordMatch(String keyword) {
		return (root, query, cb) -> {
			String like = "%" + keyword.toLowerCase() + "%";
			return cb.or(
			    cb.like(cb.lower(root.get("receiverName")), like),
			    cb.like(cb.lower(root.get("receiverPhone")), like),
			    cb.like(cb.lower(root.get("accountId")), like)
			);
		};
	}
	
	public static Specification<Order> hasLatestStatus(String status) {
		return (root, query, cb) -> {
			// Join với OrderStatus
			Join<Order, OrderStatus> join = root.join("statuses", JoinType.INNER);
			
			// Subquery để tìm id của OrderStatus mới nhất cho từng Order
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<OrderStatus> subRoot = subquery.from(OrderStatus.class);
			subquery.select(cb.max(subRoot.get("id")))
			    .where(cb.equal(subRoot.get("order").get("id"), root.get("id")));
			
			// So sánh id và status
			return cb.and(
			    cb.equal(join.get("status"), OrderStatus.OrderStatusEnum.valueOf(status)),
			    cb.equal(join.get("id"), subquery)
			);
		};
	}

	
	
	public static Specification<Order> hasPaymentMethod(String method) {
		return (root, query, cb) -> {
			Join<Order, Payment> join = root.join("payments", JoinType.LEFT);
			return cb.equal(join.get("paymentMethod"), method);
		};
	}
	
	public static Specification<Order> fromDate(LocalDate fromDate) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("orderTime"), fromDate.atStartOfDay());
	}
	
	public static Specification<Order> toDate(LocalDate toDate) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("orderTime"), toDate.plusDays(1).atStartOfDay());
	}
}
