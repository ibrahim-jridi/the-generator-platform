package com.pfe.web.rest.errors;

public class ProcessEngineException extends RuntimeException {
    public ProcessEngineException(String message) {
        super(message);
    }

    public ProcessEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
