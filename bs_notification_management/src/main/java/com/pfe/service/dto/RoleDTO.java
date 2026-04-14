package com.pfe.service.dto;

import com.google.common.base.Objects;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class RoleDTO implements Serializable {

  private static final long serialVersionUID = -2920288367485494447L;

  private UUID id;

  private Boolean isActive;

  @Size(max = 25)
  @NotNull(message = "LABEL_REQUIRED")
  @Pattern(regexp = "^[a-zA-Z_-]+$", message = "INVALID_PATTERN")
  private String label;

  @Size(max = 150)
  private String description;

  private int version;

  private Instant createdDate;

  private boolean hasUsers;


  public RoleDTO() {
  }

  public RoleDTO(UUID id, Boolean isActive, String label, String description, Instant createdDate,
                 int version, boolean hasUsers) {
    this.id = id;
    this.isActive = isActive;
    this.label = label;
    this.createdDate = createdDate;
    this.description = description;
    this.version = version;
    this.hasUsers = hasUsers;
  }

  public Boolean getActive() {
    return this.isActive;
  }

  public void setActive(Boolean active) {
    this.isActive = active;
  }

  public boolean isHasUsers() {
    return this.hasUsers;
  }

  public void setHasUsers(boolean hasUsers) {
    this.hasUsers = hasUsers;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Boolean getIsActive() {
    return this.isActive;
  }

  public void setIsActive(Boolean active) {
    this.isActive = active;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public Instant getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleDTO roleDTO = (RoleDTO) o;
    return Objects.equal(this.id, roleDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id);
  }

  @Override
  public String toString() {
    return "RoleDTO{" +
        "id=" + this.id +
        ", isActive=" + this.isActive +
        ", label='" + this.label + '\'' +
        ", description='" + this.description + '\'' +
        ", createdDate=" + this.createdDate +
        ", version=" + this.version +
        ", hasUsers=" + this.hasUsers +
        '}';
  }
}
