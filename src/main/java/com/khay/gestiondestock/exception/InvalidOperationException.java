package com.khay.gestiondestock.exception;

import lombok.Getter;
import java.util.List;

public class InvalidOperationException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCodes = errorCode;
    }

    public InvalidOperationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCodes = errorCode;
    }

    public InvalidOperationException(String message, ErrorCodes errorCodes, List<String> errors) {
        super(message);
        this.errorCodes = errorCodes;
    }
}
