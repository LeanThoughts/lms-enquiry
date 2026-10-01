import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { ProcessEnquiryService } from '../process-enquiry/process-enquiry.service';
import { RiskAssessmentService } from '../risk-assessment/risk-assessment.service';

@Injectable({
  providedIn: 'root'
})
export class ApplicationFeeService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    /**
     * Constructor
     */
    constructor(
        private http: HttpClient,
        private loanContractSearchService: LoanContractSearchService,
        private processEnquiryService: ProcessEnquiryService,
        private riskAssessmentService: RiskAssessmentService
    ) {
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            preliminaryRiskAssessment: this.getPreliminaryRiskAssessment(route.params['loanApplicationId']),
            status: this.getTermSheetStatuses(),
            state: this.loanContractSearchService.getStates(),
            loanClass: this.loanContractSearchService.getLoanClasses(true),
            projectType: this.loanContractSearchService.getProjectTypes(true),
            financingType: this.loanContractSearchService.getFinancingTypes(true),
            assistanceType: this.loanContractSearchService.getAssistanceTypes(true),
            loanType: this.loanContractSearchService.getLoanTypes(true),
            purposeOfLoan: this.loanContractSearchService.getPurposeOfLoans(true),
            projectTypeCoreSector: this.loanContractSearchService.getProjectTypeCoreSectors(true),
            projectCapacityUnit: this.loanContractSearchService.getProjectCapacityUnits(),
            productTypeCode: this.processEnquiryService.getProductTypes(),
            term: this.processEnquiryService.getTerms(),
            moratoriumPeriodUnit: this.getPeriodUnits(),
            constructionPeriodUnit: this.getPeriodUnits()
        });
    }

    /**
     * Get the preliminary risk assessment of the loan application. Used as the minimum issuance date of a term sheet.
     */
    getPreliminaryRiskAssessment(loanApplicationId: string): Observable<any> {
        return this.riskAssessmentService.getRiskAssessment(loanApplicationId).pipe(
            switchMap((riskAssessment: any) => this.riskAssessmentService.getPreliminaryRiskAssessment(riskAssessment.id)),
            map((preliminaryRiskAssessments: any[]) => preliminaryRiskAssessments?.[0] ?? null),
            catchError(() => of(null))
        );
    }

    /**
     * Get term sheet statuses
     */
    getTermSheetStatuses(): Observable<any> {
        return of([
            { code: 'Draft', description: 'Draft' },
            { code: 'Final', description: 'Final' }
        ]);
    }

    /**
     * Get moratorium and construction period units. Application fee project details store these codes.
     */
    getPeriodUnits(): Observable<any> {
        return of([
            { code: '0', description: 'Days' },
            { code: '1', description: 'Weeks' },
            { code: '2', description: 'Months' },
            { code: '3', description: 'Years' }
        ]);
    }

    /**
     * Upload vault document
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/upload', file);
    }

    /**
     * Get Application Fee
     */
    public getApplicationFee(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/applicationFees/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get Term Sheets
     */
    public getTermSheets(applicationFeeId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/termSheets/search/findByApplicationFeeIdOrderBySerialNumber?applicationFeeId=' + applicationFeeId).pipe(
            map((response: any) => response?._embedded?.termSheets ?? [])
        );
    }

    /**
     * Create Term Sheet
     */
    public createTermSheet(termSheet: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/termSheets/create', termSheet);
    }

    /**
     * Update Term Sheet
     */
    public updateTermSheet(termSheet: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/termSheets/update', termSheet);
    }

    /**
     * Get Formal Requests
     */
    public getFormalRequests(applicationFeeId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/formalRequests/search/findByApplicationFeeIdOrderBySerialNumber?applicationFeeId=' + applicationFeeId).pipe(
            map((response: any) => response?._embedded?.formalRequests ?? [])
        );
    }

    /**
     * Create Formal Request
     */
    public createFormalRequest(formalRequest: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/formalRequests/create', formalRequest);
    }

    /**
     * Update Formal Request
     */
    public updateFormalRequest(formalRequest: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/formalRequests/update', formalRequest);
    }

    /**
     * Get Inception Fees
     */
    public getInceptionFees(applicationFeeId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/inceptionFees/' + applicationFeeId);
    }

    /**
     * Update Inception Fee
     */
    public updateInceptionFee(inceptionFee: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/inceptionFees/update', inceptionFee);
    }

    /**
     * Get Invoicing Details. Returns null when the application fee has no invoicing details yet.
     */
    public getInvoicingDetails(applicationFeeId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/invoicingDetails/search/findByApplicationFeeId?applicationFeeId=' + applicationFeeId).pipe(
            catchError(() => of(null))
        );
    }

    /**
     * Get the partner linked to the invoicing details
     */
    public getInvoicingDetailPartner(invoicingDetailId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/invoicingDetails/' + invoicingDetailId + '/partner');
    }

    /**
     * Create Invoicing Detail
     */
    public createInvoicingDetail(invoicingDetail: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/invoicingDetails/create', invoicingDetail);
    }

    /**
     * Update Invoicing Detail
     */
    public updateInvoicingDetail(invoicingDetail: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/invoicingDetails/update', invoicingDetail);
    }

    /**
     * Search partners for the invoicing details
     */
    public searchPartners(partner: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/invoicingDetails/searchPartners', partner);
    }

    /**
     * Link the invoicing partner to the loan application
     */
    public updateLoanApplication(loanApplicationId: string, partnerId: string): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/invoicingDetails/updateLoanApplication', { loanApplicationId, partnerId });
    }

    /**
     * Get loan partners of the loan application with the given role type
     */
    public getLoanPartnersByRoleType(loanApplicationId: string, roleType: string): Observable<any[]> {
        return this.http.get<any>(environment.primaryApiHost + '/loanPartners/search/findByLoanApplicationIdAndRoleType?loanApplicationId=' + 
                loanApplicationId + '&roleType=' + roleType).pipe(
            map((response: any) => response?._embedded?.loanPartners ?? [])
        );
    }

    /**
     * Create Loan Partner
     */
    public createLoanPartner(loanPartner: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/loanPartners/create', loanPartner);
    }

    /**
     * Update Loan Partner
     */
    public updateLoanPartner(loanPartner: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/loanPartners/update', loanPartner);
    }

    /**
     * Get Project Details. Returns null when the application fee has no project details yet.
     */
    public getProjectDetails(applicationFeeId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/applicationFeeProjectDetails/search/findByApplicationFeeId?applicationFeeId=' + applicationFeeId).pipe(
            catchError(() => of(null))
        );
    }

    /**
     * Create Project Details
     */
    public createProjectDetails(projectDetails: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/applicationFeeProjectDetails/create', projectDetails);
    }

    /**
     * Update Project Details
     */
    public updateProjectDetails(projectDetails: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/applicationFeeProjectDetails/update', projectDetails);
    }

    /**
     * Send Application Fee for approval
     */
    public sendApplicationFeeForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        const requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'ApplicationFee'
        };
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }
}
