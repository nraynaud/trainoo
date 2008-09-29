package com.nraynaud.sport.data;

import com.nraynaud.sport.Group;
import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.User;

import java.util.Collection;

public class GroupPageData {
    public final Group group;
    public final boolean isMember;
    public final Collection<GroupData> allGroups;
    public final PaginatedCollection<PublicMessage> messages;
    public final StatisticsData<DisciplineData.Count> statistics;
    public final PaginatedCollection<User> users;

    public GroupPageData(final Group group, final boolean member, final Collection<GroupData> allGroups,
                         final PaginatedCollection<PublicMessage> messages,
                         final StatisticsData<DisciplineData.Count> statistics,
                         final PaginatedCollection<User> users) {
        this.group = group;
        isMember = member;
        this.allGroups = allGroups;
        this.messages = messages;
        this.statistics = statistics;
        this.users = users;
    }
}
