package com.nraynaud.sport.data;

import com.nraynaud.sport.Group;

import java.util.Collection;

public class GroupPageData {
    public final Group group;
    public final Collection<GroupData> others;

    public GroupPageData(final Group group, final Collection<GroupData> others) {
        this.group = group;
        this.others = others;
    }
}
