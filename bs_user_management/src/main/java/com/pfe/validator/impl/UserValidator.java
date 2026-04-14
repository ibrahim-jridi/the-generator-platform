package com.pfe.validator.impl;

import com.pfe.config.Constants;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.domain.enumeration.UserType;
import com.pfe.repository.GroupRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.validator.IUserValidator;
import com.pfe.security.Jwt.JwtTokenUtil;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("userValidator")
public class UserValidator implements IUserValidator {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final JwtTokenUtil jwtTokenUtil;
  private final GroupRepository groupRepository;
  private final Pattern emailPattern;
  private final Pattern cinNationalIdPattern;
  private final Pattern passportNationalIdPattern;
  private final Pattern phoneNumberPattern;
  private final Pattern firstNameLastNamePattern;
  private final Pattern dateFormatPattern;
  private final Pattern passwordPattern;

  private final Pattern addressPattern;
  private static final String ERROR_USER_DOES_NOT_EXIST = "User does not exist";
  private static final String ERROR_ID_IS_REQUIRED = "User id is required";
  private static final String ERROR_STATUS_REQUIRED = "Status cannot be null";
  private static final String REGEX_EMAIL_PATTERN = Constants.EMAIL_REGEX;
  private static final String REGEX_TUNISIAN_PHONE_NUMBER_PATTERN = Constants.TUNISIAN_PHONE_NUMBER_REGEX;
  private static final String REGEX_CIN_NATIONAL_ID_PATTERN = Constants.CIN_NATIONAL_ID_REGEX;
  private static final String REGEX_FIRST_NAME_LAST_NAME_PATTERN = Constants.FIRSTNAME_REGEX;
  private static final String REGEX_PASSPORT_NATIONAL_ID_PATTERN = Constants.PASSPORT_REGEX;
  private static final String REGEX_PASSWORD_PATTERN = Constants.PASSWORD_REGEX;
  private static final String REGEX_DATE_FORMAT = Constants.DATAE_FORMAT_REGEX;
  private static final String ADDRESS_REGEX = Constants.ADDRESS_REGEX;
  private static final String ERROR_MESSAGE_MAX_SIZE = "Max size required is 50 characters";
  private static final String FIRST_NAME_REQUIRED = "Firstname is required";
  private static final String REQUIRED_LAST_NAME = "Lastname is required";
  private static final String NATIONALITY_REQUIRED = "Nationality is required";
  private static final String REQUIRED_GROUPS = "Group is required";
  private static final String REQUIRED_ROLE = "Role is required";
  private static final String ERROR_NATIONAL_ID_REQUIRED = "NationalId is required";
  private static final String ERROR_GROUP_DOES_NOT_EXIST = "Group label does not exist";
  private static final String ERROR_ROLE_DOES_NOT_EXIST = "Role label does not exist";
  private static final String INVALID_EMAIL_PATTERN = "Email pattern is invalid";
  private static final String COUNTRY_IS_REQUIRED = "Country is required";
  private static final String GENDER_REQUIRED = "Gender is required";
  private static final String REQUIRED_BIRTHDATE = "Birthdate is required";
  private static final String INVALID_BIRTHDATE_FORMAT = "Invalid birthdate format";
  private static final String ERROR_NATIONAL_ID_ALREADY_EXIST = "NationalId is already exist";
  private static final String INVALID_CIN_NATIONAL_ID_PATTERN = "Invalid cin national id pattern";
  private static final String INVALID_PASSPORT_NATIONAL_ID_PATTERN = "Invalid passport national id pattern";
  private static final String BIRTHDATE_TOO_RECENT = "User must be at least 18 years old";
  private static final String BIRTHDATE_TOO_OLD = "Birth date indicates age over 100 years.";
  private static final String PHONE_NUMBER_ALREADY_EXIST = "User with the same poneNumber exist";
  private static final String TAX_REGISTRATION_ALREADY_EXIST = "Tax registration already exist";
  private static final String INVALID_ADDRESS_PATTERN = "Address should have at least 10 characters";
  private static final String PHONE_DOES_NOT_MATCH_PATTERN = "Invalid phone number pattern";
  private static final String REQUIRED_DENOMINATION = "Denomination cannot be null";
  private static final String INVALID_REGISTRY_STATUS = "Invalid registry status";
  private static final String FILE_PATENT_CANNOT_BE_NULL = "File patent cannot be null";
  private static final String FILE_STATUS_CANNOT_BE_NULL = "File status cannot be null";
  private static final String ACTIVITY_TYPE_LIST_CANNOT_BE_NULL = "Activity type list cannot be null";
  private static final String REQUIRED_TAX_REGISTRATION = "Tax registration is required";
  private static final String REQUIRED_SOCIAL_REASON = "Social reason is required";
  private static final String REQUIRED_LEGAL_STATUS = "Legal status is required";
  private static final String ADDRESS_REQUIRED = "Address is required";
  private static final String PHONE_REQUIRED = "Phone number is required";
  private static final String REQUIRED_EMAIL = "Email is required";
  private static final String ERROR_TAX_REGISTRATION_CANNOT_BE_NULL = "Tax registration cannot be null";
  public UserValidator(UserRepository userRepository, RoleRepository roleRepository,
      JwtTokenUtil jwtTokenUtil,
      GroupRepository groupRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.jwtTokenUtil = jwtTokenUtil;
    this.groupRepository = groupRepository;
    this.passwordPattern = Pattern.compile(REGEX_PASSWORD_PATTERN);
    this.firstNameLastNamePattern = Pattern.compile(REGEX_FIRST_NAME_LAST_NAME_PATTERN);
    this.emailPattern = Pattern.compile(REGEX_EMAIL_PATTERN);
    this.cinNationalIdPattern = Pattern.compile(REGEX_CIN_NATIONAL_ID_PATTERN);
    this.phoneNumberPattern = Pattern.compile(REGEX_TUNISIAN_PHONE_NUMBER_PATTERN);
    this.passportNationalIdPattern = Pattern.compile(REGEX_PASSPORT_NATIONAL_ID_PATTERN);
    this.dateFormatPattern = Pattern.compile(REGEX_DATE_FORMAT);
    this.addressPattern = Pattern.compile(ADDRESS_REGEX);
  }


