package com.runflow.engine;

public class RunFlowException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RunFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunFlowException(String message) {
        super(message);
    }
}
