package com.nraynaud.sport.data;

public class ConversationSumary implements Comparable<ConversationSumary> {
    public final long messageCount;
    public final long newMessageCount;
    public final String correspondentName;

    public ConversationSumary(final String correspondentName, final long messageCount, final long newMessageCount) {
        this.correspondentName = correspondentName;
        this.messageCount = messageCount;
        this.newMessageCount = newMessageCount;
    }

    public int compareTo(final ConversationSumary o) {
        return correspondentName.compareTo(o.correspondentName);
    }
}
