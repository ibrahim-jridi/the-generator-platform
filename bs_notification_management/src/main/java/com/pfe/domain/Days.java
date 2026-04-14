package com.pfe.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Days.
 */
@Entity
@Table(name = "bs_days")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Days extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = -4301392507085461220L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Days id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Days name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Days)) {
            return false;
        }
        return getId() != null && getId().equals(((Days) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Days{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

