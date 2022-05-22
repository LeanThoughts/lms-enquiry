package pfs.lms.enquiry.monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfs.lms.enquiry.controller.FileUploadResponse;
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
import pfs.lms.enquiry.monitoring.repository.SiteVisitRepository;
import pfs.lms.enquiry.monitoring.resource.*;
import pfs.lms.enquiry.monitoring.service.ILoanMonitoringService;
import pfs.lms.enquiry.monitoring.tra.TRAResource;
import pfs.lms.enquiry.monitoring.tra.TRAStatementResource;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class LoanMonitoringController {

   // private final ILIEService lieService;
   // private final ILIEReportAndFeeService lieReportAndFeeService;

    private final ILoanMonitoringService loanMonitoringService;

    private final SiteVisitRepository siteVisitRepository;

    // Create update and list (LIE)

    @PostMapping("/loanApplications/lendersindependentengineers/create")
    public ResponseEntity createLIE(@RequestBody LIEResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersIndependentEngineer lendersIndependentEngineer =
                loanMonitoringService.saveLIE(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersIndependentEngineer);
    }

    @PutMapping("/loanApplications/lendersindependentengineers/{id}")
    public ResponseEntity updateLIE(@PathVariable("id") String lieId, @RequestBody LIEResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersIndependentEngineer lendersIndependentEngineer =
                loanMonitoringService.updateLIE(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(lendersIndependentEngineer);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/lendersIndependentEngineers")
    public ResponseEntity getLendersIndependentEngineers(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<LIEResource> lendersIndependentEngineers = loanMonitoringService.getLendersIndependentEngineers(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersIndependentEngineers);
    }



    // create, update and list (LIE Report Submission and Fee)

    @PostMapping("/loanApplications/liereportandfeesubmission/create")
    public ResponseEntity createLIEReportSubmissionAndFee(@RequestBody LIEReportAndFeeResource resource, HttpServletRequest request) {
        LIEReportAndFee lieReportAndFee =
                loanMonitoringService.saveLIEReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lieReportAndFee);
    }

    @PutMapping("/loanApplications/liereportandfeesubmission/{id}")
    public ResponseEntity updateLIEReportSubmissionAndFee(@PathVariable("id") String lieId, @RequestBody LIEReportAndFeeResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LIEReportAndFee lieReportAndFee =
                loanMonitoringService.updateLIEReportAndFee(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(lieReportAndFee);

    }

    @GetMapping("/loanApplications/lendersIndependentEngineer/{lendersindependentengineerid}/lieReceiptsAndFees")
    public ResponseEntity getLIEReportAndFee(@PathVariable("lendersindependentengineerid")
                                                     String lendersIndependentEngineerId,
                                             HttpServletRequest request)
    {
        List<LIEReportAndFeeResource>  lieReportAndFeeResources = loanMonitoringService.getLIEReportAndFee(lendersIndependentEngineerId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(lieReportAndFeeResources);
    }

    // create, update and list (LFA)

    @PostMapping("/loanApplications/lendersfinancialAdvisors/create")
    public ResponseEntity createLFA(@RequestBody LFAResource resource, HttpServletRequest request) {
        LendersFinancialAdvisor lendersFinancialAdvisor =
                loanMonitoringService.saveLFA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersFinancialAdvisor);
    }

    @PutMapping("/loanApplications/lendersfinancialAdvisors/{id}")
    public ResponseEntity updateLFA(@PathVariable("id") String lieId, @RequestBody LFAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersFinancialAdvisor lendersFinancialAdvisor =
                loanMonitoringService.updateLFA(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(lendersFinancialAdvisor);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/lendersFinancialAdvisors")
    public ResponseEntity getLendersFinancialAdvisors(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<LFAResource> lendersFinancialAdvisors = loanMonitoringService.getLendersFinancialAdvisors(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersFinancialAdvisors);
    }



    // create, update and list (LFA Report Submission and Fee)

    @PostMapping("/loanApplications/lfareportandfeesubmission/create")
    public ResponseEntity createLFAReportSubmissionAndFee(@RequestBody LFAReportAndFeeResource resource, HttpServletRequest request) {
        LFAReportAndFee lfaReportAndFee =
                loanMonitoringService.saveLFAReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lfaReportAndFee);
    }

    @PutMapping("/loanApplications/lfareportandfeesubmission/{id}")
    public ResponseEntity updateLFAReportSubmissionAndFee(@PathVariable("id") String lieId, @RequestBody LFAReportAndFeeResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LFAReportAndFee lfaReportAndFee =
                loanMonitoringService.updateLFAReportAndFee(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(lfaReportAndFee);

    }

    @GetMapping("/loanApplications/lendersFinancialAdvisor/{lendersfinancialadvisorid}/lfaReceiptsAndFees")
    public ResponseEntity getLFAReportAndFee(@PathVariable("lendersfinancialadvisorid")
                                                     String lendersFinancialAdvisorId,
                                             HttpServletRequest request)
    {
        List<LFAReportAndFeeResource>  lfaReportAndFeeResources = loanMonitoringService.getLFAReportAndFee(lendersFinancialAdvisorId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(lfaReportAndFeeResources);
    }



    // Create update and list (TRA)

    @PostMapping("/loanApplications/trustretentionaccount/create")
    public ResponseEntity createTRA(@RequestBody TRAResource resource, HttpServletRequest request) {
        TrustRetentionAccount trustRetentionAccount =
                loanMonitoringService.saveTRA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(trustRetentionAccount);
    }

    @PutMapping("/loanApplications/trustretentionaccounts/{id}")
    public ResponseEntity updateTRA(@PathVariable("id") String traId, @RequestBody TRAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        TrustRetentionAccount trustRetentionAccount =
                loanMonitoringService.updateTRA(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(trustRetentionAccount);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/trustretentionaccounts")
    public ResponseEntity getTrustRetentionAccounts(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<TRAResource> trustRetentionAccounts = loanMonitoringService.getTrustRetentionAccounts(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(trustRetentionAccounts);
    }

    // create, update and list (TRA Statement)

    @PostMapping("/loanApplications/trastatement/create")
    public ResponseEntity createTRAStatement(@RequestBody TRAStatementResource resource, HttpServletRequest request) {
        TrustRetentionAccountStatement trustRetentionAccountStatement =
                loanMonitoringService.saveTRAStatement(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(trustRetentionAccountStatement);
    }

    @PutMapping("/loanApplications/trastatement/{id}")
    public ResponseEntity updateTRAStatement(@PathVariable("id") String traId, @RequestBody TRAStatementResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        TrustRetentionAccountStatement trustRetentionAccountStatement =
                loanMonitoringService.updateTRAStatement(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(trustRetentionAccountStatement);

    }

    @GetMapping("/loanApplications/trustretentionaccount/{trustretentionaccountid}/traStatements")
    public ResponseEntity getTRAStatements(@PathVariable("trustretentionaccountid")
                                                     String trustRetentionAccountid,
                                             HttpServletRequest request)
    {
        List<TRAStatementResource>  traStatementResources = loanMonitoringService.getTrustRetentionAccountStatements(trustRetentionAccountid,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(traStatementResources);
    }


    // Create update and list (Terms and Conditions)

    @PostMapping("/loanApplications/termsandconditions/create")
    public ResponseEntity createTermsAndConditions(@RequestBody TermsAndConditionsResource resource, HttpServletRequest request) {
        TermsAndConditionsModification termsAndConditions =
                loanMonitoringService.saveTermsAndConditions(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(termsAndConditions);
    }

    @PutMapping("/loanApplications/termsandconditions/{id}")
    public ResponseEntity updateTermsAndConditions(@PathVariable("id") String traId, @RequestBody TermsAndConditionsResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        TermsAndConditionsModification termsAndConditions =
                loanMonitoringService.updateTermsAndConditions(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(termsAndConditions);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/termsandconditions")
    public ResponseEntity getTermsAndConditions(@PathVariable("loanapplicationid") String loanApplicationId,
                                                    HttpServletRequest request)
    {
        List<TermsAndConditionsResource> termsAndConditions = loanMonitoringService.getTermsAndConditions(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(termsAndConditions);
    }

    /*File upload*/
    @PostMapping("/loanApplications/{id}/files")
    public ResponseEntity<?> upload(@PathVariable("id") String loanApplicationId, @RequestParam("file")MultipartFile file){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new FileUploadResponse("File uploaded successfully"));
    }


    // Create update and list (Security Compliance)

    @PostMapping("/loanApplications/securitycompliance/create")
    public ResponseEntity createSecurityCompliance(@RequestBody SecurityComplianceResource resource, HttpServletRequest request) {
        SecurityCompliance securityCompliance =
                loanMonitoringService.saveSecurityCompliance(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityCompliance);
    }

    @PutMapping("/loanApplications/securitycompliance/{id}")
    public ResponseEntity updateSecurityCompliance(@PathVariable("id") String securityComplianceId, @RequestBody SecurityComplianceResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        SecurityCompliance securityCompliance =
                loanMonitoringService.updateSecurityCompliance(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(securityCompliance);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/securitycompliances")
    public ResponseEntity getSecurityCompliance(@PathVariable("loanapplicationid") String loanApplicationId,
                                                HttpServletRequest request)
    {
        List<SecurityComplianceResource> securityCompliances = loanMonitoringService.getSecurityCompliance(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityCompliances);
    }

    // Create update and list (Site Visit)

    @PostMapping("/loanApplications/sitevisit/create")
    public ResponseEntity createSiteVisit(@RequestBody SiteVisitResource resource, @RequestParam("app") String app,
                                          HttpServletRequest request) {
        SiteVisit siteVisit = loanMonitoringService.saveSiteVisit(resource, app, request.getUserPrincipal().getName());
        return ResponseEntity.ok(siteVisit);
    }

    @PutMapping("/loanApplications/sitevisit/{id}")
    public ResponseEntity updateSiteVisit(@PathVariable("id") String siteVisiteId, @RequestBody SiteVisitResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        SiteVisit siteVisit =
                loanMonitoringService.updateSiteVisit(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(siteVisit);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/sitevisits")
    public ResponseEntity getSiteVisit(@PathVariable("loanapplicationid") String loanApplicationId,
                                                HttpServletRequest request)
    {
        List<SiteVisitResource> siteVisits = loanMonitoringService.getSiteVisit(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(siteVisits);
    }

    // Create update and list (Operating Parameters)

    @PostMapping("/loanApplications/operatingparameter/create")
    public ResponseEntity createOperatingParameter(@RequestBody OperatingParameterResource resource, HttpServletRequest request) {
        OperatingParameter operatingParameter =
                loanMonitoringService.saveOperatingParameter(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(operatingParameter);
    }

    @PutMapping("/loanApplications/operatingparameter/{id}")
    public ResponseEntity updateOperatingParameter(@PathVariable("id") String operatingParameterId, @RequestBody OperatingParameterResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        OperatingParameter operatingParameter =
                loanMonitoringService.updateOperatingParameter(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(operatingParameter);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/operatingparameters")
    public ResponseEntity getOperatingParameter(@PathVariable("loanapplicationid") String loanApplicationId,
                                       HttpServletRequest request)
    {
        List<OperatingParameterResource> operatingParameters = loanMonitoringService.getOperatingParameter(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(operatingParameters);
    }

    // Create update and list (Rate Of Interest)

    @PostMapping("/loanApplications/rateofinterest/create")
    public ResponseEntity createRateOfInterest(@RequestBody RateOfInterestResource resource, HttpServletRequest request) {
        RateOfInterest rateOfInterest =
                loanMonitoringService.saveRateOfInterest(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(rateOfInterest);
    }

    @PutMapping("/loanApplications/rateofinterest/{id}")
    public ResponseEntity updateRateOfInterest(@PathVariable("id") String rateOfInterestId, @RequestBody RateOfInterestResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        RateOfInterest rateOfInterest =
                loanMonitoringService.updateRateOfInterest(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(rateOfInterest);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/rateofinterest")
    public ResponseEntity getRateOfInterest(@PathVariable("loanapplicationid") String loanApplicationId,
                                                HttpServletRequest request)
    {
        List<RateOfInterestResource> rateOfInterest = loanMonitoringService.getRateOfInterest(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(rateOfInterest);
    }


    // Create update and list (Borrower Financials)

    @PostMapping("/loanApplications/borrowerfinancials/create")
    public ResponseEntity createBorrowerFinancials(@RequestBody BorrowerFinancialsResource resource, HttpServletRequest request) {
        BorrowerFinancials borrowerFinancials =
                loanMonitoringService.saveBorrowerFinancials(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(borrowerFinancials);
    }

    @PutMapping("/loanApplications/borrowerfinancials/{id}")
    public ResponseEntity updateBorrowerFinancials(@PathVariable("id") String borrowerFinancialsId, @RequestBody BorrowerFinancialsResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        BorrowerFinancials borrowerFinancials =
                loanMonitoringService.updateBorrowerFinancials(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(borrowerFinancials);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/borrowerfinancials")
    public ResponseEntity getBorrowerFinancials(@PathVariable("loanapplicationid") String loanApplicationId,
                                            HttpServletRequest request)
    {
        List<BorrowerFinancialsResource> borrowerFinancials = loanMonitoringService.getBorrowerFinancials(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(borrowerFinancials);
    }

    // Create update and list (Promoter Financials)

    @PostMapping("/loanApplications/promoterfinancials/create")
    public ResponseEntity createPromoterFinancials(@RequestBody PromoterFinancialsResource resource, HttpServletRequest request) {
        PromoterFinancials promoterFinancials =
                loanMonitoringService.savePromoterFinancials(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(promoterFinancials);
    }

    @PutMapping("/loanApplications/promoterfinancials/{id}")
    public ResponseEntity updatePromoterFinancials(@PathVariable("id") String promoterFinancialsId, @RequestBody PromoterFinancialsResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        PromoterFinancials promoterFinancials =
                loanMonitoringService.updatePromoterFinancials(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(promoterFinancials);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/promoterfinancials")
    public ResponseEntity getPromoterFinancials(@PathVariable("loanapplicationid") String loanApplicationId,
                                                HttpServletRequest request)
    {
        List<PromoterFinancialsResource> promoterFinancials = loanMonitoringService.getPromoterFinancials(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(promoterFinancials);
    }


    // Create update and list (Financial Covenants)

    @PostMapping("/loanApplications/financialcovenants/create")
    public ResponseEntity createFinancialCovenants(@RequestBody FinancialCovenantsResource resource, HttpServletRequest request) {
        FinancialCovenants financialCovenants =
                loanMonitoringService.saveFinancialCovenants(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(financialCovenants);
    }

    @PutMapping("/loanApplications/financialcovenants/{id}")
    public ResponseEntity updateFinancialCovenants(@PathVariable("id") String financialCovenantsId, @RequestBody FinancialCovenantsResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        FinancialCovenants financialCovenants =
                loanMonitoringService.updateFinancialCovenants(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(financialCovenants);

    }

    @GetMapping("/loanApplications/{loanapplicationid}/financialcovenants")
    public ResponseEntity getFinancialCovenants(@PathVariable("loanapplicationid") String loanApplicationId,
                                                HttpServletRequest request)
    {
        List<FinancialCovenantsResource> financialCovenants = loanMonitoringService.getFinancialCovenants(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(financialCovenants);
    }

    @GetMapping("/loanApplications/{loanContractId}/loanMonitor")
    public ResponseEntity getLoanMonitorByLoanContractId(@PathVariable("loanContractId") UUID loanContractId){

        LoanMonitor loanMonitor = loanMonitoringService.getByLoanContractId(loanContractId);
        return ResponseEntity.ok(loanMonitor);
    }
}
