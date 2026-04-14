package com.pfe.service.dto.request;

import java.util.Objects;

public class TokenRequest {

  private String refreshToken;

  public TokenRequest() {
  }

  public TokenRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenRequest that = (TokenRequest) o;
    return Objects.equals(refreshToken, that.refreshToken);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(refreshToken);
  }

  @Override
  public String toString() {
    return "TokenRequest{" +
        "refreshToken='" + refreshToken + '\'' +
        '}';
  }
}
