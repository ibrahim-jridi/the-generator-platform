package com.pfe.security.validation.error;

import java.io.Serial;
import org.springframework.security.core.AuthenticationException;

public class TokenValidatorException extends AuthenticationException {

  @Serial
  private static final long serialVersionUID = -756456100716683401L;

  public TokenValidatorException(String message) {
    this(message, null);
  }

  public TokenValidatorException(Throwable cause) {
    this("", cause);
  }

  public TokenValidatorException(String message, Throwable cause) {
    super(message, cause);
  }
}
