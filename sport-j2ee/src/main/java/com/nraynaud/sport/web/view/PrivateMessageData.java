package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Workout;

public class PrivateMessageData {
    public final String receiver;
    public final Workout aboutWorkout;

    public PrivateMessageData(final String receiver) {
        this.receiver = receiver;
        aboutWorkout = null;
    }

    public PrivateMessageData(final String receiver, final Workout aboutWorkout) {
        this.receiver = receiver;
        this.aboutWorkout = aboutWorkout;
    }
}
