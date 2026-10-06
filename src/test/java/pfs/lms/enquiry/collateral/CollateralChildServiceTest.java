package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionRoc;
import pfs.lms.enquiry.collateral.domain.CollateralSecuritiesPosition;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Child tables of a collateral: Coverage, RoC / CERSAI / NeSL events, Documents and Securities Positions. */
@DisplayName("Collateral child tables")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollateralChildServiceTest extends CollateralTestSupport {

    private UUID itemId;
    private Long checklistIdNo;

    @BeforeAll
    void collateral() {
        UUID loanId = loan("0000010003300");
        CollateralItem item = createItem(loanId, "Z30001");
        itemId = item.getId();
        checklistIdNo = item.getChecklistIdNo();
    }

    @Test
    @Order(1)
    @DisplayName("coverage rows are numbered per collateral and get the basis description")
    void coverage() {
        CollateralCoverage first = (CollateralCoverage) create(CollateralChildType.COVERAGE,
                "{'effectiveFromDate':'2001-01-01','expectedCoverageAmount':1212.00,'expectedCoveragePercentage':12,"
                        + "'coverageBasis':'2','serialNumber':99,'remarks':' Initial '}");
        CollateralCoverage second = (CollateralCoverage) create(CollateralChildType.COVERAGE,
                "{'effectiveFromDate':'2002-01-01','coverageBasis':'3'}");

        assertThat(first.getSerialNumber()).isEqualTo(1);
        assertThat(second.getSerialNumber()).isEqualTo(2);
        assertThat(first.getCoverageBasisDescription()).isEqualTo("Effective Capital");
        assertThat(first.getRemarks()).isEqualTo("Initial");
        assertThat(first.getChecklistIdNo()).isEqualTo(checklistIdNo);
        assertThat(first.getSourceOfEntry()).isEqualTo("PORTAL");

        expectError(412, () -> create(CollateralChildType.COVERAGE, "{'coverageBasis':'2'}"));
        expectError(412, () -> create(CollateralChildType.COVERAGE, "{'effectiveFromDate':'2001-01-01','coverageBasis':'9'}"));
        expectError(412, () -> create(CollateralChildType.COVERAGE,
                "{'effectiveFromDate':'2001-01-01','expectedCoveragePercentage':120}"));
    }

    @Test
    @Order(2)
    @DisplayName("perfection events need an event type and get its description; change writes a change document")
    void perfectionEvents() throws Exception {
        CollateralPerfectionRoc roc = (CollateralPerfectionRoc) create(CollateralChildType.ROC,
                "{'eventType':'2','eventDate':'2001-01-01','rocSecuritySatisfactionDate':'2001-02-01'}");
        assertThat(roc.getEventDescription()).isEqualTo("Perfection/ Satisfaction");

        CollateralPerfectionRoc changed = (CollateralPerfectionRoc) childService.update(CollateralChildType.ROC, roc.getId(),
                json("{'eventType':'3','eventDate':'2001-01-05','remarks':'Modified'}"), LEGAL);
        assertThat(changed.getEventDescription()).isEqualTo("Modification");
        assertThat(changedAttributes(CollateralChildType.ROC.getSubProcessName())).contains("eventType: 2 -> 3");

        create(CollateralChildType.CERSAI, "{'eventType':'2','eventDate':'2006-01-01','cersaiAcknowledgmentNumber':'ACK-1'}");
        create(CollateralChildType.NESL, "{'eventType':'1','neslReference':'NESL-1'}");

        expectError(412, () -> create(CollateralChildType.ROC, "{'eventDate':'2001-01-01'}"));
        expectError(412, () -> create(CollateralChildType.NESL, "{'eventType':'1','eventDate':'01/01/2001'}"));
    }

    @Test
    @Order(3)
    @DisplayName("documents need stage and type, take the descriptions and keep the uploaded file reference")
    void documents() throws Exception {
        String reference = UUID.randomUUID().toString();
        CollateralDocument document = (CollateralDocument) create(CollateralChildType.DOCUMENT,
                "{'documentStage':'1','documentType':'ZPFSLM101','documentTitle':' Opinion ','fileReference':'" + reference
                        + "','fileName':'Opinion.pdf','bdsDocumentId':'HACK','documentTypeDescription':'HACK'}");

        assertThat(document.getSerialNumber()).isEqualTo(1);
        assertThat(document.getDocumentStageDescription()).isEqualTo("Creation");
        assertThat(document.getDocumentTypeDescription()).isEqualTo("Legal Counsel Report");
        assertThat(document.getDocumentTitle()).isEqualTo("Opinion");
        assertThat(document.getBdsDocumentId()).isNull();
        assertThat(document.getPortalDocumentId()).isEqualTo(reference);

        CollateralDocument long40 = (CollateralDocument) create(CollateralChildType.DOCUMENT,
                "{'documentStage':'2','documentType':'ZPFSLM104'}");
        assertThat(long40.getDocumentTypeDescription()).hasSize(40);

        CollateralDocument withoutFile = (CollateralDocument) childService.update(CollateralChildType.DOCUMENT, document.getId(),
                json("{'documentStage':'3','documentType':'ZPFSLM101','fileReference':null,'fileName':'x.pdf'}"), LEGAL);
        assertThat(withoutFile.getFileName()).isNull();
        assertThat(withoutFile.getPortalDocumentId()).isNull();

        expectError(412, () -> create(CollateralChildType.DOCUMENT, "{'documentType':'ZPFSLM101'}"));
        expectError(412, () -> create(CollateralChildType.DOCUMENT, "{'documentStage':'1'}"));
        expectError(412, () -> create(CollateralChildType.DOCUMENT, "{'documentStage':'1','documentType':'NOPE'}"));
        expectError(412, () -> create(CollateralChildType.DOCUMENT, "{'documentStage':'9','documentType':'ZPFSLM101'}"));
        expectError(412, () -> create(CollateralChildType.DOCUMENT,
                "{'documentStage':'1','documentType':'ZPFSLM101','fileReference':'../../etc/passwd'}"));
        expectError(412, () -> create(CollateralChildType.DOCUMENT,
                "{'documentStage':'1','documentType':'ZPFSLM101','documentTitle':'" + "t".repeat(101) + "'}"));
    }

    @Test
    @Order(4)
    @DisplayName("securities positions take the short name from the type, default INR and check the numbers")
    void securities() throws Exception {
        CollateralSecuritiesPosition first = (CollateralSecuritiesPosition) create(CollateralChildType.SECURITIES_POSITION,
                "{'securitiesType':'CCD','securitiesChangeDate':'2001-01-01','numberOfUnits':100,'nominalValue':10}");
        assertThat(first.getSerialNumber()).isEqualTo(1);
        assertThat(first.getSecuritiesShortName()).isEqualTo("Pledge CCD (Comp. Convertible Deben.)");
        assertThat(first.getNominalValueCurrency()).isEqualTo("INR");

        CollateralSecuritiesPosition second = (CollateralSecuritiesPosition) create(CollateralChildType.SECURITIES_POSITION,
                "{'securitiesType':'SHARE_PL_EQ_SHARES','holdingPercentage':10.125,'nominalValueCurrency':'USD'}");
        CollateralSecuritiesPosition changed = (CollateralSecuritiesPosition) childService.update(
                CollateralChildType.SECURITIES_POSITION, second.getId(), json("{'securitiesType':'NCD','holdingPercentage':12}"), LEGAL);
        assertThat(changed.getSecuritiesShortName()).isEqualTo("Pledge NCD (Non- Convertible Debentures)");
        assertThat(changed.getSerialNumber()).isEqualTo(2);

        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'numberOfUnits':1}"));
        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'securitiesType':'CCD','holdingPercentage':100.5}"));
        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'securitiesType':'CCD','holdingPercentage':1.1234}"));
        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'securitiesType':'CCD','numberOfUnits':-1}"));
        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'securitiesType':'CCD','nominalValueCurrency':'XXX'}"));
        expectError(412, () -> create(CollateralChildType.SECURITIES_POSITION, "{'securitiesType':'BOND'}"));
    }

    @Test
    @Order(5)
    @DisplayName("the detail of a collateral holds all child rows; deleting a row writes a change document")
    void detailAndDelete() {
        CollateralCoverage extra = (CollateralCoverage) create(CollateralChildType.COVERAGE, "{'effectiveFromDate':'2003-01-01'}");
        CollateralItemDetailDto detail = collateralService.getItem(itemId);
        assertThat(detail.getCoverages()).isNotEmpty();
        assertThat(detail.getDocuments()).isNotEmpty();
        assertThat(detail.getSecuritiesPositions()).isNotEmpty();

        int before = childService.list(CollateralChildType.COVERAGE, itemId).size();
        childService.delete(CollateralChildType.COVERAGE, extra.getId(), WRITER);
        assertThat(childService.list(CollateralChildType.COVERAGE, itemId)).hasSize(before - 1);
        List<String> documents = changeDocuments(CollateralChildType.COVERAGE.getSubProcessName());
        assertThat(documents.stream().filter(text -> text.startsWith("Deleted")).collect(Collectors.toList())).hasSize(1);
        expectError(404, () -> childService.delete(CollateralChildType.COVERAGE, extra.getId(), WRITER));
    }

    private CollateralChildRecord<?> create(CollateralChildType type, String body) {
        return childService.create(type, itemId, json(body), WRITER);
    }
}
