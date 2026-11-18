package pfs.lms.enquiry.config;

import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final RoleGroupProperties roleGroupProperties;
    private final ModuleGroupIds moduleGroupIds;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        List<String> groupIds = moduleGroupIds.expand(
                groupIds(oidcUser, userRequest.getAccessToken().getTokenValue()));
        Collection<GrantedAuthority> authorities = roleGroupProperties.authoritiesFor(groupIds);
        List<String> permissions = roleGroupProperties.permissionsFor(groupIds);
        if (groupIds.isEmpty()) {
            log.warn("Entra token has no groups claim; module permissions will be empty");
        } else {
            log.info("Module permissions resolved from Entra groups: {}", permissions);
        }
        return withAuthorities(oidcUser, authorities);
    }

    private static List<String> groupIds(OidcUser oidcUser, String accessToken) {
        List<String> groupIds = new ArrayList<>();
        groupIds.addAll(RoleGroupProperties.strings(oidcUser.getIdToken().getClaim("groups")));
        groupIds.addAll(groupsFromAccessToken(accessToken));
        return groupIds;
    }

    private static List<String> groupsFromAccessToken(String tokenValue) {
        if (tokenValue == null || tokenValue.chars().filter(ch -> ch == '.').count() != 2) {
            return List.of();
        }
        try {
            return RoleGroupProperties.strings(JWTParser.parse(tokenValue).getJWTClaimsSet().getClaim("groups"));
        } catch (ParseException ex) {
            log.debug("Access token is not a readable JWT; groups claim was not read from it");
            return List.of();
        }
    }

    /**
     * Keep the principal name as the email claim when Entra sends one, so {@code /api/me}
     * can still load the local user. Fall back to {@code sub} when none of those claims exist.
     */
    private static OidcUser withAuthorities(OidcUser oidcUser, Collection<GrantedAuthority> authorities) {
        OidcIdToken idToken = oidcUser.getIdToken();
        for (String claim : new String[]{"preferred_username", "email", "upn", "unique_name"}) {
            String value = idToken.getClaimAsString(claim);
            if (value != null && !value.isEmpty()) {
                return new DefaultOidcUser(authorities, idToken, oidcUser.getUserInfo(), claim);
            }
        }
        return new DefaultOidcUser(authorities, idToken, oidcUser.getUserInfo());
    }
}
