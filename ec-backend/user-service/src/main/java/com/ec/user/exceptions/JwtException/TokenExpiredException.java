package com.ec.user.exceptions.JwtException;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
  public TokenExpiredException(String message) {
    super(message);
  }
}
