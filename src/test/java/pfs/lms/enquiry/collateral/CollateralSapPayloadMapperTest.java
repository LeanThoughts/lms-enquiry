package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.collateral.batch.CollateralSapAttachment;
import pfs.lms.enquiry.collateral.batch.CollateralSapEntitySet;
import pfs.lms.enquiry.collateral.batch.CollateralSapFieldNaming;
import pfs.lms.enquiry.collateral.batch.CollateralSapPayloadMapper;
import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Collateral → SAP mapping, checked against the ZPFS_LMS_COLLATERAL_SRV $metadata: OData property names, Edm.DateTime
 * ({@code /Date(ms)/}), Edm.Boolean vs 1-char flag, ZID_NO padding, and the {@code (ZidNo=…,ItemId=guid'…')} key.
 */
@DisplayName("Collateral SAP integration: payload mapping and routing")
class CollateralSapPayloadMapperTest {

    private final CollateralSapPayloadMapper mapper = new CollateralSapPayloadMapper();

    @Test
    @DisplayName("sub-process names route to the correct gateway entity set (header = collateral item)")
    void routing() {
        assertThat(CollateralSapEntitySet.forSubProcess("Collateral Item")).contains(CollateralSapEntitySet.HEADER);
        assertThat(CollateralSapEntitySet.HEADER.getEntitySet()).isEqualTo("ChecklistHeaderSet");
        assertThat(CollateralSapEntitySet.HEADER.getEntityClass()).isEqualTo(CollateralItem.class);
        assertThat(CollateralSapEntitySet.forSubProcess("Collateral Coverage")).contains(CollateralSapEntitySet.COVERAGE);
        assertThat(CollateralSapEntitySet.forSubProcess("Security Perfection RoC")).contains(CollateralSapEntitySet.PERFECTION_ROC);
        assertThat(CollateralSapEntitySet.forSubProcess("Collateral Checklist")).isEmpty();
    }

    @Test
    @DisplayName("property names, dates (/Date(ms)/), Edm.Boolean vs 1-char flag, decimals, null omission")
    void headerPayload() {
        CollateralItem item = new CollateralItem();
        item.setChecklistIdNo(912L);
        item.setApplicable(true);                        // ZAPPLICABLE -> Zapplicable (Edm.String flag)
        item.setMasterCondition(false);                  // ZMASTER_COND -> ZmasterCond (Edm.String flag)
        item.setApplicableForEveryDisbursement(true);    // ZAPPL_ERY_DISBUR -> ZapplEryDisbur (Edm.Boolean)
        item.setProductType("ZCOL");
        item.setValidFromDate(LocalDate.of(2026, 3, 5));
        item.setExpectedValue(new BigDecimal("1500000.50"));

        Map<String, Object> p = mapper.toPayload(item, "ChecklistHeaderSet");

        assertThat(p).containsEntry("ZidNo", "0000000912");
        assertThat(p).containsEntry("Zapplicable", "X");         // flag -> "X"
        assertThat(p).containsEntry("ZmasterCond", "");          // flag false -> ""
        assertThat(p).containsEntry("ZapplEryDisbur", Boolean.TRUE); // real Edm.Boolean
        assertThat(p).containsEntry("ZproductType", "ZCOL");
        long ms = LocalDate.of(2026, 3, 5).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertThat(p).containsEntry("ValidFromDate", "/Date(" + ms + ")/");
        assertThat(p).containsEntry("ZcolExpValue", "1500000.50");
        assertThat(p).doesNotContainKey("ZconditionDesc");       // null omitted
    }

    @Test
    @DisplayName("header carries the loan contract number as ZloanNumber (not nullable in SAP); blank is omitted")
    void loanNumber() {
        assertThat(CollateralSapFieldNaming.propertyName(CollateralSapPayloadMapper.ZLOAN_NUMBER)).isEqualTo("ZloanNumber");

        Map<String, Object> header = new LinkedHashMap<>();
        mapper.putLoanNumber(header, " 0000010003400 ");
        assertThat(header).containsEntry("ZloanNumber", "0000010003400"); // trimmed, 13 chars as in SAP

        Map<String, Object> none = new LinkedHashMap<>();
        mapper.putLoanNumber(none, "  ");
        mapper.putLoanNumber(none, null);
        assertThat(none).doesNotContainKey("ZloanNumber");
    }

