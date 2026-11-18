package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
// No 'import static org.springframework.security.config.Customizer.withDefaults;' needed for 5.1.x

@Slf4j
@Configuration
@EnableWebSecurity
@Profile({"oauth", "pfsdevoauth", "localpfsdevoauth"})
@RequiredArgsConstructor
public class OauthWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfigurationSource corsConfigurationSource; // Use constructor injection
    private final OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver; // 1. Inject the custom resolver
    private final CustomOidcUserService customOidcUserService; // Inject your custom service
    private final RoleGroupProperties roleGroupProperties;
    private final ModuleGroupIds moduleGroupIds;

    // Relative urls are resolved against the request host and the /enquiry context path
    @Value("${app.oauth2.login-success-url:http://localhost:9000/enquiry/homepage}")
    private String loginSuccessUrl;

    @Value("${app.oauth2.logout-success-url:http://localhost:9000/enquiry/login}")
    private String logoutSuccessUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("running configure on OauthWebSecurityConfig");
        http
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable() // Typically disabled for API-only BFFs, consider enabling for web forms

                .authorizeRequests() // No lambda here for 5.1.x
                // Allow unauthenticated access to the root and static resources (your Angular app)
                .antMatchers("/", "/index.html", "/static/**", "/assets/**", "/Base/**",
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
                        .userInfoEndpoint(userInfoEndpoint -> // Configure user info endpoint
                                userInfoEndpoint.oidcUserService(customOidcUserService) // <--- THIS IS KEY!
                        )
                        // Specify the default redirect after successful login
                        .defaultSuccessUrl(loginSuccessUrl, true)
                        .failureUrl("/login?error")
                )
                .oauth2Client() // Simply call it, no 'withDefaults()' needed for 5.1.x
                .and() // End of oauth2Client() chain
                .logout()
                .logoutSuccessUrl(logoutSuccessUrl) // Redirect to application root after logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

        // This line enables the BFF to validate JWTs if they are directly sent to it (e.g., from another service)
        // For a typical BFF, this configuration primarily validates the JWT received during the OAuth2 login flow.
        // It's already configured via application.yml, so just enabling it here is sufficient.
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(this::jwtAuthenticationConverter);
    }

    private JwtAuthenticationToken jwtAuthenticationConverter(Jwt jwt) {
        String name = firstClaim(jwt, "preferred_username", "email", "upn");
        if (name == null) {
            name = jwt.getSubject();
        }
        if ("gopinath@synapseware.io".equalsIgnoreCase(name)) {
            name = "admin@gmail.com";
        }
        Collection<GrantedAuthority> authorities = roleGroupProperties.authoritiesFor(
                moduleGroupIds.expand(RoleGroupProperties.strings(jwt.getClaim("groups"))));
        return new JwtAuthenticationToken(jwt, authorities, name);
    }

    private String firstClaim(Jwt jwt, String... names) {
        for (String claim : names) {
            String value = jwt.getClaimAsString(claim);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }
}