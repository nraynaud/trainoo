package com.nraynaud.sport.web;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserStore;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SportApplication {

    public SportSession storeIntoRequest(final UserStore userStore, final HttpServletRequest request) {
        return SportSession.storeIntoRequest(userStore, request);
    }

    public SportSession openSession(final User user,
                                    final UserStore userStore,
                                    final HttpServletRequest request) {
        return SportSession.openSession(user, userStore, request);
    }
}
