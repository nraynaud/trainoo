package com.nraynaud.sport;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public interface Workout extends Topic {
    String ELLIPTIC_BIKE = "vélo elliptique";
    String HOME_BIKE = "vélo d'appartement";
    Collection<String> DISCIPLINES = Arrays.asList("course",
            "vélo",
            "VTT",
            "marche",
            "natation",
            "roller",
            ELLIPTIC_BIKE,
            HOME_BIKE);

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

    Long getMessageCount();

    Collection<User> getParticipants();

    boolean isNikePlus();

    String getNikePlusId();

    UserString getDebriefing();

    Long getEnergy();

    Track getTrack();
}
