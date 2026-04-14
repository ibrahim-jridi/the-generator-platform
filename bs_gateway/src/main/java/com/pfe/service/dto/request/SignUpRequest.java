package com.pfe.service.dto.request;


import java.time.LocalDate;
import java.util.UUID;

public class SignUpRequest {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String nationality;
  private String country;
  private String nationalId;
  private String eBarid;
  private String passportAttachment;
  private String passportNumber;
  private String gender;
  private String age;
  private LocalDate birthDate;
  private String phoneNumber;
  private String address;

  public SignUpRequest() {
  }

  public SignUpRequest(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getNationality() {
    return this.nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
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

  public String getPassportAttachment() {
    return this.passportAttachment;
  }

  public void setPassportAttachment(String passportAttachment) {
    this.passportAttachment = passportAttachment;
  }

  public String getPassportNumber() {
    return this.passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getAge() {
    return this.age;
  }

  public void setAge(String age) {
    this.age = age;
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

  public void setAddress(String adress) {
    this.address = adress;
  }
}
