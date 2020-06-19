package com.karasik;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerAuthenticationContext implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("INITIALIZED");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("DESTROYED");
    }
}
