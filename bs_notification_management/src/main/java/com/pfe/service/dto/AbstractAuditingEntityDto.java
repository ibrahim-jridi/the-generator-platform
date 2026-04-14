package com.pfe.service.dto;

import com.pfe.domain.AbstractAuditingEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link AbstractAuditingEntity}
 */

@AllArgsConstructor
@NoArgsConstructor
public class AbstractAuditingEntityDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 7408446887615286879L;
  private UUID id;

  private UUID createdBy;
  private Instant createdDate;

  private UUID lastModifiedBy;
  private Instant lastModifiedDate;
  private int version;
  private Boolean deleted;

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractAuditingEntityDto that)) {
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
    return "AbstractAuditingEntityDto{" +
        "id=" + this.id +
        ", createdBy='" + this.createdBy + '\'' +
        ", createdDate=" + this.createdDate +
        ", lastModifiedBy='" + this.lastModifiedBy + '\'' +
        ", lastModifiedDate=" + this.lastModifiedDate +
        ", deleted=" + this.deleted +
        '}';
  }
}
