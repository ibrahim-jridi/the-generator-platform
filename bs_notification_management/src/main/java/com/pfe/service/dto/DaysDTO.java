package com.pfe.service.dto;

import com.pfe.domain.Days;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link Days} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DaysDTO implements Serializable {

    private UUID id;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DaysDTO)) {
            return false;
        }

        DaysDTO daysDTO = (DaysDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, daysDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DaysDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
