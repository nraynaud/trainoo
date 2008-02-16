package com.nraynaud.sport;

public class NameClashException extends Exception {
    public NameClashException() {
    }

    public NameClashException(final String message) {
        super(message);
    }

    public NameClashException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NameClashException(final Throwable cause) {
        super(cause);
    }
}
