package com.pfe.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UserDenominationDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 6668854335015545109L;
  private String username;
  private String denomination;


  public UserDenominationDTO() {
  }

  public UserDenominationDTO(String username, String denomination) {
    this.username = username;
    this.denomination = denomination;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDenomination() {
    return denomination;
  }

  public void setDenomination(String denomination) {
    this.denomination = denomination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDenominationDTO that)) {
      return false;
    }

    if (this.username == null) {
      return false;
    }
    return Objects.equals(this.username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.username);
  }

  @Override
  public String toString() {
    return "UserDenominationDTO{" +
        "username='" + getUsername() + "'" +
        ", denomination='" + getDenomination() + "'" +
        "}";
  }
}
