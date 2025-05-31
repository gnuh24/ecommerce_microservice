package com.ec.user.entity;

import com.ec.user.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id = IdGenerator.generateId();
	
	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String role;
	
	@Column(nullable = false)
	private String status;
	
	private String avatar;
}


