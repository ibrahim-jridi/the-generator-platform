package com.pfe.service.dto.request;


import java.util.Objects;

public class SignInRequest {

  private String username;
  private String fiscalIdOrUsername;
  private String password;

  public SignInRequest() {
  }

  public SignInRequest(String username,String fiscalId, String password) {
    this.username = username;
    this.fiscalIdOrUsername = fiscalId;
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignInRequest that = (SignInRequest) o;
    return Objects.equals(this.username, that.username) && Objects.equals(this.password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.username, this.password);
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFiscalIdOrUsername() {
    return fiscalIdOrUsername;
  }

  public void setFiscalIdOrUsername(String fiscalId) {
    this.fiscalIdOrUsername = fiscalId;
  }
}
