package com.nraynaud.sport;

public interface PrivateMessage extends Message {

    User getReceiver();

    boolean isNew();
}
