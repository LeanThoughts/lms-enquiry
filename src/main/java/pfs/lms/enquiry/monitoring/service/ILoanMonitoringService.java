package pfs.lms.enquiry.monitoring.service;

import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancialsResource;
import pfs.lms.enquiry.monitoring.domain.*;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFeeResource;
import pfs.lms.enquiry.monitoring.lfa.LFAResource;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFeeResource;
import pfs.lms.enquiry.monitoring.lie.LIEResource;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterResource;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancialsResource;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.tra.TRAResource;
import pfs.lms.enquiry.monitoring.tra.TRAStatementResource;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;

import java.util.List;
import java.util.UUID;

public interface ILoanMonitoringService {

    //Lenders Independent Engineer
    LendersIndependentEngineer saveLIE(LIEResource resource, String username) throws CloneNotSupportedException;
    LendersIndependentEngineer updateLIE(LIEResource resource, String username) throws CloneNotSupportedException;

    LendersIndependentEngineer deleteLIE(UUID lieId, String moduleName, String username);

    List<LIEResource> getLendersIndependentEngineers(String loanApplicationId, String name);


    //Lenders Independent Engineer Report And Fee
    LIEReportAndFee saveLIEReportAndFee(LIEReportAndFeeResource resource, String username);
    LIEReportAndFee updateLIEReportAndFee(LIEReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    LIEReportAndFee deleteLIEReportAndFee(UUID lieReportAndFeeId, String moduleName, String username);

    List<LIEReportAndFeeResource> getLIEReportAndFee(String loanApplicationId, String name);


    //Lenders financial Advisor
    LendersFinancialAdvisor saveLFA(LFAResource resource, String username);
    LendersFinancialAdvisor updateLFA(LFAResource resource, String username) throws CloneNotSupportedException;
    List<LFAResource> getLendersFinancialAdvisors(String loanApplicationId, String name);


    LendersFinancialAdvisor deleteLFA(UUID lfaId, String moduleName, String username);

    //Lenders Financial Advisor Report And Fee
    LFAReportAndFee saveLFAReportAndFee(LFAReportAndFeeResource resource, String username);
    LFAReportAndFee updateLFAReportAndFee(LFAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    LFAReportAndFee deleteLFAReportAndFee(UUID lfaReportAndFeeId, String moduleName, String username);

    List<LFAReportAndFeeResource> getLFAReportAndFee(String loanApplicationId, String name);

    // Trust Retention Account
    TrustRetentionAccount saveTRA(TRAResource resource, String username);
    TrustRetentionAccount updateTRA(TRAResource resource, String username) throws CloneNotSupportedException;

    TrustRetentionAccountStatement deleteTRAStatement(UUID traStatementId, String moduleName, String username);

    TrustRetentionAccount deleteTRA(UUID traId, String moduleName, String username);

    List<TRAResource> getTrustRetentionAccounts(String loanApplicationId, String name);

    //TRA STATEMENT
    TrustRetentionAccountStatement saveTRAStatement(TRAStatementResource resource, String username);
    TrustRetentionAccountStatement updateTRAStatement(TRAStatementResource resource, String username) throws CloneNotSupportedException;
    List<TRAStatementResource> getTrustRetentionAccountStatements(String loanApplicationId, String name);


    //Terms And Conditions
    TermsAndConditionsModification saveTermsAndConditions(TermsAndConditionsResource resource, String username);
    TermsAndConditionsModification updateTermsAndConditions(TermsAndConditionsResource resource, String username) throws CloneNotSupportedException;

    TermsAndConditionsModification deleteTermsAndConditions(UUID siteVisitId, String username);

    List<TermsAndConditionsResource> getTermsAndConditions(String loanApplicationId, String name);

    // Security Compliance
    SecurityCompliance saveSecurityCompliance(SecurityComplianceResource resource, String username);
    SecurityCompliance updateSecurityCompliance(SecurityComplianceResource resource, String username) throws CloneNotSupportedException;

    SecurityCompliance deleteSecurityCompliance(UUID securityComplianceId, String username);

    List<SecurityComplianceResource> getSecurityCompliance(String loanApplicationId, String name);

    // Site Visit
    SiteVisit saveSiteVisit(SiteVisitResource resource, String app, String username) throws CloneNotSupportedException;
    SiteVisit updateSiteVisit(SiteVisitResource resource, String username) throws CloneNotSupportedException;

    SiteVisit deleteSiteVisit(UUID siteVisitId, String moduleName, String username);

    List<SiteVisitResource> getSiteVisit(String loanApplicationId, String name);

    // Operating Parameter
    OperatingParameter saveOperatingParameter(OperatingParameterResource resource, String username);
    OperatingParameter updateOperatingParameter(OperatingParameterResource resource, String username) throws CloneNotSupportedException;
    List<OperatingParameterResource> getOperatingParameter(String loanApplicationId, String name);

    OperatingParameter deleteOperatingParameter(UUID operatingParameterId, String username);

    // Rate Of Interest
    RateOfInterest saveRateOfInterest(RateOfInterestResource resource, String username);
    RateOfInterest updateRateOfInterest(RateOfInterestResource resource, String username) throws CloneNotSupportedException;
    List<RateOfInterestResource> getRateOfInterest(String loanApplicationId, String name);

    RateOfInterest deleteRateOfInterest(UUID rateOfInterestId, String username);

    // Borrower Financials
    BorrowerFinancials saveBorrowerFinancials(BorrowerFinancialsResource resource, String username);
    BorrowerFinancials updateBorrowerFinancials(BorrowerFinancialsResource resource, String username) throws CloneNotSupportedException;
    List<BorrowerFinancialsResource> getBorrowerFinancials(String loanApplicationId, String name);

    BorrowerFinancials deleteBorrowerFinancials(UUID borrowerFinancialsId, String username);

    // Promoter Financials
    PromoterFinancials savePromoterFinancials(PromoterFinancialsResource resource, String username);
    PromoterFinancials updatePromoterFinancials(PromoterFinancialsResource resource, String username) throws CloneNotSupportedException;
    List<PromoterFinancialsResource> getPromoterFinancials(String loanApplicationId, String name);

    PromoterFinancials deletePromoterFinancials(UUID promoterFinancialsId, String username);

    //  Financial Covenants
    FinancialCovenants saveFinancialCovenants(FinancialCovenantsResource resource, String username);
    FinancialCovenants updateFinancialCovenants(FinancialCovenantsResource resource, String username) throws CloneNotSupportedException;
    List<FinancialCovenantsResource> getFinancialCovenants(String loanApplicationId, String name);

    //  Promoter Details Item
    // PromoterDetailsItem savePromoterDetailsItem(PromoterDetailsItemResource resource, String username);
    // PromoterDetailsItem updatePromoterDetailsItem(PromoterDetailsItemResource resource, String username);
    // List<PromoterDetailsItemResource> getPromoterDetailsItem(String loanApplicationId, String name);

    FinancialCovenants deleteFinancialCovenants(UUID financialCovenantsId, String username);

    //Find By Loan Contract Id
    public LoanMonitor getByLoanContractId (UUID loanContractId);

    public LoanMonitor createLoanMonitor(LoanApplication loanApplication, String username);

    public UUID getLoanBusinessProcessObjectId (LoanMonitor loanMonitor, LoanAppraisal loanAppraisal, String moduleName);

}
