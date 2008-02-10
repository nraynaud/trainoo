package com.nraynaud.sport.data;

import com.nraynaud.sport.PrivateMessage;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;

public class BibPageData {
    public final User user;

    public final PaginatedCollection<PrivateMessage> privateMessages;
    public final PaginatedCollection<Workout> workouts;

    public BibPageData(final User user, final PaginatedCollection<PrivateMessage> privateMessages,
                       final PaginatedCollection<Workout> workouts) {
        this.user = user;
        this.privateMessages = privateMessages;
        this.workouts = workouts;
    }
}
