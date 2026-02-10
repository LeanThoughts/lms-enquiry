package pfs.lms.enquiry.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfigurationSource corsConfigurationSource;
    private final OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver;
    private final CustomOidcUserService customOidcUserService;

    public WebSecurityConfig(CorsConfigurationSource corsConfigurationSource,
                             OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver,
                             CustomOidcUserService customOidcUserService) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.pkceAuthorizationRequestResolver = pkceAuthorizationRequestResolver;
        this.customOidcUserService = customOidcUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .authorizeRequests()
                // Allow Angular static files
                .antMatchers(
                        "/enquiry/",
                        "/enquiry/index.html",
                        "/enquiry/*.js",
                        "/enquiry/*.css",
                        "/enquiry/*.ico",
                        "/enquiry/assets/**"
                ).permitAll()

                // OAuth2 login endpoints
                .antMatchers("/login/**", "/oauth2/**").permitAll()

                // Secured API
                .antMatchers("/api/**").authenticated()

                // Everything else requires authentication
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/homepage", true)
                .and()
                .logout()
                .logoutSuccessUrl("/enquiry/");
    }
}
