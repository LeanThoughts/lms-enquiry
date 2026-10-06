package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Collateral Documents of a collateral: a row of SAP table ZCOL_DOC (1:N with the collateral).
 * All SAP columns are kept except the client (MANDT) and the SAP audit include. Generated from the SAP field list.
 */
@Entity
@Table(name = "collateral_document")
public class CollateralDocument extends CollateralChildRecord<CollateralDocument> {

    /** Serial Number */
    @SapField("SERIAL_NO")
    private Integer serialNumber;

    /** Document Type */
    @SapField("DOC_TYPE")
    @Column(length = 10)
    private String documentType;

    /** Document Type description */
    @SapField("DOC_TYPE_DESC")
    @Column(length = 40)
    private String documentTypeDescription;

    /** Document Stage */
    @SapField("DOC_STAGE")
    @Column(length = 1)
    private String documentStage;

    /** Document Stage description */
    @SapField("DOC_STAGE_DESC")
    @Column(length = 60)
    private String documentStageDescription;

    /** Document Title */
    @SapField("DOC_TITLE")
    @Column(length = 100)
    private String documentTitle;

    /** Remarks */
    @SapField("REMARKS")
    @Column(length = 60)
    private String remarks;

    /** Business Document Service: Component ID */
    @SapField("BDS_DOC_ID")
    @Column(length = 255)
    private String bdsDocumentId;

    /** Uploaded document (portal file reference) (portal only) */
    @SapField("")
    @Column(length = 40)
    private String fileReference;

    /** File name of the uploaded document (portal only) */
    @SapField("")
    @Column(length = 255)
    private String fileName;

    public CollateralDocument() {
    }

    public Integer getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentTypeDescription() { return documentTypeDescription; }
    public void setDocumentTypeDescription(String documentTypeDescription) { this.documentTypeDescription = documentTypeDescription; }
    public String getDocumentStage() { return documentStage; }
    public void setDocumentStage(String documentStage) { this.documentStage = documentStage; }
    public String getDocumentStageDescription() { return documentStageDescription; }
    public void setDocumentStageDescription(String documentStageDescription) { this.documentStageDescription = documentStageDescription; }
    public String getDocumentTitle() { return documentTitle; }
    public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getBdsDocumentId() { return bdsDocumentId; }
    public void setBdsDocumentId(String bdsDocumentId) { this.bdsDocumentId = bdsDocumentId; }
    public String getFileReference() { return fileReference; }
    public void setFileReference(String fileReference) { this.fileReference = fileReference; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}
