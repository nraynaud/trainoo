package com.nraynaud.sport;

import java.util.Collection;
import java.util.Date;

public interface Group extends Topic {
    Long getId();

    UserString getName();

    User getOwner();

    UserString getDescription();

    Date getCreationDate();

    Collection<? extends User> getMembers();
}
