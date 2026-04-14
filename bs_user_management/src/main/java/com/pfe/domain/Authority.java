package com.pfe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 * A Authority.
 */
@Entity
@Table(name = "bs_authority")
public class Authority extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = -8621380081030425515L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "label")
    private String label;

    @Column(name = "description", length = 150)
    @Size(max = 150)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "first_insert")
    private Boolean firstInsert;


    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Authority id(UUID id) {
        this.setId(id);
        return this;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Authority isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Authority label(String label) {
        this.setLabel(label);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Authority description(String description) {
        this.setDescription(description);
        return this;
    }

    public Boolean getFirstInsert() {
        return this.firstInsert;
    }

    public void setFirstInsert(Boolean firstInsert) {
        this.firstInsert = firstInsert;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return getId() != null && getId().equals(((Authority) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Authority{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
