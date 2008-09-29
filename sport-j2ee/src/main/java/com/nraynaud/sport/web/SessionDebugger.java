package com.nraynaud.sport.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionDebugger implements HttpSessionListener {
    public void sessionCreated(final HttpSessionEvent event) {
        System.out.println("------------ session creation");
        //noinspection ThrowableInstanceNeverThrown
        new Exception().printStackTrace(System.out);
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
    }
}
