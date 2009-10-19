package com.nraynaud.sport;

/**
 * in simpler words, a foreign key was violated
 */
public class EntityStillInUseException extends Exception {
    public EntityStillInUseException(final Throwable cause) {
        super(cause);
    }
}
