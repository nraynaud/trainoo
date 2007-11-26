package com.nraynaud.sport.web;

import com.nraynaud.sport.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static User getUser(final HttpSession session) {
        if (session == null)
            return null;
        return (User) session.getAttribute("user");
    }
}
