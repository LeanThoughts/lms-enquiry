package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.configuration.bptables.BpTableDefinition;
import pfs.lms.enquiry.configuration.bptables.BpTableDefinitions;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TablePage;
import pfs.lms.enquiry.configuration.bptables.BpTableService;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/** Configuration > Loans: the tables of the configInitializer CommandLineRunners, maintained like the BP tables. */
@DisplayName("Configuration: loan tables")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoanTableServiceTest extends CollateralTestSupport {

    private BpTableService service;

    @BeforeAll
    void setUp() {
        service = bean(BpTableService.class);
    }

    private static Map<String, Object> values(Object... pairs) {
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            values.put((String) pairs[i], pairs[i + 1]);
        }
        return values;
    }

    @Test
    @Order(1)
    @DisplayName("the Loans module has its own 35 tables, every one opens; the BP module keeps its 23")
    void everyTableOpens() {
        assertThat(service.getOverview(BpTableDefinitions.LOANS, WRITER).getTables()).hasSize(35);
        assertThat(service.getOverview(WRITER).getTables()).hasSize(23);
        for (BpTableDefinition table : BpTableDefinitions.all(BpTableDefinitions.LOANS)) {
            TablePage page = service.getPage(table.getKey(), 0, 10, "x", READER);
            assertThat(page.getTable().getModule()).isEqualTo("loans");
            assertThat(page.isCanChange()).isFalse();
        }
    }

    @Test
    @Order(2)
    @DisplayName("code tables with UUID keys: add, change, delete; duplicate codes and missing values are refused")
    void codeTable() {
        Row row = service.create("loan-classes", values("code", "99", "value", "Test Class"), WRITER);
        assertThat(row.getId()).hasSize(36);
        assertThat(expectError(409, () -> service.create("loan-classes", values("code", "99", "value", "Again"), WRITER)))
                .contains("already exists");
        assertThat(expectError(412, () -> service.create("loan-classes", values("code", "98", "value", ""), WRITER)))
                .contains("required");
        Row changed = service.update("loan-classes", row.getId(), values("code", "XX", "value", "Changed"), WRITER);
        assertThat(changed.getValues()).containsEntry("code", "99").containsEntry("value", "Changed");
        assertThat(service.getPage("loan-classes", 0, 10, "chang", READER).getTotal()).isEqualTo(1);
        expectError(403, () -> service.update("loan-classes", row.getId(), values("value", "Reader"), READER));
        service.delete("loan-classes", row.getId(), WRITER);
        assertThat(service.getPage("loan-classes", 0, 10, null, READER).getTotal()).isZero();
    }

    @Test
    @Order(3)
    @DisplayName("numeric codes, generated Long keys with audit timestamps, references, entered keys")
    void otherKeyKinds() {
        service.create("functional-statuses", values("code", "7", "value", "Monitoring"), WRITER);
        assertThat(expectError(412, () -> service.create("functional-statuses", values("code", "x", "value", "Bad"), WRITER)))
                .contains("whole number");
        Row object = service.create("authorization-objects",
                values("authorizationObject", "Execute Test", "businessProcessName", "Test"), WRITER);
        service.create("authorization-access", values("authorizationObject", "Execute Test", "userRoleCode", "ZLM023",
                "userRoleName", "IT", "accessAllowed", true), WRITER);
        assertThat(expectError(412, () -> service.create("authorization-access", values("authorizationObject", "Unknown",
                "userRoleCode", "ZLM023"), WRITER))).contains("does not exist");
        assertThat(object.getId()).isNotBlank();

        service.create("countries", values("countryCode", "IN", "value", "India"), WRITER);
        Row region = service.create("regions", values("countryCode", "IN", "regionCode", "27", "value", "Chandigarh"), WRITER);
        assertThat(region.getLabels().get("countryCode")).isEqualTo("IN · India");
        Row second = service.create("regions", values("countryCode", "IN", "regionCode", "30", "value", "Delhi"), WRITER);
        assertThat(Integer.parseInt(second.getId())).isEqualTo(Integer.parseInt(region.getId()) + 1);
        assertThat(expectError(409, () -> service.create("regions", values("countryCode", "IN", "regionCode", "27",
                "value", "Again"), WRITER))).contains("already exists");

        service.create("portal-common-config", values("systemId", "DEV", "bdTeamEmail", "bd@test.local"), WRITER);
        service.create("states", values("code", "36", "name", "Telangana"), WRITER);
        service.create("payment-forms", values("code", "01", "description", "Monthly"), WRITER);
        assertThat(service.getPage("payment-forms", 0, 10, null, READER).getRows()).hasSize(1);
    }
}
