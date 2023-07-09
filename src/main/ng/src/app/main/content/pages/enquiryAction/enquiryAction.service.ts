import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class EnquiryActionService implements Resolve<any> {

    _enquiryAction: BehaviorSubject<any> = new BehaviorSubject({});

    _loanApplication: any;

    /**
     * constructor()
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        console.log('in resolve :: enquiryActionId is ', this._enquiryAction.value.id);
        return forkJoin([
            this.getReasonForDelay(this._enquiryAction.value.id),
            this.getRejectByPFS(this._enquiryAction.value.id),
            this.getRejectByCustomers(this._enquiryAction.value.id),
            this.getEnquiryCompletion(this._enquiryAction.value.id),
            this.getOtherDetails(this._enquiryAction.value.id),
            this.getProjectProposals(this._enquiryAction.value.id)
        ]);
    }

    public sendEnquiryActionForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'EnquiryAction'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }

    public deleteShareHolder(shareHolder: any): Observable<any> {
        const url = "enquiry/api/shareHolders/delete/" + shareHolder.id;
        return this._http.delete(url);
    }

    public deleteCreditRating(creditRating: any): Observable<any> {
        const url = "enquiry/api/creditRatings/delete/" + creditRating.id;
        return this._http.delete(url);
    }
    
    public deleteOtherDetailsDocument(otherDetialsDocument: any): Observable<any> {
        const url = "enquiry/api/otherDetailsDocuments/delete/" + otherDetialsDocument.id;
        return this._http.delete(url);
    }

    /**
     * getLoanApplication()
     */
    public getLoanApplication(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanApplications/" + loanApplicationId);
    }

    /**
     * getEnquiryAction()
     */
    public getEnquiryAction(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/enquiryActions/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    /**
     * getOtherDetailsDocument()
     */
    public getOtherDetailsDocuments(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/otherDetailsDocuments/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createOtherDetailsDocument()
     */
    public createOtherDetailsDocument(otherDetialsDocument: any): Observable<any> {
        return this._http.post("enquiry/api/otherDetailsDocuments/create", otherDetialsDocument);
    }

    /**
     * updateOtherDetailsDocument()
     */
    public updateOtherDetailsDocument(otherDetialsDocument: any): Observable<any> {
        return this._http.put("enquiry/api/otherDetailsDocuments/update", otherDetialsDocument);
    }

    /**
     * getProjectProposalOtherDetails()
     */
    public getProjectProposalOtherDetails(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/projectProposalOtherDetails/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createProjectProposalOtherDetails()
     */
    public createProjectProposalOtherDetails(otherDetials: any): Observable<any> {
        return this._http.post("enquiry/api/projectProposalOtherDetails/create", otherDetials);
    }

    /**
     * updateProjectProposalOtherDetails()
     */
    public updateProjectProposalOtherDetails(otherDetials: any): Observable<any> {
        return this._http.put("enquiry/api/projectProposalOtherDetails/update", otherDetials);
    }

    /**
     * getCreditRatings()
     */
    public getCreditRatings(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/creditRatings/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createCreditRating()
     */
    public createCreditRating(creditRating: any): Observable<any> {
        return this._http.post("enquiry/api/creditRatings/create", creditRating);
    }

    /**
     * updateCreditRating()
     */
    public updateCreditRating(creditRating: any): Observable<any> {
        return this._http.put("enquiry/api/creditRatings/update", creditRating);
    }

    /**
     * getProjectDetail()
     */
    public getProjectDetail(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/projectDetails/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createProjectDetail()
     */
    public createProjectDetail(projectDetail: any): Observable<any> {
        return this._http.post("enquiry/api/projectDetails/create", projectDetail);
    }

    /**
     * updateProjectDetail()
     */
    public updateProjectDetail(projectDetail: any): Observable<any> {
        return this._http.put("enquiry/api/projectDetails/update", projectDetail);
    }

    /**
     * getShareHolders()
     */
    public getFinancials(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/promoterBorrowerFinancials/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createFinancial()
     */
    public createFinancial(financial: any): Observable<any> {
        return this._http.post("enquiry/api/promoterBorrowerFinancials/create", financial);
    }

    /**
     * updateFinancial()
     */
    public updateFinancial(financial: any): Observable<any> {
        return this._http.put("enquiry/api/promoterBorrowerFinancials/update", financial);
    }

    /**
     * getShareHolders()
     */
    public getShareHolders(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/shareHolders/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createShareHolder()
     */
    public createShareHolder(shareHolder: any): Observable<any> {
        return this._http.post("enquiry/api/shareHolders/create", shareHolder);
    }

    /**
     * updateShareHolder()
     */
    public updateShareHolder(shareHolder: any): Observable<any> {
        return this._http.put("enquiry/api/shareHolders/update", shareHolder);
    }

    /**
     * getProjectCost()
     */
    public getProjectCost(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/projectCosts/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createProjectCost()
     */
    public createProjectCost(projectCost: any): Observable<any> {
        return this._http.post("enquiry/api/projectCosts/create", projectCost);
    }

    /**
     * updateProjectCost()
     */
    public updateProjectCost(projectCost: any): Observable<any> {
        return this._http.put("enquiry/api/projectCosts/update", projectCost);
    }

    /**
     * getCollateralDetails()
     */
    public getCollateralDetails(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/collateralDetails/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createCollateralDetail()
     */
    public createCollateralDetail(collateralDetail: any): Observable<any> {
        return this._http.post("enquiry/api/collateralDetails/create", collateralDetail);
    }

    /**
     * updateCollateralDetail()
     */
    public updateCollateralDetail(collateralDetail: any): Observable<any> {
        return this._http.put("enquiry/api/collateralDetails/update", collateralDetail);
    }

    /**
     * getDealGuaranteeTimeline()
     */
    public getDealGuaranteeTimeline(projectProposalId: string): Observable<any> {
        return this._http.get("enquiry/api/dealGuaranteeTimelines/search/findByProjectProposalId?projectProposalId=" + projectProposalId);
    }

    /**
     * createDealGuaranteeTimeline()
     */
    public createDealGuaranteeTimeline(dealGuaranteeTimeline: any): Observable<any> {
        return this._http.post("enquiry/api/dealGuaranteeTimelines/create", dealGuaranteeTimeline);
    }

    /**
     * updateDealGuaranteeTimeline()
     */
    public updateDealGuaranteeTimeline(dealGuaranteeTimeline: any): Observable<any> {
        return this._http.put("enquiry/api/dealGuaranteeTimelines/update", dealGuaranteeTimeline);
    }

    /**
     * getProjectProposals()
     */
    public getProjectProposals(enquiryActionId: string): Observable<any> {
        return this._http.get("enquiry/api/projectProposals/search/findByEnquiryActionId?enquiryActionId=" + enquiryActionId);
    }

    /**
     * createProjectProposal()
     */
    public createProjectProposal(projectProposal: any): Observable<any> {
        return this._http.post("enquiry/api/projectProposals/create", projectProposal);
    }

    /**
     * updateProjectProposal()
     */
    public updateProjectProposal(projectProposal: any): Observable<any> {
        return this._http.put("enquiry/api/projectProposals/update", projectProposal);
    }

    /**
     * getOtherDetails()
     */
    public getOtherDetails(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/otherDetails/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
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
     * createOtherDetails()
     */
    public createOtherDetails(otherDetails: any): Observable<any> {
        return this._http.post("enquiry/api/otherDetails/create", otherDetails);
    }

    /**
     * updateOtherDetails()
     */
    public updateOtherDetails(otherDetails: any): Observable<any> {
        return this._http.put("enquiry/api/otherDetails/update", otherDetails);
    }

    /**
     * getEnquiryCompletion()
     */
    public getEnquiryCompletion(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/enquiryCompletions/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
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
     * createEnquiryCompletion()
     */
    public createEnquiryCompletion(enquiryCompletion: any): Observable<any> {
        return this._http.post("enquiry/api/enquiryCompletions/create", enquiryCompletion);
    }

    /**
     * updateEnquiryCompletion()
     */
    public updateEnquiryCompletion(enquiryCompletion: any): Observable<any> {
        return this._http.put("enquiry/api/enquiryCompletions/update", enquiryCompletion);
    }

    /**
     * getReasonForDelay()
     */
    public getReasonForDelay(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/enquiryActionReasonForDelays/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
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
        return this._http.post("enquiry/api/enquiryActionReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateReasonForDelay()
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/enquiryActionReasonForDelays/update", reasonForDelay);
    }

    /**
     * getRejectByCustomers()
     */
    public getRejectByCustomers(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/rejectByCustomers/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
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
     * createRejectByCustomers()
     */
    public createRejectByCustomers(rejectByCustomer: any): Observable<any> {
        return this._http.post("enquiry/api/rejectByCustomers/create", rejectByCustomer);
    }

    /**
     * updateRejectByCustomers()
     */
    public updateRejectByCustomers(rejectByCustomer: any): Observable<any> {
        return this._http.put("enquiry/api/rejectByCustomers/update", rejectByCustomer);
    }

    /**
     * getRejectByPFS()
     */
    public getRejectByPFS(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/rejectByPfses/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
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
     * createRejectByPFS()
     */
    public createRejectByPFS(rejectByPFS: any): Observable<any> {
        return this._http.post("enquiry/api/rejectByPfses/create", rejectByPFS);
    }

    /**
     * updateRejectByPFS()
     */
    public updateRejectByPFS(rejectByPFS: any): Observable<any> {
        return this._http.put("enquiry/api/rejectByPfses/update", rejectByPFS);
    }
}
