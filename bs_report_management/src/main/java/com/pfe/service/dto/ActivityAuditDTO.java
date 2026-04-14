package com.pfe.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class ActivityAuditDTO implements Serializable {

    private UUID id;

    private String action;

    private UUID entityId;

    private String entity;

    private String details;

    private UUID submittedBy;
    private Instant submissionDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public UUID getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(UUID submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Instant getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityAuditDTO)) {
            return false;
        }

        ActivityAuditDTO activityAudit = (ActivityAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityAudit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "ActivityAuditDTO{" +
            "id=" + id +
            ", action='" + action + '\'' +
            ", entityId=" + entityId +
            ", entity='" + entity + '\'' +
            ", details='" + details + '\'' +
            ", submittedBy=" + submittedBy +
            ", submissionDate=" + submissionDate +
            '}';
    }
}
