package com.pfe.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pfe.config.Constants;
import com.pfe.dto.GroupDTO;
import com.pfe.dto.RoleDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CreateAccountRequest implements Serializable {

    private static final long serialVersionUID = -7015460470009721454L;
    @NotNull
    @Pattern(regexp = Constants.FIRSTNAME_REGEX, message = Constants.FIRSTNAME_REGEX_ERROR_MSG)
    private String firstName;
    @NotNull
    @Pattern(regexp = Constants.LASTNAME_REGEX, message = Constants.LASTNAME_REGEX_ERROR_MSG)
    private String lastName;
    private String address;
    private String phoneNumber ;
    private String nationality;
    @NotNull
    @Pattern(regexp = Constants.EMAIL_REGEX, message = Constants.EMAIL_REGEX_ERROR_MSG)
    private String email;
    private String nationalId;
    private String country;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String gender;
    @NotNull
    @NotEmpty
    private Set<RoleDTO> roles = new HashSet<>();
    @NotNull
    @NotEmpty
    private Set<GroupDTO> groups = new HashSet<>();

    public CreateAccountRequest(String firstName, String lastName, String address, String phoneNumber,
                                String nationality, String email, String nationalId, String country, LocalDate birthDate,
                                String gender, Set<RoleDTO> roles, Set<GroupDTO> groups) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.email = email;
        this.nationalId = nationalId;
        this.country = country;
        this.birthDate = birthDate;
        this.gender = gender;
        this.roles = roles;
        this.groups = groups;
    }

    public CreateAccountRequest() {
    }

    public Set<GroupDTO> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<GroupDTO> groups) {
        this.groups = groups;
    }

    public Set<RoleDTO> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
