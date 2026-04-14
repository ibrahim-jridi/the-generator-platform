package com.pfe.dto;

import java.util.Objects;
import java.util.UUID;

public class ActivityTypeDTO extends AbstractAuditingEntityDto {

  private static final long serialVersionUID = 7762839630867250026L;
  private UUID id;
  private String code;
  private String name;

  private UUID userId;

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
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
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

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }
}
