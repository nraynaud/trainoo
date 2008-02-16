package com.nraynaud.sport;

public interface Message {
    Long getId();

    UserString getContent();

    User getSender();

    Workout getWorkout();

    boolean canWrite(User user);
}
