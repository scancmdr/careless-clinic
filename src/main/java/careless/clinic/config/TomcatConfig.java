package careless.clinic.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat configuration for properly handling X-Forwarded headers.
 * <p>
 * Configures RemoteIpValve to trust proxy headers and correctly determine
 * the external protocol, port, and hostname for redirect generation.
 *
 * @author jay
 */
@Slf4j
@Configuration
public class TomcatConfig {

    /**
     * Customizes Tomcat to properly handle X-Forwarded-* headers.
     * <p>
     * This ensures that redirects use the external HTTPS URL without port 8080.
     *
     * @return WebServerFactoryCustomizer for Tomcat
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            log.info("Configuring Tomcat RemoteIpValve for proxy header handling");

            RemoteIpValve remoteIpValve = new RemoteIpValve();

            // Trust all proxies (we're behind Traefik and nginx)
            remoteIpValve.setInternalProxies(".*");

            // Configure which headers to use
            remoteIpValve.setRemoteIpHeader("X-Forwarded-For");
            remoteIpValve.setProxiesHeader("X-Forwarded-By");
            remoteIpValve.setProtocolHeader("X-Forwarded-Proto");
            remoteIpValve.setProtocolHeaderHttpsValue("https");
            remoteIpValve.setPortHeader("X-Forwarded-Port");
            remoteIpValve.setHostHeader("X-Forwarded-Host");

            // Enable changing protocol based on X-Forwarded-Proto
            remoteIpValve.setChangeLocalPort(true);

            log.info("RemoteIpValve configured - internalProxies: .*, protocolHeader: X-Forwarded-Proto, portHeader: X-Forwarded-Port");

            factory.addEngineValves(remoteIpValve);
        };
    }
}
