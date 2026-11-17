package pfs.lms.enquiry.sapintegrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the {@link RestTemplate}s the portal uses to call SAP, with the {@link SapHttpLoggingInterceptor}
 * attached, so that every request to SAP ends up in the SAP HTTP log. Use it wherever a {@code new RestTemplate()}
 * for SAP used to be created:
 * <pre>
 * RestTemplate restTemplate = SapRestTemplates.logged();
 * RestTemplate restTemplate = SapRestTemplates.logged(restTemplateBuilder.errorHandler(…));
 * </pre>
 * The static access exists because the SAP services create their templates inside their methods; the interceptor
 * is registered when this component starts; a template created before that logs from then on (in a plain unit
 * test without the Spring context it simply has no logging).
 * <p>
 * The templates use a buffering request factory so that the response body can be read by the log and again by
 * the caller.
 */
@Component
public class SapRestTemplates {

    private static final Logger log = LoggerFactory.getLogger(SapRestTemplates.class);

    private static volatile SapHttpLoggingInterceptor interceptor;

    /**
     * What is attached to the templates: forwards to the logging interceptor once this component has started, and
     * passes the call through untouched before that — so a template created in a bean constructor (before this
     * component initialised) still logs every later call.
     */
    private static final ClientHttpRequestInterceptor DELEGATE = (request, body, execution) -> {
        SapHttpLoggingInterceptor current = interceptor;
        if (current == null) {
            log.warn("SAP HTTP log: not registered yet, {} {} is not logged", request.getMethodValue(), request.getURI());
            return execution.execute(request, body);
        }
        return current.intercept(request, body, execution);
    };

    private final SapHttpLoggingInterceptor loggingInterceptor;

    public SapRestTemplates(SapHttpLoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @PostConstruct
    void register() {
        interceptor = loggingInterceptor;
        log.info("SAP HTTP log: RestTemplate logging registered");
    }

    /** A new RestTemplate for SAP calls, logged. */
    public static RestTemplate logged() {
        RestTemplate restTemplate = new RestTemplate(requestFactory());
        attach(restTemplate);
        return restTemplate;
    }

    /** A RestTemplate built with the given builder (error handler, converters, …), logged. */
    public static RestTemplate logged(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.requestFactory(SapRestTemplates::requestFactory).build();
        attach(restTemplate);
        return restTemplate;
    }

    /**
     * Adds the logging to a RestTemplate whose request factory already buffers (as those from {@link #logged()}
     * do); without buffering the response body would be consumed by the log.
     */
    public static RestTemplate attach(RestTemplate restTemplate) {
        if (!restTemplate.getInterceptors().contains(DELEGATE)) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
            interceptors.add(DELEGATE);
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;
    }

    private static ClientHttpRequestFactory requestFactory() {
        return new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
    }
}
