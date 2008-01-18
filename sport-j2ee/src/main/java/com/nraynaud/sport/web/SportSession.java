package com.nraynaud.sport.web;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserStore;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class SportSession {

    private final UserStore userStore;
    private final HttpSession session;
    private static final String SPORT_SESSION = SportSession.class.getName();
    private static final String USER = "user";

    public SportSession(final UserStore userStore, final HttpSession session) {
        this.userStore = userStore;
        this.session = session;
        assert getUser() != null;
    }

    public User getUser() {
        final UserWrapper userWrapper = (UserWrapper) session.getAttribute(USER);
        return userWrapper.getUser(userStore);
    }

    public static SportSession fromRequest(final ServletRequest request) {
        return (SportSession) request.getAttribute(SPORT_SESSION);
    }

    public static SportSession storeIntoRequest(final UserStore userStore, final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        final SportSession sportSession = new SportSession(userStore, session);
        request.setAttribute(SPORT_SESSION, sportSession);
        return sportSession;
    }

    public static void openSession(final User user, final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 24 * 3600);
        session.setAttribute(USER, new UserWrapper(user));
    }

    public static class UserWrapper implements Serializable {
        @SuppressWarnings({"TransientFieldNotInitialized"})
        private transient User user;
        private Long userId;

        public UserWrapper(final User user) {
            this.user = user;
            this.userId = user.getId();
        }

        public User getUser(final UserStore userStore) {
            if (user == null)
                user = userStore.getUser(userId);
            return user;
        }
    }
}
