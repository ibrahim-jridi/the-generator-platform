package com.pfe.validator.impl;

import com.pfe.config.Constants;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.repository.GroupRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.service.dto.request.CreateAccountRequest;
import com.pfe.validator.ICreateAccountRequestValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("createAccountRequestValidator")
public class CreateAccountRequestValidator implements ICreateAccountRequestValidator {


  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final GroupRepository groupRepository;
  private final Pattern emailPattern;
  private final Pattern cinNationalIdPattern;
  private final Pattern passportNationalIdPattern;
  private final Pattern phoneNumberPattern;
  private final Pattern firstNameLastNamePattern;
  private final Pattern dateFormatPattern;

  private static final String REGEX_EMAIL_PATTERN = Constants.EMAIL_REGEX;
  private static final String REGEX_TUNISIAN_PHONE_NUMBER_PATTERN = Constants.TUNISIAN_PHONE_NUMBER_REGEX;
  private static final String REGEX_CIN_NATIONAL_ID_PATTERN = Constants.CIN_NATIONAL_ID_REGEX;
  private static final String REGEX_FIRST_NAME_LAST_NAME_PATTERN = Constants.FIRSTNAME_REGEX;
  private static final String REGEX_PASSPORT_NATIONAL_ID_PATTERN = Constants.PASSPORT_REGEX;
  private static final String REGEX_DATE_FORMAT = Constants.DATAE_FORMAT_REGEX;
  private static final String ERROR_MESSAGE_MAX_SIZE = "Max size required is 50 characters";
  private static final String ERROR_FIRST_NAME_REQUIRED = "Firstname is required";
  private static final String ERROR_LAST_NAME_REQUIRED = "Lastname is required";
  private static final String ERROR_NATIONALITY_REQUIRED = "Nationality is required";
  private static final String ERROR_EMAIL_REQUIRED = "Email is required";
  private static final String ERROR_PHONE_NUMBER_REQUIRED = "PhoneNumber is required";
  private static final String ERROR_ADRESS_REQUIRED = "Adress is required";
  private static final String ERROR_GROUP_REQUIRED = "Group is required";
  private static final String ERROR_ROLE_REQUIRED = "Role is required";
  private static final String ERROR_NATIONAL_ID_REQUIRED = "NationalId is required";
  private static final String ERROR_NATIONAL_ID_ALREADY_EXIST = "NationalId is already exist";
  private static final String ERROR_USER_WITH_NATIONAL_ID_EXIST = "User with the same nationalId already exist";
  private static final String ERROR_GROUP_DOES_NOT_EXIST = "Group label does not exist";
  private static final String ERROR_ROLE_DOES_NOT_EXIST = "Role label does not exist";
  private static final String INVALID_PHONE_NUMBER_PATTERN = "invalid phone number pattern";
  private static final String INVALID_CIN_NATIONAL_ID_PATTERN = "Invalid cin national id pattern";
  private static final String INVALID_PASSPORT_NATIONAL_ID_PATTERN = "Invalid passport national id pattern";
  private static final String INVALID_EMAIL_PATTERN = "Email pattern is invalid";
  private static final String ERROR_PHONE_NUMBER_ALREADY_EXIST = "Phone number already exist";
  private static final String ERROR_COUNTRY_IS_REQUIRED = "Country is required";
  private static final String ERROR_GENDER_REQUIRED = "Gender is required";
  private static final String ERROR_BIRTHDATE_REQUIRED = "Birthdate is required";
  private static final String INVALID_BIRTHDATE_FORMAT = "Invalid birthdate format";
  private static final String ERROR_INVALID_LENGTH_ADDRESS = "Min size for address is 10 ";
  private static final String ERROR_IN_NATIONALITY_AND_COUNTRY = "Error in nationality and country";
  public CreateAccountRequestValidator(UserRepository userRepository,
      RoleRepository roleRepository, GroupRepository groupRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.groupRepository = groupRepository;
    this.firstNameLastNamePattern = Pattern.compile(REGEX_FIRST_NAME_LAST_NAME_PATTERN);
    this.emailPattern = Pattern.compile(REGEX_EMAIL_PATTERN);
    this.cinNationalIdPattern = Pattern.compile(REGEX_CIN_NATIONAL_ID_PATTERN);
    this.passportNationalIdPattern = Pattern.compile(REGEX_PASSPORT_NATIONAL_ID_PATTERN);
    this.phoneNumberPattern = Pattern.compile(REGEX_TUNISIAN_PHONE_NUMBER_PATTERN);
    this.dateFormatPattern = Pattern.compile(REGEX_DATE_FORMAT);
  }

