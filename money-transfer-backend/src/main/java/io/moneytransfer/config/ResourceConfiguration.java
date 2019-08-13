package io.moneytransfer.config;

import org.glassfish.jersey.server.ResourceConfig;

public class ResourceConfiguration extends ResourceConfig {
    public ResourceConfiguration() {
        register(new CdiBinder());
        packages(true, "io.moneytransfer");
    }
}
