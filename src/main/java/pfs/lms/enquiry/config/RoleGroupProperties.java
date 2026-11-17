package pfs.lms.enquiry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Maps Entra group object ids to module permission names.
 * The token carries the id; {@code /api/me} returns the name.
 */
@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class RoleGroupProperties {

    private List<Mapping> roleGroups = new ArrayList<>();

    public void setRoleGroups(List<Mapping> roleGroups) {
        this.roleGroups = roleGroups == null ? new ArrayList<>() : roleGroups;
    }

    /**
     * Permission names whose configured group id is in {@code groupIds}.
     * Comparison ignores case and surrounding spaces. Order follows the configuration.
     */
    public List<String> permissionsFor(Collection<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty() || roleGroups.isEmpty()) {
            return List.of();
        }
        Set<String> presented = new HashSet<>();
        for (String groupId : groupIds) {
            String normalized = normalize(groupId);
            if (normalized != null) {
                presented.add(normalized);
            }
        }
        List<String> names = new ArrayList<>();
        for (Mapping mapping : roleGroups) {
            if (mapping == null) {
                continue;
            }
            String name = normalizeName(mapping.getName());
            String groupId = normalize(mapping.getGroupId());
            if (name != null && groupId != null && presented.contains(groupId) && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    /**
     * Permission names already stored on the authentication. Unknown authorities,
     * including {@code ROLE_USER}, are left out.
     */
    public List<String> permissionsOf(Authentication authentication) {
        if (authentication == null) {
            return List.of();
        }
        Set<String> known = new HashSet<>();
        for (Mapping mapping : roleGroups) {
            if (mapping == null) {
                continue;
            }
            String name = normalizeName(mapping.getName());
            if (name != null) {
                known.add(name);
            }
        }
        List<String> names = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String value = authority.getAuthority();
            if (known.contains(value) && !names.contains(value)) {
                names.add(value);
            }
        }
        return names;
    }

    public List<GrantedAuthority> authoritiesFor(Collection<String> groupIds) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        for (String permission : permissionsFor(groupIds)) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    public static List<String> strings(Object claim) {
        if (claim == null) {
            return List.of();
        }
        if (claim instanceof String) {
            String value = ((String) claim).trim();
            return value.isEmpty() ? List.of() : List.of(value);
        }
        if (claim instanceof Collection) {
            List<String> values = new ArrayList<>();
            for (Object item : (Collection<?>) claim) {
                if (item == null) {
                    continue;
                }
                String value = item.toString().trim();
                if (!value.isEmpty()) {
                    values.add(value);
                }
            }
            return values;
        }
        return List.of();
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed.toLowerCase(Locale.ROOT);
    }

    private static String normalizeName(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Getter
    @Setter
    public static class Mapping {
        private String name;
        private String groupId;
    }
}
