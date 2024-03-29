package com.nraynaud.sport.web.view;

import com.nraynaud.sport.formatting.DateIO;
import com.nraynaud.sport.formatting.FormatHelper;

import java.util.Date;

public class WorkoutView {
    public final String id;
    public final String discipline;
    public final String date;
    public final String distance;
    public final String duration;
    public final String energy;
    public final String debriefing;
    public final String trackId;

    public WorkoutView(final String id, final String discipline, final String date, final String distance,
                       final String duration, final String energy, final String debriefing, final String trackId) {
        this.id = id;
        this.discipline = discipline;
        this.date = date;
        this.distance = distance;
        this.duration = duration;
        this.energy = energy;
        this.debriefing = debriefing;
        this.trackId = trackId;
    }

    public static WorkoutView createView(final String id, final String discipline, final Date date,
                                         final Double distance, final Long duration, final Long energy,
                                         final String debriefing, final String trackId) {
        return new WorkoutView(id,
                discipline,
                DateIO.DATE_FORMATTER.print(date.getTime()),
                FormatHelper.formatDistance(distance, "", ""),
                FormatHelper.formatDuration(duration, ""),
                FormatHelper.formatEnergy(energy, "", ""),
                debriefing, trackId);
    }
}
