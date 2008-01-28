package com.nraynaud.sport.web.result;

import org.apache.struts2.dispatcher.ServletActionRedirectResult;

/**
 * shorter and more intentional name
 */
public class Redirect extends ServletActionRedirectResult {
    public Redirect(final String namespace, final String actionName, final String method) {
        super(namespace, actionName, method);
    }

    public Redirect() {
    }
}