  @Override
  public void beforeUpdate(CreateAccountRequest updateRequest) {

  }

  @Override
  public void beforeSave(CreateAccountRequest createRequest) {
    List<Reason> reasons = new ArrayList<>();

    if (createRequest.getNationalId().isEmpty()) {
      reasons.add(new Reason<>("NATIONAL_ID_COULD_NOT_BE_NULL", ERROR_NATIONAL_ID_REQUIRED));
    } else {
      User userWithNationalIdExist = this.userRepository.findByNationalIdAndDeletedFalse(
          createRequest.getNationalId());
      Matcher cinNationalIdMatcher = this.cinNationalIdPattern.matcher(
          createRequest.getNationalId());
      Matcher passportNationalIdMatcher = this.passportNationalIdPattern.matcher(
          createRequest.getNationalId());
      if (userWithNationalIdExist != null) {
        reasons.add(new Reason<>("NATIONAL_ID_EXIST", ERROR_NATIONAL_ID_ALREADY_EXIST));
      } else {
        // case cin
        if (createRequest.getNationality().toUpperCase().equals(Nationality.TUNISIAN.toString())) {
          if (!cinNationalIdMatcher.matches()) {
            reasons.add(
                new Reason<>("INVALID_CIN_NATIONAL_ID_PATTERN", INVALID_CIN_NATIONAL_ID_PATTERN));
          }
        } else {
          //case passport
          if (!passportNationalIdMatcher.matches()) {
            reasons.add(new Reason<>("INVALID_PASSPORT_NATIONAL_ID_PATTERN",
                INVALID_PASSPORT_NATIONAL_ID_PATTERN));
          }
        }
      }
    }

    if (createRequest.getPhoneNumber().isEmpty()) {
      reasons.add(new Reason<>("PHONE_NUMBER_COULD_NOT_BE_NULL", ERROR_PHONE_NUMBER_REQUIRED));
    } else {
      Matcher tunisianPhoneNumberMatcher = this.phoneNumberPattern.matcher(
          createRequest.getPhoneNumber());
      User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
          createRequest.getPhoneNumber());
      if (userWithPhoneNumberExist != null) {
        reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", ERROR_PHONE_NUMBER_ALREADY_EXIST));
      } else {
        if (createRequest.getNationality().toUpperCase().equals(Nationality.TUNISIAN.toString())
            && !tunisianPhoneNumberMatcher.matches()) {
          reasons.add(new Reason<>("PHONE_NUMBER_IVALID_PATTERN", INVALID_PHONE_NUMBER_PATTERN));
        }
      }
    }

