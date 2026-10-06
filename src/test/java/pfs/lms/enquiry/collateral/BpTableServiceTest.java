package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.configuration.bptables.BpTableDefinition;
import pfs.lms.enquiry.configuration.bptables.BpTableDefinitions;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Overview;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TablePage;
import pfs.lms.enquiry.configuration.bptables.BpTableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Configuration apps of the business partner configuration tables (CommandLineRunner configs). */
@DisplayName("Configuration: business partner tables")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BpTableServiceTest extends CollateralTestSupport {

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

    private static List<Object> column(TablePage page, String field) {
        return page.getRows().stream().map(row -> row.getValues().get(field)).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    @DisplayName("every table opens (paged, sorted, searchable); ZLM023 may change, other roles only display")
    void everyTableOpens() {
        Overview overview = service.getOverview(WRITER);
        assertThat(overview.getTables()).hasSize(23);
        assertThat(overview.isCanChange()).isTrue();
        assertThat(service.getOverview(READER).isCanChange()).isFalse();
        for (BpTableDefinition table : BpTableDefinitions.all()) {
            TablePage page = service.getPage(table.getKey(), 0, 10, "x", READER);
            assertThat(page.getTable().getKey()).isEqualTo(table.getKey());
            assertThat(page.isCanChange()).isFalse();
        }
        assertThat(service.getPage("document-types", null, null, null, WRITER).getTotal()).isEqualTo(2);
        expectError(404, () -> service.getPage("unknown", 0, 10, null, WRITER));
    }

    @Test
    @Order(2)
    @DisplayName("long lists are paged on the server: 60 countries, 25 per page, last page has 10, search finds a country")
    void paging() {
        for (int i = 0; i < 60; i++) {
            String code = String.format("C%02d", i);
            service.create("country-codes", values("code", code, "value", i == 42 ? "India" : "Country " + code), WRITER);
        }
        TablePage first = service.getPage("country-codes", 0, 25, null, WRITER);
        assertThat(first.getTotal()).isEqualTo(60);
        assertThat(first.getRows()).hasSize(25);
        assertThat(column(first, "code").get(0)).isEqualTo("C00");
        TablePage last = service.getPage("country-codes", 2, 25, null, WRITER);
        assertThat(last.getRows()).hasSize(10);
        assertThat(column(last, "code").get(9)).isEqualTo("C59");
        // a page after the end shows the last page
        assertThat(service.getPage("country-codes", 9, 25, null, WRITER).getPage()).isEqualTo(2);
        TablePage found = service.getPage("country-codes", 0, 25, "indi", WRITER);
        assertThat(found.getTotal()).isEqualTo(1);
        assertThat(column(found, "code")).containsExactly("C42");
        // page size is limited
        assertThat(service.getPage("country-codes", 0, 5000, null, WRITER).getSize()).isEqualTo(200);
    }

    @Test
    @Order(3)
    @DisplayName("checks: required, length, duplicates, fixed values, no edit access")
    void checks() {
        assertThat(expectError(412, () -> service.create("legal-forms", values("code", "", "value", "X"), WRITER)))
                .isEqualTo("Legal Form is required.");
        assertThat(expectError(412, () -> service.create("legal-forms", values("code", "01", "value", " "), WRITER)))
                .isEqualTo("Description is required.");
        assertThat(expectError(412, () -> service.create("legal-forms", values("code", "12345678901", "value", "X"), WRITER)))
                .isEqualTo("Legal Form can have at most 10 characters.");
        service.create("legal-forms", values("code", "01", "value", "Private Limited"), WRITER);
        assertThat(expectError(409, () -> service.create("legal-forms", values("code", "01", "value", "Again"), WRITER)))
                .contains("already exists");
        assertThat(expectError(412, () -> service.create("titles", values("code", "0009", "value", "Dr.",
                "partnerCategory", "7"), WRITER))).isEqualTo("Partner Category 7 is not allowed.");
        assertThat(expectError(403, () -> service.create("legal-forms", values("code", "02", "value", "Public"), READER)))
                .startsWith("No edit access for the user with the role ZLM014");
    }

    @Test
    @Order(4)
    @DisplayName("changes keep the keys; generated ids; booleans")
    void update() {
        Row title = service.create("titles", values("code", "0001", "value", "Ms.", "partnerCategory", "1"), WRITER);
        assertThat(title.getLabels()).containsEntry("partnerCategory", "Person");
        Row changed = service.update("titles", title.getId(), values("code", "9999", "value", "Mrs.",
                "partnerCategory", "3"), WRITER);
        assertThat(changed.getValues()).containsEntry("code", "0001").containsEntry("value", "Mrs.")
                .containsEntry("partnerCategory", "3");
        expectError(409, () -> service.create("titles", values("code", "0001", "value", "Again"), WRITER));

        Row category = service.create("identification-categories", values("code", "Z00002", "value", "PAN",
                "duplicateCheckRequired", true), WRITER);
        assertThat(category.getValues()).containsEntry("duplicateCheckRequired", true)
                .containsEntry("panNumberValidation", false);
        Row unchecked = service.update("identification-categories", category.getId(),
                values("duplicateCheckRequired", false, "panNumberValidation", true), WRITER);
        assertThat(unchecked.getValues()).containsEntry("duplicateCheckRequired", false)
                .containsEntry("panNumberValidation", true);
        expectError(404, () -> service.update("titles", "424242", values("value", "x"), WRITER));
    }

    @Test
    @Order(5)
    @DisplayName("references: values must exist in the referenced table; rows show their description")
    void references() {
        assertThat(expectError(412, () -> service.create("business-partner-roles", values("code", "TR0100",
                "value", "Main Loan Partner", "partnerGroup", "0001"), WRITER)))
                .isEqualTo("Partner Group 0001 does not exist in Partner Groups.");
        service.create("partner-groups", values("code", "0001", "value", "PFS-Main Loan Partners",
                "externalNumberRange", false), WRITER);
        Row role = service.create("business-partner-roles", values("code", "TR0100", "value", "Main Loan Partner",
                "partnerGroup", "0001"), WRITER);
        assertThat(role.getLabels()).containsEntry("partnerGroup", "0001 · PFS-Main Loan Partners");
        TablePage page = service.getPage("business-partner-roles", 0, 25, null, WRITER);
        assertThat(page.getReferences().get("partner-groups")).extracting("value").contains("0001");

        // portal-numbered ids, business key role + partner group
        Row first = service.create("role-partner-groups", values("roleType", "TR0100", "partnerGroup", "0001"), WRITER);
        assertThat(first.getId()).isEqualTo("1");
        assertThat(expectError(409, () -> service.create("role-partner-groups",
                values("roleType", "TR0100", "partnerGroup", "0001"), WRITER)))
                .isEqualTo("Role TR0100 / Partner Group 0001 already exists in Role Partner Groups.");
        service.create("partner-groups", values("code", "0013", "value", "PFS-Other Loan Partners"), WRITER);
        assertThat(service.create("role-partner-groups", values("roleType", "TR0100", "partnerGroup", "0013"), WRITER)
                .getId()).isEqualTo("2");
    }

    @Test
    @Order(6)
    @DisplayName("industry types belong to an industry system; a used industry system cannot be deleted")
    void industryTypes() {
        Row system = service.create("industry-systems", values("code", "0001", "value", "Standard Industry System"), WRITER);
        Row type = service.create("industry-types", values("code", "01", "value", "Power",
                "industrySystem", system.getId()), WRITER);
        assertThat(type.getLabels()).containsEntry("industrySystem", "0001 · Standard Industry System");
        // the same code may exist in another industry system, not twice in the same
        Row other = service.create("industry-systems", values("code", "10", "value", "Public-Central Govt"), WRITER);
        service.create("industry-types", values("code", "01", "value", "Power", "industrySystem", other.getId()), WRITER);
        expectError(409, () -> service.create("industry-types", values("code", "01", "value", "Again",
                "industrySystem", system.getId()), WRITER));
        expectError(412, () -> service.create("industry-types", values("code", "02", "value", "Rail",
                "industrySystem", "999999"), WRITER));
        // search also finds industry types by their industry system
        assertThat(service.getPage("industry-types", 0, 25, "standard", WRITER).getTotal()).isEqualTo(1);
        assertThat(service.getPage("industry-types", 0, 25, null, WRITER).getTotal()).isEqualTo(2);

        assertThat(expectError(409, () -> service.delete("industry-systems", system.getId(), WRITER)))
                .isEqualTo("Industry System 0001 is used by other entries and cannot be deleted.");
        service.delete("industry-types", type.getId(), WRITER);
        service.delete("industry-systems", system.getId(), WRITER);
        assertThat(service.getPage("industry-systems", 0, 25, null, WRITER).getTotal()).isEqualTo(1);
        expectError(404, () -> service.delete("industry-systems", system.getId(), WRITER));
        expectError(403, () -> service.delete("industry-systems", other.getId(), READER));
    }
}
