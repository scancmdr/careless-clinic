package careless.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Careless Clinic demonstration application.
 * <p>
 * Manually configures OAuth2 flow by excluding Spring Security autoconfiguration
 * to prevent aggressive forcing of all traffic through a single OAuth2 endpoint.
 *
 * @author jay
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration.class
})
public class CarelessClinicApplication {
    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CarelessClinicApplication.class, args);
    }
}
