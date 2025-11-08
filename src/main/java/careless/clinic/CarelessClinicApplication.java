package careless.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * We're going to manually configure our OAuth2 flow because Spring autoconfiguration aggressively forces all traffic through
 * a single endpoint that requires OAuth2, effectively ignoring all other configuration.
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration.class
})
public class CarelessClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarelessClinicApplication.class, args);
    }
}