    @Override
    public void beforeUpdate(UserDTO updateRequest) {
        List<Reason> reasons = new ArrayList<>();
        checkEmailAndAddress(updateRequest, reasons);

        if (updateRequest.getId() == null) {
            reasons.add(new Reason<>("ID_IS_REQUIRED_TO_UPDATE", ERROR_ID_IS_REQUIRED));
        }
        if (updateRequest.getId() != null && !this.userRepository.existsById(updateRequest.getId())) {
            reasons.add(new Reason<>("USER_DOES_NOT_EXIST", ERROR_USER_DOES_NOT_EXIST));
        }
        if (updateRequest.getUserType().equals(UserType.COMPANY)) {
            checkCompanyUserInformation(updateRequest, reasons);
        } else {


            List<String> existingUsernames = userRepository.findUsernameByNationalId(updateRequest.getNationalId());
            boolean hasIntUser = existingUsernames.stream()
                .anyMatch(u -> u.toLowerCase().startsWith("int"));
            if (hasIntUser) {
                checkLastNameAndFirstName(updateRequest, reasons);
                checkBirthdateCondition(updateRequest, reasons);
                verifCountryAndNationality(updateRequest, reasons);
                verifGender(updateRequest, reasons);
                verifRolesAndGroups(updateRequest, reasons);
                checkPhoneNumberBeforeUpdate(updateRequest, reasons);

            } else {

                checkLastNameAndFirstName(updateRequest, reasons);
                checkBirthdateCondition(updateRequest, reasons);
                verifCountryAndNationality(updateRequest, reasons);
                verifGender(updateRequest, reasons);
                verifRolesAndGroups(updateRequest, reasons);

                if (updateRequest.getNationalId().isEmpty()) {
                    reasons.add(new Reason<>("NATIONAL_ID_COULD_NOT_BE_NULL", ERROR_NATIONAL_ID_REQUIRED));
                } else {
                    checkNationalIdBeforeUpdate(updateRequest, reasons);
                }

                if (updateRequest.getPhoneNumber().isEmpty()) {
                    reasons.add(new Reason<>("PHONE_NUMBER_COULD_NOT_BE_NULL", PHONE_REQUIRED));
                } else {
                    checkPhoneNumberBeforeUpdate(updateRequest, reasons);
                }
                if (updateRequest.getIsActive() == null) {
                    reasons.add(new Reason<>("STATUS_CANNOT_BE_NULL", ERROR_STATUS_REQUIRED));
                }

                if (!reasons.isEmpty()) {
                    throw new ValidationException(String.valueOf(reasons.get(0).code));
                }
            }
        }
    }

