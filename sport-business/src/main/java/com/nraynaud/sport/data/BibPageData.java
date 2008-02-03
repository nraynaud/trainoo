package com.nraynaud.sport.data;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;

import java.util.List;

public class BibPageData {
    public final User user;

    public final List<Message> messages;
    public final PaginatedCollection<Workout> workouts;

    public BibPageData(final User user, final List<Message> messages, final PaginatedCollection<Workout> workouts) {
        this.user = user;
        this.messages = messages;
        this.workouts = workouts;
    }
}
