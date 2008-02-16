package com.nraynaud.sport;

public interface Message {
    public enum Kind {
        PUBLIC, PRIVATE
    }

    Long getId();

    UserString getContent();

    User getSender();

    Workout getWorkout();

    boolean canWrite(User user);

    Kind getKind();
}