    private static void verifGender(UserDTO updateRequest, List<Reason> reasons) {
        if (updateRequest.getGender() == null) {
            reasons.add(new Reason<>("GENDER_COULD_NOT_BE_NULL", GENDER_REQUIRED));
        }
    }

    private static void verifCountryAndNationality(UserDTO updateRequest, List<Reason> reasons) {
        if (updateRequest.getNationality() == null) {
            reasons.add(new Reason<>("NATIONALITY_IS_REQUIRED", NATIONALITY_REQUIRED));
        }
        if (updateRequest.getCountry().isEmpty()) {
            reasons.add(new Reason<>("COUNTRY_IS_REQUIRED", COUNTRY_IS_REQUIRED));
        }
    }

    @Override
    public void beforeSave(UserDTO createRequest) {
        List<Reason> reasons = new ArrayList<>();

        if (UserType.COMPANY.equals(createRequest.getUserType())) {
            checkCompanyUserInformation(createRequest, reasons);
        } else {
            String newUsername = createRequest.getUsername();
            boolean isNewUserInt = newUsername != null && newUsername.toLowerCase().startsWith("int");
            boolean isNewUserExt = newUsername != null && newUsername.toLowerCase().startsWith("ext");

            List<String> existingUsernames = userRepository.findUsernameByNationalId(createRequest.getNationalId());

            if (!existingUsernames.isEmpty()) {
                boolean hasIntUser = existingUsernames.stream()
                    .anyMatch(u -> u.toLowerCase().startsWith("int"));

                boolean hasExtUser = existingUsernames.stream()
                    .anyMatch(u -> u.toLowerCase().startsWith("ext"));
                if ((isNewUserInt && hasIntUser) || (isNewUserExt && hasExtUser)) {
                    throw new IllegalArgumentException(
                        "Cannot create user: National ID type conflict with existing user"
                    );
                }
            }

            if (isNewUserInt || isNewUserExt) {
                checkUserInformations(createRequest);
                verifRolesAndGroups(createRequest, reasons);
            }else {
                checkIfNationalIdExist(createRequest.getNationalId());
                checkUserInformations(createRequest);
                verifRolesAndGroups(createRequest, reasons);
            }
        }
        if (!reasons.isEmpty()) {
            throw new ValidationException(String.valueOf(reasons.get(0).code));
        }
    }

    private void verifRolesAndGroups(UserDTO createRequest, List<Reason> reasons) {
        if (createRequest.getGroups() == null || createRequest.getGroups().isEmpty()) {
            reasons.add(new Reason<>("GROUPS_ARE_REQUIRED", REQUIRED_GROUPS));
        } else {
            createRequest.getGroups().forEach(groupe -> {
                if (!this.groupRepository.existsByLabelAndDeletedFalse(groupe.getLabel())) {
                    reasons.add(new Reason<>("GROUP_WITH_LABEL_DOES_NOT_EXIST:" + groupe.getLabel(),
                        ERROR_GROUP_DOES_NOT_EXIST));
                }
            });
        }
        if (createRequest.getRoles() == null || createRequest.getRoles().isEmpty()) {
            reasons.add(new Reason<>("ROLES_ARE_REQUIRED", REQUIRED_ROLE));
        } else {
            createRequest.getRoles().forEach(role -> {
                if (!this.roleRepository.existsByLabelAndDeletedFalse(role.getLabel())) {
                    reasons.add(new Reason<>("ROLE_WITH_LABEL_DOES_NOT_EXIST:" + role.getLabel(),
                        ERROR_ROLE_DOES_NOT_EXIST));
                }
            });
        }
    }

    private void checkBirthdateCondition(UserDTO updateRequest, List<Reason> reasons) {
        if (updateRequest.getBirthDate() == null) {
            reasons.add(new Reason<>("BIRTHDATE_IS_REQUIRED", REQUIRED_BIRTHDATE));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedBirthDate = updateRequest.getBirthDate().format(formatter);
            Matcher birthdateMatcher = this.dateFormatPattern.matcher(
                formattedBirthDate);
            if (!birthdateMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_BIRTHDATE_FORMAT", INVALID_BIRTHDATE_FORMAT));
            } else {
                LocalDate birthDate = updateRequest.getBirthDate();
                LocalDate today = LocalDate.now();
                LocalDate minimumAgeDate = today.minusYears(18);
                LocalDate minAllowedDate = today.minusYears(100);

                if (birthDate.isAfter(minimumAgeDate)) {
                    reasons.add(new Reason<>("BIRTHDATE_TOO_RECENT", BIRTHDATE_TOO_RECENT));
                }else if (birthDate.isBefore(minAllowedDate)) {
                  reasons.add(new Reason<>("BIRTHDATE_TOO_OLD", BIRTHDATE_TOO_OLD));
                }
            }
        }
    }

