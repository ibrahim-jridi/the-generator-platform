package com.pfe.web.rest.errors;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 6585348330590245566L;
  private List<Reason> reasons = new ArrayList<>();

  public ValidationException(List<Reason> reasons) {
    super(generateJoinedMessage(reasons));
  }

  public ValidationException(String msg) {
    super(msg);
  }

  private static String generateJoinedMessage(List<Reason> reasons) {
    List<String> messages = reasons.stream()
        .map(reason -> reason.getCode().toString())
        .toList();
    return String.join(", ", messages);
  }

  @Override
  public String toString() {
    return "ValidationException{" +
        "reasons=" + this.reasons +
        '}';
  }
}
