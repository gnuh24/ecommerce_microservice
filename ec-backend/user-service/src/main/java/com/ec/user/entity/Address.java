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
	
	private String title;
	
	private String address;
	
	@Column(nullable = false)
	private boolean isDefault ;
	
	@Column(nullable = false)
	private boolean isDeleted = false;
	
	private String fullName;
	
	private String phone;
}
