package com.pfe.domain;

import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bs_waiting_list")
public class WaitingList extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1897996422455102372L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "id_user", unique = true)
    private UUID idUser;

    @NotNull
    @Column(name = "rank")
    private int rank;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "governorate")
    private Governorate governorate;

    @Column(name = "delegation")
    @Size(max = 50)
    private String delegation;

    @Column(name = "municipality")
    @Size(max = 50)
    private String municipality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusWaitingList status;

    @Column(name = "date_renewal")
    private LocalDate dateRenewal;

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(@NotNull UUID idUser) {
        this.idUser = idUser;
    }

    @NotNull
    public int getRank() {
        return rank;
    }

    public void setRank(@NotNull int rank) {
        this.rank = rank;
    }

    public @NotNull Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull Category category) {
        this.category = category;
    }

    public @NotNull Governorate getGovernorate() {
        return governorate;
    }

    public void setGovernorate(@NotNull Governorate governorate) {
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

    public LocalDate getDateRenewal() {
        return dateRenewal;
    }

    public @NotNull StatusWaitingList getStatus() {
        return status;
    }

    public void setStatus(@NotNull StatusWaitingList status) {
        this.status = status;
    }

    public void setDateRenewal(LocalDate dateRenewal) {
        this.dateRenewal = dateRenewal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaitingList that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WaitingList{" +
            "id=" + id +
            ", idUser=" + idUser +
            ", rang=" + rank +
            ", category=" + category +
            ", governorate=" + governorate +
            ", delegation='" + delegation + '\'' +
            ", municipality='" + municipality + '\'' +
            ", status=" + status +
            ", dateRenewal=" + dateRenewal +
            '}';
    }
}
