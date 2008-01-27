package com.nraynaud.sport;

import java.util.List;

public class BibPageData {
    public final User user;

    public final List<Message> messages;

    public BibPageData(final User user, final List<Message> messages) {
        this.user = user;
        this.messages = messages;
    }
}
