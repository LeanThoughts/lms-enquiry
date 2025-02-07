import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, Observable, forkJoin } from 'rxjs';
import { EnquiryActionService } from '../enquiryAction/enquiryAction.service';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class BMCApprovalService {

    _bmcIccApproval: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient, 
        private _loanEnquiryService: LoanEnquiryService,
        private _enquiryActionService: EnquiryActionService) {
    }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return forkJoin([
            this.getBmcICCFurtherDetails(this._bmcIccApproval.value.id),
            this.getBmcLoanEnhancements(this._bmcIccApproval.value.id),
            this.getEnquiryCompletion(this._loanEnquiryService.selectedLoanApplicationId.value)
        ]);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    /**
     * getEnquiryCompletion()
     */
    getEnquiryCompletion(loanApplicationId: string): Observable<any> {
        return new Observable((observer) => {
            this._enquiryActionService.getEnquiryAction(loanApplicationId).subscribe(response => {
                this._enquiryActionService.getEnquiryCompletion(response.id).subscribe(response => {
                    observer.next(response);
                    observer.complete();
                });
            });
        });
    }

    /**
     * getBmcICCApproval()
     */
    public getBmcICCApproval(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/bmcIccApprovals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getBmcICCFurtherDetails()
     */
    public getBmcICCFurtherDetails(bmcIccApprovalId: string): Observable<any> {
        // return this._http.get("enquiry/api/iCCFurtherDetails/search/findByIccApprovalId?iccApprovalId=" + iccApprovalId);
        return new Observable((observer) => {
            this._http.get('enquiry/api/bmcIccFurtherDetails/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).subscribe(
                ((response: any) => {
                    observer.next(response._embedded.bmcIccFurtherDetails);
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
     * deleteBmcFurtherDetail()
     */
    public deleteBmcFurtherDetail(furtherDetailId: string): Observable<any> {
        return this._http.delete("enquiry/api/bmcIccFurtherDetails/delete/" + furtherDetailId);
    }

    /**
     * createBmcFurtherDetail()
     */
    public createBmcFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.post("enquiry/api/bmcIccFurtherDetails/create", furtherDetail);
    }

    /**
     * updateBmcFurtherDetail()
     */
    public updateBmcFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.put("enquiry/api/bmcIccFurtherDetails/update", furtherDetail);
    }

    /**
     * getBmcReasonForDelay()
     */
    public getBmcReasonForDelay(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/bmcIccReasonForDelays/search/findByBmcICCApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * createBmcReasonForDelay()
     */
    public createBmcReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/bmcIccReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateBmcReasonForDelay()
     */
    public updateBmcReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/bmcIccReasonForDelays/update", reasonForDelay);
    }

    /**
     * getBmcRejectedByICC()
     */
    public getBmcRejectedByICC(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/bmcRejectedByIccs/search/findByBmcICCApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * createBmcRejectedByICC()
     */
    public createBmcRejectedByICC(rejectedByICC: any): Observable<any> {
        return this._http.post("enquiry/api/bmcRejectedByIccs/create", rejectedByICC);
    }

    /**
     * updateBmcRejectedByICC()
     */
    public updateBmcRejectedByICC(rejectedByICC: any): Observable<any> {
        return this._http.put("enquiry/api/bmcRejectedByIccs/update", rejectedByICC);
    }

    /**
     * getBmcApprovalByICC()
     */
    public getBmcApprovalByICC(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/bmcApprovalByIccs/search/findByBmcICCApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * createBmcApprovalByICC()
     */
    public createBmcApprovalByICC(approvalByICC: any): Observable<any> {
        return this._http.post("enquiry/api/bmcApprovalByIccs/create", approvalByICC);
    }

    /**
     * updateBmcApprovalByICC()
     */
    public updateBmcApprovalByICC(approvalByICC: any): Observable<any> {
        return this._http.put("enquiry/api/bmcApprovalByIccs/update", approvalByICC);
    }

    /**
     * getBmcRejectedByCustomer()
     */
    public getBmcRejectedByCustomer(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/bmcRejectedByCustomers/search/findByBmcICCApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * createBmcRejectedByCustomer()
     */
    public createBmcRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.post("enquiry/api/bmcRejectedByCustomers/create", rejectedByCustomer);
    }

    /**
     * updateBmcRejectedByCustomer()
     */
    public updateBmcRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.put("enquiry/api/bmcRejectedByCustomers/update", rejectedByCustomer);
    }

    /**
     * getBmcLoanEnhancements()
     */
    public getBmcLoanEnhancements(iccApprovalId: string): Observable<any> {
        // return this._http.get("enquiry/api/loanEnhancements/search/findByIccApprovalId?iccApprovalId=" + iccApprovalId);
        return new Observable((observer) => {
            this._http.get('enquiry/api/bmcLoanEnhancements/search/findByBmcICCApprovalId?iccApprovalId=' + iccApprovalId).subscribe(
                ((response: any) => {
                    observer.next(response._embedded.bmcLoanEnhancements);
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
     * deleteBmcLoanEnhancement()
     */
    public deleteBmcLoanEnhancement(loanEnhancementId: string): Observable<any> {
        return this._http.delete("enquiry/api/bmcLoanEnhancements/delete/" + loanEnhancementId);
    }

    /**
     * createBmcLoanEnhancement()
     */
    public createBmcLoanEnhancement(loanEnhancement: any): Observable<any> {
        return this._http.post("enquiry/api/bmcLoanEnhancements/create", loanEnhancement);
    }

    /**
     * updateBmcLoanEnhancement()
     */
    public updateBmcLoanEnhancement(loanEnhancement: any): Observable<any> {
        return this._http.put("enquiry/api/bmcLoanEnhancements/update", loanEnhancement);
    }

    /**
     * sendApplicationFeeForApproval()
     */
    public sendICCApprovalForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'ICCApproval'
        }
        return this._http.post<any>('enquiry/api/iccApprovals/sendForApproval', requestObj);
    }    
}
