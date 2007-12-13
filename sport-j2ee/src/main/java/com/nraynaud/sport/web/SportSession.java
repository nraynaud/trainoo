package com.nraynaud.sport.web;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class SportSession {

    private final Application application;
    private final HttpSession session;
    private static final String SPORT_SESSION = SportSession.class.getName();
    private static final String USER = "user";

    public SportSession(final Application application, final HttpSession session) {
        this.application = application;
        this.session = session;
        assert getUser() != null;
    }

    public SportSession(final Application application, final HttpSession session, final User user) {
        this(application, session);
        session.setAttribute(USER, new UserWrapper(user));
    }

    public User getUser() {
        final UserWrapper userWrapper = (UserWrapper) session.getAttribute(USER);
        return userWrapper.getUser(application);
    }

    public static SportSession fromRequest(final ServletRequest request) {
        return (SportSession) request.getAttribute(SPORT_SESSION);
    }

    public static SportSession toRequest(final Application application, final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        final SportSession sportSession = new SportSession(application, session);
        request.setAttribute(SPORT_SESSION, sportSession);
        return sportSession;
    }

    public static SportSession openSession(final User user,
                                           final Application application,
                                           final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 24 * 3600);
        return new SportSession(application, session, user);
    }

    public static class UserWrapper implements Serializable {
        @SuppressWarnings({"TransientFieldNotInitialized"})
        private transient User user;
        private long userId;

        public UserWrapper(final User user) {
            this.user = user;
            this.userId = user.getId();
        }

        public User getUser(final Application application) {
            if (user == null)
                user = application.findUser(userId);
            return user;
        }
    }
}
