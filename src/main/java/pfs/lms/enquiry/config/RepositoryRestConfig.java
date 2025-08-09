package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Import CorsRegistry

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        System.out.println("Exposing ids ~~~~~~~~~~~");

        List<Class<?>> entityClasses = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .map(Type::getJavaType)
                .collect(Collectors.toList());

        config.exposeIdsFor(entityClasses.toArray(new Class[0]));

        cors.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedHeaders("x-requested-with", "authorization", "content-type")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(true)
                .maxAge(3600);
    }
}