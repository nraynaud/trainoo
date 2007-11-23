package com.nraynaud.sport.web;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

public class SessionDebugger implements HttpSessionListener {
    public void sessionCreated(final HttpSessionEvent event) {
        System.out.println("------------ session creation");
        new Exception().printStackTrace(System.out);
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
    }
}
