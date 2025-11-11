package careless.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
    public static Provider securityProvider;
    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        securityProvider = getBouncyCastleProvider();
        SpringApplication.run(CarelessClinicApplication.class, args);
    }

    public static Provider getBouncyCastleProvider() {
        // 1. Check if the provider is already installed/available
        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);

        if (provider == null) {
            // 2. If not, add it (this ensures it's available for the rest of the JVM)
            provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            System.out.println("Bouncy Castle Security Provider registered.");
        } else {
            System.out.println("Security Provider already registered: "+provider.getName());
        }
        return provider;
    }
}
