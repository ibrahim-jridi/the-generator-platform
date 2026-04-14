package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pfe.domain.enumeration.Gender;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.domain.enumeration.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A User.
 */
@Entity
@Table(name = "bs_user")
public class User extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1897996422455102372L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "keycloak_id", unique = true)
    private UUID keycloakId;

    @NotNull
    @Column(name = "email", nullable = false, length = 150)
    @Size(min = 4, max = 150)
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @NotNull
    @Column(name = "username", nullable = false, unique = true, length = 25)
    @Size(min = 2, max = 25)
    private String username;

    @Column(name = "first_name", length = 25)
    @Size(max = 25)
    private String firstName;

    @Column(name = "last_name", length = 25)
    @Size(max = 25)
    private String lastName;

    @Column(name = "national_id")
    private String nationalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address", length = 150)
    @Size(max = 150)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality")
    private Nationality nationality;

    @Column(name = "country", length = 100)
    @Size(max = 100)
    private String country;

    @Column(name = "e_barid", length = 25)
    @Size(max = 25)
    private String eBarid;

    @Column(name = "profile_completed")
    private Boolean isProfileCompleted;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "tax_registration")
    private String taxRegistration;

    @Column(name = "social_reason")
    private String socialReason;

    @Column(name = "legal_status")
    private String legalStatus;

    @Column(name = "activity_domain")
    private String activityDomain;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "bs_rel_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties(value = {"createdBy", "authorities", "users"}, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "bs_rel_user_group",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnoreProperties(value = {"createdBy", "parent", "users"}, allowSetters = true)
    private Set<Group> groups = new HashSet<>();

    @Column(name = "file_status")
    private String fileStatus;

    @Column(name = "file_patent")
    private String filePatent;

    @Enumerated(EnumType.STRING)
    @Column(name = "registry_status")
    private RegistryStatus registryStatus;

    @Column(name = "denomination")
    private String denomination;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<ActivityType> activitiesType = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "bs_profile", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")

    private Map<String, String> profile = new HashMap<>();
    public Map<String, String> getProfile() {
        return this.profile;
    }

    public void setProfile(Map<String, String> profile) {
        this.profile = profile;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User id(UUID id) {
        this.setId(id);
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User email(String email) {
        this.setEmail(email);
        return this;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User username(String username) {
        this.setUsername(username);
        return this;
    }

    public Boolean getEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public User emailVerified(Boolean emailVerified) {
        this.setEmailVerified(emailVerified);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }
    public User addRoles(Set<Role> roles){
        this.roles.addAll(roles);
        return this;
    }

    public User removeRole(Role role) {
        this.roles.remove(role);
        return this;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public User groups(Set<Group> groups) {
        this.setGroups(groups);
        return this;
    }

    public Boolean getActive() {
        return this.isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Nationality getNationality() {
        return this.nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Boolean getProfileCompleted() {
        return this.isProfileCompleted;
    }

    public void setProfileCompleted(Boolean profileCompleted) {
        this.isProfileCompleted = profileCompleted;
    }

    public Set<ActivityType> getActivitiesType() {
        return this.activitiesType;
    }

    public void setActivitiesType(Set<ActivityType> activitiesType) {
        this.activitiesType = activitiesType;
    }

    public User addGroup(Group group) {
        this.groups.add(group);
        return this;
    }

    public User removeGroup(Group group) {
        this.groups.remove(group);
        return this;
    }

    public UUID getKeycloakId() {
        return this.keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


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
        if (!(o instanceof User that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.id);
    }

    @Override
    public String toString() {
        return "user{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", gender='" + getGender() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", username='" + getUsername() + "'" +
            ", emailVerified='" + getEmailVerified() + "'" +
            "}";
    }

}
