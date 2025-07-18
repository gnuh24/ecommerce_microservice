package com.ec.email.exceptions.JwtException;

import org.springframework.security.core.AuthenticationException;

public class InvalidJWTSignatureException extends AuthenticationException {
    public InvalidJWTSignatureException(String message) {
        super(message);
    }
}
