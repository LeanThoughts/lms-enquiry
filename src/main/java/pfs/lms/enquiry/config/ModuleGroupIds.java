package pfs.lms.enquiry.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Token group ids plus the groups those groups belong to.
 * AppraisalOfficer on the user can belong to BusinessPartners.Display; both ids are returned
 * so {@code /api/me} can map them to module permission names.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ModuleGroupIds {

    private final GroupMembershipLookup membershipLookup;

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
}
