package pfs.lms.enquiry.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * @author : fahadfazil
 * @since : 22/12/17
*/

@Slf4j
@Order(110)

@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off

        http
                .logout().logoutUrl("/logout")
                .and().authorizeRequests().antMatchers(
                        "/api/password/strength",
                "/api/signup",
                "/api/signup/verify/**",
                "/api/password/reset",
                "/signup",
                "/passwordReset",
                "/assests",
                "/assets/**",
                "/assets/**/**",
                "/runtime.js",
                "/polyfills.js",
                "/styles.js",
                "/vendor.js",
                "/main.js",
                "/style.css",
                "/favicon.ico",
                "/*.js","/*.css",
                "/api/me").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().csrf().disable().headers().frameOptions().disable();



        // @formatter:on
    }
}
