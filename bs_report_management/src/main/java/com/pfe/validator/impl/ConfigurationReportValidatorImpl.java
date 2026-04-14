package com.pfe.validator.impl;

import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.validator.IConfigurationReportValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("ConfigurationReportValidator")
public class ConfigurationReportValidatorImpl implements IConfigurationReportValidator {

    private static String NAME_PATTERN = "Invalid name pattern";
    private static String ADDRESS_PATTERN = "Invalid address pattern";
    private static String PHONE_PATTERN = "Invalid phone pattern";
    private static String FAX_PATTERN = "Invalid fax pattern";
    private static String POSTAL_PATTERN = "Invalid postal code pattern";
    private static String EMAIL_PATTERN = "Invalid email pattern";
    private static String ADDRESS_REQUIRED = "Address is required";
    private static String NAME_REQUIRED = "Name is required";
    private static String EMAIL_REQUIRED = "Email is required";
    private static String POSTAL_REQUIRED = "Postal code is required";
    private static String PHONE_REQUIRED = "Phone is required";
    private static String FAX_REQUIRED = "Fax code is required";

    private static String COMPANY_NAME_REGEX = "^(?! )[A-Za-zÀ-ÖØ-öø-ÿ\u0600-\u06FF0-9 .,'&()_-]{1,99}[A-Za-zÀ-ÖØ-öø-ÿ\u0600-\u06FF0-9]$";
    private static String COMPANY_ADDRESS_REGEX = "^[\u0600-\u06FFa-zA-ZÀ-ÖØ-öø-ÿ0-9 ,.()'_-]*$";
    private final String POSTAL_REGEX = "^\\d{4}$";
    private final String PHONE_REGEX = "^(?:\\+216)?(2|5|9|4|7)[0-9]{7}$";
    private final String EMAIL_REGEX = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    private final Pattern namePattern;
    private final Pattern addressPattern;
    private final Pattern faxPattern;
    private final Pattern phonePattern;
    private final Pattern postalPattern;
    private final Pattern emailPattern;


    public ConfigurationReportValidatorImpl() {
        this.phonePattern = Pattern.compile(PHONE_REGEX);
        this.faxPattern = Pattern.compile(PHONE_REGEX);
        this.postalPattern = Pattern.compile(POSTAL_REGEX);
        this.emailPattern = Pattern.compile(EMAIL_REGEX);
        this.namePattern = Pattern.compile(COMPANY_NAME_REGEX);
        this.addressPattern = Pattern.compile(COMPANY_ADDRESS_REGEX);
    }


    @Override
    public void beforeSave(ConfigurationReportDTO configurationReportDTO) {
        List<Reason> reasons = new ArrayList<>();
        validateCompanyName(reasons,configurationReportDTO.getName());
        validateCompanyAddress(reasons,configurationReportDTO.getAddress());
        validatePostalCode(reasons,configurationReportDTO.getPostalCode());
        validatePhone(reasons,configurationReportDTO.getPhone());
        validateFax(reasons,configurationReportDTO.getFax());
        validateEmail(reasons,configurationReportDTO.getEmail());
        if (!reasons.isEmpty()) {
            throw new ValidationException(
                reasons.stream()
                    .map(reason -> String.valueOf(reason.code))
                    .collect(Collectors.joining(", "))
            );
        }
    }

    @Override
    public void validateCompanyAddress(List<Reason> reasons, String address) {

        if (address == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.ADDRESS_REQUIRED, ADDRESS_REQUIRED));
        }
        else if (!validatePattern(addressPattern, address)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.ADDRESS_PATTERN, ADDRESS_PATTERN));
        }
    }

    @Override
    public void validateCompanyName(List<Reason> reasons, String name) {
        if (name == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.NAME_REQUIRED, NAME_REQUIRED));
        }
        else if (!validatePattern(namePattern, name)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.NAME_PATTERN, NAME_PATTERN));
        }
    }

    @Override
    public void validateEmail(List<Reason> reasons, String email) {
        if (email == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.EMAIL_REQUIRED, EMAIL_REQUIRED));
        }
        else if (!validatePattern(emailPattern, email)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.EMAIL_PATTERN, EMAIL_PATTERN));
        }
    }

    @Override
    public void validatePostalCode(List<Reason> reasons, String postalCode) {
        if (postalCode == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.POSTAL_REQUIRED, POSTAL_REQUIRED));
        }
        else if (!validatePattern(postalPattern, postalCode)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.POSTAL_PATTERN, POSTAL_PATTERN));
        }
    }

    @Override
    public void validateFax(List<Reason> reasons, String fax) {

        if (fax == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.FAX_REQUIRED, FAX_REQUIRED));
        }
        else if (!validatePattern(faxPattern, fax)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.FAX_PATTERN, FAX_PATTERN));
        }
    }

    @Override
    public void validatePhone(List<Reason> reasons, String phone) {
        if (phone == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.PHONE_REQUIRED, PHONE_REQUIRED));
        }
        else if (!validatePattern(phonePattern, phone)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.PHONE_PATTERN, PHONE_PATTERN));
        }
    }

    public boolean validatePattern(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
