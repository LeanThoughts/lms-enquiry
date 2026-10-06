package pfs.lms.enquiry.collateral;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.ResponseEntity;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionRoc;
import pfs.lms.enquiry.collateral.domain.CollateralSecuritiesPosition;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;
import pfs.lms.enquiry.collateral.migration.CollateralMigrationController;
import pfs.lms.enquiry.collateral.migration.CollateralMigrationService;
import pfs.lms.enquiry.collateral.migration.MigrationResultDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Migration API for the ABAP program: checklists from SAP with their child tables. */
@DisplayName("SAP migration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralMigrationServiceTest extends CollateralTestSupport {

    /** One loan with collateral 912 (coverage, RoC, document, securities), 913 with problems, and one without ZID_NO. */
    private static final String CHECKLIST = "{'RANL':'10003500','items':["
            + "{'ZID_NO':'0000000912','ZCONDITION_GRP':'Collateral Conditions','ZCOND_CAT':'4','ZCOLLATERAL_TYPE':'Z00007',"
            + "'ZCONDITION_DESC':'Collateral','VALID_FROM_DATE':'00000000','ZCOMPLIANCE_DATE':'20210601','ZSTATUS':'1',"
            + "'ZAPPL_ERY_DISBUR':'X','ZAPPL_PER_DISBUR':'050','PENAL_CHG_PCT':'2.50','ZCOL_VALUE':'1000000000.00',"
            + "'ZRESPON_PARTY':'Promoter','MANDT':'100','CREATED_BY':'JJONES','CREATED_AT':'20210515103000',"
            + "'securityTypeText':'Promoter guarantee\\nLine 2',"
            + "'ZCOL_COV':[{'ITEM_ID':'0050568c1a2b1edf9a8b000000000001','SERIAL_NO':'0000000001','EFFECTIVE_FROM_DATE':'20010101',"
            + "'EXP_COV_VALUE_AMT':'1212.00','COV_BASIS':'2'},"
            + "{'ITEM_ID':'0050568C1A2B1EDF9A8B000000000002','SERIAL_NO':'2','EFFECTIVE_FROM_DATE':'20020101','BASIS_AMOUNT':'12.50-'}],"
            + "'ZCOL_SPFCT_ROC':[{'ITEM_ID':'A1','EVENT_TYPE':'2','EVENT_DATE':'20010101'}],"
            + "'ZCOL_DOC':[{'ITEM_ID':'D1','SERIAL_NO':'1','DOC_TYPE':'ZPFSLM101','DOC_STAGE':'Perfection','DOC_TITLE':'LLC opinion',"
            + "'BDS_DOC_ID':'005056A1B2C31EDFAB000000ABCDEF01'}],"
            + "'ZCOL_SEC_POS':[{'ITEM_ID':'S1','SERIAL_NO':'1','SEC_CHANGE_DATE':'20010101','ZSEC_NO_OF_UNITS':'100.00000',"
            + "'ZSEC_VALUE':'10.00','ZSEC_VALUE_CURR':'INR','ZSEC_PCT_HOLDING':'0.000','SECURITIES_TYPE':'CCD'}]},"
            + "{'ZID_NO':'913','ZCONDITION_GRP':'04','ZCOND_CAT':'04 Collateral conditions','ZCOLLATERAL_TYPE':'ZRE001',"
            + "'ZCONDITION_DESC':'RE Condition with a description that is far too long for sixty characters','ZSTATUS':'9','BOGUS':'x'},"
            + "{'ZCONDITION_GRP':'04'}]}";

    private CollateralMigrationService migration;
    private UUID loanId;

    @BeforeAll
    void setUp() {
        migration = bean(CollateralMigrationService.class);
        loanId = loan("0000010003500");
    }

    @Test
    @Order(1)
    @DisplayName("a dry run checks everything and saves nothing")
    void dryRun() {
        MigrationResultDto result = migrate(CHECKLIST, true).get(0);

        assertThat(result.isDryRun()).isTrue();
        assertThat(result.getItemsCreated()).isEqualTo(2);
        assertThat(result.getChecklistId()).isNull();
        assertThat(collateralService.getChecklist(loanId).getItems()).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("collaterals and child rows are loaded; codes, dates and numbers are converted")
    void load() {
        MigrationResultDto result = migrate(CHECKLIST, false).get(0);

        assertThat(result.getStatus().name()).isEqualTo("PARTIAL");
        assertThat(result.getItemsCreated()).isEqualTo(2);
        assertThat(result.getItemsSkipped()).isEqualTo(1);
        assertThat(result.getChildRowsCreated()).isEqualTo(5);
        assertThat(messages(result)).anySatisfy(text -> assertThat(text).contains("without ZID_NO"))
                .anySatisfy(text -> assertThat(text).contains("BOGUS"));

        CollateralItem item = item(912L);
        assertThat(item.getConditionGroup()).isEqualTo("04");
        assertThat(item.getConditionCategory()).isEqualTo("04");
        assertThat(item.getResponsibleParty()).isEqualTo("0");
        assertThat(item.getComplianceDate()).hasToString("2021-06-01");
        assertThat(item.getValidFromDate()).isNull();
        assertThat(item.getApplicableForEveryDisbursement()).isTrue();
        assertThat(item.getPenalChargesPercentage()).isEqualByComparingTo("2.50");
        assertThat(item.getComplianceStatusText()).isEqualTo("Complied");
        assertThat(item.getCreatedByUserName()).isEqualTo("JJONES");
        assertThat(item.getSourceOfEntry()).isEqualTo("SAP_MIGRATION");
        assertThat(item.getSecurityTypeText()).isEqualTo("Promoter guarantee\nLine 2");

        CollateralItemDetailDto detail = collateralService.getItem(item.getId());
        assertThat(detail.getCoverages()).hasSize(2);
        assertThat(((CollateralCoverage) detail.getCoverages().get(1)).getBasisAmount()).isEqualByComparingTo("-12.50");
        assertThat(((CollateralCoverage) detail.getCoverages().get(0)).getSapItemId()).isEqualTo("0050568C1A2B1EDF9A8B000000000001");
        assertThat(((CollateralPerfectionRoc) detail.getRocEvents().get(0)).getEventDescription()).isEqualTo("Perfection/ Satisfaction");
        CollateralDocument document = (CollateralDocument) detail.getDocuments().get(0);
        assertThat(document.getDocumentStage()).isEqualTo("2");
        assertThat(document.getBdsDocumentId()).isEqualTo("005056A1B2C31EDFAB000000ABCDEF01");
        CollateralSecuritiesPosition position = (CollateralSecuritiesPosition) detail.getSecuritiesPositions().get(0);
        assertThat(position.getSecuritiesShortName()).isEqualTo("Pledge CCD (Comp. Convertible Deben.)");
        assertThat(position.getNumberOfUnits()).isEqualByComparingTo("100");
    }

    @Test
    @Order(3)
    @DisplayName("a second run updates the collaterals and removes child rows that are no longer sent")
    void rerun() {
        String changed = CHECKLIST
                .replace(",{'ITEM_ID':'0050568C1A2B1EDF9A8B000000000002','SERIAL_NO':'2','EFFECTIVE_FROM_DATE':'20020101','BASIS_AMOUNT':'12.50-'}", "")
                .replace("'PENAL_CHG_PCT':'2.50'", "'PENAL_CHG_PCT':'3.00'");
        MigrationResultDto result = migrate(changed, false).get(0);

        assertThat(result.getItemsCreated()).isZero();
        assertThat(result.getItemsUpdated()).isEqualTo(2);
        assertThat(result.getChildRowsDeleted()).isEqualTo(1);
        assertThat(item(912L).getPenalChargesPercentage()).isEqualByComparingTo("3.00");
        assertThat(collateralService.getItem(item(912L).getId()).getCoverages()).hasSize(1);
    }

    @Test
    @Order(4)
    @DisplayName("ZID_NO must be a number and must not belong to another loan; unknown loans fail")
    void rejected() {
        loan("0000010003600");
        MigrationResultDto other = migrate("{'RANL':'0000010003600','items':[{'ZID_NO':'912','ZCONDITION_GRP':'04',"
                + "'ZCOND_CAT':'04','ZCOLLATERAL_TYPE':'Z00007'},{'ZID_NO':'ABC'},{'ZID_NO':'12345678901'}]}", false).get(0);
        assertThat(other.getItemsSkipped()).isEqualTo(3);
        assertThat(messages(other)).anySatisfy(text -> assertThat(text).contains("already belongs to a collateral of loan 0000010003500"))
                .anySatisfy(text -> assertThat(text).contains("'ABC' is not a number"));

        MigrationResultDto unknown = migrate("{'RANL':'999','items':[]}", false).get(0);
        assertThat(unknown.getStatus().name()).isEqualTo("FAILED");
    }

    @Test
    @Order(5)
    @DisplayName("the API takes one checklist or an array and can be switched off")
    void controller() {
        CollateralMigrationController on = new CollateralMigrationController(migration, true);
        ResponseEntity<List<MigrationResultDto>> response = on.migrate(json("[{'RANL':'999','items':[]},{'RANL':'998','items':[]}]"), true);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        assertThat(on.ping().getStatusCodeValue()).isEqualTo(200);

        CollateralMigrationController off = new CollateralMigrationController(migration, false);
        assertThat(off.ping().getStatusCodeValue()).isEqualTo(404);
        assertThat(off.migrate(json("{'RANL':'999'}"), false).getStatusCodeValue()).isEqualTo(404);
    }

    private List<MigrationResultDto> migrate(String body, boolean dryRun) {
        JsonNode node = json(body);
        List<JsonNode> checklists = new java.util.ArrayList<>();
        if (node.isArray()) {
            node.forEach(checklists::add);
        } else {
            checklists.add(node);
        }
        return migration.migrate(checklists, dryRun);
    }

    private CollateralItem item(Long checklistIdNo) {
        return collateralService.getChecklist(loanId).getItems().stream()
                .filter(item -> checklistIdNo.equals(item.getChecklistIdNo()))
                .findFirst().orElseThrow(() -> new AssertionError("collateral " + checklistIdNo + " not found"));
    }

    private static List<String> messages(MigrationResultDto result) {
        return result.getMessages().stream().map(message -> message.getSeverity() + " " + message.getText())
                .collect(Collectors.toList());
    }
}
