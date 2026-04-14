package com.pfe.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ActivityTypeDTO implements Serializable {

  private static final long serialVersionUID = 7762839630867250026L;
  private UUID id;
  private String code;
  private String name;

  private UUID userId;
  private UUID createdBy;

  private Instant createdDate;
  private UUID lastModifiedBy;
  private Instant lastModifiedDate = Instant.now();
  private int version;
  private Boolean deleted;

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

    public UUID getId() {
      return this.id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

  public UUID getUserId() {
    return this.userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }
    
  public UUID getCreatedBy() {
    return this.createdBy;
  }

  public void setCreatedBy(UUID createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public UUID getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  public void setLastModifiedBy(UUID lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Instant getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(Instant lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public Boolean getDeleted() {
    return this.deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityTypeDTO activityTypeDTO)) {
      return false;
    }

    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, activityTypeDTO.id);
  }

}
