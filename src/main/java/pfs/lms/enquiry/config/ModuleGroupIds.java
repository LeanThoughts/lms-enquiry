package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Group ids used to build module permissions for {@code /api/me}.
 * The signed-in user's own Entra membership, including nested groups, is the list that is mapped.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ModuleGroupIds {

    private final GroupMembershipLookup membershipLookup;

    /**
     * Groups that decide module permissions for this sign-in.
     * Graph's membership for the user wins. The token group list is used only when Graph cannot be reached,
     * because that claim can list every group assigned to the application rather than the user's own groups.
     */
    public List<String> forSignIn(String userObjectId, Collection<String> tokenGroupIds) {
        if (userObjectId != null && !userObjectId.trim().isEmpty()) {
            Optional<Collection<String>> membership = membershipLookup.groupsOfUser(userObjectId.trim());
            if (membership.isPresent()) {
                return distinct(membership.get());
            }
        }
        return distinct(tokenGroupIds);
    }

    public List<String> expand(Collection<String> tokenGroupIds) {
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        if (tokenGroupIds != null) {
            for (String id : tokenGroupIds) {
                if (id != null && !id.trim().isEmpty()) {
                    ids.add(id.trim());
                }
            }
        }
        if (ids.isEmpty()) {
            return List.of();
        }
        int direct = ids.size();
        Collection<String> memberships = membershipLookup.memberGroupIds(ids);
        if (memberships != null) {
            for (String id : memberships) {
                if (id != null && !id.trim().isEmpty()) {
                    ids.add(id.trim());
                }
            }
        }
        if (ids.size() > direct) {
            log.info("Entra group memberships included in module permission lookup: {}", ids);
        }
        return new ArrayList<>(ids);
    }

    private static List<String> distinct(Collection<String> groupIds) {
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        if (groupIds != null) {
            for (String id : groupIds) {
                if (id != null && !id.trim().isEmpty()) {
                    ids.add(id.trim());
                }
            }
        }
        return new ArrayList<>(ids);
    }
}
