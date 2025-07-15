package com.ec.order.repository;

import com.ec.order.dto.statistic.*;
import com.ec.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
	Page<Order> findByAccountIdAndIsTempFalse(String accountId, Pageable pageable);
	
	Optional<Order> findByIdAndAccountId(String orderId, String accountId);
	
	@Query(value = """
	    SELECT
	        p.id AS productId,
	        od.productName AS productName,
	        od.productThumbnail AS productThumbnail,
	        b.id AS brandId,
	        b.brandName AS brandName,
	        c.id AS categoryId,
	        c.categoryName AS categoryName,
	        SUM(od.quantity) AS totalQuantitySold,
	        SUM(od.totalPrice) AS totalRevenue
	    FROM OrderDetail od
	    JOIN `Order` o ON od.orderId = o.id
	    JOIN ProductVariant pv ON od.productVariantId = pv.id
	    JOIN Product p ON pv.productId = p.id
	    JOIN Brand b ON p.brandId = b.id
	    JOIN Category c ON p.categoryId = c.id
	    WHERE EXISTS (
	        SELECT 1
	        FROM OrderStatus s
	        WHERE s.orderId = o.id
	          AND s.updateTime = (
	              SELECT MAX(s2.updateTime)
	              FROM OrderStatus s2
	              WHERE s2.orderId = o.id
	          )
	          AND s.status = 'COMPLETE'
	    )
	    AND o.isTemp = FALSE
	    AND (:fromDate IS NULL OR o.orderTime >= :fromDate)
	    AND (:toDate IS NULL OR o.orderTime <= :toDate)
	    AND (:brandId IS NULL OR b.id = :brandId)
	    AND (:categoryId IS NULL OR c.id = :categoryId)
	    GROUP BY p.id, od.productName, od.productThumbnail,
	             b.id, b.brandName, c.id, c.categoryName
	    ORDER BY totalQuantitySold DESC
	    LIMIT :top
	    """, nativeQuery = true)
	List<BestSellingProductDto> getBestSellingProducts(
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate,
	    @Param("brandId") String brandId,
	    @Param("categoryId") String categoryId,
	    @Param("top") int top
	);
	
	
	@Query(value = """
	    SELECT
	        pv.id AS productVariantId,
	        pv.volume AS volume,
	        SUM(od.quantity) AS totalQuantitySold,
	        SUM(od.totalPrice) AS totalRevenue
	    FROM OrderDetail od
	    JOIN `Order` o ON od.orderId = o.id
	    JOIN ProductVariant pv ON od.productVariantId = pv.id
	    JOIN Product p ON pv.productId = p.id
	    WHERE EXISTS (
	        SELECT 1
	        FROM OrderStatus s
	        WHERE s.orderId = o.id
	          AND s.updateTime = (
	              SELECT MAX(s2.updateTime)
	              FROM OrderStatus s2
	              WHERE s2.orderId = o.id
	          )
	          AND s.status = 'COMPLETE'
	    )
	    AND o.isTemp = FALSE
	    AND p.id = :productId
	    AND (:fromDate IS NULL OR o.orderTime >= :fromDate)
	    AND (:toDate IS NULL OR o.orderTime <= :toDate)
	    GROUP BY pv.id, pv.volume
	    ORDER BY totalQuantitySold DESC
	    """, nativeQuery = true)
	List<BestSellingVariantDto> getBestSellingVariantsByProductId(
	    @Param("productId") String productId,
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate
	);
	
	@Query(value = """
	        SELECT
	            DATE_FORMAT(o.orderTime, :format) AS timeUnit,
	            SUM(od.totalPrice) AS totalRevenue
	        FROM `Order` o
	        JOIN OrderDetail od ON od.orderId = o.id
	        JOIN OrderStatus s ON s.orderId = o.id
	        WHERE s.updateTime = (
	            SELECT MAX(s2.updateTime)
	            FROM OrderStatus s2
	            WHERE s2.orderId = o.id
	        )
	        AND s.status = 'COMPLETE'
	        AND o.isTemp = FALSE
	        AND (:fromDate IS NULL OR o.orderTime >= :fromDate)
	        AND (:toDate IS NULL OR o.orderTime <= :toDate)
	        GROUP BY timeUnit
	        ORDER BY timeUnit
	    """, nativeQuery = true)
	List<RevenueByDateDto> getRevenueByDate(
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate,
	    @Param("format") String format
	);
	
	@Query(value = """
	        SELECT
	            DATE_FORMAT(s.updateTime, :format) AS timeUnit,
	            s.status AS status,
	            COUNT(*) AS count
	        FROM OrderStatus s
	        JOIN (
	            SELECT orderId, MAX(updateTime) AS latestTime
	            FROM OrderStatus
	            GROUP BY orderId
	        ) latest ON latest.orderId = s.orderId AND latest.latestTime = s.updateTime
	        WHERE (:fromDate IS NULL OR s.updateTime >= :fromDate)
	          AND (:toDate IS NULL OR s.updateTime <= :toDate)
	        GROUP BY timeUnit, s.status
	        ORDER BY timeUnit ASC
	    """, nativeQuery = true)
	List<OrderStatusDailyDto> getOrderStatusDaily(
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate,
	    @Param("format") String format
	);
	
	@Query(value = """
    SELECT
        s.status AS status,
        COUNT(*) AS count
    FROM OrderStatus s
    JOIN (
        SELECT orderId, MAX(updateTime) AS latestTime
        FROM OrderStatus
        GROUP BY orderId
    ) latest ON latest.orderId = s.orderId AND latest.latestTime = s.updateTime
    JOIN `Order` o ON o.id = s.orderId
    WHERE o.isTemp = FALSE
      AND (:fromDate IS NULL OR o.orderTime >= :fromDate)
      AND (:toDate IS NULL OR o.orderTime <= :toDate)
    GROUP BY s.status
""", nativeQuery = true)
	List<OrderStatusSummaryDto> getOrderStatusSummary(
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate
	);
	
	@Query(value = """
    SELECT
        COUNT(DISTINCT o.id) AS totalOrder,
        COUNT(DISTINCT CASE WHEN s.status = 'COMPLETE' THEN o.id END) AS completeOrder,
        COUNT(DISTINCT CASE WHEN s.status = 'CANCELED' THEN o.id END) AS canceledOrder,
        COUNT(DISTINCT CASE WHEN s.status IN ('PROCESSING', 'SHIPPING') THEN o.id END) AS processingOrder,
        COUNT(DISTINCT CASE WHEN s.status = 'PENDING' THEN o.id END) AS pendingOrder, -- 👈 THÊM DÒNG NÀY
        SUM(CASE WHEN s.status = 'COMPLETE' THEN od.totalPrice ELSE 0 END) AS totalRevenue
    FROM `Order` o
    JOIN (
        SELECT orderId, MAX(updateTime) AS latestTime
        FROM OrderStatus
        GROUP BY orderId
    ) latest ON latest.orderId = o.id
    JOIN OrderStatus s ON s.orderId = o.id AND s.updateTime = latest.latestTime
    JOIN OrderDetail od ON od.orderId = o.id
    WHERE o.isTemp = FALSE
      AND (:fromDate IS NULL OR o.orderTime >= :fromDate)
      AND (:toDate IS NULL OR o.orderTime <= :toDate)
""", nativeQuery = true)
	DashboardOverviewDto getDashboardOverview(
	    @Param("fromDate") LocalDateTime fromDate,
	    @Param("toDate") LocalDateTime toDate
	);
	
	
}
