package com.pfe.service.dto.request;

import java.io.Serializable;

public class ChangePasswordRequestDTO implements Serializable {

  private String username;
  private String email;

  public ChangePasswordRequestDTO() {
  }

  public ChangePasswordRequestDTO(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "ChangePasswordRequestDTO{" +
        "username='" + username + '\'' +
        ", email='" + email + '\'' +
        '}';
  }

}
