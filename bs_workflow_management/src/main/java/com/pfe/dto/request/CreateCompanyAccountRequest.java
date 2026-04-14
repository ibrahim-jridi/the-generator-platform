package com.pfe.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pfe.config.Constants;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.dto.ActivityTypeDTO;
import com.pfe.dto.RoleDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CreateCompanyAccountRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7029446552400979569L;
    private String address;
    private String phoneNumber;
    @NotNull
    @Pattern(regexp = Constants.EMAIL_REGEX, message = Constants.EMAIL_REGEX_ERROR_MSG)
    private String email;
    private String country;
    private String nationality;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate creationDate;
    private String taxRegistration;
    private String socialReason;
    private String legalStatus;
    private String activityDomain;
    private Set<ActivityTypeDTO> activitiesType = new HashSet<>();

    private String fileStatus;

    private String filePatent;

    private RegistryStatus registryStatus;

    private String denomination;
    @NotNull
    @NotEmpty
    private Set<RoleDTO> roles = new HashSet<>();


    public CreateCompanyAccountRequest() {
    }

    public CreateCompanyAccountRequest(String address, String phoneNumber, String email,
        String country, String nationality, LocalDate creationDate, String taxRegistration,
        String socialReason, String legalStatus, String activityDomain,
        Set<ActivityTypeDTO> activitiesType, String fileStatus, String filePatent,
        RegistryStatus registryStatus, String denomination, Set<RoleDTO> roles) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.country = country;
        this.nationality = nationality;
        this.creationDate = creationDate;
        this.taxRegistration = taxRegistration;
        this.socialReason = socialReason;
        this.legalStatus = legalStatus;
        this.activityDomain = activityDomain;
        this.activitiesType = activitiesType;
        this.fileStatus = fileStatus;
        this.filePatent = filePatent;
        this.registryStatus = registryStatus;
        this.denomination = denomination;
        this.roles = roles;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getTaxRegistration() {
        return this.taxRegistration;
    }

    public void setTaxRegistration(String taxRegistration) {
        this.taxRegistration = taxRegistration;
    }

    public String getSocialReason() {
        return this.socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getLegalStatus() {
        return this.legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getActivityDomain() {
        return this.activityDomain;
    }

    public void setActivityDomain(String activityDomain) {
        this.activityDomain = activityDomain;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<ActivityTypeDTO> getActivitiesType() {
        return this.activitiesType;
    }

    public void setActivitiesType(Set<ActivityTypeDTO> activitiesType) {
        this.activitiesType = activitiesType;
    }

    public String getFileStatus() {return fileStatus;}

    public void setFileStatus(String fileStatus) {this.fileStatus = fileStatus;}

    public String getFilePatent() {return filePatent;}

    public void setFilePatent(String filePatent) {this.filePatent = filePatent;}

    public RegistryStatus getRegistryStatus() {return registryStatus;}

    public void setRegistryStatus(RegistryStatus registryStatus) {this.registryStatus = registryStatus;}

    public String getDenomination() { return denomination;}

    public void setDenomination(String denomination) {this.denomination = denomination;}

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }


}
