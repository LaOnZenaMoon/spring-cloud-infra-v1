package me.lozm.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"!test"})
@Configuration
public class ApplicationConfig {
//    @Bean
//    public GracefulShutdown gracefulShutdown() {
//        return new GracefulShutdown();
//    }
//
//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//        factory.addConnectorCustomizers(gracefulShutdown);
//        return factory;
//    }
}
