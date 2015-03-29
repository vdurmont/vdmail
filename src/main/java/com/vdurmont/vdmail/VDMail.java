package com.vdurmont.vdmail;

import com.vdurmont.vdmail.config.WebConfig;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Main class for the API.
 *
 * Creates the servlets, the web server and runs!
 */
public class VDMail {
    private static final Logger LOGGER = Logger.getLogger(VDMail.class);

    public static void main(String[] args) throws Exception {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(WebConfig.class);
        applicationContext.setDisplayName("VDMail");

        ServletHolder servletHolder = new ServletHolder("vdmail", new DispatcherServlet(applicationContext));

        ServletContextHandler servletContext = new ServletContextHandler(null, "/");
        servletContext.addEventListener(new ContextLoaderListener(applicationContext));
        servletContext.addServlet(servletHolder, "/*");

        LOGGER.info("Registering Spring MVC Servlet");
        FilterHolder filterHolder = new FilterHolder(DelegatingFilterProxy.class);
        filterHolder.setName("springSecurityFilterChain");
        servletContext.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

        // Server
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        server.setHandler(servletContext);
        server.start();
        server.join();
    }
}
