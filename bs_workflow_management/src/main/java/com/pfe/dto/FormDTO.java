package com.pfe.dto;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.pfe.domain.Form} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormDTO implements Serializable {

    private static final long serialVersionUID = 2898404359772229339L;
    private UUID id;
    @Size(max = 25)
    private String label;
    @Size(max = 255)
    private String description;
    private String fields;
    private Long version;

    private Boolean isDeleted;
    private Set<SubmissionDTO> submissions = new HashSet<>();

    private UUID createdBy;
    private Instant createdDate;

    private UUID lastModifiedBy;
    private Instant lastModifiedDate;

    public FormDTO(UUID id, String label, String description, String fields, Long version,
                   Boolean isDeleted, Set<SubmissionDTO> submissions, UUID createdBy, Instant createdDate,
                   UUID lastModifiedBy, Instant lastModifiedDate) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.fields = fields;
        this.version = version;
        this.isDeleted = isDeleted;
        this.submissions = submissions;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public FormDTO() {
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

    public String getFields() {
        return this.fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
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

    public Set<SubmissionDTO> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(Set<SubmissionDTO> submissions) {
        this.submissions = submissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormDTO)) {
            return false;
        }

        FormDTO formDTO = (FormDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "FormDTO{" +
            "id=" + this.id +
            ", label='" + this.label + '\'' +
            ", description='" + this.description + '\'' +
            ", fields='" + this.fields + '\'' +
            ", version=" + this.version +
            ", isDeleted=" + this.isDeleted +
            ", createdBy='" + this.createdBy + '\'' +
            ", createdDate=" + this.createdDate +
            ", lastModifiedBy='" + this.lastModifiedBy + '\'' +
            ", lastModifiedDate=" + this.lastModifiedDate +
            '}';
    }
}
