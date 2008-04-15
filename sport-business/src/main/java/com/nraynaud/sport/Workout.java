package com.nraynaud.sport;

import java.util.Collection;
import java.util.Date;

public interface Workout extends Topic {
    Date getDate();

    User getUser();

    /**
     * @return workout distance in km
     */
    Double getDistance();

    /**
     * @return workout duration in seconds
     */
    Long getDuration();

    Long getId();

    UserString getDiscipline();

    Long getMessageNumber();

    Collection<User> getParticipants();

    boolean isNikePlus();

    String getNikePlusId();

    UserString getComment();
}
