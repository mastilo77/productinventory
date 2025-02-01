package com.pt.productinventory.error.exceptions;

public class IllegalParameterException extends RuntimeException {
    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
