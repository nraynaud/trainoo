package com.nraynaud.sport.data;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.Workout;

import java.util.List;

public class ConversationData {
    public final Workout aboutWorkout;
    public final List<Message> messages;

    public ConversationData(final List<Message> messages, final Workout aboutWorkout) {
        this.messages = messages;
        this.aboutWorkout = aboutWorkout;
    }
}
