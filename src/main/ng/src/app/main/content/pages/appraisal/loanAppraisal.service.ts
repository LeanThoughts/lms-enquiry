import { Observable, forkJoin } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class LoanAppraisalService implements Resolve<any> {

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * resolve()
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return forkJoin([
            this.getLoanOfficers(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getLaonAppraisalKYCs(this._loanEnquiryService.selectedLoanApplicationId.value)
        ]);
    }
    
    /**
     * getLaonAppraisal()
     * @param loanApplicationId 
     */
    public getLaonAppraisal(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanAppraisals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getPartnersByRoleType()
     * @param roleType 
     */
    public getPartnersByRole(role: string): Observable<any> {
        return this._http.get("enquiry/api/partners/role/" + role);
    }
    
    /**
     * getAppraisalOfficers()
     * @param loanApplicationId 
     */
    public getLoanOfficers(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdOrderBySerialNumberDesc?loanApplicationId=" 
                + loanApplicationId);
    }

    /**
     * createAppraisalOfficer()
     * @param appraisalOfficer 
     */
    public createLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.post("enquiry/api/loanPartners/create", loanOfficer);
    }

    /**
     * updateLoanOfficer()
     * @param loanOfficer 
     */
    public updateLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.put("enquiry/api/loanPartners/update", loanOfficer);
    }

    /**
     * createLoanAppraisalKYC()
     * @param loanAppraisalKYC 
     */
    public createLoanAppraisalKYC(loanAppraisalKYC: any): Observable<any> {
        return this._http.post("enquiry/api/loanAppraisalKYCs/create", loanAppraisalKYC);
    }

    /**
     * updateLoanAppraisalKYC()
     * @param loanAppraisalKYC 
     */
    public updateLoanAppraisalKYC(loanAppraisalKYC: any): Observable<any> {
        return this._http.put("enquiry/api/loanAppraisalKYCs/update", loanAppraisalKYC);
    }

    /**
     * getLaonAppraisalKYCs()
     * @param loanApplicationId 
     */
    public getLaonAppraisalKYCs(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanAppraisalKYCs/search/findByLoanAppraisalLoanApplicationIdOrderBySerialNumberDesc?loanApplicationId=" 
                + loanApplicationId);
    }

    /**
     * getSyndicateConsortiums()
     * @param loanApplicationId 
     */
    public getSyndicateConsortiums(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/syndicateConsortiums/search/findByLoanAppraisalLoanApplicationIdOrderBySerialNumberDesc?loanApplicationId=" 
                + loanApplicationId);
    }

    /**
     * createSyndicateConsortium()
     * @param syndicateConsortium 
     */
    public createSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.post("enquiry/api/syndicateConsortiums/create", syndicateConsortium);
    }

    /**
     * updateSyndicateConsortium()
     * @param syndicateConsortium 
     */
    public updateSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.put("enquiry/api/syndicateConsortiums/update", syndicateConsortium);
    }

    /**
     * uploadVaultDocument()
     * @param file 
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }
}
