package com.pfe.service.dto;

import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class WaitingListDTO implements Serializable {

    private static final long serialVersionUID = 4878398806592067018L;
    private UUID id;
    private UUID idUser;

    private int rank;

    private Category category;

    private Governorate governorate;
    @Size(max = 50)
    private String delegation;
    @Size(max = 50)
    private String municipality;
    private StatusWaitingList status;
    private LocalDate dateRenewal;
    private UUID createdBy;

    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate = Instant.now();
    private int version;
    private Boolean deleted;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Governorate getGovernorate() {
        return governorate;
    }

    public void setGovernorate(Governorate governorate) {
        this.governorate = governorate;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    public StatusWaitingList getStatus() {
        return status;
    }

    public void setStatus(StatusWaitingList status) {
        this.status = status;
    }

    public LocalDate getDateRenewal() {
        return dateRenewal;
    }

    public void setDateRenewal(LocalDate dateRenewal) {
        this.dateRenewal = dateRenewal;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaitingListDTO that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WaitingListDTO{" +
            "id=" + id +
            ", idUser=" + idUser +
            ", rank=" + rank +
            ", category=" + category +
            ", governorate=" + governorate +
            ", delegation='" + delegation + '\'' +
            ", municipality='" + municipality + '\'' +
            ", status=" + status +
            ", dateRenewal=" + dateRenewal +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy=" + lastModifiedBy +
            ", lastModifiedDate=" + lastModifiedDate +
            ", version=" + version +
            ", deleted=" + deleted +
            '}';
    }
}
