package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.dto.ChecklistIdConfigurationDto;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralNumberRangeRepository;
import pfs.lms.enquiry.collateral.service.impl.CollateralNumberRangeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/** Checklist ID No.: generated numbers, start number, highest SAP ZID_NO (Configuration app). */
@DisplayName("Checklist ID number range")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralNumberRangeServiceTest extends CollateralTestSupport {

    private CollateralNumberRangeService numberRange;
    private UUID loanId;

    @Override
    protected Map<String, Object> properties() {
        return Map.of("collateral.checklist-id.start-number", 5000);
    }

    @BeforeAll
    void setUp() {
        numberRange = bean(CollateralNumberRangeService.class);
        loanId = loan("0000010003700");
    }

    @Test
    @Order(1)
    @DisplayName("numbers start at the configured start number")
    void startNumber() {
        assertThat(createItem(loanId, "Z00007").getChecklistIdNo()).isEqualTo(5000L);
        assertThat(createItem(loanId, "Z00007").getChecklistIdNo()).isEqualTo(5001L);
    }

    @Test
    @Order(2)
    @DisplayName("collaterals without a number are numbered at startup, child rows included")
    void numberMissing() throws Exception {
        CollateralItem item = createItem(loanId, "ZRE001");
        childService.create(CollateralChildType.COVERAGE, item.getId(), json("{'effectiveFromDate':'2001-01-01'}"), WRITER);
        tx.execute(status -> {
            CollateralItem stored = bean(CollateralItemRepository.class).findById(item.getId()).orElseThrow();
            stored.setChecklistIdNo(null);
            childService.list(CollateralChildType.COVERAGE, item.getId()).forEach(row -> row.setChecklistIdNo(null));
            return null;
        });

        Integer numbered = tx.execute(status -> numberRange.initialize());
        Integer again = tx.execute(status -> numberRange.initialize());
        assertThat(numbered).isEqualTo(1);
        assertThat(again).isZero();

        CollateralItem renumbered = collateralService.getItem(item.getId()).getItem();
        assertThat(renumbered.getChecklistIdNo()).isEqualTo(5003L);
        assertThat(((CollateralCoverage) collateralService.getItem(item.getId()).getCoverages().get(0)).getChecklistIdNo())
                .isEqualTo(5003L);
    }

    @Test
    @Order(3)
    @DisplayName("numbers that already exist (e.g. loaded from SAP) are skipped")
    void skipExisting() {
        CollateralItem migrated = createItem(loanId, "Z00008");
        tx.execute(status -> {
            CollateralItem stored = bean(CollateralItemRepository.class).findById(migrated.getId()).orElseThrow();
            stored.setChecklistIdNo(5005L);
            stored.setSourceOfEntry("SAP_MIGRATION");
            return null;
        });
        assertThat(createItem(loanId, "Z00007").getChecklistIdNo()).isEqualTo(5006L);
    }

    @Test
    @Order(4)
    @DisplayName("users creating collaterals at the same time get different numbers")
    void concurrent() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            futures.add(executor.submit(() -> createItem(loanId, "ZRE001").getChecklistIdNo()));
        }
        Set<Long> numbers = new TreeSet<>();
        for (Future<Long> future : futures) {
            numbers.add(future.get());
        }
        executor.shutdown();
        assertThat(numbers).hasSize(24);
    }

    @Test
    @Order(5)
    @DisplayName("Configuration app: only ZLM023 may set the highest SAP ZID_NO; numbers continue above it")
    void sapHighestNumber() {
        ChecklistIdConfigurationDto display = numberRange.getConfiguration(READER);
        assertThat(display.isCanChange()).isFalse();
        assertThat(display.getUserRole()).isEqualTo("ZLM014");
        assertThat(display.getHighestMigratedNumber()).isEqualTo(5005L);
        assertThat(numberRange.getConfiguration(WRITER).isCanChange()).isTrue();
        assertThat(numberRange.getConfiguration(LEGAL).isCanChange()).isFalse();

        expectError(403, () -> numberRange.setSapHighestNumber(100000L, READER));
        expectError(403, () -> numberRange.setSapHighestNumber(100000L, LEGAL));
        expectError(412, () -> numberRange.setSapHighestNumber(-1L, WRITER));
        expectError(412, () -> numberRange.setSapHighestNumber(9_999_999_999L, WRITER));
        expectError(412, () -> numberRange.setSapHighestNumber(5004L, WRITER)); // below the highest migrated number

        ChecklistIdConfigurationDto changed = numberRange.setSapHighestNumber(100000L, WRITER);
        assertThat(changed.getNextNumber()).isEqualTo(100001L);
        assertThat(changed.getChangedBy()).isEqualTo(WRITER);
        assertThat(createItem(loanId, "Z00007").getChecklistIdNo()).isEqualTo(100001L);

        // numbers never go down
        assertThat(numberRange.setSapHighestNumber(50000L, WRITER).getNextNumber()).isEqualTo(100002L);
        assertThat(bean(CollateralNumberRangeRepository.class).count()).isEqualTo(1);
    }
}
