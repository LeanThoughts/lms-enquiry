//package pfs.lms.enquiry.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@Slf4j
//
//// Development
//@Profile("dev")
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class DevSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // @formatter:off
//
//        http
//                //Configure HTTP Basic Authentication
//                .httpBasic()
//
//                //Exemptions
//                .and().antMatcher("/api/**").authorizeRequests().antMatchers("/api/time").permitAll()
//
//
//                .and().authorizeRequests().antMatchers("/api/dispenser/**","/api/signage/**", "/api/devices/**","/push/**","/api/time", "/api/download/**","/api/config","/api/config/**", "/announcements/**").permitAll()
//
//
//                //Configure the path for Basic Authentication
//                .and().authorizeRequests().anyRequest().authenticated()
//
//                //Disable CSRF and FrameOptions for H2
//                .and().csrf().disable().headers().frameOptions().disable()
//
//                //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                //Disable Basic Authentication for OPTIONS
//                .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                //Added headers for CORS
//                .and().headers().addHeaderWriter((request, response) -> {
//            response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//        });
//
//
//        // @formatter:on
//    }
//
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("admin")
//                        .roles("ADMIN")
//                        .build();
//
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(admin,user);
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager () throws  Exception{
//        return super.authenticationManager();
//    }
//}