    private void checkEmailAndAddress(UserDTO updateRequest, List<Reason> reasons) {
        if (updateRequest.getEmail().isEmpty()) {
            reasons.add(new Reason<>("EMAIL_COULD_NOT_BE_NULL", REQUIRED_EMAIL));
        } else {
            Matcher emailMatcher = this.emailPattern.matcher(updateRequest.getEmail());
            if (!emailMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_EMAIL_PATTERN", INVALID_EMAIL_PATTERN));
            }
        }

        if (updateRequest.getAddress().isEmpty()) {
            reasons.add(new Reason<>("ADRESS_COULD_NOT_BE_NULL", ADDRESS_REQUIRED));
        } else if (updateRequest.getAddress().length() < 10) {
            reasons.add(new Reason<>("INVALID_LENGTH_ADDRESS", INVALID_ADDRESS_PATTERN));
        }
    }

    private void checkLastNameAndFirstName(UserDTO updateRequest, List<Reason> reasons) {
        if (updateRequest.getLastName().isEmpty()) {
            reasons.add(new Reason<>("LAST_NAME_COULD_NOT_BE_NULL", REQUIRED_LAST_NAME));
        } else {
            Matcher lastNameMatcher = this.firstNameLastNamePattern.matcher(
                updateRequest.getLastName());
            if (updateRequest.getLastName().length() > 25) {
                reasons.add(new Reason<>("LAST_NAME_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
            }
            if (!lastNameMatcher.matches()) {
                reasons.add(
                    new Reason<>("INVALID_LAST_NAME_PATTERN", REGEX_FIRST_NAME_LAST_NAME_PATTERN));
            }
        }

        if (updateRequest.getFirstName().isEmpty()) {
            reasons.add(new Reason<>("FIRST_NAME_COULD_NOT_BE_NULL", FIRST_NAME_REQUIRED));
        } else {
            Matcher fisrtNameMatcher = this.firstNameLastNamePattern.matcher(
                updateRequest.getFirstName());
            if (updateRequest.getFirstName().length() > 25) {
                reasons.add(new Reason<>("FIRST_NAME_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
            }
            if (!fisrtNameMatcher.matches()) {
                reasons.add(
                    new Reason<>("INVALID_FIRST_NAME_PATTERN", REGEX_FIRST_NAME_LAST_NAME_PATTERN));
            }
        }
    }

    private void checkPhoneNumberBeforeUpdate(UserDTO updateRequest, List<Reason> reasons) {
        Matcher tunisianPhoneNumberMatcher = this.phoneNumberPattern.matcher(
            updateRequest.getPhoneNumber());
        User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
            updateRequest.getPhoneNumber());
        if (userWithPhoneNumberExist != null && !userWithPhoneNumberExist.getId()
            .equals(updateRequest.getId())) {
            reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", PHONE_NUMBER_ALREADY_EXIST));
        } else {
            if (updateRequest.getNationality().equals(Nationality.TUNISIAN.toString())
                && !tunisianPhoneNumberMatcher.matches()) {
                reasons.add(new Reason<>("PHONE_NUMBER_IVALID_PATTERN", PHONE_DOES_NOT_MATCH_PATTERN));
            }
        }
    }

    private void checkNationalIdBeforeUpdate(UserDTO updateRequest, List<Reason> reasons) {
        User userWithNationalId = this.userRepository.findByNationalIdAndDeletedFalse(
            updateRequest.getNationalId());
        Matcher cinNationalIdMatcher = this.cinNationalIdPattern.matcher(
            updateRequest.getNationalId());
        Matcher passportNationalIdMatcher = this.passportNationalIdPattern.matcher(
            updateRequest.getNationalId());
        if (userWithNationalId != null && !userWithNationalId.getId()
            .equals(updateRequest.getId())) {
            reasons.add(new Reason<>("NATIONAL_ID_ALREADY_EXIST", ERROR_NATIONAL_ID_ALREADY_EXIST));
        } else {
            if (updateRequest.getNationality().equals(Nationality.TUNISIAN)
                && !cinNationalIdMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_CIN_NATIONAL_ID_PATTERN",
                    INVALID_CIN_NATIONAL_ID_PATTERN));
            } else if (!updateRequest.getNationality().equals(Nationality.TUNISIAN)
                && !passportNationalIdMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_PASSPORT_NATIONAL_ID_PATTERN",
                    INVALID_PASSPORT_NATIONAL_ID_PATTERN));
            }
        }
    }

    @Override
    public void checkUserInformations(UserDTO createRequest) {
        List<Reason> reasons = new ArrayList<>();
        if (createRequest.getNationalId().isEmpty()) {
            reasons.add(new Reason<>("NATIONAL_ID_COULD_NOT_BE_NULL", ERROR_NATIONAL_ID_REQUIRED));
        } else {
            Matcher cinNationalIdMatcher = this.cinNationalIdPattern.matcher(
                createRequest.getNationalId());
            Matcher passportNationalIdMatcher = this.passportNationalIdPattern.matcher(
                createRequest.getNationalId());
          if (createRequest.getNationality().equals(Nationality.TUNISIAN)
              && passportNationalIdMatcher.matches()
              && !cinNationalIdMatcher.matches()) {
            return;
          }
          if (createRequest.getNationality().equals(Nationality.TUNISIAN)
                && !cinNationalIdMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_CIN_NATIONAL_ID_PATTERN",
                    INVALID_CIN_NATIONAL_ID_PATTERN));
            } else if (!createRequest.getNationality().equals(Nationality.TUNISIAN)
                && !passportNationalIdMatcher.matches()) {
                reasons.add(new Reason<>("INVALID_PASSPORT_NATIONAL_ID_PATTERN",
                    INVALID_PASSPORT_NATIONAL_ID_PATTERN));
            }
        }

        if (createRequest.getPhoneNumber().isEmpty()) {
            reasons.add(new Reason<>("PHONE_NUMBER_COULD_NOT_BE_NULL", PHONE_REQUIRED));
        } else {
            Matcher tunisianPhoneNumberMatcher = this.phoneNumberPattern.matcher(
                createRequest.getPhoneNumber());
            User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
                createRequest.getPhoneNumber());
            if (userWithPhoneNumberExist != null) {
                reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", PHONE_NUMBER_ALREADY_EXIST));
            } else {
                if (createRequest.getNationality().equals(Nationality.TUNISIAN.toString())
                    && !tunisianPhoneNumberMatcher.matches()) {
                    reasons.add(new Reason<>("PHONE_NUMBER_IVALID_PATTERN", PHONE_DOES_NOT_MATCH_PATTERN));
                }
            }
        }

        checkLastNameAndFirstName(createRequest, reasons);

        checkEmailAndAddress(createRequest, reasons);
        verifCountryAndNationality(createRequest, reasons);
        verifGender(createRequest, reasons);
        checkBirthdateCondition(createRequest, reasons);
        if (!reasons.isEmpty()) {
            throw new ValidationException(String.valueOf(reasons.get(0).code));
        }
    }

    @Override
    public void checkIfNationalIdExist(String nationalId) {
        List<Reason> reasons = new ArrayList<>();
        if (nationalId.isEmpty()) {
            reasons.add(new Reason<>("NATIONAL_ID_COULD_NOT_BE_NULL", ERROR_NATIONAL_ID_REQUIRED));
        } else {
            User userWithNationalIdExist = this.userRepository.findByNationalIdAndDeletedFalse(
                nationalId);
            if (userWithNationalIdExist != null) {
                reasons.add(new Reason<>("NATIONAL_ID_EXIST", ERROR_NATIONAL_ID_ALREADY_EXIST));
            }
            if (!reasons.isEmpty()) {
                throw new ValidationException(String.valueOf(reasons.get(0).code));
            }
        }
    }
    public void checkIfTaxRegistrationExist(String taxRegistration) {
        List<Reason> reasons = new ArrayList<>();
        if (taxRegistration.isEmpty()) {
            reasons.add(new Reason<>("TAX_REGISTRATION_CANNOT_BE_NULL", ERROR_TAX_REGISTRATION_CANNOT_BE_NULL));
        } else {
            User userWithTaxRegistrationExist = this.userRepository.findByTaxRegistrationAndDeletedFalse(
                taxRegistration);
            if (userWithTaxRegistrationExist != null) {
                reasons.add(new Reason<>("TAX_REGISTRATION_ALREADY_EXIST", TAX_REGISTRATION_ALREADY_EXIST));
            }
            if (!reasons.isEmpty()) {
                throw new ValidationException(String.valueOf(reasons.get(0).code));
            }
        }
    }
    @Override
  public void beforePasswordChange(ResetPasswordDTO resetPasswordDTO, String token) {
    List<Reason> reasons = new ArrayList<>();
    Matcher passwordMatcher = this.passwordPattern.matcher(resetPasswordDTO.getNewPassword());
    if (!passwordMatcher.matches()) {
      reasons.add(new Reason("INVALID_PASSWORD_PATTERN", "password  does not match pattern"));
    }

    if (!this.jwtTokenUtil.getUserNameFromToken(token).equals(resetPasswordDTO.getUsername())) {
      reasons.add(new Reason("INVALID_TOKEN", "Token does not match with user name"));
    }

    if (!resetPasswordDTO.getConfirmPassword().equals(resetPasswordDTO.getNewPassword())) {
      reasons.add(new Reason("NEW_PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCH",
          "New password and confirm new password does not  match"));
    }

    if (!reasons.isEmpty()) {
      throw new ValidationException(reasons);
    }
  }
  public void checkCompanyUserInformation(UserDTO updateRequest, List<Reason> reasons) {
        checkEmailAndAddress(updateRequest, reasons);
        User existingUser = this.userRepository.findByTaxRegistrationAndDeletedFalse(
            updateRequest.getTaxRegistration());
        if (updateRequest.getTaxRegistration() == null) {
            reasons.add(new Reason<>("TAX_REGISTRATION_CANNOT_BE_NULL", REQUIRED_TAX_REGISTRATION));
        } else {
            if (existingUser != null && !existingUser.getId().equals(updateRequest.getId())) {
                reasons.add(new Reason<>("TAX_REGISTRATION_ALREADY_EXIST", TAX_REGISTRATION_ALREADY_EXIST));
            }
        }

        if (updateRequest.getSocialReason() == null) {
            reasons.add(new Reason<>("SOCIAL_REASON_CANNOT_BE_NULL", REQUIRED_SOCIAL_REASON));
        }

        if (updateRequest.getLegalStatus() == null) {
            reasons.add(new Reason<>("LEGAL_STATUS_CANNOT_BE_NULL", REQUIRED_LEGAL_STATUS));
        }

        if (!isValidStatus(updateRequest.getRegistryStatus().name())) {
            reasons.add(new Reason<>("INVALID_REGISTRY_STATUS", INVALID_REGISTRY_STATUS));
        }

        if (updateRequest.getDenomination() == null) {
            reasons.add(new Reason<>("DENOMINATION_CANNOT_BE_NULL", REQUIRED_DENOMINATION));
        }
        if (updateRequest.getPhoneNumber() == null) {
            reasons.add(new Reason<>("PHONENUMBER_CANNOT_BE_NULL", PHONE_REQUIRED));
        } else {
            Matcher phoneNumberMatcher = this.phoneNumberPattern.matcher(updateRequest.getPhoneNumber());
            User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
                updateRequest.getPhoneNumber());
            if (userWithPhoneNumberExist != null && !userWithPhoneNumberExist.getId()
                .equals(updateRequest.getId())) {
                reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", PHONE_NUMBER_ALREADY_EXIST));
            }
        }
        if (updateRequest.getFileStatus() == null) {
            reasons.add(new Reason<>("FILE_STATUS_CANNOT_BE_NULL", FILE_STATUS_CANNOT_BE_NULL));
        }
        if (updateRequest.getFilePatent() == null) {
            reasons.add(new Reason<>("FILE_PATENT_CANNOT_BE_NULL", FILE_PATENT_CANNOT_BE_NULL));
        }
        if (updateRequest.getActivitiesType().isEmpty()) {
            reasons.add(
                new Reason<>("ACTIVITY_TYPE_LIST_CANNOT_BE_NULL", ACTIVITY_TYPE_LIST_CANNOT_BE_NULL));
        }

        if (!reasons.isEmpty()) {
            throw new ValidationException(String.valueOf(reasons.get(0).code));
        }
    }

    public boolean isValidStatus(String value) {
        for (RegistryStatus status : RegistryStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
