import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class LoanMonitoringService implements Resolve<any> {

    // public enquirySearchList: BehaviorSubject<any>;

    loanMonitor: BehaviorSubject<any> = new BehaviorSubject({});

    selectedLIE: BehaviorSubject<any> = new BehaviorSubject({});
    selectedLIA: BehaviorSubject<any> = new BehaviorSubject({});
    selectedLFA: BehaviorSubject<any> = new BehaviorSubject({});
    selectedTRA: BehaviorSubject<any> = new BehaviorSubject({});
    
    public banks: any;

    loanContractExtension: any;

    /**
     *
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
        this.getBanks().subscribe(response => {
            this.banks = response;
        })
    }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return this.getLoanContractExtension(this._loanEnquiryService.selectedEnquiry.value.id);
    }

    // All about LIA

    public getLendersInsuranceAdvisors(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersInsuranceAdvisors');
    }

    public saveLIA(lia: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersInsuranceAdvisors/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersInsuranceAdvisor':lia });
    }

    public updateLIA(lia: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersInsuranceAdvisors/" + lia.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersInsuranceAdvisor':lia });
    }

    // All about LIA Reports And Fees
    
    public getLIAReportsAndFees(liaId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/liaReportAndFeeSubmission/' + liaId + '/liaReceiptsAndFees');
    }
    
    public saveLIAReportAndFee(liaReportAndFee: any, liaId: string): Observable<any> {
        const url = "enquiry/api/loanApplications/liaReportAndFeeSubmission/create";
        return this._http.post(url, { 'lendersInsuranceAdvisorId': liaId, 'liaReportAndFee': liaReportAndFee });
    }

    public updateLIAReportAndFee(liaReportAndFee: any): Observable<any> {
        console.log('in service', liaReportAndFee);
        const url = "enquiry/api/loanApplications/liaReportAndFeeSubmission/" + liaReportAndFee.id;
        return this._http.put(url, { 'lendersInsuranceAdvisorId': '', 'liaReportAndFee': liaReportAndFee });
    }

    // All about LIE

    public getLendersIndependentEngineers(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersIndependentEngineers');
    }

    public saveLIE(lie: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersindependentengineers/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersIndependentEngineer':lie });
    }

    public updateLIE(lie: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersindependentengineers/" + lie.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersIndependentEngineer':lie });
    }

    // All about LIE Reports And Fees
    
    public getLIEReportsAndFees(lieId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/lendersIndependentEngineer/' + lieId + '/lieReceiptsAndFees');
    }
    
    public saveLIEReportAndFee(lieReportAndFee: any, lieId: string): Observable<any> {
        const url = "enquiry/api/loanApplications/liereportandfeesubmission/create";
        return this._http.post(url, { 'lendersIndependentEngineerId': lieId, 'lieReportAndFee': lieReportAndFee });
    }

    public updateLIEReportAndFee(lieReportAndFee: any): Observable<any> {
        console.log('in service', lieReportAndFee);
        const url = "enquiry/api/loanApplications/liereportandfeesubmission/" + lieReportAndFee.id;
        return this._http.put(url, { 'lendersIndependentEngineerId': '', 'lieReportAndFee': lieReportAndFee });
    }

    // All about LFA

    public getLendersFinancialAdvisors(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersFinancialAdvisors');
    }
    
    public saveLFA(lfa: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersfinancialAdvisors/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersFinancialAdvisor':lfa });
    }

    public updateLFA(lfa: any): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersfinancialAdvisors/" + lfa.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersFinancialAdvisor':lfa });
    }

    // All about LFA Reports And Fees

    public getLFAReportsAndFees(lfaId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/lendersFinancialAdvisor/' + lfaId + '/lfaReceiptsAndFees');
    }

    public saveLFAReportAndFee(lfaReportAndFee: any, lfaId: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lfareportandfeesubmission/create";
        return this._http.post(url, { 'lendersFinancialAdvisorId': lfaId, 'lfaReportAndFee': lfaReportAndFee });
    }

    public updateLFAReportAndFee(lfaReportAndFee: any): Observable<any> {
        console.log('in service', lfaReportAndFee);
        const url = "enquiry/api/loanApplications/lfareportandfeesubmission/" + lfaReportAndFee.id;
        return this._http.put(url, { 'lendersFinancialAdvisorId': '', 'lfaReportAndFee': lfaReportAndFee });
    }

    // All about TRA

    public getTrustRetentionaccounts(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/trustretentionaccounts');
    }

    public saveTRA(tra: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/trustretentionaccount/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'trustRetentionAccount':tra });
    }

    public updateTRA(tra: any): Observable<any> {
        const url = "enquiry/api/loanApplications/trustretentionaccounts/" + tra.id;
        return this._http.put(url, { 'loanApplicationId':'', 'trustRetentionAccount':tra });
    }
    
    // All about TRA Statement

    public getTRAStatements(traId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/trustretentionaccount/' + traId + '/traStatements');
    }

    public saveTRAStatement(traStatement: any, traId: string): Observable<any> {
        const url = "enquiry/api/loanApplications/trastatement/create";
        return this._http.post(url, { 'trustRetentionAccountId': traId, 'trustRetentionAccountStatement': traStatement });
    }

    public updateTRAStatement(traStatement: any): Observable<any> {
        const url = "enquiry/api/loanApplications/trastatement/" + traStatement.id;
        return this._http.put(url, { 'trustRetentionAccountId': '', 'trustRetentionAccountStatement': traStatement });
    }

    // All about Terms & Conditions

    public getTermsAndConditions(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/termsandconditions');
    }

    public saveTandC(tandc: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/termsandconditions/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'termsAndConditionsModification':tandc });
    }

    public updateTandC(tandc: any): Observable<any> {
        const url = "enquiry/api/loanApplications/termsandconditions/" + tandc.id;
        return this._http.put(url, { 'loanApplicationId':'', 'termsAndConditionsModification':tandc });
    }

    // All about Security Compliance

    public getSecurityCompliances(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/securitycompliances');
    }

    public saveSecurityCompliance(securityCompliance: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/securitycompliance/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'securityCompliance':securityCompliance });
    }

    public updateSecurityCompliance(securityCompliance: any): Observable<any> {
        const url = "enquiry/api/loanApplications/securitycompliance/" + securityCompliance.id;
        return this._http.put(url, { 'loanApplicationId':'', 'securityCompliance':securityCompliance });
    }    
    
    // All about Site Visit

    public getSiteVisits(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/sitevisits');
    }

    public saveSiteVisit(siteVisit: any, app: string, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/sitevisit/create?app=" + app;
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'siteVisit':siteVisit });
    }

    public updateSiteVisit(siteVisit: any): Observable<any> {
        const url = "enquiry/api/loanApplications/sitevisit/" + siteVisit.id;
        return this._http.put(url, { 'loanApplicationId':'', 'siteVisit':siteVisit });
    }    

    // All about Rate of Interest
    
    public getRateOfInterests(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/rateofinterest');
    }

    public saveRateOfInterest(rateOfInterest: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/rateofinterest/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'rateOfInterest':rateOfInterest });
    }

    public updateRateOfInterest(rateOfInterest: any): Observable<any> {
        const url = "enquiry/api/loanApplications/rateofinterest/" + rateOfInterest.id;
        return this._http.put(url, { 'loanApplicationId':'', 'rateOfInterest':rateOfInterest });
    }  

    // All about Borrower Financials
    
    public getBorrowerFinancials(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/borrowerfinancials');
    }

    public saveBorrowerFinancials(borrowerfinancials: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/borrowerfinancials/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'borrowerFinancials':borrowerfinancials });
    }

    public updateBorrowerFinancials(borrowerfinancials: any): Observable<any> {
        const url = "enquiry/api/loanApplications/borrowerfinancials/" + borrowerfinancials.id;
        return this._http.put(url, { 'loanApplicationId':'', 'borrowerFinancials':borrowerfinancials });
    }


    // All about Promoter Financials
    
    public getPromoterFinancials(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/promoterfinancials');
    }

    public savePromoterFinancials(promoterfinancials: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/promoterfinancials/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'promoterFinancials':promoterfinancials });
    }

    public updatePromoterFinancials(promoterfinancials: any): Observable<any> {
        const url = "enquiry/api/loanApplications/promoterfinancials/" + promoterfinancials.id;
        return this._http.put(url, { 'loanApplicationId':'', 'promoterFinancials':promoterfinancials });
    }
    
    // All about Financial Covenants
    
    public getFinancialCovenants(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/financialcovenants');
    }

    public saveFinancialCovenants(financialCovenants: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/financialcovenants/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'financialCovenants':financialCovenants });
    }

    public updateFinancialCovenants(financialCovenants: any): Observable<any> {
        const url = "enquiry/api/loanApplications/financialcovenants/" + financialCovenants.id;
        return this._http.put(url, { 'loanApplicationId':'', 'financialCovenants':financialCovenants });
    }

    // All about Promoter Details

    public getPromoterDetails(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/promoterDetails/loanApplication/' + loanApplicationId);
    }

    public savePromoterDetails(promoterDetail: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/promoterDetails";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'promoterDetail':promoterDetail });
    }

    public updatePromoterDetails(promoterDetail: any): Observable<any> {
        const url = "enquiry/api/promoterDetails/" + promoterDetail.id;
        return this._http.put(url, { 'loanApplicationId':'', 'promoterDetail':promoterDetail });
    }

    // All about Operating Parameters 

    public getOperatingParameters(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/operatingparameters');
    }
    
    public saveOperatingParameter(operatingParameter: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/operatingparameter/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'operatingParameter':operatingParameter });
    }

    public updateOperatingParameter(operatingParameter: any): Observable<any> {
        const url = "enquiry/api/loanApplications/operatingparameter/" + operatingParameter.id;
        return this._http.put(url, { 'loanApplicationId':'', 'operatingParameter':operatingParameter });
    }

    // All about Operating Parameter PLF

    public getOperatingParameterPLFs(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/operatingparameterplfs');
    }
    
    public saveOperatingParameterPLF(operatingParameterPLF: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/loanApplications/operatingparameterplf/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'operatingParameterPLF':operatingParameterPLF });
    }

    public updateOperatingParameterPLF(operatingParameterPLF: any): Observable<any> {
        const url = "enquiry/api/loanApplications/operatingparameterplf/" + operatingParameterPLF.id;
        return this._http.put(url, { 'loanApplicationId':'', 'operatingParameterPLF':operatingParameterPLF });
    }

    // All about Project Monitoring Data

    public getProjectMonitoringData(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/projectMonitoringDatas/loanApplication/' + loanApplicationId);
    }

    public saveProjectMonitoringData(loanApplicationId: string): Observable<any> {
        return this._http.post('enquiry/api/projectMonitoringDatas/loanApplication/' + loanApplicationId, {});
    }

    public updateProjectMonitoringDataItem(projectMonitoringDataItem: any): Observable<any> {
        return this._http.put('enquiry/api/projectMonitoringDataItems/' + projectMonitoringDataItem.id, projectMonitoringDataItem);
    }

    public getProjectMonitoringDataItemHistory(projectMonitoringDataId: string, particulars: string): Observable<any> {
        const url = 'enquiry/api/projectMonitoringDataItemHistories/search/findByProjectMonitoringDataIdAndParticularsOrderByDateOfEntryDesc' 
                + '?projectMonitoringDataId=' + projectMonitoringDataId + '&particulars=' + particulars;
        return this._http.get(url);
    }
    
    // Others

    public getBanks(): Observable<any> {
        return this._http.get('enquiry/api/bankmasters/all');
    }

    public searchLoanEnquiries(request: any): Observable<any> {
        return this._http.put<any>('enquiry/api/loanApplications/search', request);
    }

    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    public uploadDocument(file: any): Observable<any> {
        if (file !== '') {
            var formData = new FormData();
            formData.append('file', file);
            return this._http.post('enquiry/api/upload', formData);
        }
        else {
            return new Observable((observer) => {
                observer.next({'fileReference': ''});
                observer.complete();
            })
        }
    }

    public getLIAs(): Observable<any> {
        // return this._http.get<any>('enquiry/api/partner/lias');
        return this._http.get<any>('enquiry/api/partner/lies');
    }

    public getLIEs(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/lies');
    }

    public getLFAs(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/lfas');
    }

    public getTRAAuthorizedPersons(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/traAuthorizedPersons');
    }

    public getReferenceInterestRates(): Observable<any> {
        return this._http.get<any>('enquiry/api/referenceinterestrates/all');
    }

    public getReferenceInterestRateSigns(): Observable<any> {
        return this._http.get<any>('enquiry/api/referenceinterestratesign/all');
    }

    getConditionTypes(): Observable<any> {
        return this._http.get<any>('enquiry/api/conditiontype/all');
    }

    getInterestPaymentFrequencies(): Observable<any> {
        return this._http.get<any>('enquiry/api/conditiontype/all');
    }

    getPaymentForms(): Observable<any> {
        return this._http.get<any>('enquiry/api/paymentform/all');
    }

    getInterestCalculationMethods(): Observable<any> {
        return this._http.get<any>('enquiry/api/interestcalcmethod/all');
    }

    /**
     * getLoanMonitor()
     * @param loanApplicationId 
     */
    public getLoanMonitor(loanApplicationId: any): Observable<any>
    {
        return this._http.get<any>('enquiry/api/loanApplications/' + loanApplicationId + '/loanMonitor');
    }

    /**
     * getLoanContractExtension()
     * @param loanApplicationId 
     */
    public getLoanContractExtension(loanApplicationId: any): Observable<any>
    {
        return this._http.get<any>('enquiry/api/loancontractextension/' + loanApplicationId);
    }

    /**
     * sendMonitoringForApproval()
     */
    public sendMonitoringForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Monitoring'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}
