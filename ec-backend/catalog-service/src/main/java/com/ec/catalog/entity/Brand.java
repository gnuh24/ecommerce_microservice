package com.ec.catalog.entity;

import com.ec.catalog.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Brand")
@Data
public class Brand {

    @Id
    @Column(length = 10)
    private String id = IdGenerator.generateId();

    @Column(length = 100, nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false)
    private Integer productCount = 0;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
