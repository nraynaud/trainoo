package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Track;
import com.nraynaud.sport.web.ActionDetail;

public class WorkoutEditPageDetails extends WorkoutPageDetails {
    public final Iterable<Track> userTracks;

    public WorkoutEditPageDetails(final WorkoutView workoutView, final String pageTile, final ActionDetail doAction,
                                  final ActionDetail cancelAction, final Iterable<Track> userTracks) {
        super(workoutView, pageTile, doAction, cancelAction);
        this.userTracks = userTracks;
    }
}
