package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

public class NewMessageData {
    public final UserString sender;
    public final long messageCount;

    public NewMessageData(final String sender, final long messageCount) {
        this.sender = UserStringImpl.valueOf(sender);
        this.messageCount = messageCount;
    }
}
