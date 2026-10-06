package pfs.lms.enquiry.collateral.dto;

import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** One collateral with the loan and checklist status it belongs to and its child rows (detail page). */
public class CollateralItemDetailDto {

    private CollateralItem item;
    private LoanSummaryDto loan;
    private Integer workFlowStatusCode;
    private String workFlowStatusDescription;
    private List<? extends CollateralChildRecord<?>> coverages = new ArrayList<>();
    private List<? extends CollateralChildRecord<?>> rocEvents = new ArrayList<>();
    private List<? extends CollateralChildRecord<?>> cersaiEvents = new ArrayList<>();
    private List<? extends CollateralChildRecord<?>> neslEvents = new ArrayList<>();
    private List<? extends CollateralChildRecord<?>> documents = new ArrayList<>();
    private List<? extends CollateralChildRecord<?>> securitiesPositions = new ArrayList<>();
    /** Names of the partners in Security Trustee, Security Agent and Custodian, keyed by party number. */
    private Map<String, String> partnerNames = new LinkedHashMap<>();

    public CollateralItemDetailDto() {
    }

    public CollateralItemDetailDto(CollateralItem item, LoanSummaryDto loan, Integer workFlowStatusCode,
                                   String workFlowStatusDescription) {
        this.item = item;
        this.loan = loan;
        this.workFlowStatusCode = workFlowStatusCode;
        this.workFlowStatusDescription = workFlowStatusDescription;
    }

    public CollateralItem getItem() { return item; }
    public void setItem(CollateralItem item) { this.item = item; }

    public LoanSummaryDto getLoan() { return loan; }
    public void setLoan(LoanSummaryDto loan) { this.loan = loan; }

    public Integer getWorkFlowStatusCode() { return workFlowStatusCode; }
    public void setWorkFlowStatusCode(Integer workFlowStatusCode) { this.workFlowStatusCode = workFlowStatusCode; }

    public String getWorkFlowStatusDescription() { return workFlowStatusDescription; }
    public void setWorkFlowStatusDescription(String workFlowStatusDescription) { this.workFlowStatusDescription = workFlowStatusDescription; }

    public List<? extends CollateralChildRecord<?>> getCoverages() { return coverages; }
    public void setCoverages(List<? extends CollateralChildRecord<?>> coverages) { this.coverages = coverages; }

    public List<? extends CollateralChildRecord<?>> getRocEvents() { return rocEvents; }
    public void setRocEvents(List<? extends CollateralChildRecord<?>> rocEvents) { this.rocEvents = rocEvents; }

    public List<? extends CollateralChildRecord<?>> getCersaiEvents() { return cersaiEvents; }
    public void setCersaiEvents(List<? extends CollateralChildRecord<?>> cersaiEvents) { this.cersaiEvents = cersaiEvents; }

    public List<? extends CollateralChildRecord<?>> getNeslEvents() { return neslEvents; }
    public void setNeslEvents(List<? extends CollateralChildRecord<?>> neslEvents) { this.neslEvents = neslEvents; }

    public Map<String, String> getPartnerNames() { return partnerNames; }
    public void setPartnerNames(Map<String, String> partnerNames) { this.partnerNames = partnerNames; }

    public List<? extends CollateralChildRecord<?>> getDocuments() { return documents; }
    public void setDocuments(List<? extends CollateralChildRecord<?>> documents) { this.documents = documents; }

    public List<? extends CollateralChildRecord<?>> getSecuritiesPositions() { return securitiesPositions; }
    public void setSecuritiesPositions(List<? extends CollateralChildRecord<?>> securitiesPositions) { this.securitiesPositions = securitiesPositions; }
}
