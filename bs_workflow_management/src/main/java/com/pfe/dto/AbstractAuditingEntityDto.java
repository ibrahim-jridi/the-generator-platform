package com.pfe.dto;


import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


public class AbstractAuditingEntityDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6096455884067975083L;
    private UUID createdBy;
    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate;
    private int version;
    private Boolean deleted;

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
    public String toString() {
        return "AbstractAuditingEntityDto{" +
            "createdBy=" + this.createdBy +
            ", createdDate=" + this.createdDate +
            ", lastModifiedBy=" + this.lastModifiedBy +
            ", lastModifiedDate=" + this.lastModifiedDate +
            ", version=" + this.version +
            ", deleted=" + this.deleted +
            '}';
    }
}
