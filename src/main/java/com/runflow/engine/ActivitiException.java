package com.runflow.engine;

public class ActivitiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ActivitiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivitiException(String message) {
        super(message);
    }
}
