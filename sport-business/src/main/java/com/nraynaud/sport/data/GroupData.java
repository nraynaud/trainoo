package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;

public class GroupData {
    public final long id;
    public final UserString name;
    public final long memberCount;
    public final boolean isMember;
    public final int newMessagesCount;

    public GroupData(final long id, final UserString name, final long memberCount, final boolean isMember,
                     final int newMessagesCount) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.isMember = isMember;
        this.newMessagesCount = newMessagesCount;
    }
}
