package com.nraynaud.sport.web;

import com.opensymphony.xwork2.ActionContext;

import javax.servlet.*;
import java.beans.Introspector;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class SportFilter implements Filter {
    public void init(final FilterConfig filterConfig) throws ServletException {
        cleanupActionContext();
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws
            IOException,
            ServletException {
        chain.doFilter(request, response);
    }

    public void destroy() {
        cleanupActionContext();
        try {
            Introspector.flushCaches();
            for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();) {
                final Driver driver = (Driver) e.nextElement();
                if (driver.getClass().getClassLoader() == getClass().getClassLoader()) {
                    DriverManager.deregisterDriver(driver);
                }
            }
        } catch (Throwable e) {
            System.err.println("Failled to cleanup ClassLoader for webapp");
            e.printStackTrace();
        }
    }

    /**
     * necessary because struts creates threadlocals at deploy and undeploy
     */
    private static void cleanupActionContext() {
        ActionContext.setContext(null);
    }
}
