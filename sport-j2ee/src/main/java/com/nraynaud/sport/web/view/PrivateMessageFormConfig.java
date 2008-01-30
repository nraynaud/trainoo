package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Workout;

public class PrivateMessageFormConfig {
    public final String receiver;
    public final Workout aboutWorkout;

    public PrivateMessageFormConfig(final String receiver) {
        this.receiver = receiver;
        aboutWorkout = null;
    }

    public PrivateMessageFormConfig(final String receiver, final Workout aboutWorkout) {
        this.receiver = receiver;
        this.aboutWorkout = aboutWorkout;
    }
}
