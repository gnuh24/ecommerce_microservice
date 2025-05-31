package com.ec.user.exceptions;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	

    @NonNull
    private Integer status;

    @NonNull
    private String message;

    @NonNull
    private String detailMessage;

    private Object error;

    @NonNull
    private Integer code;

}
