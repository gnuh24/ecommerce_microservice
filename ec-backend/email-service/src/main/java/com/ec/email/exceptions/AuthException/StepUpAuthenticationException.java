package com.ec.email.exceptions.AuthException;

import org.springframework.security.core.AuthenticationException;

public class StepUpAuthenticationException extends AuthenticationException {
    public StepUpAuthenticationException(String msg) {
        super(msg);
    }
}