    @Test
    @DisplayName("portal id: PtlId on a child record and PortalId on the header carry the entity's own id")
    void portalId() {
        assertThat(CollateralSapFieldNaming.propertyName(CollateralSapPayloadMapper.PTL_ID)).isEqualTo("PtlId");
        assertThat(CollateralSapFieldNaming.propertyName(CollateralSapPayloadMapper.PORTAL_ID)).isEqualTo("PortalId");

        java.util.UUID id = java.util.UUID.fromString("1a2b3c4d-5e6f-7081-9abc-def012345678");
        Map<String, Object> child = new LinkedHashMap<>();
        mapper.putPortalId(child, id, false);
        assertThat(child).containsEntry("PtlId", "1a2b3c4d-5e6f-7081-9abc-def012345678").doesNotContainKey("PortalId");

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("PortalId", "stale");                       // whatever the entity field carried
        mapper.putPortalId(header, id, true);
        assertThat(header).containsEntry("PortalId", "1a2b3c4d-5e6f-7081-9abc-def012345678"); // id always wins
        assertThat(((String) header.get("PortalId")).length()).isLessThanOrEqualTo(40);          // SAP MaxLength

        Map<String, Object> none = new LinkedHashMap<>();
        mapper.putPortalId(none, null, false);
        assertThat(none).isEmpty();
    }

