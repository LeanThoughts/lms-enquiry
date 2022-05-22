package pfs.lms.enquiry.monitoring.service;

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
    List<LIEResource> getLendersIndependentEngineers(String loanApplicationId, String name);


    //Lenders Independent Engineer Report and Fee
    LIEReportAndFee saveLIEReportAndFee(LIEReportAndFeeResource resource, String username);
    LIEReportAndFee updateLIEReportAndFee(LIEReportAndFeeResource resource, String username) throws CloneNotSupportedException;
    List<LIEReportAndFeeResource> getLIEReportAndFee(String loanApplicationId, String name);


    //Lenders financial Advisor
    LendersFinancialAdvisor saveLFA(LFAResource resource, String username);
    LendersFinancialAdvisor updateLFA(LFAResource resource, String username) throws CloneNotSupportedException;
    List<LFAResource> getLendersFinancialAdvisors(String loanApplicationId, String name);


    //Lenders Financial Advisor Report and Fee
    LFAReportAndFee saveLFAReportAndFee(LFAReportAndFeeResource resource, String username);
    LFAReportAndFee updateLFAReportAndFee(LFAReportAndFeeResource resource, String username) throws CloneNotSupportedException;
    List<LFAReportAndFeeResource> getLFAReportAndFee(String loanApplicationId, String name);

    // Trust Retention Account
    TrustRetentionAccount saveTRA(TRAResource resource, String username);
    TrustRetentionAccount updateTRA(TRAResource resource, String username) throws CloneNotSupportedException;
    List<TRAResource> getTrustRetentionAccounts(String loanApplicationId, String name);

    //TRA STATEMENT
    TrustRetentionAccountStatement saveTRAStatement(TRAStatementResource resource, String username);
    TrustRetentionAccountStatement updateTRAStatement(TRAStatementResource resource, String username) throws CloneNotSupportedException;
    List<TRAStatementResource> getTrustRetentionAccountStatements(String loanApplicationId, String name);


    //Terms and Conditions
    TermsAndConditionsModification saveTermsAndConditions(TermsAndConditionsResource resource, String username);
    TermsAndConditionsModification updateTermsAndConditions(TermsAndConditionsResource resource, String username) throws CloneNotSupportedException;
    List<TermsAndConditionsResource> getTermsAndConditions(String loanApplicationId, String name);

    // Security Compliance
    SecurityCompliance saveSecurityCompliance(SecurityComplianceResource resource, String username);
    SecurityCompliance updateSecurityCompliance(SecurityComplianceResource resource, String username) throws CloneNotSupportedException;
    List<SecurityComplianceResource> getSecurityCompliance(String loanApplicationId, String name);

    // Site Visit
    SiteVisit saveSiteVisit(SiteVisitResource resource, String app, String username);
    SiteVisit updateSiteVisit(SiteVisitResource resource, String username) throws CloneNotSupportedException;
    List<SiteVisitResource> getSiteVisit(String loanApplicationId, String name);

    // Operating Parameter
    OperatingParameter saveOperatingParameter(OperatingParameterResource resource, String username);
    OperatingParameter updateOperatingParameter(OperatingParameterResource resource, String username) throws CloneNotSupportedException;
    List<OperatingParameterResource> getOperatingParameter(String loanApplicationId, String name);

    // Rate Of Interest
    RateOfInterest saveRateOfInterest(RateOfInterestResource resource, String username);
    RateOfInterest updateRateOfInterest(RateOfInterestResource resource, String username) throws CloneNotSupportedException;
    List<RateOfInterestResource> getRateOfInterest(String loanApplicationId, String name);

    // Borrower Financials
    BorrowerFinancials saveBorrowerFinancials(BorrowerFinancialsResource resource, String username);
    BorrowerFinancials updateBorrowerFinancials(BorrowerFinancialsResource resource, String username) throws CloneNotSupportedException;
    List<BorrowerFinancialsResource> getBorrowerFinancials(String loanApplicationId, String name);

    // Promoter Financials
    PromoterFinancials savePromoterFinancials(PromoterFinancialsResource resource, String username);
    PromoterFinancials updatePromoterFinancials(PromoterFinancialsResource resource, String username) throws CloneNotSupportedException;
    List<PromoterFinancialsResource> getPromoterFinancials(String loanApplicationId, String name);

    //  Financial Covenants
    FinancialCovenants saveFinancialCovenants(FinancialCovenantsResource resource, String username);
    FinancialCovenants updateFinancialCovenants(FinancialCovenantsResource resource, String username) throws CloneNotSupportedException;
    List<FinancialCovenantsResource> getFinancialCovenants(String loanApplicationId, String name);

    //  Promoter Details Item
    // PromoterDetailsItem savePromoterDetailsItem(PromoterDetailsItemResource resource, String username);
    // PromoterDetailsItem updatePromoterDetailsItem(PromoterDetailsItemResource resource, String username);
    // List<PromoterDetailsItemResource> getPromoterDetailsItem(String loanApplicationId, String name);

    //Find By Loan Contract Id
    public LoanMonitor getByLoanContractId (UUID loanContractId);


}
