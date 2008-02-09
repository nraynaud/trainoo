package com.nraynaud.sport.data;

public class GroupData {
    public final long id;
    public final String name;
    public final long memberCount;
    public final boolean isMember;

    public GroupData(final long id, final String name, final long memberCount, final boolean isMember) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.isMember = isMember;
    }
}