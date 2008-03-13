package com.nraynaud.sport.importer;

public class FailureException extends Exception {
    public FailureException(final Throwable cause) {
        super(cause);
    }

    public FailureException() {
    }
}
