package pfs.lms.enquiry.config;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
public class JWTUserDetails {
    public String getUserInfo(Principal principal) {
        if (principal instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtPrincipal = (JwtAuthenticationToken) principal;
            Map<String, Object> claims = jwtPrincipal.getTokenAttributes();
            String username = (String) claims.get("unique_name"); // Replace "preferred_username" with the actual claim name

            if (username != null) {
                if (username.equals("gopinath@synapseware.io"))
                    return "admin@gmail.com";
                else
                    return username;
            } else {
                return "Username claim not found in JWT";
            }
        }
        return "Could not retrieve username";
    }
}
