package com.pfe.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pfe.domain.Authority} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityDTO implements Serializable {

    private static final long serialVersionUID = -5443549196355907246L;
    @NotNull
    private UUID id;
    private String label;
    private String description;
    private Boolean isActive;
    private UUID createdBy;
    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate;
    private int version;
    private Boolean deleted;

    public AuthorityDTO() {
    }

    public AuthorityDTO(@NotNull UUID id, String label, String description, Boolean isActive,
                        UUID createdBy, Instant createdDate, UUID lastModifiedBy, Instant lastModifiedDate,
                        int version, Boolean deleted) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
        this.deleted = deleted;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean active) {
        this.isActive = active;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorityDTO that = (AuthorityDTO) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "id=" + this.id +
            ", label='" + this.label + '\'' +
            ", description='" + this.description + '\'' +
            ", isActive=" + this.isActive +
            ", createdBy=" + this.createdBy +
            ", createdDate=" + this.createdDate +
            ", lastModifiedBy=" + this.lastModifiedBy +
            ", lastModifiedDate=" + this.lastModifiedDate +
            ", version=" + this.version +
            ", deleted=" + this.deleted +
            '}';
    }
}
