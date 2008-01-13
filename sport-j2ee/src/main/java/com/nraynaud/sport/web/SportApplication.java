package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;

import javax.servlet.http.HttpServletRequest;

public class SportApplication {

    public SportSession storeIntoRequest(final UserStore userStore, final HttpServletRequest request) {
        return SportSession.storeIntoRequest(userStore, request);
    }
}
