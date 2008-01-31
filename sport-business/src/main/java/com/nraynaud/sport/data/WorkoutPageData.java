package com.nraynaud.sport.data;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.Workout;

import java.util.Collection;
import java.util.List;

public class WorkoutPageData {
    public final Workout workout;
    public final Collection<Message> messages;
    public final List<Message> privateMessages;
    public final PaginatedCollection<Workout> lastWorkouts;

    public WorkoutPageData(final Workout workout, final Collection<Message> messages,
                           final PaginatedCollection<Workout> lastWorkouts,
                           final List<Message> privateMessages) {
        this.workout = workout;
        this.messages = messages;
        this.lastWorkouts = lastWorkouts;
        this.privateMessages = privateMessages;
    }
}
