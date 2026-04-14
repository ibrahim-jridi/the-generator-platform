package com.pfe.service.dto.request;

public class ResetPasswordRequestDTO {

  private String userEmail;
  private String username;

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
