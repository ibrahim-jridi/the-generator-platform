package com.pfe.web.rest.errors;


public class Reason<T> {

  public T code;
  public String message;

  public T getCode() {
    return this.code;
  }

  public void setCode(T code) {
    this.code = code;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Reason(T code) {
    this.code = code;
  }

  public Reason(T code, String message) {
    this.code = code;
    this.message = message;
  }
}