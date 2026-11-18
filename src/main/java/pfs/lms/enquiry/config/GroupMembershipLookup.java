package pfs.lms.enquiry.config;

import java.util.Collection;

/**
 * Groups that the given Entra groups belong to (the portal "Group memberships" list).
 */
public interface GroupMembershipLookup {

    Collection<String> memberGroupIds(Collection<String> groupIds);
}