    @Test
    @DisplayName("DocumentAttachment: the owner's fields are mapped to the media entity, keyed by the vault file id")
    void documentAttachment() {
        java.util.UUID itemId = java.util.UUID.fromString("aaaaaaaa-1111-4111-8111-111111111111");
        java.util.UUID docId = java.util.UUID.fromString("bbbbbbbb-2222-4222-8222-222222222222");
        java.util.UUID fileId = java.util.UUID.fromString("cccccccc-3333-4333-8333-333333333333");

        // a child record: its own Bukrs, Ranl, SerialNo, DocType, DocTypeDesc, DocTitle, Remarks, BdsDocId go across
        CollateralItem item = new CollateralItem();
        item.setId(itemId);
        CollateralDocument document = new CollateralDocument();
        document.setId(docId);
        document.setItem(item);
        document.setChecklistIdNo(912L);
        document.setCompanyCode("1000");
        document.setContractNumber("0000010003400");
        document.setSerialNumber(3);
        document.setDocumentType("SANC");
        document.setDocumentTypeDescription("Sanction letter");
        document.setDocumentStage("PRE");                      // owner-only: must not leak
        document.setDocumentTitle("Sanction Letter");
        document.setRemarks("signed copy");
        document.setBdsDocumentId("BDS-77");
        Map<String, Object> owner = mapper.toPayload(document, "DocumentSet");
        mapper.putChecklistNumber(owner, 912L);
        mapper.putItemId(owner, docId.toString());
        mapper.putPortalId(owner, docId, false);

        Map<String, Object> a = mapper.toAttachmentPayload(CollateralSapEntitySet.DOCUMENT, owner, docId, fileId);
        assertThat(a).containsEntry("BusinessObjectName", "Document")   // the main entity the file belongs to
                .containsEntry("ZidNo", "0000000912")
                .containsEntry("ItemId", docId.toString())
                .containsEntry("Bukrs", "1000").containsEntry("Ranl", "0000010003400")
                .containsEntry("DocType", "SANC").containsEntry("DocTypeDesc", "Sanction letter")
                .containsEntry("DocTitle", "Sanction Letter").containsEntry("Remarks", "signed copy")
                .containsEntry("BdsDocId", "BDS-77")
                .containsEntry("PtlId", docId.toString())
                .containsEntry("PtlDocuId", fileId.toString());                 // the key
        assertThat(String.valueOf(a.get("SerialNo"))).isEqualTo("3");
        assertThat(a).doesNotContainKeys("DocStage", "Sourceofentry");          // not part of DocumentAttachment
        assertThat(a.keySet()).containsExactlyInAnyOrder("BusinessObjectName", "ZidNo", "ItemId", "Bukrs", "Ranl",
                "SerialNo", "DocType", "DocTypeDesc", "DocTitle", "Remarks", "BdsDocId", "PtlId", "PtlDocuId");

        // the header's post-execution document: DocType / DocTitle from PostExecDoc*, Ranl from the loan number
        CollateralItem header = new CollateralItem();
        header.setId(itemId);
        header.setChecklistIdNo(913L);
        header.setPostExecutionDocumentType("DEED");
        header.setPostExecutionDocumentTitle("Title Deed");
        header.setDocumentTitle("the item's own document title");   // must not be used for the attachment
        Map<String, Object> h = mapper.toPayload(header, "ChecklistHeaderSet");
        mapper.putLoanNumber(h, "0000010003400");
        Map<String, Object> b = mapper.toAttachmentPayload(CollateralSapEntitySet.HEADER, h, itemId, fileId);
        assertThat(b).containsEntry("BusinessObjectName", "ChecklistHeader")
                .containsEntry("ZidNo", "0000000913").containsEntry("ItemId", itemId.toString())
                .containsEntry("DocType", "DEED").containsEntry("DocTitle", "Title Deed")
                .containsEntry("Ranl", "0000010003400")
                .containsEntry("PtlId", itemId.toString()).containsEntry("PtlDocuId", fileId.toString());

        // all key properties (everything but BdsDocId) in the URL, in SEGW order; empty keys as '', ItemId as guid
        assertThat(mapper.attachmentKeyPredicate(a)).isEqualTo("(BusinessObjectName='Document',ZidNo='0000000912',"
                + "ItemId=guid'bbbbbbbb-2222-4222-8222-222222222222',Bukrs='1000',Ranl='0000010003400',SerialNo='3',"
                + "DocType='SANC',DocTypeDesc='Sanction%20letter',DocTitle='Sanction%20Letter',Remarks='signed%20copy',"
                + "PtlId='bbbbbbbb-2222-4222-8222-222222222222',PtlDocuId='cccccccc-3333-4333-8333-333333333333')");
        assertThat(mapper.attachmentKeyPredicate(b)).contains("Bukrs=''").contains("SerialNo=''")
                .contains("DocTypeDesc=''").contains("Remarks=''")              // header: no such fields -> ''
                .contains("ItemId=guid'aaaaaaaa-1111-4111-8111-111111111111'");
        Map<String, Object> hostileKeys = new LinkedHashMap<>(a);
        hostileKeys.put("DocTitle", "O'Brien & Co / 2026");
        hostileKeys.remove("ItemId");
        assertThat(mapper.attachmentKeyPredicate(hostileKeys))
                .contains("DocTitle='O%27%27Brien%20%26%20Co%20%2F%202026'")   // quote doubled, path-safe
                .contains("ItemId=guid'00000000-0000-0000-0000-000000000000'"); // missing guid -> nil guid

        // the Slug of the media create (CREATE_STREAM) carries the fields; values are percent-encoded so ';', '=',
        // spaces and non-ASCII characters survive, and the whole header is printable ASCII
        document.setDocumentTypeDescription("Sanction letter; signed=yes");
        Map<String, Object> hostile = mapper.toAttachmentPayload(CollateralSapEntitySet.DOCUMENT,
                mapper.toPayload(document, "DocumentSet"), docId, fileId);
        String slug = mapper.attachmentSlug(hostile, "Sanction Letter 2026.pdf");
        assertThat(slug).matches("[\\x21-\\x7E]+")
                .startsWith("BusinessObjectName=Document;")
                .contains(";PtlDocuId=" + fileId + ";")
                .contains("DocTypeDesc=Sanction%20letter%3B%20signed%3Dyes")
                .endsWith(";FileName=Sanction%20Letter%202026.pdf");
        assertThat(CollateralSapEntitySet.DOCUMENT.getEntityType()).isEqualTo("Document");
        assertThat(CollateralSapEntitySet.PERFECTION_ROC.getEntityType()).isEqualTo("PerfRoc");
    }

