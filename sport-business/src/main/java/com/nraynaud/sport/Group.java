package com.nraynaud.sport;

import java.util.Collection;
import java.util.Date;

public interface Group {
    Long getId();

    String getName();

    User getOwner();

    String getDescription();

    Date getCreationDate();

    Collection<? extends User> getMembers();
}
