package com.karasik;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.karasik.modules.AuthenticationModule;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ServerAuthenticationResourceConfig extends ResourceConfig {

    @Inject
    public ServerAuthenticationResourceConfig(ServiceLocator serviceLocator) {
        Injector injector = Guice.createInjector(new AuthenticationModule());
        createGuiceHK2Bridge(serviceLocator, injector);
    }

    private void createGuiceHK2Bridge(ServiceLocator serviceLocator, Injector injector) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(injector);
    }
}
