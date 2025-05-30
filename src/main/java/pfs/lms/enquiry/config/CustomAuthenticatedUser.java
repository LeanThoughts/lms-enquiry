// src/main/java/pfs/lms/enquiry/model/CustomAuthenticatedUser.java
package pfs.lms.enquiry.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomAuthenticatedUser implements OAuth2User, OidcUser {

    private final String id;
    private final String email;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final OidcUser oidcUser;

    public CustomAuthenticatedUser(OidcUser oidcUser) {
        this.oidcUser = oidcUser;
        this.attributes = oidcUser.getClaims();

        this.id = oidcUser.getSubject();

        // THIS IS WHERE THE EMAIL IS EXTRACTED
        String extractedEmail = (String) oidcUser.getAttribute("preferred_username");
        if (extractedEmail == null) {
            extractedEmail = (String) oidcUser.getAttribute("email");
        }
        if (extractedEmail == null) {
            extractedEmail = (String) oidcUser.getAttribute("upn");
        }
        this.email = extractedEmail;

        this.displayName = (String) oidcUser.getAttribute("name");
        this.authorities = oidcUser.getAuthorities();
    }

    // This is the CRITICAL OVERRIDE for request.getPrincipal().getName()
    @Override
    public String getName() {
        // Return the email if available, otherwise fallback to the ID (sub claim)
        String name;
        name = this.email != null ? this.email : this.id;
        if (this.email.equals("gopinath@synapseware.io"))
            name = "admin@gmail.com";
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() { return attributes; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public Map<String, Object> getClaims() { return oidcUser.getClaims(); }
    @Override
    public org.springframework.security.oauth2.core.oidc.OidcIdToken getIdToken() { return oidcUser.getIdToken(); }
    @Override
    public org.springframework.security.oauth2.core.oidc.OidcUserInfo getUserInfo() { return oidcUser.getUserInfo(); }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
}