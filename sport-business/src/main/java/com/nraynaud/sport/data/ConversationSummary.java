package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;

public class ConversationSummary implements Comparable<ConversationSummary> {
    public final long messageCount;
    public final long newMessageCount;
    public final UserString correspondentName;

    public ConversationSummary(final UserString correspondentName, final long messageCount,
                               final long newMessageCount) {
        this.correspondentName = correspondentName;
        this.messageCount = messageCount;
        this.newMessageCount = newMessageCount;
    }

    public int compareTo(final ConversationSummary o) {
        return correspondentName.toString().compareTo(o.correspondentName.toString());
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationSummary)) return false;
        final ConversationSummary that = (ConversationSummary) o;
        return !(correspondentName != null ? !correspondentName.equals(that.correspondentName) : that
                .correspondentName
                != null);
    }

    public int hashCode() {
        int result;
        result = (int) (messageCount ^ messageCount >>> 32);
        result = 31 * result + (int) (newMessageCount ^ newMessageCount >>> 32);
        result = 31 * result + (correspondentName != null ? correspondentName.hashCode() : 0);
        return result;
    }
}
