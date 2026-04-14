package com.pfe.service.dto;

import com.pfe.config.Constants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -758250879636654971L;
  private UUID id;
  @Size(max = 150)
  @Pattern(regexp = Constants.EMAIL_REGEX, message = "Email should be valid")
  private String email;
  @Size(max = 25)
  private String firstName;
  @Size(max = 25)
  private String lastName;

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDTO that)) {
      return false;
    }
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }


  @Override
  public String toString() {
    return "InternalUserDTO{" +
        "id=" + this.id +
        ", email='" + this.email + '\'' +
        ", firstName='" + this.firstName + '\'' +
        ", lastName='" + this.lastName + '\'' +
        '}';
  }
}
