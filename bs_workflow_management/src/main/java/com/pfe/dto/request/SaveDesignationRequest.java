package com.pfe.dto.request;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SaveDesignationRequest implements Serializable {

  private static final long serialVersionUID = 4892042290523446782L;

  private String owner;
  private String designated;
  private String role;
  private UUID pmUserId;
  private String laboratoryId;

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getDesignated() {
    return designated;
  }

  public void setDesignated(String designated) {
    this.designated = designated;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

    public UUID getPmUserId() {
        return pmUserId;
    }

    public void setPmUserId(UUID pmUserId) {
        this.pmUserId = pmUserId;
    }
    public String getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
    }


    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SaveDesignationRequest that)) return false;
    return Objects.equals(owner, that.owner) && Objects.equals(designated, that.designated)&& Objects.equals(role, that.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, designated,role);
  }

  @Override
  public String toString() {
    return "SaveDesignationRequest{" +
        "owner=" + owner +
        ", designate=" + designated +
        ", role=" + role +
        ", laboratoryId=" + laboratoryId +
        '}';
  }
}
