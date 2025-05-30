package pfs.lms.enquiry.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.web.cors.CorsConfigurationSource;
// No 'import static org.springframework.security.config.Customizer.withDefaults;' needed for 5.1.x

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfigurationSource corsConfigurationSource; // Use constructor injection
    private final OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver; // 1. Inject the custom resolver

    // Constructor injection is preferred to avoid the "might not have been initialized" error
    public WebSecurityConfig(CorsConfigurationSource corsConfigurationSource,
                             OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.pkceAuthorizationRequestResolver = pkceAuthorizationRequestResolver;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable() // Typically disabled for API-only BFFs, consider enabling for web forms

                .authorizeRequests() // No lambda here for 5.1.x
                // Allow unauthenticated access to the root and static resources (your Angular app)
                .antMatchers("/", "/index.html", "/static/**", "/assets/**",
                        "/*.js", "/*.css", "/*.ico", "/*.json", "/error",
                        "/actuator/**").permitAll()
                // Allow access to login/logout related paths handled by Spring Security
                .antMatchers("/login/**", "/oauth2/**", "/logout/**").permitAll()
                // Allow preflight OPTIONS requests for CORS
                .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // Secure your API endpoints - these require authentication
                .antMatchers("/api/**").authenticated()
                // All other requests also require authentication by default for a BFF
                // Or adjust based on what other paths are publicly accessible vs. secured
                .anyRequest().authenticated()
                .and() // End of authorizeRequests() chain
                .oauth2Login(oauth2Login -> oauth2Login // Use lambda for oauth2Login config if available
                        .authorizationEndpoint(authorizationEndpoint ->
                                // 3. Use your custom resolver here
                                authorizationEndpoint.authorizationRequestResolver(pkceAuthorizationRequestResolver)
                        )
                        // Specify the default redirect after successful login
                        .defaultSuccessUrl("http://localhost:4200/home", true)
                        .failureUrl("/login?error")
                )
                .oauth2Client() // Simply call it, no 'withDefaults()' needed for 5.1.x
                .and() // End of oauth2Client() chain
                .logout()
                .logoutSuccessUrl("/") // Redirect to application root after logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

        // This line enables the BFF to validate JWTs if they are directly sent to it (e.g., from another service)
        // For a typical BFF, this configuration primarily validates the JWT received during the OAuth2 login flow.
        // It's already configured via application.yml, so just enabling it here is sufficient.
        http.oauth2ResourceServer().jwt();
    }
}