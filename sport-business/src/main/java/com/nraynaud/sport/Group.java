package com.nraynaud.sport;

import java.util.Collection;
import java.util.Date;

public interface Group extends Topic {
    Long getId();

    String getName();

    User getOwner();

    String getDescription();

    Date getCreationDate();

    Collection<? extends User> getMembers();
}
