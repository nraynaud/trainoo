package com.nraynaud.sport;

public interface Message {
    Long getId();

    String content();

    User getSender();
}
