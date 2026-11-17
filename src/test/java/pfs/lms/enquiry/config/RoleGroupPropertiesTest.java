package pfs.lms.enquiry.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleGroupPropertiesTest {

    private RoleGroupProperties properties;

    @BeforeEach
    void setUp() {
        RoleGroupProperties.Mapping display = mapping(
                "BusinessPartners.Display", "bc44367a-9b1d-4e88-aabf-7b2d32641bb7");
        RoleGroupProperties.Mapping edit = mapping(
                "BusinessPartners.Edit", "03ef1b62-ccaa-4573-a261-fc00c231798c");
        properties = new RoleGroupProperties();
        properties.setRoleGroups(List.of(display, edit));
    }

    @Test
    void mapsGroupIdsToPermissionNames() {
        assertThat(properties.permissionsFor(List.of(
                "BC44367A-9B1D-4E88-AABF-7B2D32641BB7",
                "03ef1b62-ccaa-4573-a261-fc00c231798c")))
                .containsExactly("BusinessPartners.Display", "BusinessPartners.Edit");
    }

    @Test
    void ignoresGroupIdsThatAreNotConfigured() {
        assertThat(properties.permissionsFor(List.of("11111111-1111-1111-1111-111111111111")))
                .isEmpty();
    }

    @Test
    void readsAssignedPermissionNamesFromTheAuthentication() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(null, null, List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("BusinessPartners.Display"),
                new SimpleGrantedAuthority("BusinessPartners.Edit")));

        assertThat(properties.permissionsOf(authentication))
                .containsExactly("BusinessPartners.Display", "BusinessPartners.Edit");
    }

    private static RoleGroupProperties.Mapping mapping(String name, String groupId) {
        RoleGroupProperties.Mapping mapping = new RoleGroupProperties.Mapping();
        mapping.setName(name);
        mapping.setGroupId(groupId);
        return mapping;
    }
}
