package com.nraynaud.sport;

import java.util.Date;

public interface Message {

    Long getId();

    UserString getContent();

    User getSender();

    Workout getWorkout();

    boolean canDelete(User user);

    boolean canEdit(User user);

    MessageKind getMessageKind();

    Date getDate();
}
