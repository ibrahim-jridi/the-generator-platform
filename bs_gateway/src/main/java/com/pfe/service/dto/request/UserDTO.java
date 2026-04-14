package com.pfe.service.dto.request;

import com.pfe.service.dto.enumeration.Gender;
import com.pfe.service.dto.enumeration.Nationality;
import com.pfe.service.dto.enumeration.RegistryStatus;
import com.pfe.service.dto.enumeration.UserType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;


public class UserDTO implements Serializable {

  private static final long serialVersionUID = 4878398806592067018L;

  private UUID id;

  private UUID keycloakId;

  private String email;

  private Boolean emailVerified;

  private String username;

  private String firstName;

  private String lastName;

  private Boolean isActive;

  private Gender gender;

  private Nationality nationality;

  private String country;

  private String nationalId;

  private String eBarid;

  private LocalDate birthDate;

  private String phoneNumber;

  private String address;

  private String taxRegistration;

  private String socialReason;

  private String legalStatus;

  private String activityDomain;

  private UserType userType;

  private Boolean isProfileCompleted;

  private String password;

  private String fileStatus;

  private String filePatent;

  private RegistryStatus registryStatus;

  private String denomination;

  private UUID createdBy;

  private Instant createdDate;

  private UUID lastModifiedBy;

  private Instant lastModifiedDate = Instant.now();

  private int version;

  private Boolean deleted;

  private Map<String, String> profile;

  public Map<String, String> getProfile() {
    return profile;
  }


  public void setProfile(Map<String, String> profile) {
    this.profile = profile;
  }

  public Boolean getProfileCompleted() {
    return this.isProfileCompleted;
  }

  public void setProfileCompleted(Boolean profileCompleted) {
    this.isProfileCompleted = profileCompleted;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getIsActive() {
    return this.isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Gender getGender() {
    return this.gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean getEmailVerified() {
    return this.emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public Boolean getDeleted() {
    return this.deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }


  public String getCountry() {
    return this.country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getNationalId() {
    return this.nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  public String geteBarid() {
    return this.eBarid;
  }

  public void seteBarid(String eBarid) {
    this.eBarid = eBarid;
  }

  public LocalDate getBirthDate() {
    return this.birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public UUID getKeycloakId() {
    return this.keycloakId;
  }

  public void setKeycloakId(UUID keycloakId) {
    this.keycloakId = keycloakId;
  }

  public Nationality getNationality() {
    return this.nationality;
  }

  public void setNationality(Nationality nationality) {
    this.nationality = nationality;
  }

  public UUID getCreatedBy() {
    return this.createdBy;
  }

  public void setCreatedBy(UUID createdBy) {
    this.createdBy = createdBy;
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

  public UserType getUserType() {
    return this.userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
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

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }


  public String getFileStatus() {
    return this.fileStatus;
  }

  public void setFileStatus(String fileStatus) {this.fileStatus = fileStatus;}

  public String getFilePatent() {
    return this.filePatent;
  }

  public void setFilePatent(String filePatent) {this.filePatent = filePatent;}

  public RegistryStatus getRegistryStatus() {
    return this.registryStatus;
  }

  public void setRegistryStatus(RegistryStatus registryStatus) {this.registryStatus = registryStatus;}

  public String getDenomination() {
    return this.denomination;
  }

  public void setDenomination(String denomination) {this.denomination = denomination;}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDTO userDTO)) {
      return false;
    }

    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, userDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }
  @Override
  public String toString() {
    return "UserDTO{" +
        "id=" + id +
        ", keycloakId=" + keycloakId +
        ", email='" + email + '\'' +
        ", emailVerified=" + emailVerified +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", isActive=" + isActive +
        ", gender=" + gender +
        ", nationality=" + nationality +
        ", country='" + country + '\'' +
        ", nationalId='" + nationalId + '\'' +
        ", eBarid='" + eBarid + '\'' +
        ", birthDate=" + birthDate +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", address='" + address + '\'' +
        ", taxRegistration='" + taxRegistration + '\'' +
        ", socialReason='" + socialReason + '\'' +
        ", legalStatus='" + legalStatus + '\'' +
        ", activityDomain='" + activityDomain + '\'' +
        ", userType=" + userType +
        ", isProfileCompleted=" + isProfileCompleted +
        ", password='" + password + '\'' +
        ", fileStatus='" + fileStatus + '\'' +
        ", filePatent='" + filePatent + '\'' +
        ", registryStatus=" + registryStatus +
        ", denomination='" + denomination + '\'' +
        ", createdBy=" + createdBy +
        ", createdDate=" + createdDate +
        ", lastModifiedBy=" + lastModifiedBy +
        ", lastModifiedDate=" + lastModifiedDate +
        ", version=" + version +
        ", deleted=" + deleted +
        ", profile=" + profile +
        '}';
  }

}
