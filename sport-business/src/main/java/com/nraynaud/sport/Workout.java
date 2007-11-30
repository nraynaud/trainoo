package com.nraynaud.sport;

import java.util.Date;

public interface Workout {
    Date getDate();

    User getUser();

    /**
     * @return workout distance in km
     */
    Double getDistance();

    /**
     * @return workout duration in milleconds
     */
    Long getDuration();

    Long getId();
}
