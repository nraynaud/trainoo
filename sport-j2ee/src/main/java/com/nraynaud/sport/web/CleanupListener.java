package com.nraynaud.sport.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.beans.Introspector;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class CleanupListener implements ServletContextListener {
    public void contextInitialized(final ServletContextEvent event) {
    }

    public void contextDestroyed(final ServletContextEvent event) {
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
}