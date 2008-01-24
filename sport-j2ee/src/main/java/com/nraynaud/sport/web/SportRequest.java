package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.http.HttpServletRequest;

public class SportRequest {
    private final HttpServletRequest servletRequest;
    private final UserStore userStore;
    private SportSession session;
    public static final String SPORT_REQUEST = "sportRequest";

    public SportRequest(final UserStore userStore, final HttpServletRequest request) {
        this.userStore = userStore;
        servletRequest = request;
    }

    public boolean isLogged() {
        return getSportSession() != null;
    }

    public SportSession getSportSession() {
        if (session == null)
            session = SportSession.storeIntoRequest(userStore, servletRequest);
        return session;
    }

    public String getMethod() {
        return servletRequest.getMethod();
    }

    public static SportRequest getSportRequest() {
        return (SportRequest) ActionContext.getContext().get(SPORT_REQUEST);
    }
}
