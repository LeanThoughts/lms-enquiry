package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;
import pfs.lms.enquiry.collateral.service.impl.CollateralChangeDocumentService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Collaterals (ZPFS_T_LN_CHKLST): value lists, checklist, create / change / delete, validation, change documents. */
@DisplayName("Collateral items")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralItemServiceTest extends CollateralTestSupport {

    private UUID loanId;
    private UUID itemId;

    @Test
    @Order(1)
    @DisplayName("value lists are seeded once and offered with the portal masters")
    void valueLists() {
        bean(pfs.lms.enquiry.collateral.config.CollateralValueInitializer.class).run(); // a second run adds nothing
        Map<String, List<ValueEntryDto>> lists = collateralService.getValueLists();

        assertThat(lists.get("CONDITION_GROUP")).extracting(ValueEntryDto::getCode)
                .containsExactly("01", "02", "03", "04", "05");
        assertThat(lists.get("COLLATERAL_OBJECT_TYPE")).hasSize(48);
        assertThat(lists.get("SECURITIES_TYPE")).extracting(ValueEntryDto::getCode).contains("CCD", "NCD", "UNCALLED_SHARE_CAP");
        assertThat(lists.get("FREQUENCY_UNIT")).extracting(ValueEntryDto::getCode).containsExactly("0", "1", "2", "3");
        assertThat(lists.get("DOCUMENT_TYPE")).extracting(ValueEntryDto::getCode).containsExactly("ZPFSLM101", "ZPFSLM104");
        assertThat(collateralService.getAgreementTypes("Z00007")).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("a loan without collaterals has an empty checklist")
    void emptyChecklist() {
        loanId = loan("0000010003200");
        CollateralChecklistDto checklist = collateralService.getChecklist(loanId);

        assertThat(checklist.getId()).isNull();
        assertThat(checklist.getItems()).isEmpty();
        assertThat(checklist.getLoan().getLoanContractId()).isEqualTo("0000010003200");
        expectError(404, () -> collateralService.getChecklist(UUID.randomUUID()));
    }

    @Test
    @Order(3)
    @DisplayName("creating the first collateral creates the checklist and fills the derived fields")
    void create() {
        CollateralItem request = newItem("Z00007");
        request.setConditionDescription("  Collateral  ");
        request.setCollateralAgreementType("Z00007");
        request.setCollateralValue(new BigDecimal("1000000000.00"));
        request.setComplianceStatus("1");
        request.setComplianceDate(LocalDate.of(2021, 6, 1));
        request.setChecklistIdNo(4711L);          // ignored: the number comes from the number range
        request.setSourceOfEntry("HACK");          // ignored

        CollateralItem created = collateralService.createItem(loanId, request, WRITER);
        itemId = created.getId();

        assertThat(created.getChecklistIdNo()).isEqualTo(1L);
        assertThat(created.getConditionDescription()).isEqualTo("Collateral");
        assertThat(created.getComplianceStatusText()).isEqualTo("Complied");
        assertThat(created.getCollateralAgreementTypeDescription()).isEqualTo("Personal Guarantee");
        assertThat(created.getCollateralValueCurrency()).isEqualTo("INR");
        assertThat(created.getSourceOfEntry()).isEqualTo("PORTAL");
        assertThat(created.getCreatedByUserName()).isEqualTo(WRITER);

        CollateralChecklistDto checklist = collateralService.getChecklist(loanId);
        assertThat(checklist.getId()).isNotNull();
        assertThat(checklist.getWorkFlowStatusCode()).isZero();
        assertThat(checklist.getItems()).hasSize(1);
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST)).hasSize(1);
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_ITEM)).containsExactly("Created:1 Collateral");
    }

    @Test
    @Order(4)
    @DisplayName("changing a collateral writes a change document with the changed fields only")
    void update() {
        CollateralItem changed = collateralService.getItem(itemId).getItem();
        changed.setComplianceStatus("3");
        changed.setPenalChargesPercentage(new BigDecimal("2.50"));
        CollateralItem saved = collateralService.updateItem(itemId, changed, LEGAL);

        assertThat(saved.getComplianceStatusText()).isEqualTo("In-Process");
        assertThat(saved.getChangedByUserName()).isEqualTo(LEGAL);
        assertThat(saved.getCreatedByUserName()).isEqualTo(WRITER);
        assertThat(changedAttributes(CollateralChangeDocumentService.SUB_PROCESS_ITEM))
                .contains("complianceStatus: 1 -> 3", "penalChargesPercentage: null -> 2.50");

        int documents = changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_ITEM).size();
        collateralService.updateItem(itemId, collateralService.getItem(itemId).getItem(), LEGAL); // nothing changed
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_ITEM)).hasSize(documents);
    }

    @Test
    @Order(5)
    @DisplayName("invalid values are rejected with 412 and nothing is saved")
    void validation() {
        expectError(412, () -> {
            CollateralItem item = newItem("Z00007");
            item.setConditionGroup(null);
            collateralService.createItem(loanId, item, WRITER);
        });
        expectError(412, () -> collateralService.createItem(loanId, newItem("NOPE"), WRITER));
        expectError(412, () -> change(item -> item.setConditionDescription("x".repeat(61))));
        expectError(412, () -> change(item -> item.setPenalChargesPercentage(new BigDecimal("101"))));
        expectError(412, () -> change(item -> item.setPenalChargesPercentage(new BigDecimal("1.234"))));
        expectError(412, () -> change(item -> {
            item.setValidFromDate(LocalDate.of(2022, 1, 1));
            item.setValidToDate(LocalDate.of(2021, 1, 1));
        }));
        expectError(412, () -> change(item -> item.setPostExecutionDocumentType("NOPE")));
        expectError(412, () -> change(item -> item.setFrequencyOfRecurrenceUnit("7")));

        assertThat(collateralService.getItem(itemId).getItem().getPenalChargesPercentage()).isEqualByComparingTo("2.50");
    }

    @Test
    @Order(6)
    @DisplayName("security trustee, agent and custodian must be existing business partners")
    void partners() {
        Partner partner = new Partner();
        partner.setId(UUID.randomUUID());
        partner.setPartyNumber(1000300);
        partner.setPartyName1("Axis Bank");
        partner.setPartyName2("Custody");
        bean(PartnerRepository.class).save(partner);

        CollateralItem saved = change(item -> {
            item.setCustodian("1000300");
            item.setSecurityTrustee(null);
        });
        assertThat(saved.getCustodian()).isEqualTo("1000300");
        CollateralItemDetailDto detail = collateralService.getItem(itemId);
        assertThat(detail.getPartnerNames()).containsKey("1000300");

        expectError(412, () -> change(item -> item.setSecurityAgent("4711")));
    }

    @Test
    @Order(7)
    @DisplayName("collaterals are numbered one after the other and listed by number")
    void numbering() {
        CollateralItem second = createItem(loanId, "ZRE001");
        CollateralItem third = createItem(loanId, "ZRE002");

        assertThat(List.of(second.getChecklistIdNo(), third.getChecklistIdNo())).containsExactly(2L, 3L);
        assertThat(collateralService.getChecklist(loanId).getItems()).extracting(CollateralItem::getChecklistIdNo)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    @Order(8)
    @DisplayName("deleting a collateral removes it and its child rows and writes a change document")
    void delete() throws Exception {
        CollateralItem item = createItem(loanId, "ZRE001");
        childService.create(pfs.lms.enquiry.collateral.domain.CollateralChildType.COVERAGE, item.getId(),
                json("{'effectiveFromDate':'2001-01-01'}"), WRITER);

        collateralService.deleteItem(item.getId(), WRITER);

        expectError(404, () -> collateralService.getItem(item.getId()));
        assertThat(changeDocuments(CollateralChangeDocumentService.SUB_PROCESS_ITEM).stream()
                .filter(text -> text.startsWith("Deleted")).collect(Collectors.toList())).hasSize(1);
        assertThat(POINTERS).isNotEmpty();
    }

    private CollateralItem change(java.util.function.Consumer<CollateralItem> change) {
        CollateralItem item = collateralService.getItem(itemId).getItem();
        change.accept(item);
        return collateralService.updateItem(itemId, item, LEGAL);
    }
}
