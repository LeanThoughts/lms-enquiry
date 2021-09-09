package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    private final EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        Class[] entities = entityManager.getMetamodel().getEntities().stream().map(e -> e.getJavaType()).collect(Collectors.toList()).toArray(new Class[0]);
        config.exposeIdsFor(entities);

        config.getCorsRegistry().addMapping("/**").allowedOrigins("http://localhost:4200")
                .allowedHeaders("x-requested-with", "authorization", "content-Type")
                .allowedMethods("GET, POST, PUT, DELETE, OPTIONS, PATCH");
    }
}