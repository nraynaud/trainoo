package com.nraynaud.sport.web.view;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;

public class PrivateMessageFormConfig {
    public final UserString receiver;
    public final Workout aboutWorkout;

    public PrivateMessageFormConfig(final UserString receiver) {
        this.receiver = receiver;
        aboutWorkout = null;
    }

    public PrivateMessageFormConfig(final UserString receiver, final Workout aboutWorkout) {
        this.receiver = receiver;
        this.aboutWorkout = aboutWorkout;
    }
}
