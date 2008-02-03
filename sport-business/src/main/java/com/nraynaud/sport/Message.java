package com.nraynaud.sport;

public interface Message {
    Long getId();

    String getContent();

    User getSender();

    User getReceiver();

    Workout getWorkout();

    boolean isPublic();

    boolean isNew();
}
