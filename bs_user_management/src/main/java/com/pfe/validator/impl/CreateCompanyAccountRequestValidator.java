package com.pfe.validator.impl;

import com.pfe.config.Constants;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.repository.UserRepository;
import com.pfe.service.dto.request.CreateCompanyAccountRequest;
import com.pfe.validator.ICreateCompanyAccountRequestValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("CreateCompanyAccountRequestValidator")
public class CreateCompanyAccountRequestValidator implements ICreateCompanyAccountRequestValidator {

  private final UserRepository userRepository;
  private final Pattern emailPattern;
  private final Pattern phoneNumberPattern;
  private final Pattern addressPattern;
  private static final String REGEX_EMAIL_PATTERN = Constants.EMAIL_REGEX;
  private static final String REGEX_TUNISIAN_PHONE_NUMBER_PATTERN = Constants.TUNISIAN_PHONE_NUMBER_REGEX;
  private static final String REGEX_DATE_FORMAT = Constants.DATAE_FORMAT_REGEX;
  private static final String ADDRESS_REGEX = Constants.ADDRESS_REGEX;
  private static final String TAX_REGISTRATION_REQUIRED = "Tax registration is required";
  private static final String SOCIAL_REASON_REQUIRED = "Social reason is required";
  private static final String LEGAL_STATUS_REQUIRED = "Legal status is required";
  private static final String ACTIVITY_DOMAIN_REQUIRED = "Activity domain is required";
  private static final String ADDRESS_REQUIRED = "Address is required";
  private static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
  private static final String INVALID_PHONE_NUMBER_PATTERN = "Invalid phone number pattern";
  private static final String EMAIL_REQUIRED = "Email is required";
  private static final String INVALID_EMAIL_PATTERN = "Invalid email pattern";
  private static final String COUNTRY_IS_REQUIRED = "Country is required";
  private static final String PHONE_NUMBER_ALREADY_EXIST = "User with the same poneNumber exist";
  private static final String TAX_REGISTRATION_ALREADY_EXIST = "Tax registration already exist";
  private static final String ADDRESS_SHOULD_HAVE_AT_LEAST_10_CHARACTERS = "Address should have at least 10 characters";
  private static final String INVALID_PHONENUMBER_PATTERN = "Invalid phone number pattern";
  private static final String DENOMINATION_CANNOT_BE_NULL = "Denomination cannot be null";
  private static final String INVALID_REGISTRY_STATUS = "Invalid registry status";
  private static final String FILE_PATENT_CANNOT_BE_NULL = "File patent cannot be null";
  private static final String FILE_STATUS_CANNOT_BE_NULL = "File status cannot be null";
  private static final String ACTIVITY_TYPE_LIST_CANNOT_BE_NULL = "Activity type list cannot be null";

  public CreateCompanyAccountRequestValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.emailPattern = Pattern.compile(REGEX_EMAIL_PATTERN);
    this.phoneNumberPattern = Pattern.compile(REGEX_TUNISIAN_PHONE_NUMBER_PATTERN);
    this.addressPattern = Pattern.compile(ADDRESS_REGEX);
  }

  @Override
  public void beforeUpdate(CreateCompanyAccountRequest updateRequest) {
    List<Reason> reasons = new ArrayList<>();
      checkTaxRegistration(updateRequest, reasons);
      verifSocialReason(updateRequest, reasons);
      verifLegalStatus(updateRequest, reasons);
      verifAddress(updateRequest, reasons);
      verifFileStatusAndPatent(updateRequest, reasons);
      verifActivityType(updateRequest, reasons);
      verifEmail(updateRequest, reasons);
      verifDenomination(updateRequest, reasons);
      verifRegistryStatus(updateRequest, reasons);

      if (updateRequest.getPhoneNumber() == null) {
      reasons.add(new Reason<>("PHONENUMBER_CANNOT_BE_NULL", PHONE_NUMBER_REQUIRED));
    } else {
      Matcher phoneNumberMatcher = this.phoneNumberPattern.matcher(updateRequest.getPhoneNumber());
      User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
          updateRequest.getPhoneNumber());
      if (userWithPhoneNumberExist != null) {
        reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", PHONE_NUMBER_ALREADY_EXIST));
      } else {
        if (!phoneNumberMatcher.matches()) {
          reasons.add(new Reason<>("INVALID_PHONENUMBER_PATTERN", INVALID_PHONENUMBER_PATTERN));
        }
      }
    }
      if (!reasons.isEmpty()) {
      throw new ValidationException(reasons);
    }
  }

    @Override
    public void beforeSave(CreateCompanyAccountRequest createRequest) {
        List<Reason> reasons = new ArrayList<>();

        checkTaxRegistration(createRequest, reasons);
        verifSocialReason(createRequest, reasons);
        verifLegalStatus(createRequest, reasons);
        verifAddress(createRequest, reasons);
        verifRegistryStatus(createRequest, reasons);
        verifEmail(createRequest, reasons);
        verifDenomination(createRequest, reasons);
        verifFileStatusAndPatent(createRequest, reasons);
        verifActivityType(createRequest, reasons);

        if (createRequest.getPhoneNumber() == null) {
            reasons.add(new Reason<>("PHONENUMBER_CANNOT_BE_NULL", PHONE_NUMBER_REQUIRED));
        } else {
            Matcher phoneNumberMatcher = this.phoneNumberPattern.matcher(createRequest.getPhoneNumber());
            User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
                createRequest.getPhoneNumber());
            if (userWithPhoneNumberExist != null) {
                reasons.add(new Reason<>("PHONE_NUMBER_ALREADY_EXIST", PHONE_NUMBER_ALREADY_EXIST));
            }
        }
        if (!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
    }
    private void verifRegistryStatus(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (!isValidStatus(updateRequest.getRegistryStatus().name())) {
        reasons.add(new Reason<>("INVALID_REGISTRY_STATUS", INVALID_REGISTRY_STATUS));
      }
    }

    private static void verifActivityType(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getActivitiesType().isEmpty()) {
        reasons.add(
            new Reason<>("ACTIVITY_TYPE_LIST_CANNOT_BE_NULL", ACTIVITY_TYPE_LIST_CANNOT_BE_NULL));
      }
    }

    private static void verifDenomination(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getDenomination() == null) {
        reasons.add(new Reason<>("DENOMINATION_CANNOT_BE_NULL", DENOMINATION_CANNOT_BE_NULL));
      }
    }

    private static void verifLegalStatus(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getLegalStatus() == null) {
        reasons.add(new Reason<>("LEGAL_STATUS_CANNOT_BE_NULL", LEGAL_STATUS_REQUIRED));
      }
    }

    private static void verifSocialReason(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getSocialReason() == null) {
          reasons.add(new Reason<>("SOCIAL_REASON_CANNOT_BE_NULL", SOCIAL_REASON_REQUIRED));
        }
    }

    private static void verifFileStatusAndPatent(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getFileStatus() == null) {
          reasons.add(new Reason<>("FILE_STATUS_CANNOT_BE_NULL", FILE_STATUS_CANNOT_BE_NULL));
        }
        if (updateRequest.getFilePatent() == null) {
          reasons.add(new Reason<>("FILE_PATENT_CANNOT_BE_NULL", FILE_PATENT_CANNOT_BE_NULL));
        }
    }

    private void verifEmail(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getEmail() == null) {
          reasons.add(new Reason<>("EMAIL_CANNOT_BE_NULL", EMAIL_REQUIRED));
        } else {
          Matcher emailMatcher = this.emailPattern.matcher(updateRequest.getEmail());
          if (!emailMatcher.matches()) {
            reasons.add(new Reason<>("INVALID_EMAIL_PATTERN", INVALID_EMAIL_PATTERN));
          }
        }
    }

    private void verifAddress(CreateCompanyAccountRequest updateRequest, List<Reason> reasons) {
        if (updateRequest.getAddress() == null) {
          reasons.add(new Reason<>("ADDRESS_CANNOT_BE_NULL", ADDRESS_REQUIRED));
        } else {
          Matcher addressMatcher = this.addressPattern.matcher(updateRequest.getAddress());
          if (!addressMatcher.matches()) {
            reasons.add(new Reason<>("ADDRESS_SHOULD_HAVE_AT_LEAST_10_CHARACTERS",
                ADDRESS_SHOULD_HAVE_AT_LEAST_10_CHARACTERS));
          }
        }
    }

  private void checkTaxRegistration(CreateCompanyAccountRequest createRequest,
      List<Reason> reasons) {
    if (createRequest.getTaxRegistration() == null) {
      reasons.add(new Reason<>("TAX_REGISTRATION_CANNOT_BE_NULL", TAX_REGISTRATION_REQUIRED));
    } else {
      Boolean taxRegistrationExist = this.userRepository.existsByTaxRegistrationAndDeletedFalse(
          createRequest.getTaxRegistration().toUpperCase());
      if (taxRegistrationExist) {
        reasons.add(new Reason<>("TAX_REGISTRATION_ALREADY_EXIST", TAX_REGISTRATION_ALREADY_EXIST));
      }
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