    @Test
    @DisplayName("uploaded files: document fileReference and item post-execution document become the media stream")
    void attachments() {
        CollateralDocument document = new CollateralDocument();
        assertThat(CollateralSapAttachment.of(document)).isEmpty();                 // nothing uploaded
        document.setFileReference("1a2b3c4d-5e6f-7081-9abc-def012345678");
        document.setFileName("sanction-letter.pdf");
        CollateralSapAttachment attachment = CollateralSapAttachment.of(document).get();
        assertThat(attachment.getFileId().toString()).isEqualTo("1a2b3c4d-5e6f-7081-9abc-def012345678");
        assertThat(attachment.getFileName()).isEqualTo("sanction-letter.pdf");
        assertThat(attachment.contentType("application/pdf")).isEqualTo("application/pdf"); // vault type wins
        assertThat(attachment.contentType(null)).isEqualTo("application/pdf");              // else from the name

        CollateralItem item = new CollateralItem();
        assertThat(CollateralSapAttachment.of(item)).isEmpty();
        item.setPostExecutionDocumentReference("2a2b3c4d-5e6f-7081-9abc-def012345678");
        item.setPostExecutionDocumentName("deed.pdf");
        assertThat(CollateralSapAttachment.of(item).get().getFileName()).isEqualTo("deed.pdf");

        CollateralDocument unnamed = new CollateralDocument();
        unnamed.setFileReference("3a2b3c4d-5e6f-7081-9abc-def012345678");
        assertThat(CollateralSapAttachment.of(unnamed).get().getFileName()).isEqualTo("document");
        assertThat(CollateralSapAttachment.of(unnamed).get().contentType(null))
                .isEqualTo(CollateralSapAttachment.DEFAULT_CONTENT_TYPE);

        CollateralDocument stale = new CollateralDocument();
        stale.setFileReference("not-a-vault-id");
        assertThat(CollateralSapAttachment.of(stale)).isEmpty();                     // not a vault file
        assertThat(CollateralSapAttachment.of(new CollateralCoverage())).isEmpty();  // entity type has no file
    }

    @Test
    @DisplayName("deterministic property-name transform matches the service metadata")
    void propertyNames() {
        assertThat(CollateralSapFieldNaming.propertyName("ZID_NO")).isEqualTo("ZidNo");
        assertThat(CollateralSapFieldNaming.propertyName("ITEM_ID")).isEqualTo("ItemId");
        assertThat(CollateralSapFieldNaming.propertyName("VALID_FROM_DATE")).isEqualTo("ValidFromDate");
        assertThat(CollateralSapFieldNaming.propertyName("ZCOL_EXP_VALUE_PCT_HOLDING")).isEqualTo("ZcolExpValuePctHolding");
        assertThat(CollateralSapFieldNaming.isBooleanProperty("ZapplEryDisbur")).isTrue();
        assertThat(CollateralSapFieldNaming.isBooleanProperty("Zapplicable")).isFalse();
    }

    @Test
    @DisplayName("header key is (ZidNo='…'); child key is (ZidNo='…',ItemId=guid'…'); missing value yields null")
    void keyPredicate() {
        Map<String, Object> header = new LinkedHashMap<>();
        mapper.putChecklistNumber(header, 912L);
        assertThat(mapper.keyPredicate(header, "ChecklistHeaderSet", CollateralSapEntitySet.HEADER.getKeyColumns()))
                .isEqualTo("(ZidNo='0000000912')");

        Map<String, Object> child = new LinkedHashMap<>();
        mapper.putChecklistNumber(child, 913L);
        mapper.putItemId(child, "1a2b3c4d-5e6f-7081-9abc-def012345678");
        assertThat(mapper.keyPredicate(child, "CoverageSet", CollateralSapEntitySet.COVERAGE.getKeyColumns()))
                .isEqualTo("(ZidNo='0000000913',ItemId=guid'1a2b3c4d-5e6f-7081-9abc-def012345678')");

        // without ItemId the key cannot be built -> update/delete is skipped
        Map<String, Object> noItem = new LinkedHashMap<>();
        mapper.putChecklistNumber(noItem, 913L);
        assertThat(mapper.keyPredicate(noItem, "CoverageSet", CollateralSapEntitySet.COVERAGE.getKeyColumns())).isNull();
    }
}
