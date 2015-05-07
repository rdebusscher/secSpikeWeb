package org.omnifaces.security.glue;

import org.omnifaces.security.jaspic.core.Jaspic;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author airhacks.com
 */
@WebListener
public class SecurityStartup implements ServletContextListener {

    @Inject
    Instance<ServerAuthModule> authModule;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        for (ServerAuthModule serverAuthModule : authModule) {
            System.out.println("Found serverAuthModule = " + serverAuthModule);
            Jaspic.registerServerAuthModule(serverAuthModule, sce.getServletContext());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
