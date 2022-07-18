package pfs.lms.enquiry.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LoanApplication extends AggregateRoot<LoanApplication> {

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private EnquiryNo enquiryNo;

    private LocalDate loanEnquiryDate;

    private Long loanEnquiryId;

    private String loanContractId;

    @Type(type = "uuid-char")
    private UUID loanApplicant;

    /**
     Values maintained in DB
     */
    private String loanClass; //VDARL-SDTYP
    /**
        Values maintained in DB
     */
    private String projectType; //VDARL-ZZPROJECT_TYPE

    /**
     1	Sole Lending
     2	MultipleBanking
     3	ConsortiumLendg
        Values maintained in DB
     */
    private String financingType; //VDARL-SFIWO

    /**
     * D - Debt
     * E - Equity
     * N - NCD
     * C - CCD
     */
    private String assistanceType; //VDARL-ZZDEBIT_EQUITY

    private Double projectCapacity;

    /**
     * KW-KiloWatt
     * MW-MegaWatt
     */
    private String projectCapacityUnit;


    @Size(max = 100)
    private String projectLocationState;

    @Size(max = 100)
    private String projectDistrict;

    private Integer tenorYear;

    private Integer tenorMonth;

    private Double projectCost;

    private Double projectDebtAmount;

    // Loan Contract Amount
    private Double loanContractAmount;

    // Loan Revised Sanction Amount
    private Double loanRevisedSanctionAmount;

    //Loan Current Contract Capital Amt.
    private Double loanCurrentContractAmount;

    // Loan Disbursed Amount
    private Double loanDisbursedAmount;




    private Double equity;

    private String projectAmountCurrency = "INR";

    private Double expectedSubDebt;

    // Application Capital
    private Double pfsDebtAmount;

    private Double pfsSubDebtAmount;

    @Size(max = 100)
    private String loanPurpose;

    @Size(max = 100)
    private String leadFIName;

    private Double leadFILoanAmount;

    private Double expectedInterestRate;

    private LocalDate scheduledCOD;

    @Size(max = 100)
    private String promoterName;

    private Double promoterNetWorthAmount;

    private Double promoterPATAmount;

    @Size(max = 100)
    private String promoterAreaOfBusinessNature;

    private String rating;

    private String promoterKeyDirector;

    private String keyPromoter;

    private String term;


    /**
     * 1- Created
     * 2- Changed
     * 3- Submitted
     * 4- Approved/Taken Up for processing
     * 5- Cancelled
     * 6- Rejected
     */
    @Column(columnDefinition = "integer default 1")
    private Integer technicalStatus;

    private String technicalStatusDescription;


    /**
     * 01-Enquiry Stage
     * 02-ICC ApprovalStage
     * 03-Appraisal Stage
     * 04-Board Approval Stage
     * 05-Loan Documentation Stage
     * 06-Loan Disbursement Stage
     * 07-Approved
     * 08-Rejected
     */
    private Integer functionalStatus;

    /**
     * 01-Approved
     * 02-Rejected
     */
    private Integer finalDecisionStatus;


    /*
    1 - Rejected by Borrower
    2 - Rejected by BD
    3 - Rejected by ICC
    4 - Rejected by Appraisal
    5 - Rejected by Board
    */
    private Integer rejectionCategory;




    @Size(max = 100)
    private String rejectionReason;


    private LocalDateTime rejectionDate;

    private LocalDate decisionDate;

    private String userBPNumber;

    private String groupCompany;

    private String productCode;

    private String busPartnerNumber;

    private String projectName;

    private String projectDepartmentInitiator;

    private String monitoringDepartmentInitiator;

    private String riskDepartmentInitiator;

    //0 - Not Posted
    //1 - Attempted to Post
    //2 - Errors
    //3 - Posted Successfully
    //4 - Approved but Posting Pending
    @Nullable
    private Integer postedInSAP;

    @Nullable
    private String contactBranchAddress;
    @Nullable
    private String contactDesignation;
    @Nullable
    private String contactDepartment;
    @Nullable
    private String contactTelePhone;
    @Nullable
    private String contactLandLinePhone;
    @Nullable
    private String contactEmail;
    @Nullable
    private String contactFaxNumber;


    @JsonCreator
    public LoanApplication(@JsonProperty("id") UUID id,
                           @JsonProperty("version") Long version,
                           @JsonProperty("createdOn") LocalDate createdOn,
                           @JsonProperty("createdAt") LocalTime createdAt,
                           @JsonProperty("createdByUserName") String createdByUserName,
                           @JsonProperty("changedOn") LocalDate changedOn,
                           @JsonProperty("changedAt") LocalTime changedAt,
                           @JsonProperty("changedByUserName") String changedByUserName,
                           @JsonProperty("enquiryNo") EnquiryNo enquiryNo,
                           @JsonProperty("loanEnquiryDate") LocalDate loanEnquiryDate,
                           @JsonProperty("loanEnquiryId") Long loanEnquiryId,
                           @JsonProperty("loanContractId") String loanContractId,
                           @JsonProperty("loanApplicant") UUID loanApplicant,
                           @JsonProperty("loanClass") String loanClass,
                           @JsonProperty("projectType") String projectType,
                           @JsonProperty("financingType") String financingType,
                           @JsonProperty("assistanceType") String assistanceType,
                           @JsonProperty("projectCapacity") Double projectCapacity,
                           @JsonProperty("projectCapacityUnit") String projectCapacityUnit,
                           @JsonProperty("projectLocationState") String projectLocationState,
                           @JsonProperty("projectDistrict") String projectDistrict,
                           @JsonProperty("tenorYear") Integer tenorYear,
                           @JsonProperty("tenorMonth") Integer tenorMonth,
                           @JsonProperty("projectCost") Double projectCost,
                           @JsonProperty("projectDebtAmount") Double projectDebtAmount,
                           @JsonProperty("term") String term,

                           @JsonProperty("loanRevisedSanctionAmount") Double loanRevisedSanctionAmount,
                           @JsonProperty("loanContractAmount") Double loanContractAmount,
                           @JsonProperty("loanCurrentContractAmount") Double loanCurrentContractAmount,
                           @JsonProperty("loanDisbursedAmount") Double loanDisbursedAmount,

                           @JsonProperty("equity") Double equity,
                           @JsonProperty("projectAmountCurrency") String projectAmountCurrency,
                           @JsonProperty("expectedSubDebt") Double expectedSubDebt,
                           @JsonProperty("pfsDebtAmount") Double pfsDebtAmount,
                           @JsonProperty("pfsSubDebtAmount") Double pfsSubDebtAmount,
                           @JsonProperty("loanPurpose") String loanPurpose,
                           @JsonProperty("leadFIName") String leadFIName,
                           @JsonProperty("leadFILoanAmount") Double leadFILoanAmount,
                           @JsonProperty("expectedInterestRate") Double expectedInterestRate,
                           @JsonProperty("scheduledCOD") LocalDate scheduledCOD,
                           @JsonProperty("promoterName") String promoterName,
                           @JsonProperty("promoterNetWorthAmount") Double promoterNetWorthAmount,
                           @JsonProperty("promoterPATAmount") Double promoterPATAmount,
                           @JsonProperty("promoterAreaOfBusinessNature") String promoterAreaOfBusinessNature,
                           @JsonProperty("rating") String rating,
                           @JsonProperty("promoterKeyDirector") String promoterKeyDirector,
                           @JsonProperty("keyPromoter") String keyPromoter,
                           @JsonProperty("technicalStatus") Integer technicalStatus,
                           @JsonProperty("technicalStatusDescription") String technicalStatusDescription,
                           @JsonProperty("functionalStatus") Integer functionalStatus,
                           @JsonProperty("finalDecisionStatus") Integer finalDecisionStatus,
                           @JsonProperty("rejectionCategory") Integer rejectionCategory,
                           @JsonProperty("rejectionReason") String rejectionReason,
                           @JsonProperty("rejectionDate") LocalDateTime rejectionDate,
                           @JsonProperty("decisionDate") LocalDate decisionDate,
                           @JsonProperty("userBPNumber") String userBPNumber,
                           @JsonProperty("groupCompany") String groupCompany,
                           @JsonProperty("productCode") String productCode,
                           //         @JsonProperty("busPartnerNumber") String busPartnerNumber,
                           @JsonProperty("projectName") String projectName,
                           @JsonProperty("projectDepartmentInitiator") String projectDepartmentInitiator ,
                           @JsonProperty("monitoringDepartmentInitiator") String monitoringDepartmentInitiator ,
                           @JsonProperty("riskDepartmentInitiator") String riskDepartmentInitiator,
                           @JsonProperty("postedInSAP") Integer postedInSAP,

                           @JsonProperty("contactBranchAddress") String contactBranchAddress,
                           @JsonProperty("contactDesignation") String contactDesignation,
                           @JsonProperty("contactDepartment") String contactDepartment,
                           @JsonProperty("contactTelePhone") String contactTelePhone,
                           @JsonProperty("contactLandLinePhone") String contactLandLinePhone,
                           @JsonProperty("contactEmail") String contactEmail,
                           @JsonProperty("contactFaxNumber") String contactFaxNumber
                           ) {
        this.id = id;
        this.version = version;
        this.createdOn = createdOn;
        this.createdAt = createdAt;
        this.createdByUserName = createdByUserName;
        this.changedOn = changedOn;
        this.changedAt = changedAt;
        this.changedByUserName = changedByUserName;
        this.enquiryNo = enquiryNo;
        this.loanEnquiryDate = loanEnquiryDate;

        this.enquiryNo = new EnquiryNo();
        this.loanEnquiryId = this.enquiryNo.getId();

        this.loanContractId = loanContractId;
        this.loanApplicant = loanApplicant;
        this.loanClass = loanClass;
        this.projectType = projectType;
        this.financingType = financingType;
        this.assistanceType = assistanceType;
        this.projectCapacity = projectCapacity;
        this.projectCapacityUnit = projectCapacityUnit;
        this.projectLocationState = projectLocationState;
        this.projectDistrict = projectDistrict;
        this.tenorYear = tenorYear;
        this.tenorMonth = tenorMonth;
        this.term = term;
        this.projectCost = projectCost;
        this.projectDebtAmount = projectDebtAmount;
        this.loanRevisedSanctionAmount = loanRevisedSanctionAmount;
        this.loanContractAmount = loanContractAmount;
        this.loanCurrentContractAmount = loanCurrentContractAmount;
        this.loanDisbursedAmount = loanDisbursedAmount;

        this.equity = equity;
        this.projectAmountCurrency = projectAmountCurrency;
        this.expectedSubDebt = expectedSubDebt;
        this.pfsDebtAmount = pfsDebtAmount;
        this.pfsSubDebtAmount = pfsSubDebtAmount;
        this.loanPurpose = loanPurpose;
        this.leadFIName = leadFIName;
        this.leadFILoanAmount = leadFILoanAmount;
        this.expectedInterestRate = expectedInterestRate;
        this.scheduledCOD = scheduledCOD;
        this.promoterName = promoterName;
        this.promoterNetWorthAmount = promoterNetWorthAmount;
        this.promoterPATAmount = promoterPATAmount;
        this.promoterAreaOfBusinessNature = promoterAreaOfBusinessNature;
        this.rating = rating;
        this.promoterKeyDirector = promoterKeyDirector;
        this.keyPromoter = keyPromoter;
        this.technicalStatus = technicalStatus;
        this.technicalStatusDescription = technicalStatusDescription;
        if (functionalStatus == null)
            this.functionalStatus = 1;
        else
            this.functionalStatus = functionalStatus;

        this.finalDecisionStatus = finalDecisionStatus;
        this.rejectionCategory = rejectionCategory;
        this.rejectionReason = rejectionReason;
        this.rejectionDate = rejectionDate;
        this.decisionDate = decisionDate;
        this.userBPNumber = userBPNumber;
        this.groupCompany = groupCompany;
        this.productCode = productCode;
        // this.busPartnerNumber = busPartnerNumber;
        this.projectName = projectName;
        this.projectDepartmentInitiator  = projectDepartmentInitiator;
        this.monitoringDepartmentInitiator = monitoringDepartmentInitiator;
        this.riskDepartmentInitiator = riskDepartmentInitiator;

        this.postedInSAP = postedInSAP;

        this.contactBranchAddress = contactBranchAddress;
        this.contactDesignation = contactDesignation;
        this.contactDepartment = contactDepartment;
        this.contactTelePhone = contactTelePhone;
        this.contactLandLinePhone =contactLandLinePhone;
        this.contactEmail = contactEmail;
        this.contactFaxNumber = contactFaxNumber;
    }



    public LoanApplication applicant(Partner partner) {
        this.loanApplicant = partner.getId();
        return this;
    }

    public LoanApplication created(Partner partner, String userName) {
        this.createdAt = LocalTime.now();
        this.createdOn = LocalDate.now();
        this.createdByUserName = userName;
        return this;
    }

    public LoanApplication modified(Partner partner, String userName) {
        this.changedAt = LocalTime.now();
        this.changedOn = LocalDate.now();
        this.changedByUserName = userName;
        return this;
    }

    public LoanApplication reject(String rejectionCategory, String reason,Partner modified){
        this.functionalStatus = 8;
        this.finalDecisionStatus = 2;
        this.rejectionReason = reason;
        this.rejectionCategory = Integer.parseInt(rejectionCategory);
        this.rejectionDate = LocalDateTime.now();
        registerEvent(LoanApplicationRejected.of(this));
        return this;
    }

    public LoanApplication responseFromSAP(SAPLoanApplicationResource sapLoanApplicationResource){
        this.functionalStatus = 2;
        this.loanContractId = sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getLoanContract();
        this.busPartnerNumber = sapLoanApplicationResource.getSapLoanApplicationDetailsResource().getBusPartnerNumber();
        return this;
    }

    public LoanApplication updateStatusFromSAP(Integer status, Double amount) {

        this.pfsDebtAmount = amount;
        this.functionalStatus = status;
        this.changedAt = LocalTime.now();
        this.changedOn = LocalDate.now();
        this.changedByUserName = "SAP";
        return this;
    }

    public LoanApplication updateProcessors(String projectDepartmentInitiator,
                                            String monitoringDepartmentInitiator,
                                            String riskDepartmentInitiator) {
        this.projectDepartmentInitiator = projectDepartmentInitiator;
        this.monitoringDepartmentInitiator = monitoringDepartmentInitiator;
        this.riskDepartmentInitiator = riskDepartmentInitiator;
        return this;
    }

    public EnquiryNo getEnquiryNo() {
        return this.enquiryNo;
    }

    public LocalDate getLoanEnquiryDate() {
        return this.loanEnquiryDate;
    }

    public Long getLoanEnquiryId() {
        return this.loanEnquiryId;
    }

    public String getLoanContractId() {
        return this.loanContractId;
    }

    public UUID getLoanApplicant() {
        return this.loanApplicant;
    }

    public String getLoanClass() {
        return this.loanClass;
    }

    public String getProjectType() {
        return this.projectType;
    }

    public String getFinancingType() {
        return this.financingType;
    }

    public String getAssistanceType() {
        return this.assistanceType;
    }

    public Double getProjectCapacity() {
        return this.projectCapacity;
    }

    public String getProjectCapacityUnit() {
        return this.projectCapacityUnit;
    }

    public @Size(max = 100) String getProjectLocationState() {
        return this.projectLocationState;
    }

    public @Size(max = 100) String getProjectDistrict() {
        return this.projectDistrict;
    }

    public Integer getTenorYear() {
        return this.tenorYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getTenorMonth() {
        return this.tenorMonth;
    }

    public Double getProjectCost() {
        return this.projectCost;
    }

    public Double getProjectDebtAmount() {
        return this.projectDebtAmount;
    }

    public Double getEquity() {
        return this.equity;
    }

    public String getProjectAmountCurrency() {
        return this.projectAmountCurrency;
    }

    public Double getExpectedSubDebt() {
        return this.expectedSubDebt;
    }

    public Double getPfsDebtAmount() {
        return this.pfsDebtAmount;
    }

    public Double getLoanRevisedSanctionAmount() {
        return loanRevisedSanctionAmount;
    }

    public Double getLoanContractAmount() {
        return loanContractAmount;
    }

    public Double getLoanCurrentContractAmount() {
        return loanCurrentContractAmount;
    }

    public Double getLoanDisbursedAmount() {
        return loanDisbursedAmount;
    }


    public Double getPfsSubDebtAmount() {
        return this.pfsSubDebtAmount;
    }

    public @Size(max = 100) String getLoanPurpose() {
        return this.loanPurpose;
    }

    public @Size(max = 100) String getLeadFIName() {
        return this.leadFIName;
    }

    public Double getLeadFILoanAmount() {
        return this.leadFILoanAmount;
    }

    public Double getExpectedInterestRate() {
        return this.expectedInterestRate;
    }

    public LocalDate getScheduledCOD() {
        return this.scheduledCOD;
    }

    public @Size(max = 100) String getPromoterName() {
        return this.promoterName;
    }

    public Double getPromoterNetWorthAmount() {
        return this.promoterNetWorthAmount;
    }

    public Double getPromoterPATAmount() {
        return this.promoterPATAmount;
    }

    public @Size(max = 100) String getPromoterAreaOfBusinessNature() {
        return this.promoterAreaOfBusinessNature;
    }

    public String getRating() {
        return this.rating;
    }

    public String getPromoterKeyDirector() {
        return this.promoterKeyDirector;
    }

    public String getKeyPromoter() {
        return this.keyPromoter;
    }

    public Integer getTechnicalStatus() {
        return this.technicalStatus;
    }

    public String getTechnicalStatusDescription() {
        return technicalStatusDescription;
    }

    public Integer getFunctionalStatus() {
        return this.functionalStatus;
    }

    public Integer getFinalDecisionStatus() {
        return this.finalDecisionStatus;
    }

    public Integer getRejectionCategory() {
        return rejectionCategory;
    }

    public LocalDateTime getRejectionDate() {
        return rejectionDate;
    }

    public String getUserBPNumber() {
        return userBPNumber;
    }

    public String getGroupCompany() {
        return this.groupCompany;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public String getbusPartnerNumber() {
        return this.busPartnerNumber;
    }

    public @Size(max = 100) String getRejectionReason() {
        return this.rejectionReason;
    }

    public LocalDate getDecisionDate() {
        return this.decisionDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDepartmentInitiator() {
        return projectDepartmentInitiator;
    }

    public String getMonitoringDepartmentInitiator() {
        return monitoringDepartmentInitiator;
    }

    public String getRiskDepartmentInitiator() {
        return riskDepartmentInitiator;
    }

    @Nullable
    public Integer getPostedInSAP() {
        return postedInSAP;
    }

    @Nullable
    public String getContactBranchAddress() {
        return contactBranchAddress;
    }

    @Nullable
    public String getContactDesignation() {
        return contactDesignation;
    }

    @Nullable
    public String getContactDepartment() {
        return contactDepartment;
    }

    @Nullable
    public String getContactTelePhone() {
        return contactTelePhone;
    }

    @Nullable
    public String getContactLandLinePhone() {
        return contactLandLinePhone;
    }

    @Nullable
    public String getContactEmail() {
        return contactEmail;
    }

    @Nullable
    public String getContactFaxNumber() {
        return contactFaxNumber;
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class LoanApplicationApproved {

        final LoanApplication loanApplication;
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class LoanApplicationCreated {

        final LoanApplication loanApplication;
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class LoanApplicationRejected {

        final LoanApplication loanApplication;
    }
}
