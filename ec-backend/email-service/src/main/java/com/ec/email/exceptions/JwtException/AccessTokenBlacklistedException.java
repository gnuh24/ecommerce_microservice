package com.ec.email.exceptions.JwtException;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenBlacklistedException extends AuthenticationException {
    public AccessTokenBlacklistedException(String message) {
        super(message);
    }
}