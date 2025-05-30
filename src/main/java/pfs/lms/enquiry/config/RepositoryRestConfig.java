package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Import CorsRegistry

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        Class<?>[] entities = entityManager.getMetamodel().getEntities().stream()
                .map(e -> e.getJavaType()).collect(Collectors.toList()).toArray(new Class[0]);
        config.exposeIdsFor(entities);
    }

    // ADD THIS NEW METHOD TO CONFIGURE CORS
    public void configureCors(CorsRegistry cors) {
        cors.addMapping("/**") // Apply to all paths exposed by Spring Data REST
                .allowedOrigins("http://localhost:4200")
                .allowedHeaders("x-requested-with", "authorization", "content-Type") // Use correct casing for Content-Type
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(true) // Crucial for session-based authentication (like with BFF)
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}