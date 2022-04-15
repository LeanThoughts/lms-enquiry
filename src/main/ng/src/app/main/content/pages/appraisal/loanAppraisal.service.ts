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
            this.getLoanOfficersForKyc(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getBanks(),
            this.getSyndicateConsortiums(this._loanAppraisal.id),
            this.getProposalDetail(this._loanAppraisal.id),
            this.getFurtherDetail(this._loanAppraisal.id),
            this.getSiteVisits(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getProjectAppraisalCompletion(this._loanAppraisal.id),
            this.getReasonForDelay(this._loanAppraisal.id),
            this.getCustomerRejection(this._loanAppraisal.id),
            this.getProjectData(this._loanAppraisal.id),
            this._loanEnquiryService.getUnitOfMeasures(),
            this._loanEnquiryService.getProjectTypes(),
            this.getBusinessPartners('ZLM015'),
            this.getBusinessPartners('ZLM028')
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
     * getLoanOfficersForKyc()
     */
    public getLoanOfficersForKyc(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdAndKycRequired?loanApplicationId=" 
                + loanApplicationId + '&kycRequired=true');
    }

    /**
     * deleteLoanOfficer()
     */
    public deleteLoanOfficer(partnerId: string): Observable<any> {
        return this._http.delete("enquiry/api/loanPartners/delete/" + partnerId);
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
     * getKYCDocuments()
     */
    public getKYCDocuments(loanPartnerId: string): Observable<any> {
        return this._http.get("enquiry/api/knowYourCustomers/search/findByLoanPartnerId?loanPartnerId=" + loanPartnerId);
    }

    /**
     * updateKYC()
     */
    public updateKYC(kyc: string): Observable<any> {
        return this._http.put("enquiry/api/knowYourCustomers/update", kyc);
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
     * deleteLoanOfficer()
     */
    public deleteSyndicateConsortium(bankId: string): Observable<any> {
        return this._http.delete("enquiry/api/syndicateConsortiums/delete/" + bankId);
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
        return this._http.get('enquiry/api/siteVisits/search/findByLoanMonitorLoanApplicationIdAndSiteVisitTypeOrderBySerialNumberDesc?loanApplicationId=' 
                + loanApplicationId + '&siteVisitType=Site Visit');
    }

    /**
     * deleteSiteVisit()
     */
    public deleteSiteVisit(id: string): Observable<any> {
        return this._http.delete("enquiry/api/siteVisits/delete/" + id);
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
     * getReasonForDelay()
     */
    public getReasonForDelay(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/reasonForDelays/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createReasonForDelay()
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/reasonForDelays/create", reasonForDelay);
    }

    /**
     * updateReasonForDelay()
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/reasonForDelays/update", reasonForDelay);
    }

    /**
     * getCustomerRejection()
     */
    public getCustomerRejection(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/customerRejections/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createCustomerRejection()
     */
    public createCustomerRejection(customerRejection: any): Observable<any> {
        return this._http.post("enquiry/api/customerRejections/create", customerRejection);
    }

    /**
     * updateCustomerRejection()
     */
    public updateCustomerRejection(customerRejection: any): Observable<any> {
        return this._http.put("enquiry/api/customerRejections/update", customerRejection);
    }

    /**
     * getProjectData()
     */
    public getProjectData(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/projectDatas/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createProjectData()
     */
    public createProjectData(projectData: any): Observable<any> {
        return this._http.post("enquiry/api/projectDatas/create", projectData);
    }

    /**
     * updateProjectData()
     */
    public updateProjectData(projectData: any): Observable<any> {
        return this._http.put("enquiry/api/projectDatas/update", projectData);
    }

    /**
     * getBusinessPartners()
     */
    public getBusinessPartners(role: string): Observable<any> {
        return this._http.get("enquiry/api/partners/role/" + role);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }    
}
