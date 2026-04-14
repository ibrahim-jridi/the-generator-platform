package com.pfe.security.validation.config;

public class TokenValidatorUtils {

  public static String getTokenFromAuthorization(String authorization) {
    return
        authorization != null && authorization.length() != 0 && authorization.startsWith("[Bearer ")
            ? authorization.substring(8, authorization.length() - 1)
            : null;
  }
}
