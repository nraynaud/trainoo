package com.nraynaud.sport;

import java.util.Date;

public interface Track {
    UserString getTitle();

    Long getId();

    User getUser();

    String getPoints();

    Date getCreationDate();
}
