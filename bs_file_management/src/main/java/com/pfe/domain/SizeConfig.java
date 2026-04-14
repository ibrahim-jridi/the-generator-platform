package com.pfe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bs_size_config")
public class SizeConfig extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 6557920337150760975L;
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;
    @NotNull
    @Column(name = "extension", nullable = false, length = 25)
    @Size(min = 1, max = 25)
    private String extension;
    @NotNull
    @Column(name = "max_size", nullable = false)
    private Double maxSize;

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Double getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(Double maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SizeConfig sizeConfig = (SizeConfig) o;
        return Objects.equals(this.id, sizeConfig.id);
    }


    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "SizeConfig{" +
            "id=" + getId() +
            ", extension='" + getExtension() + '\'' +
            ", maxSize=" + getMaxSize() +
            '}';
    }


}
