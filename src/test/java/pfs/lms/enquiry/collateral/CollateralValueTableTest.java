package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.configuration.bptables.BpTableDefinitions;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TablePage;
import pfs.lms.enquiry.configuration.bptables.BpTableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Configuration > Collaterals > Collateral Value Lists: the collateral_value table, filtered by value list. */
@DisplayName("Configuration: collateral value lists")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralValueTableTest extends CollateralTestSupport {

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

    private static Map<String, String> list(String name) {
        Map<String, String> filter = new HashMap<>();
        filter.put("listName", name);
        return filter;
    }

    private static List<Object> codes(TablePage page) {
        return page.getRows().stream().map(row -> row.getValues().get("code")).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    @DisplayName("one app in the Collaterals module; the Value List filter offers every list and only shows its entries")
    void filterByValueList() {
        assertThat(BpTableDefinitions.all(BpTableDefinitions.COLLATERALS)).extracting("key").containsExactly("collateral-values");
        TablePage all = service.getPage("collateral-values", 0, 200, null, null, READER);
        assertThat(all.getTable().getFields().stream().filter(f -> f.isFilter()).map(f -> f.getName()))
                .containsExactly("listName");
        assertThat(all.getTable().field("listName").get().getOptions()).hasSize(CollateralValueList.values().length);

        TablePage stages = service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), READER);
        assertThat(stages.getFilters()).containsEntry("listName", "DOCUMENT_STAGE");
        assertThat(codes(stages)).containsExactly("1", "2", "3", "4", "5");    // by sort order
        assertThat(stages.getTotal()).isEqualTo(5);
        assertThat(service.getPage("collateral-values", 0, 50, "release", list("DOCUMENT_STAGE"), READER).getTotal())
                .isEqualTo(2);
        // sortable by any column: description descending, then back to the table's order for unknown columns
        TablePage byDescription = service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), "description", "desc", READER);
        assertThat(byDescription.getRows().stream().map(r -> r.getValues().get("description")).collect(Collectors.toList()))
                .containsExactly("Release-Pre-Requisites", "Release-Final", "Perfection", "Modification", "Creation");
        assertThat(byDescription.getSort()).isEqualTo("description");
        assertThat(byDescription.getSortDir()).isEqualTo("desc");
        assertThat(codes(service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), "code", "desc", READER)))
                .containsExactly("5", "4", "3", "2", "1");
        TablePage unknown = service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), "id", "asc", READER);
        assertThat(unknown.getSort()).isNull();
        assertThat(codes(unknown)).containsExactly("1", "2", "3", "4", "5");
        // an unknown list is ignored (all entries)
        assertThat(service.getPage("collateral-values", 0, 50, null, list("NOPE"), READER).getTotal()).isEqualTo(all.getTotal());
    }

    @Test
    @Order(2)
    @DisplayName("ZLM023 adds, changes and deletes entries; codes must fit the collateral field of the list")
    void maintain() {
        assertThat(expectError(412, () -> service.create("collateral-values",
                values("listName", "DOCUMENT_STAGE", "code", "66", "description", "Too long"), WRITER)))
                .contains("at most 1 character");
        Row row = service.create("collateral-values",
                values("listName", "DOCUMENT_STAGE", "code", "6", "description", "Archived", "sortOrder", "6"), WRITER);
        assertThat(expectError(409, () -> service.create("collateral-values",
                values("listName", "DOCUMENT_STAGE", "code", "6", "description", "Again"), WRITER))).contains("already exists");
        // the same code in another list is fine
        service.create("collateral-values", values("listName", "CONDITION_GROUP", "code", "6", "description", "Group 6"), WRITER);
        assertThat(expectError(412, () -> service.create("collateral-values",
                values("listName", "UNKNOWN", "code", "1", "description", "X"), WRITER))).contains("not allowed");
        expectError(403, () -> service.create("collateral-values",
                values("listName", "DOCUMENT_STAGE", "code", "7", "description", "Reader"), READER));

        Row changed = service.update("collateral-values", row.getId(), values("description", "Archived (closed)", "sortOrder", "0"), WRITER);
        assertThat(changed.getValues()).containsEntry("description", "Archived (closed)").containsEntry("sortOrder", 0);
        assertThat(codes(service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), READER)))
                .containsExactly("6", "1", "2", "3", "4", "5");
        service.delete("collateral-values", row.getId(), WRITER);
        assertThat(service.getPage("collateral-values", 0, 50, null, list("DOCUMENT_STAGE"), READER).getTotal()).isEqualTo(5);
    }
}
