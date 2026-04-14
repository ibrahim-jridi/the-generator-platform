package com.pfe.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A ReportTemplate.
 */
@Entity
@Table(name = "bs_report_template")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportTemplate  extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    private String type;

    @Column(name = "path")
    private String path;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ReportTemplate id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ReportTemplate type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return this.path;
    }

    public ReportTemplate path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportTemplate{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
