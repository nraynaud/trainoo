package com.nraynaud.sport.data;

import com.nraynaud.sport.Group;
import com.nraynaud.sport.PublicMessage;

import java.util.Collection;

public class GroupPageData {
    public final Group group;
    public final Collection<GroupData> others;
    public final PaginatedCollection<PublicMessage> messages;

    public GroupPageData(final Group group, final Collection<GroupData> others,
                         final PaginatedCollection<PublicMessage> messages) {
        this.group = group;
        this.others = others;
        this.messages = messages;
    }
}
