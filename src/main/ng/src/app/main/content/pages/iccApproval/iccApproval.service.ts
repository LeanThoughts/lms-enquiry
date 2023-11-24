import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, Observable, forkJoin } from 'rxjs';

@Injectable()
export class ICCApprovalService {

    _iccApproval: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        console.log('in resolve :: iccApprovalId is ', this._iccApproval.value.id);
        return forkJoin([
            this.getICCFurtherDetails(this._iccApproval.value.id)
        ]);
    }

    /**
     * getICCApproval()
     */
    public getICCApproval(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/iCCApprovals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getICCFurtherDetails()
     */
    public getICCFurtherDetails(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/iCCFurtherDetails/search/findByIccApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * deleteFurtherDetail()
     */
    public deleteFurtherDetail(furtherDetailId: string): Observable<any> {
        return this._http.delete("enquiry/api/iCCFurtherDetails/delete/" + furtherDetailId);
    }

    /**
     * createFurtherDetail()
     */
    public createFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.post("enquiry/api/iCCFurtherDetails/create", furtherDetail);
    }

    /**
     * updateFurtherDetail()
     */
    public updateFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.put("enquiry/api/iCCFurtherDetails/update", furtherDetail);
    }

    /**
     * getICCFurtherDetails()
     */
    public getReasonForDelay(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/iCCReasonForDelays/search/findByIccApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * deleteReasonForDelay()
     */
    public deleteReasonForDelay(reasonForDelayId: string): Observable<any> {
        return this._http.delete("enquiry/api/iCCReasonForDelays/delete/" + reasonForDelayId);
    }

    /**
     * createReasonForDelay()
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/iCCReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateReasonForDelay()
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/iCCReasonForDelays/update", reasonForDelay);
    }

    /**
     * getRejectedByICC()
     */
    public getRejectedByICC(iccApprovalId: string): Observable<any> {
        return this._http.get("enquiry/api/rejectedByICCs/search/findByIccApprovalId?iccApprovalId=" + iccApprovalId);
    }

    /**
     * deleteRejectedByICC()
     */
    public deleteRejectedByICC(rejectedByICCId: string): Observable<any> {
        return this._http.delete("enquiry/api/rejectedByICCs/delete/" + rejectedByICCId);
    }

    /**
     * createRejectedByICC()
     */
    public createRejectedByICC(rejectedByICC: any): Observable<any> {
        return this._http.post("enquiry/api/rejectedByICCs/create", rejectedByICC);
    }

    /**
     * updateReasonForDelay()
     */
    public updateRejectedByICC(rejectedByICC: any): Observable<any> {
        return this._http.put("enquiry/api/rejectedByICCs/update", rejectedByICC);
    }    
}
