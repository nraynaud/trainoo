package com.nraynaud.sport.data;

import com.nraynaud.sport.PrivateMessage;
import com.nraynaud.sport.Workout;

public class ConversationData {
    public final Workout aboutWorkout;
    public final PaginatedCollection<PrivateMessage> privateMessages;

    public ConversationData(final PaginatedCollection<PrivateMessage> privateMessages, final Workout aboutWorkout) {
        this.privateMessages = privateMessages;
        this.aboutWorkout = aboutWorkout;
    }
}
