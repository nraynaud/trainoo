package com.nraynaud.sport.web;

public class DataInputException extends RuntimeException {
    public DataInputException() {
    }

    public DataInputException(final String message) {
        super(message);
    }

    public DataInputException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataInputException(final Throwable cause) {
        super(cause);
    }
}
