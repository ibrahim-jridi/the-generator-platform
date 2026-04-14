package com.pfe.service.dto;

import com.pfe.domain.ReportTemplate;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ReportTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportTemplateDTO extends  AbstractAuditingEntityDto   implements Serializable {

    private UUID id;

    private String type;

    private String path;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportTemplateDTO)) {
            return false;
        }

        ReportTemplateDTO reportTemplateDTO = (ReportTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportTemplateDTO{" +
            "id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
