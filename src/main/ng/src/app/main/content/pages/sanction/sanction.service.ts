import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class SanctionService {

    _sanction: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * getSanction()
     */
    public getSanction(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/sanctions/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getReasonForDelays()
     */
    public getReasonForDelays(): Observable<any> {
        return this._http.get("enquiry/api/sanctionReasonForDelays/search/findBySanctionId?sanctionId=" + 
            this._sanction.value.id);
    }

    /**
     * createSanctionReasonForDelay()
     */
    public createSanctionReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/sanctionReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateSanctionReasonForDelay()
     */
    public updateSanctionReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/sanctionReasonForDelays/update", reasonForDelay);
    }

    /**
     * deleteSanctionReasonForDelay()
     */
    public deleteSanctionReasonForDelay(sanctionReasonForDelayId: string) {
        return this._http.delete("enquiry/api/sanctionReasonForDelays/delete/" + sanctionReasonForDelayId);
    }

    /**
     * getCustomerRejectionReasons()
     */
    public getCustomerRejectionReasons(): Observable<any> {
        return this._http.get("enquiry/api/customerRejectionReasons");
    }

    /**
     * getRejectedByCustomers()
     */
    public getRejectedByCustomers(): Observable<any> {
        return this._http.get("enquiry/api/sanctionRejectedByCustomers/search/findBySanctionId?sanctionId=" + 
            this._sanction.value.id);
    }

    /**
     * createRejectedByCustomer()
     */
    public createRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.post("enquiry/api/sanctionRejectedByCustomers/create", rejectedByCustomer);
    }

    /**
     * updateRejectedByCustomer()
     */
    public updateRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.put("enquiry/api/sanctionRejectedByCustomers/update", rejectedByCustomer);
    }

    /**
     * deleteRejectedByCustomer()
     */
    public deleteRejectedByCustomer(rejectedByCustomerId: string) {
        return this._http.delete("enquiry/api/sanctionRejectedByCustomers/delete/" + rejectedByCustomerId);
    }

    /**
     * sendSanctionForWorkflowApproval()
     */
    public sendSanctionForWorkflowApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Sanction'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}