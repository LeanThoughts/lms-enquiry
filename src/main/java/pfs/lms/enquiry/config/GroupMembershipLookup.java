package pfs.lms.enquiry.config;

import java.util.Collection;
import java.util.Optional;

/**
 * Entra group lookups used to turn a signed-in user into module permissions.
 */
public interface GroupMembershipLookup {

    /**
     * Groups that the given groups belong to (the portal "Group memberships" list).
     */
    Collection<String> memberGroupIds(Collection<String> groupIds);

    /**
     * Groups the user belongs to, including nested groups.
     * Empty when Graph cannot be reached. An empty list means the user is in no groups.
     */
    default Optional<Collection<String>> groupsOfUser(String userObjectId) {
        return Optional.empty();
    }
}
