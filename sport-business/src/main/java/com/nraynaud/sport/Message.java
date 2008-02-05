package com.nraynaud.sport;

public interface Message {
    Long getId();

    String getContent();

    User getSender();

    Workout getWorkout();

    boolean canDelete(User user);
}
