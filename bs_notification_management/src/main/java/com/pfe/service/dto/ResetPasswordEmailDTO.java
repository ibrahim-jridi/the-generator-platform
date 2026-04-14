package com.pfe.service.dto;

import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

public class ResetPasswordEmailDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -5956216753376639226L;
  @Size(max = 50)
  private String sendTo;
  @Size(max = 50)
  private String username;

  public ResetPasswordEmailDTO(String sendTo, String username) {
    this.sendTo = sendTo;
    this.username = username;
  }

  public String getSendTo() {
    return this.sendTo;
  }

  public void setSendTo(String sendTo) {
    this.sendTo = sendTo;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
