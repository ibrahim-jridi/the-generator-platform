package com.pfe.web.rest.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    public enum ReasonCode {
        TYPE_REQUIRED, FAX_PATTERN, PHONE_PATTERN, POSTAL_PATTERN, EMAIL_PATTERN, INVALID_EXTENSION, NAME_PATTERN, ADDRESS_PATTERN, POSTAL_REQUIRED,
        PHONE_REQUIRED, FAX_REQUIRED, EMAIL_REQUIRED, NAME_REQUIRED, ADDRESS_REQUIRED;
    }

    private List<Reason> reasons = new ArrayList<>();

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(List<Reason> reasons) {
        super(generateJoinedMessage(reasons));

    }

    private static String generateJoinedMessage(List<Reason> reasons) {
        List<String> messages = reasons.stream()
            .map(Reason::getMessage)
            .collect(Collectors.toList());
        return String.join(", ", messages);
    }

    @Override
    public String toString() {
        return "ValidationException{" +
            "reasons=" + reasons +
            '}';
    }
}
