package com.ec.order.dto.order;

import com.ec.order.entity.Payment.PaymentMethod;
import com.ec.order.entity.Payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private String id;
	private PaymentStatus paymentStatus;
	private PaymentMethod paymentMethod;
	
	public static PaymentDTO fromEntity(com.ec.order.entity.Payment payment) {
		if (payment == null) return null;
		
		return PaymentDTO.builder()
		    .id(payment.getId())
		    .paymentStatus(payment.getPaymentStatus())
		    .paymentMethod(payment.getPaymentMethod())
		    .build();
	}
}
