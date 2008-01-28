package com.nraynaud.sport.data;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.User;

import java.util.List;

public class BibPageData {
    public final User user;

    public final List<Message> messages;

    public BibPageData(final User user, final List<Message> messages) {
        this.user = user;
        this.messages = messages;
    }
}
