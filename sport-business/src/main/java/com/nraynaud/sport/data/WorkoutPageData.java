package com.nraynaud.sport.data;

import com.nraynaud.sport.PrivateMessage;
import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.Workout;

import java.util.Collection;
import java.util.List;

public class WorkoutPageData {
    public final Workout workout;
    public final Collection<PublicMessage> messages;
    public final List<PrivateMessage> privateMessages;
    public final PaginatedCollection<Workout> lastWorkouts;

    public WorkoutPageData(final Workout workout, final Collection<PublicMessage> messages,
                           final PaginatedCollection<Workout> lastWorkouts,
                           final List<PrivateMessage> privateMessages) {
        this.workout = workout;
        this.messages = messages;
        this.lastWorkouts = lastWorkouts;
        this.privateMessages = privateMessages;
    }
}