    if (createRequest.getLastName().isEmpty()) {
      reasons.add(new Reason<>("LAST_NAME_COULD_NOT_BE_NULL", ERROR_LAST_NAME_REQUIRED));
    } else {
      Matcher lastNameMatcher = this.firstNameLastNamePattern.matcher(createRequest.getLastName());
      if (createRequest.getLastName().length() > 50) {
        reasons.add(new Reason<>("LAST_NAME_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
      }
      if (!lastNameMatcher.matches()) {
        reasons.add(new Reason<>("INVALID_LAST_NAME_PATTERN", REGEX_FIRST_NAME_LAST_NAME_PATTERN));
      }
    }

    if (createRequest.getFirstName().isEmpty()) {
      reasons.add(new Reason<>("FIRST_NAME_COULD_NOT_BE_NULL", ERROR_FIRST_NAME_REQUIRED));
    } else {
      Matcher fisrtNameMatcher = this.firstNameLastNamePattern.matcher(
          createRequest.getFirstName());
      if (createRequest.getFirstName().length() > 50) {
        reasons.add(new Reason<>("FIRST_NAME_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
      }
      if (!fisrtNameMatcher.matches()) {
        reasons.add(new Reason<>("INVALID_FIRST_NAME_PATTERN", REGEX_FIRST_NAME_LAST_NAME_PATTERN));
      }
    }

    if (createRequest.getEmail().isEmpty()) {
      reasons.add(new Reason<>("EMAIL_COULD_NOT_BE_NULL", ERROR_EMAIL_REQUIRED));
    } else {
      Matcher emailMatcher = this.emailPattern.matcher(createRequest.getEmail());
      if (!emailMatcher.matches()) {
        reasons.add(new Reason<>("INVALID_EMAIL_PATTERN", INVALID_EMAIL_PATTERN));
      }
    }

    if (createRequest.getAddress().isEmpty()) {
      reasons.add(new Reason<>("ADRESS_COULD_NOT_BE_NULL", ERROR_ADRESS_REQUIRED));
    } else if (createRequest.getAddress().length() < 10) {
      reasons.add(new Reason<>("INVALID_LENGTH_ADDRESS", ERROR_INVALID_LENGTH_ADDRESS));
    }
    if (createRequest.getNationality().isEmpty()) {
      reasons.add(new Reason<>("NATIONALITY_IS_REQUIRED", ERROR_NATIONALITY_REQUIRED));
    }
    if (createRequest.getCountry().isEmpty()) {
      reasons.add(new Reason<>("COUNTRY_IS_REQUIRED", ERROR_COUNTRY_IS_REQUIRED));
    }
    if (createRequest.getGender().isEmpty()) {
      reasons.add(new Reason<>("GENDER_COULD_NOT_BE_NULL", ERROR_GENDER_REQUIRED));
    }
    if (createRequest.getBirthDate() == null) {
      reasons.add(new Reason<>("BIRTHDATE_IS_REQUIRED", ERROR_BIRTHDATE_REQUIRED));
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      String formattedBirthDate = createRequest.getBirthDate().format(formatter);
      Matcher birthdateMatcher = this.dateFormatPattern.matcher(
          formattedBirthDate);
      if (!birthdateMatcher.matches()) {
        reasons.add(new Reason<>("INVALID_BIRTHDATE_FORMAT", INVALID_BIRTHDATE_FORMAT));
      }
    }
    if (createRequest.getGroups() == null || createRequest.getGroups().isEmpty()) {
      reasons.add(new Reason<>("GROUPS_ARE_REQUIRED", ERROR_GROUP_REQUIRED));
    } else {
      createRequest.getGroups().forEach(groupe -> {
        if (!this.groupRepository.existsByLabelAndDeletedFalse(groupe.getLabel())) {
          reasons.add(new Reason<>("GROUP_WITH_LABEL_DOES_NOT_EXIST:" + groupe.getLabel(),
              ERROR_GROUP_DOES_NOT_EXIST));
        }
      });
      if (createRequest.getGroups() == null || createRequest.getGroups().isEmpty()) {
        reasons.add(new Reason<>("GROUPS_ARE_REQUIRED", ERROR_GROUP_REQUIRED));
      } else {
        createRequest.getGroups().forEach(groupe -> {
          if (!this.groupRepository.existsByLabelAndDeletedFalse(groupe.getLabel())) {
            reasons.add(new Reason<>("GROUP_WITH_LABEL_DOES_NOT_EXIST:" + groupe.getLabel(),
                ERROR_GROUP_DOES_NOT_EXIST));
          }
        });

      }

      if (createRequest.getRoles() == null || createRequest.getRoles().isEmpty()) {
        reasons.add(new Reason<>("ROLES_ARE_REQUIRED", ERROR_ROLE_REQUIRED));
      } else {
        createRequest.getRoles().forEach(role -> {
          if (!this.roleRepository.existsByLabelAndDeletedFalse(role.getLabel())) {
            reasons.add(new Reason<>("ROLE_WITH_LABEL_DOES_NOT_EXIST:" + role.getLabel(),
                ERROR_ROLE_DOES_NOT_EXIST));
          }
        });

        if (!reasons.isEmpty()) {
          throw new ValidationException(reasons);
        }
      }
    }}}
