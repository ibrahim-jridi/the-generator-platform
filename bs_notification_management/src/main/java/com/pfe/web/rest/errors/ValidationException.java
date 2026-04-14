package com.pfe.web.rest.errors;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private List<Reason<?>> reasons = new ArrayList<>();


    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(List<Reason<?>> reasons) {
        super(generateJoinedMessage(reasons));

    }

    private static String generateJoinedMessage(List<Reason<?>> reasons) {
        List<String> messages = reasons.stream()
            .map(reason -> reason.getCode().toString())
            .toList();
        return String.join(", ", messages);
    }

    @Override
    public String toString() {
        return "ValidationException{" +
            "reasons=" + reasons +
            '}';
    }
}
