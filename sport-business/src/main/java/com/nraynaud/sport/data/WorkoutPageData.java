package com.nraynaud.sport.data;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.Workout;

import java.util.List;

public class WorkoutPageData {
    public final Workout workout;
    public final List<Message> messages;
    public final List<Message> privateMessages;
    public final List<Workout> lastWorkouts;

    public WorkoutPageData(final Workout workout, final List<Message> messages, final List<Workout> lastWorkouts,
                           final List<Message> privateMessages) {
        this.workout = workout;
        this.messages = messages;
        this.lastWorkouts = lastWorkouts;
        this.privateMessages = privateMessages;
    }
}
