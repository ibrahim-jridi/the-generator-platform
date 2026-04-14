package com.pfe.dto;

import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pfe.domain.Submission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionDTO implements Serializable {

    private static final long serialVersionUID = -7971966700392805010L;
    private UUID id;
    @Size(max = 50)
    private String key;
    private String value;
    @Size(min = 3, max = 50)
    private String instanceTaskId;
    private FormDTO form;
    private String formId;
    private UUID createdBy;
    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate;

    public SubmissionDTO(UUID id, String key, String value, String instanceTaskId, FormDTO form,
                         String formId, UUID createdBy, Instant createdDate, UUID lastModifiedBy,
                         Instant lastModifiedDate) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.instanceTaskId = instanceTaskId;
        this.form = form;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.formId = formId;
    }

    public SubmissionDTO() {
    }

    public String getFormId() {
        return this.formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInstanceTaskId() {
        return this.instanceTaskId;
    }

    public void setInstanceTaskId(String instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }

    public FormDTO getForm() {
        return this.form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
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

    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + this.id +
            ", key='" + this.key + '\'' +
            ", value='" + this.value + '\'' +
            ", instanceTaskId='" + this.instanceTaskId + '\'' +
            ", form=" + this.form +
            ", createdBy='" + this.createdBy + '\'' +
            ", createdDate=" + this.createdDate +
            ", lastModifiedBy='" + this.lastModifiedBy + '\'' +
            ", lastModifiedDate=" + this.lastModifiedDate +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionDTO that)) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
