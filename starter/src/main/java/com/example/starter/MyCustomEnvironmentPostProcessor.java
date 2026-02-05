package com.example.starter;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

// environment variables  (SERVER_PORT)
// application.(properties,yaml) (server.port)

class MyCustomEnvironmentPostProcessor
        implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
         environment.getPropertySources()
                 .addFirst(new PropertySource<>("springonetour") {
                     @Override
                     public @Nullable Object getProperty(String name) {
                         if (name.equals("a.b.c")) return "42";
                         if (name.equals("server.port")) return "8010";
                         return null;
                     }
                 });
    }
}
