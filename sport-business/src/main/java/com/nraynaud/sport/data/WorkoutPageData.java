package com.nraynaud.sport.data;

import com.nraynaud.sport.PrivateMessage;
import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.Workout;

public class WorkoutPageData {
    public final Workout workout;
    public final PaginatedCollection<PublicMessage> messages;
    public final PaginatedCollection<PrivateMessage> privateMessages;
    public final PaginatedCollection<Workout> lastWorkouts;

    public WorkoutPageData(final Workout workout, final PaginatedCollection<PublicMessage> messages,
                           final PaginatedCollection<Workout> lastWorkouts,
                           final PaginatedCollection<PrivateMessage> privateMessages) {
        this.workout = workout;
        this.messages = messages;
        this.lastWorkouts = lastWorkouts;
        this.privateMessages = privateMessages;
    }
}
