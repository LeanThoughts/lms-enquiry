package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableService;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusConfigurationService;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Security assessment (VAPT) regression suite for the Configuration APIs. Fires injection, access-control and
 * input-validation payloads at the services and asserts they are handled safely:
 *   - injection payloads are treated as literal data (bound parameters), never executed;
 *   - only the configuration roles (ZLM023) may change data; others and unauthenticated callers are refused;
 *   - keys cannot be tampered with on update;
 *   - invalid input is rejected.
 * This is a defensive guard: a future change that introduced string-built queries or dropped an access check would
 * make one of these tests fail. Users: {@link #WRITER} (ZLM023, admin), {@link #READER} (ZLM014, display only).
 */
@DisplayName("Security assessment (VAPT): Configuration APIs")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityAssessmentTest extends CollateralTestSupport {

    /** Payloads that would break a string-concatenated SQL/JPQL query. They must be treated as literal text. */
    private static final List<String> INJECTION = List.of(
            "' OR '1'='1",
            "'; DROP TABLE country_code; --",
            "\" OR \"\"=\"",
            "1; DELETE FROM country_code",
            "' UNION SELECT username, password FROM users --",
            "admin'--",
            "') OR ('1'='1");

    private BpTableService tables;
    private FieldStatusConfigurationService fieldStatus;

    @BeforeAll
    void setUp() {
        tables = bean(BpTableService.class);
        fieldStatus = bean(FieldStatusConfigurationService.class);
    }

    private static Map<String, Object> values(Object... pairs) {
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            values.put((String) pairs[i], pairs[i + 1]);
        }
        return values;
    }

    private long total(String table) {
        return tables.getPage(table, 0, 200, null, WRITER).getTotal();
    }

    // ------------------------------------------------------------------ A03:2021 Injection

    @Test
    @Order(1)
    @DisplayName("A03 Injection — search payloads are matched as literal text, no query is executed")
    void injectionInSearch() {
        tables.create("country-codes", values("code", "IN", "value", "India"), WRITER);
        tables.create("country-codes", values("code", "US", "value", "United States"), WRITER);
        tables.create("country-codes", values("code", "GB", "value", "United Kingdom"), WRITER);
        long before = total("country-codes");
        assertThat(before).isEqualTo(3);

        for (String payload : INJECTION) {
            // treated as a literal substring: it matches nothing, and no injected statement runs
            assertThat(tables.getPage("country-codes", 0, 200, payload, WRITER).getTotal())
                    .as("payload must not alter the result set: %s", payload)
                    .isZero();
        }
        // the table was neither dropped nor emptied, and ordinary search still works
        assertThat(total("country-codes")).isEqualTo(before);
        assertThat(tables.getPage("country-codes", 0, 200, "united", WRITER).getTotal()).isEqualTo(2);
    }

    @Test
    @Order(2)
    @DisplayName("A03 Injection — a payload stored as a field value round-trips as literal text and executes nothing")
    void injectionInStoredValue() {
        String payload = "Robert'); DROP TABLE legal_form; --";
        Row row = tables.create("legal-forms", values("code", "X1", "value", payload), WRITER);
        assertThat(row.getValues()).containsEntry("value", payload);
        // table intact, value stored verbatim
        assertThat(total("legal-forms")).isEqualTo(1);
        assertThat(tables.getPage("legal-forms", 0, 10, "drop table", WRITER).getRows().get(0).getValues())
                .containsEntry("value", payload);
    }

    @Test
    @Order(3)
    @DisplayName("A03 Injection — payloads in the row id (update/delete) and in the table key are rejected safely")
    void injectionInIdentifiers() {
        tables.create("legal-forms", values("code", "L3", "value", "Private Limited"), WRITER);
        long before = total("legal-forms");
        for (String payload : INJECTION) {
            expectError(404, () -> tables.update("legal-forms", payload, values("value", "x"), WRITER));
            expectError(404, () -> tables.delete("legal-forms", payload, WRITER));
            expectError(404, () -> tables.getPage(payload, 0, 10, null, WRITER));
        }
        // nothing was changed or deleted by any payload
        assertThat(total("legal-forms")).isEqualTo(before);
    }

    // ------------------------------------------------------------------ A01:2021 Broken Access Control

    @Test
    @Order(4)
    @DisplayName("A01 Access Control — only ZLM023 may change; display-only roles are refused with 403")
    void changeRequiresAuthorization() {
        Row row = tables.create("legal-forms", values("code", "L4", "value", "Private Limited"), WRITER);
        assertThat(expectError(403, () -> tables.create("legal-forms", values("code", "L5", "value", "Public"), READER)))
                .startsWith("No edit access for the user with the role ZLM014");
        expectError(403, () -> tables.update("legal-forms", row.getId(), values("value", "Changed"), READER));
        expectError(403, () -> tables.delete("legal-forms", row.getId(), READER));
        // the display-only role may read, and is told it cannot change
        assertThat(tables.getPage("legal-forms", 0, 10, null, READER).isCanChange()).isFalse();
        // the same rule guards the field status API
        expectError(403, () -> fieldStatus.saveChanges("TR0100", new FieldStatusDtos.Changes(), READER));
    }

    @Test
    @Order(5)
    @DisplayName("A01 Access Control — an unauthenticated caller cannot change data")
    void unauthenticatedCannotChange() {
        // the controller rejects a missing principal with 401; the service treats a null principal as no access
        expectError(403, () -> tables.create("legal-forms", values("code", "03", "value", "LLP"), null));
        assertThat(tables.getOverview(null).isCanChange()).isFalse();
    }

    // ------------------------------------------------------------------ Parameter tampering / mass assignment

    @Test
    @Order(6)
    @DisplayName("Mass assignment — keys cannot be tampered with on update; only changeable columns are applied")
    void keysCannotBeTampered() {
        Row title = tables.create("titles", values("code", "0001", "value", "Ms.", "partnerCategory", "1"), WRITER);
        // attacker tries to overwrite the primary key and business key through update
        Row after = tables.update("titles", title.getId(),
                values("code", "9999", "value", "Mrs.", "partnerCategory", "3"), WRITER);
        assertThat(after.getValues()).containsEntry("code", "0001");   // key ignored
        assertThat(after.getValues()).containsEntry("value", "Mrs.");  // ordinary column applied
    }

    // ------------------------------------------------------------------ A04/A05 Input validation (defense in depth)

    @Test
    @Order(7)
    @DisplayName("Input validation — required, length and fixed-value constraints are enforced server-side")
    void inputValidation() {
        assertThat(expectError(412, () -> tables.create("legal-forms", values("code", "", "value", "X"), WRITER)))
                .isEqualTo("Legal Form is required.");
        assertThat(expectError(412, () -> tables.create("legal-forms",
                values("code", "123456789012345", "value", "X"), WRITER)))
                .isEqualTo("Legal Form can have at most 10 characters.");
        assertThat(expectError(412, () -> tables.create("titles",
                values("code", "0009", "value", "Dr.", "partnerCategory", "99"), WRITER)))
                .isEqualTo("Partner Category 99 is not allowed.");
    }
}
