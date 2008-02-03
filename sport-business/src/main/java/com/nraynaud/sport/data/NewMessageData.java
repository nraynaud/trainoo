package com.nraynaud.sport.data;

public class NewMessageData {
    public final String sender;
    public final long messageCount;

    public NewMessageData(final String sender, final long messageCount) {
        this.sender = sender;
        this.messageCount = messageCount;
    }
}
