package pfs.lms.enquiry.config;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

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
