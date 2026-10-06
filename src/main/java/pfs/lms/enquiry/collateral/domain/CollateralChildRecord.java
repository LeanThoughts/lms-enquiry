package pfs.lms.enquiry.collateral.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Common part of the rows that belong to one collateral (SAP child tables ZCOL_COV, ZCOL_SPFCT_ROC, ZCOL_DOC, ZCOL_SEC_POS,
 * ZCOL_SPFCT_CERSAI, ZCOL_SPFCT_NESL): the link to the collateral and the SAP keys and portal ids that every
 * one of these SAP tables has. The UUID id replaces the SAP key ZID_NO + ITEM_ID (NFR 17); the SAP values are kept.
 */
@MappedSuperclass
public abstract class CollateralChildRecord<T extends CollateralChildRecord<T>> extends AggregateRoot<T> {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "collateral_item_id", nullable = false)
    private CollateralItem item;

    /** Checklist Identification Number of the collateral */
    @SapField("ZID_NO")
    private Long checklistIdNo;

    /** SAP row GUID (ITEM_ID, RAW 16 as 32 hex characters) */
    @SapField("ITEM_ID")
    @Column(length = 32)
    private String sapItemId;

    /** Company Code */
    @SapField("BUKRS")
    @Column(length = 4)
    private String companyCode;

    /** Contract Number */
    @SapField("RANL")
    @Column(length = 13)
    private String contractNumber;

    /** Portal Id */
    @SapField("PTL_ID")
    @Column(length = 40)
    private String portalId;

    /** Portal document Id */
    @SapField("PTL_DOCU_ID")
    @Column(length = 40)
    private String portalDocumentId;

    /** Source of Entry */
    @SapField("SOURCEOFENTRY")
    @Column(length = 32)
    private String sourceOfEntry;

    public UUID getItemId() {
        return item != null ? item.getId() : null;
    }

    public CollateralItem getItem() { return item; }
    public void setItem(CollateralItem item) { this.item = item; }

    public Long getChecklistIdNo() { return checklistIdNo; }
    public void setChecklistIdNo(Long checklistIdNo) { this.checklistIdNo = checklistIdNo; }

    public String getSapItemId() { return sapItemId; }
    public void setSapItemId(String sapItemId) { this.sapItemId = sapItemId; }

    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public String getPortalId() { return portalId; }
    public void setPortalId(String portalId) { this.portalId = portalId; }

    public String getPortalDocumentId() { return portalDocumentId; }
    public void setPortalDocumentId(String portalDocumentId) { this.portalDocumentId = portalDocumentId; }

    public String getSourceOfEntry() { return sourceOfEntry; }
    public void setSourceOfEntry(String sourceOfEntry) { this.sourceOfEntry = sourceOfEntry; }
}
