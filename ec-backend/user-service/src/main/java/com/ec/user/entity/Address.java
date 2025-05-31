package com.ec.user.entity;

import com.ec.user.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id = IdGenerator.generateId();
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private boolean isDefault ;
	
	@Column(nullable = false)
	private boolean isDeleted = false;
	
	@Column(nullable = false)
	private String fullName;
	
	@Column(nullable = false)
	private String phone;
}
