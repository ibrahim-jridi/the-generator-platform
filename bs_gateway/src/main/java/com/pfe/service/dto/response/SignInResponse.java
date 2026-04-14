package com.pfe.service.dto.response;

import java.util.Objects;
import java.util.UUID;
import org.keycloak.representations.AccessTokenResponse;

public class SignInResponse extends AccessTokenResponse {

  private boolean isEmailVerified;
  private UUID userId;
  private boolean isProfileCompleted;
  private String email;
  private String gender;
  private String firstName;
  private String lastName;
  private String username;
  private String nationality;
  private String country;
  private String cin;
  private String eBarid;
  private String passportAttachment;
  private String passportNumber;
  private String birthDate;
  private String phoneNumber;
  private String address;
  private String age;

  public SignInResponse() {
  }

  public SignInResponse(boolean isEmailVerified, UUID userId, boolean isProfileCompleted) {
    this.isEmailVerified = isEmailVerified;
    this.userId = userId;
    this.isProfileCompleted = isProfileCompleted;
  }

  public boolean isEmailVerified() {
    return isEmailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.isEmailVerified = emailVerified;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public boolean isProfileCompleted() {
    return isProfileCompleted;
  }

  public void setProfileCompleted(boolean profileCompleted) {
    this.isProfileCompleted = profileCompleted;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCin() {
    return cin;
  }

  public void setCin(String cin) {
    this.cin = cin;
  }

  public String geteBarid() {
    return eBarid;
  }

  public void seteBarid(String eBarid) {
    this.eBarid = eBarid;
  }

  public String getPassportAttachment() {
    return passportAttachment;
  }

  public void setPassportAttachment(String passportAttachment) {
    this.passportAttachment = passportAttachment;
  }

  public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignInResponse that = (SignInResponse) o;
    return Objects.equals(this.userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  @Override
  public String toString() {
    return "SignInResponse{" +
        "isEmailVerified=" + isEmailVerified +
        ", userId=" + userId +
        ", isProfileCompleted=" + isProfileCompleted +
        ", email='" + email + '\'' +
        ", gender=" + gender +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", username='" + username + '\'' +
        ", nationality='" + nationality + '\'' +
        ", country='" + country + '\'' +
        ", cin='" + cin + '\'' +
        ", eBarid='" + eBarid + '\'' +
        ", passportAttachment='" + passportAttachment + '\'' +
        ", passportNumber='" + passportNumber + '\'' +
        ", birthDate=" + birthDate +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", address='" + address + '\'' +
        ", age='" + age + '\'' +
        ", token='" + getToken() + '\'' +
        ", expiresIn=" + getExpiresIn() +
        ", refreshExpiresIn=" + getRefreshExpiresIn() +
        ", refreshToken='" + getRefreshToken() + '\'' +
        ", tokenType='" + getTokenType() + '\'' +
        ", idToken='" + getIdToken() + '\'' +
        ", notBeforePolicy=" + getNotBeforePolicy() +
        ", sessionState='" + getSessionState() + '\'' +
        ", otherClaims=" + getOtherClaims() +
        ", scope='" + getScope() + '\'' +
        ", error='" + getError() + '\'' +
        ", errorDescription='" + getErrorDescription() + '\'' +
        ", errorUri='" + getErrorUri() + '\'' +
        '}';
  }
}
