package careless.clinic.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration with dual {@link SecurityFilterChain} setup.
 * <p>
 * Configures public routes (page-one) and OAuth2-protected routes (page-two).
 *
 * @author jay
 * @see SecurityFilterChain
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    /**
     * Post-construction initialization logging.
     */
    @PostConstruct
    public void init() {
        log.info("===> SecurityConfig manually initialized! <===");
        log.info("Google Client ID configured: {}", clientId != null && !clientId.isEmpty() ? "YES" : "NO");
    }

    /**
     * Configures public {@link SecurityFilterChain} without OAuth2 login.
     * <p>
     * Evaluated first (Order 1) for public routes.
     *
     * @param http Spring Security HTTP configuration
     * @return configured security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("===> Configuring PUBLIC SecurityFilterChain <===");

        return http
                .securityMatcher("/", "/index", "/index.html", "/page-one", "/public/**",
                               "/css/**", "/js/**", "/images/**", "/error", "/favicon.ico")
                .authorizeHttpRequests(authorize -> {
                    log.info("PUBLIC chain: Configuring permitAll() for all requests");
                    authorize.anyRequest().permitAll();
                })
                .csrf(csrf -> csrf.disable())
                .build();
    }

    /**
     * Configures protected {@link SecurityFilterChain} with OAuth2 login.
     * <p>
     * Evaluated second (Order 2) for protected routes requiring authentication.
     *
     * @param http Spring Security HTTP configuration
     * @return configured security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(2)
    public SecurityFilterChain protectedSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("===> Configuring PROTECTED SecurityFilterChain with OAuth2 <===");

        return http
                .securityMatcher( "/protected/**", "/private/**", "/page-two",
                                "/oauth2/**", "/login/oauth2/**")
                .authorizeHttpRequests(authorize -> {
                    log.info("PROTECTED chain: Configuring authorization rules");
                    authorize
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth2 -> {
                    log.info("PROTECTED chain: Configuring OAuth2 login");
                    oauth2.defaultSuccessUrl("/page-two", true);
                })
                .build();
    }

    /**
     * Creates OAuth2 {@link ClientRegistrationRepository} for Google authentication.
     *
     * @return in-memory client registration repository
     */
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        log.info("===> Configuring ClientRegistrationRepository manually <===");
        return new InMemoryClientRegistrationRepository(googleClientRegistration());
    }

    /**
     * Builds Google {@link ClientRegistration} from configuration properties.
     *
     * @return configured Google client registration
     */
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    /**
     * Provides default {@link OAuth2UserService} for loading user info.
     *
     * @return default OAuth2 user service
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService();
    }
}
