package com.pfe.service.dto;

import com.pfe.domain.enumeration.ErrorStatus;
import com.pfe.domain.enumeration.ErrorType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class ErrorAuditDTO extends  AbstractAuditingEntityDto  implements Serializable {

    private UUID id;

    private String url;

    private String method;

    private String description;

    private ErrorType errorType;

    private ErrorStatus errorStatus;

    private Instant createdDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    @Override
    public Instant getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorAuditDTO)) {
            return false;
        }

        ErrorAuditDTO errorAuditDTO = (ErrorAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, errorAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ErrorAuditDTO{" +
            "id='" + getId() + "'" +
            ", url='" + getUrl() + "'" +
            ", method='" + getMethod() + "'" +
            ", description='" + getDescription() + "'" +
            ", errorType='" + getErrorType() + "'" +
            ", errorStatus='" + getErrorStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
