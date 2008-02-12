package com.nraynaud.sport.data;

import com.nraynaud.sport.Group;
import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.User;

import java.util.Collection;

public class GroupPageData {
    public final Group group;
    public final Collection<GroupData> others;
    public final PaginatedCollection<PublicMessage> messages;
    public final StatisticsData statistics;
    public final PaginatedCollection<User> users;

    public GroupPageData(final Group group, final Collection<GroupData> others,
                         final PaginatedCollection<PublicMessage> messages, final StatisticsData statistics,
                         final PaginatedCollection<User> users) {
        this.group = group;
        this.others = others;
        this.messages = messages;
        this.statistics = statistics;
        this.users = users;
    }
}
