package com.nraynaud.sport.data;

import com.nraynaud.sport.PrivateMessage;
import com.nraynaud.sport.Workout;

import java.util.List;

public class ConversationData {
    public final Workout aboutWorkout;
    public final List<PrivateMessage> privateMessages;

    public ConversationData(final List<PrivateMessage> privateMessages, final Workout aboutWorkout) {
        this.privateMessages = privateMessages;
        this.aboutWorkout = aboutWorkout;
    }
}
