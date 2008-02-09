package com.nraynaud.sport;

public interface Topic {
    public enum Kind {
        WORKOUT, GROUP
    }

    Long getId();

    public Kind getKind();
}

