package pfs.lms.enquiry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;


@Configuration
public class OAuth2PKCEConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2PKCEConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public OAuth2AuthorizationRequestResolver pkceAuthorizationRequestResolver() {
        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository,
                        // This is the base URI for initiating OAuth2 login, should match your
                        // <a href="/oauth2/authorization/{registrationId}"> link
                        "/oauth2/authorization"
                );

        // This is the most reliable way to force PKCE for all clients.
        // It applies the PKCE customizer to every authorization request.
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        return resolver;
    }
}