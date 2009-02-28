package com.nraynaud.sport.web.view;

import com.nraynaud.sport.web.ActionDetail;

public class WorkoutPageDetails {
    public final WorkoutView workoutView;
    public final String pageTile;
    public final ActionDetail doAction;
    public final ActionDetail cancelAction;

    public WorkoutPageDetails(final WorkoutView workoutView, final String pageTile, final ActionDetail doAction,
                              final ActionDetail cancelAction) {
        this.workoutView = workoutView;
        this.pageTile = pageTile;
        this.doAction = doAction;
        this.cancelAction = cancelAction;
    }
}
