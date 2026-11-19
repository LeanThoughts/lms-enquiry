package pfs.lms.enquiry.config;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleGroupIdsTest {

    private static final String APPRAISAL_OFFICER = "f2a82b28-d18a-4387-ae80-6de7adfe0caa";
    private static final String BUSINESS_PARTNERS_DISPLAY = "bc44367a-9b1d-4e88-aabf-7b2d32641bb7";
    private static final String BUSINESS_PARTNERS_EDIT = "03ef1b62-ccaa-4573-a261-fc00c231798c";

    @Test
    void addsGroupsTheRoleGroupBelongsTo() {
        ModuleGroupIds moduleGroupIds = new ModuleGroupIds(groupIds -> {
            assertThat(groupIds).containsExactly(APPRAISAL_OFFICER);
            return List.of(BUSINESS_PARTNERS_DISPLAY, BUSINESS_PARTNERS_EDIT);
        });

        assertThat(moduleGroupIds.expand(List.of(APPRAISAL_OFFICER)))
                .containsExactly(APPRAISAL_OFFICER, BUSINESS_PARTNERS_DISPLAY, BUSINESS_PARTNERS_EDIT);
    }

    @Test
    void mapsThoseMembershipsToModulePermissionNames() {
        RoleGroupProperties properties = new RoleGroupProperties();
        properties.setRoleGroups(List.of(
                mapping("BusinessPartners.Display", BUSINESS_PARTNERS_DISPLAY),
                mapping("BusinessPartners.Edit", BUSINESS_PARTNERS_EDIT)));
        ModuleGroupIds moduleGroupIds = new ModuleGroupIds(
                (Collection<String> groupIds) -> List.of(BUSINESS_PARTNERS_DISPLAY, BUSINESS_PARTNERS_EDIT));

        assertThat(properties.permissionsFor(moduleGroupIds.expand(List.of(APPRAISAL_OFFICER))))
                .containsExactly("BusinessPartners.Display", "BusinessPartners.Edit");
    }

    @Test
    void usesOnlyTheGroupsTheUserBelongsTo() {
        String processEnquiry = "72b06008-1cae-46eb-973a-9ef298831e65";
        String iccApproval = "b25f7d7b-7221-4bee-addc-09f3a6062c96";
        RoleGroupProperties properties = new RoleGroupProperties();
        properties.setRoleGroups(List.of(
                mapping("LoanContracts.ProcessEnquiry.Display", processEnquiry),
                mapping("LoanContracts.IccApproval.Display", iccApproval)));
        ModuleGroupIds moduleGroupIds = new ModuleGroupIds(new GroupMembershipLookup() {
            @Override
            public Collection<String> memberGroupIds(Collection<String> groupIds) {
                return List.of(processEnquiry, iccApproval);
            }

            @Override
            public Optional<Collection<String>> groupsOfUser(String userObjectId) {
                return Optional.of(List.of(processEnquiry));
            }
        });

        assertThat(properties.permissionsFor(moduleGroupIds.forSignIn(
                "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", List.of(processEnquiry, iccApproval))))
                .containsExactly("LoanContracts.ProcessEnquiry.Display");
    }

    @Test
    void keepsTheTokenGroupsWhenMembershipLookupReturnsNothing() {
        ModuleGroupIds moduleGroupIds = new ModuleGroupIds(groupIds -> List.of());

        assertThat(moduleGroupIds.expand(List.of(APPRAISAL_OFFICER)))
                .containsExactly(APPRAISAL_OFFICER);
    }

    private static RoleGroupProperties.Mapping mapping(String name, String groupId) {
        RoleGroupProperties.Mapping mapping = new RoleGroupProperties.Mapping();
        mapping.setName(name);
        mapping.setGroupId(groupId);
        return mapping;
    }
}
