import { Observable, forkJoin } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class LoanAppraisalService implements Resolve<any> {

    _loanAppraisal: any;

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
        console.log('in resolve :: loanAppraisalId is ', this._loanAppraisal.id);
        return forkJoin([
            this.getLoanOfficers(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getLaonAppraisalKYCs(this._loanAppraisal.id),
            this.getBanks(),
            this.getSyndicateConsortiums(this._loanAppraisal.id),
            this.getProposalDetail(this._loanAppraisal.id),
            this.getFurtherDetail(this._loanAppraisal.id),
            this.getSiteVisits(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getProjectAppraisalCompletion(this._loanAppraisal.id)
        ]);
    }
    
    /**
     * getBanks()
     */
    public getBanks(): Observable<any> {
        return this._http.get('enquiry/api/bankmasters/all');
    }

    /**
     * getLaonAppraisal()
     */
    public getLaonAppraisal(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanAppraisals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getPartnersByRoleType()
     */
    public getPartnersByRole(role: string): Observable<any> {
        return this._http.get("enquiry/api/partners/role/" + role);
    }
    
    /**
     * getAppraisalOfficers()
     */
    public getLoanOfficers(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdOrderBySerialNumberDesc?loanApplicationId=" 
                + loanApplicationId);
    }

    /**
     * createAppraisalOfficer()
     */
    public createLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.post("enquiry/api/loanPartners/create", loanOfficer);
    }

    /**
     * updateLoanOfficer()
     */
    public updateLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.put("enquiry/api/loanPartners/update", loanOfficer);
    }

    /**
     * createLoanAppraisalKYC()
     */
    public createLoanAppraisalKYC(loanAppraisalKYC: any): Observable<any> {
        return this._http.post("enquiry/api/knowYourCustomers/create", loanAppraisalKYC);
    }

    /**
     * updateLoanAppraisalKYC()
     */
    public updateLoanAppraisalKYC(loanAppraisalKYC: any): Observable<any> {
        return this._http.put("enquiry/api/knowYourCustomers/update", loanAppraisalKYC);
    }

    /**
     * getLaonAppraisalKYCs()
     */
    public getLaonAppraisalKYCs(loanAppraisalId: string): Observable<any> {
        if (loanAppraisalId === '') {
            return Observable.of({});
        }
        else {
            return this._http.get("enquiry/api/knowYourCustomers/search/findByLoanAppraisalIdOrderBySerialNumberDesc?loanAppraisalId=" 
                    + loanAppraisalId);
        }
    }

    /**
     * getSyndicateConsortiums()
     */
    public getSyndicateConsortiums(loanAppraisalId: string): Observable<any> {
        if (loanAppraisalId === '') {
            return Observable.of({});
        }
        else {
            return this._http.get("enquiry/api/syndicateConsortiums/search/findByLoanAppraisalIdOrderBySerialNumberDesc?loanAppraisalId=" 
                    + loanAppraisalId);
        }
    }

    /**
     * createSyndicateConsortium()
     */
    public createSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.post("enquiry/api/syndicateConsortiums/create", syndicateConsortium);
    }

    /**
     * updateSyndicateConsortium()
     */
    public updateSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.put("enquiry/api/syndicateConsortiums/update", syndicateConsortium);
    }

    /**
     * getProposalDetail()
     */
    public getProposalDetail(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/proposalDetails/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }

    /**
     * createProposalDetail()
     */
    public createProposalDetail(proposalDetail: any): Observable<any> {
        return this._http.post("enquiry/api/proposalDetails/create", proposalDetail);
    }

    /**
     * updateProposalDetail()
     */
    public updateProposalDetail(proposalDetail: any): Observable<any> {
        return this._http.put("enquiry/api/proposalDetails/update", proposalDetail);
    }

    /**
     * getFurtherDetails()
     */
    public getFurtherDetail(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/furtherDetails/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }

    /**
     * updateFurtherDetail()
     */
    public updateFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.put("enquiry/api/furtherDetails/update", furtherDetail);
    }

    /**
     * getSiteVisits()
     */
    getSiteVisits(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/siteVisits/search/findByLoanMonitorLoanApplicationIdAndSiteVisitType?loanApplicationId=' 
                + loanApplicationId + '&siteVisitType=Site Visit');
    }

    /**
     * getProjectAppraisalCompletion()
     */
    public getProjectAppraisalCompletion(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/projectAppraisalCompletions/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }
    
    /**
     * createProjectAppraisalCompletion()
     */
    public createProjectAppraisalCompletion(proposalDetail: any): Observable<any> {
        return this._http.post("enquiry/api/projectAppraisalCompletions/create", proposalDetail);
    }

    /**
     * updateProjectAppraisalCompletion()
     */
    public updateProjectAppraisalCompletion(proposalDetail: any): Observable<any> {
        return this._http.put("enquiry/api/projectAppraisalCompletions/update", proposalDetail);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }
}
