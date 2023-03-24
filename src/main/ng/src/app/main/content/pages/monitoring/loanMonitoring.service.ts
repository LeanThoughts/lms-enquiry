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
    selectedLLC: BehaviorSubject<any> = new BehaviorSubject({});
    selectedCLA: BehaviorSubject<any> = new BehaviorSubject({});
    selectedValuer: BehaviorSubject<any> = new BehaviorSubject({});
    
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

    // All about Valuer

    public getValuers(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/valuers');
    }

    public saveValuer(valuer: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/valuers/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'valuer':valuer, 'moduleName':module });
    }

    public updateValuer(valuer: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/valuers/" + valuer.id;
        return this._http.put(url, { 'loanApplicationId':'', 'valuer':valuer, 'moduleName':module });
    }

    // All about Valuer Reports And Fees
    
    public getValuerReportsAndFees(valuerId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/valuerReportAndFeeSubmission/' + valuerId + '/valuerReceiptsAndFees');
    }
    
    public saveValuerReportAndFee(valuerReportAndFee: any, valuerId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/valuerReportAndFeeSubmission/create";
        return this._http.post(url, { 'valuerId': valuerId, 'valuerReportAndFee': valuerReportAndFee, 'moduleName':module });
    }

    public updateValuerReportAndFee(valuerReportAndFee: any, module: string): Observable<any> {
        console.log('in service', valuerReportAndFee);
        const url = "enquiry/api/loanApplications/valuerReportAndFeeSubmission/" + valuerReportAndFee.id;
        return this._http.put(url, { 'valuerId': '', 'valuerReportAndFee': valuerReportAndFee, 'moduleName':module });
    }

    // All about LLC

    public getLendersLegalCouncils(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersLegalCouncils');
    }

    public saveLLC(llc: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersLegalCouncils/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersLegalCouncil':llc, 'moduleName':module });
    }

    public updateLLC(llc: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersLegalCouncils/" + llc.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersLegalCouncil':llc, 'moduleName':module });
    }

    // All about LLC Reports And Fees
    
    public getLLCReportsAndFees(llcId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/llcReportAndFeeSubmission/' + llcId + '/llcReceiptsAndFees');
    }
    
    public saveLLCReportAndFee(llcReportAndFee: any, liaId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/llcReportAndFeeSubmission/create";
        return this._http.post(url, { 'lendersLegalCouncilId': liaId, 'llcReportAndFee': llcReportAndFee, 'moduleName':module });
    }

    public updateLLCReportAndFee(llcReportAndFee: any, module: string): Observable<any> {
        console.log('in service', llcReportAndFee);
        const url = "enquiry/api/loanApplications/llcReportAndFeeSubmission/" + llcReportAndFee.id;
        return this._http.put(url, { 'lendersLegalCouncilId': '', 'llcReportAndFee': llcReportAndFee, 'moduleName':module });
    }

    // All about LIA

    public getLendersInsuranceAdvisors(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersInsuranceAdvisors');
    }

    public saveLIA(lia: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersInsuranceAdvisors/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersInsuranceAdvisor':lia, 'moduleName':module });
    }

    public updateLIA(lia: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersInsuranceAdvisors/" + lia.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersInsuranceAdvisor':lia, 'moduleName':module });
    }

    // All about LIA Reports And Fees
    
    public getLIAReportsAndFees(liaId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/liaReportAndFeeSubmission/' + liaId + '/liaReceiptsAndFees');
    }
    
    public saveLIAReportAndFee(liaReportAndFee: any, liaId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/liaReportAndFeeSubmission/create";
        return this._http.post(url, { 'lendersInsuranceAdvisorId': liaId, 'liaReportAndFee': liaReportAndFee, 'moduleName':module });
    }

    public updateLIAReportAndFee(liaReportAndFee: any, module: string): Observable<any> {
        console.log('in service', liaReportAndFee);
        const url = "enquiry/api/loanApplications/liaReportAndFeeSubmission/" + liaReportAndFee.id;
        return this._http.put(url, { 'lendersInsuranceAdvisorId': '', 'liaReportAndFee': liaReportAndFee, 'moduleName':module });
    }

    // All about CLA

    public getCommonLoanAgreements(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/commonLoanAgreements');
    }

    public saveCLA(cla: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/commonLoanAgreements/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'commonLoanAgreement':cla, 'moduleName':module });
    }

    public updateCLA(cla: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/commonLoanAgreements/" + cla.id;
        return this._http.put(url, { 'loanApplicationId':'', 'commonLoanAgreement':cla, 'moduleName':module });
    }

    // All about CLA Reports And Fees
    
    public getCLAReportsAndFees(claId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/claReportAndFeeSubmission/' + claId + '/claReceiptsAndFees');
    }
    
    public saveCLAReportAndFee(claReportAndFee: any, claId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/claReportAndFeeSubmission/create";
        return this._http.post(url, { 'commonLoanAgreementId': claId, 'claReportAndFee': claReportAndFee, 'moduleName':module });
    }

    public updateCLAReportAndFee(claReportAndFee: any, module: string): Observable<any> {
        console.log('in service', claReportAndFee);
        const url = "enquiry/api/loanApplications/claReportAndFeeSubmission/" + claReportAndFee.id;
        return this._http.put(url, { 'commonLoanAgreementId': '', 'claReportAndFee': claReportAndFee, 'moduleName':module });
    }

    // All about LIE

    public getLendersIndependentEngineers(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersIndependentEngineers');
    }

    public saveLIE(lie: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersindependentengineers/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersIndependentEngineer':lie, 'moduleName': module });
    }

    public updateLIE(lie: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersindependentengineers/" + lie.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersIndependentEngineer':lie, 'moduleName': module });
    }

    // All about LIE Reports And Fees
    
    public getLIEReportsAndFees(lieId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/lendersIndependentEngineer/' + lieId + '/lieReceiptsAndFees');
    }
    
    public saveLIEReportAndFee(lieReportAndFee: any, lieId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/liereportandfeesubmission/create";
        return this._http.post(url, { 'lendersIndependentEngineerId': lieId, 'lieReportAndFee': lieReportAndFee, 'moduleName': module });
    }

    public updateLIEReportAndFee(lieReportAndFee: any, module: string): Observable<any> {
        console.log('in service', lieReportAndFee);
        const url = "enquiry/api/loanApplications/liereportandfeesubmission/" + lieReportAndFee.id;
        return this._http.put(url, { 'lendersIndependentEngineerId': '', 'lieReportAndFee': lieReportAndFee, 'moduleName': module });
    }

    // All about LFA

    public getLendersFinancialAdvisors(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/lendersFinancialAdvisors');
    }
    
    public saveLFA(lfa: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersfinancialAdvisors/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'lendersFinancialAdvisor':lfa, 'moduleName': module });
    }

    public updateLFA(lfa: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lendersfinancialAdvisors/" + lfa.id;
        return this._http.put(url, { 'loanApplicationId':'', 'lendersFinancialAdvisor':lfa, 'moduleName': module });
    }

    // All about LFA Reports And Fees

    public getLFAReportsAndFees(lfaId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/lendersFinancialAdvisor/' + lfaId + '/lfaReceiptsAndFees');
    }

    public saveLFAReportAndFee(lfaReportAndFee: any, lfaId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/lfareportandfeesubmission/create";
        return this._http.post(url, { 'lendersFinancialAdvisorId': lfaId, 'lfaReportAndFee': lfaReportAndFee, 'moduleName': module });
    }

    public updateLFAReportAndFee(lfaReportAndFee: any, module: string): Observable<any> {
        console.log('in service', lfaReportAndFee);
        const url = "enquiry/api/loanApplications/lfareportandfeesubmission/" + lfaReportAndFee.id;
        return this._http.put(url, { 'lendersFinancialAdvisorId': '', 'lfaReportAndFee': lfaReportAndFee, 'moduleName': module });
    }

    // All about TRA

    public getTrustRetentionaccounts(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/trustretentionaccounts');
    }

    public saveTRA(tra: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/trustretentionaccount/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'trustRetentionAccount':tra, 'moduleName': module });
    }

    public updateTRA(tra: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/trustretentionaccounts/" + tra.id;
        return this._http.put(url, { 'loanApplicationId':'', 'trustRetentionAccount':tra, 'moduleName': module });
    }
    
    // All about TRA Statement

    public getTRAStatements(traId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/trustretentionaccount/' + traId + '/traStatements');
    }

    public saveTRAStatement(traStatement: any, traId: string, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/trastatement/create";
        return this._http.post(url, { 'trustRetentionAccountId': traId, 'trustRetentionAccountStatement': traStatement, 'moduleName': module });
    }

    public updateTRAStatement(traStatement: any, module: string): Observable<any> {
        const url = "enquiry/api/loanApplications/trastatement/" + traStatement.id;
        return this._http.put(url, { 'trustRetentionAccountId': '', 'trustRetentionAccountStatement': traStatement, 'moduleName': module });
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
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'siteVisit':siteVisit, 'moduleName': app });
    }

    public updateSiteVisit(siteVisit: any, moduleName: string): Observable<any> {
        const url = "enquiry/api/loanApplications/sitevisit/" + siteVisit.id;
        return this._http.put(url, { 'loanApplicationId':'', 'siteVisit':siteVisit, 'moduleName': moduleName });
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

    // All about NPA

    public getNPA(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/nPAs/loanApplication/' + loanApplicationId);
    }

    public saveNPA(npa: any, loanApplicationId: any): Observable<any> {
        const url = "enquiry/api/nPAs/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'npa':npa });
    }

    public updateNPA(npa: any): Observable<any> {
        const url = "enquiry/api/nPAs/" + npa.id;
        return this._http.put(url, { 'loanApplicationId':'', 'npa':npa });
    }

    // All about NPA Details

    public getNPADetails(npaId: string): Observable<any> {
        return this._http.get('enquiry/api/nPADetails/npaId/' + npaId);
    }

    public saveNPADetails(npaId: any, npaDetail: any): Observable<any> {
        const url = "enquiry/api/nPADetails/create";
        return this._http.post(url, { 'npaId':npaId, 'npaDetail':npaDetail });
    }

    public updateNPADetails(npaId: string, npaDetail: any): Observable<any> {
        const url = "enquiry/api/nPADetails/update";
        return this._http.put(url, { 'npaId':npaId, 'npaDetail':npaDetail });
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

    public getAllValuers(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/valuers');
    }

    public getLLCs(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/llcs');
    }

    public getLIAs(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/lias');
    }

    public getCLAs(): Observable<any> {
        return this._http.get<any>('enquiry/api/partner/clas');
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

    /**
     * getTRAAccountTypes()
     */
    public getTRAAccountTypes(): Observable<any> {
        return this._http.get('enquiry/api/traaccounttypes');
    }    
}
