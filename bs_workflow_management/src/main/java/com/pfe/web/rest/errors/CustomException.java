package com.pfe.web.rest.errors;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 5725133475522857287L;
    private final HttpStatus status;
    private final String message;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CustomException{" +
            "status=" + status +
            ", message='" + message + '\'' +
            '}';
    }
}
