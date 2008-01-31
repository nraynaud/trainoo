package com.nraynaud.sport.data;

public class ConversationSumary implements Comparable<ConversationSumary> {
    public final long messageCount;
    public final String correspondentName;

    public ConversationSumary(final String correspondentName, final long messageCount) {
        this.correspondentName = correspondentName;
        this.messageCount = messageCount;
    }

    public int compareTo(final ConversationSumary o) {
        return correspondentName.compareTo(o.correspondentName);
    }
}
