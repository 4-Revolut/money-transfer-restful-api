package io.moneytransfer.di.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DiServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        servletContextEvent.getServletContext().add
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //do nothing
    }
}
