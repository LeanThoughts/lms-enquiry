package pfs.lms.enquiry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // For demo purposes, NOT for production!
import org.springframework.security.crypto.password.PasswordEncoder; // For real password encoding

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // This is a minimal in-memory user detail service.
        // For production, you'd integrate with a database or directory service.

        UserDetails user = User.withUsername("activiti")
                .password("password") // Use a real password encoder for production!
                .roles("USER", "ACTIVITI_USER")
                .build();

        // For a simple in-memory store. Not suitable for production.
        return new InMemoryUserDetailsManager(user);
    }

    // IMPORTANT: For production, NEVER use NoOpPasswordEncoder.
    // Always use BCryptPasswordEncoder or similar.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // This is highly insecure and only for testing/development.
        // For production, use BCryptPasswordEncoder: return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}