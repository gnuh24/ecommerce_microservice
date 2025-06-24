package com.ec.catalog.entity;

import com.ec.catalog.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductImage")
@Data
public class ProductImage {
	
	@Id
	@Column(length = 10)
	private String id = IdGenerator.generateId();
	
	@Column(length = 1024, nullable = false)
	private String imageUrl;
	
	@Column(nullable = false)
	private Boolean isThumbnail = false;
	
	@Column(nullable = false)
	private Boolean isDeleted = false;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime deletedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	private Product product;
}
