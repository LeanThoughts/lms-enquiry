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
     * getPaymentReceipts()
     */
    public getPaymentReceipts(preOrPost: string): Observable<any> {
        if (preOrPost === 'pre') {
            return this._http.get("enquiry/api/paymentReceiptPreSanctions/search/findBySanctionId?sanctionId=" + 
                    this._sanction.value.id);
        }
        else {
            return this._http.get("enquiry/api/paymentReceiptPostSanctions/search/findBySanctionId?sanctionId=" + 
                    this._sanction.value.id);
        }
    }

    /**
     * createPaymentReceipt()
     */
    public createPaymentReceipt(paymentReceipt: any, preOrPost: string): Observable<any> {
        if (preOrPost === 'pre') {
            return this._http.post("enquiry/api/paymentReceiptPreSanctions/create", paymentReceipt);
        }
        else {
            return this._http.post("enquiry/api/paymentReceiptPostSanctions/create", paymentReceipt);
        }
    }

    /**
     * updatePaymentReceipt()
     */
    public updatePaymentReceipt(paymentReceipt: any, preOrPost: string): Observable<any> {
        if (preOrPost === 'pre') {
            return this._http.put("enquiry/api/paymentReceiptPreSanctions/update", paymentReceipt);
        }
        else {
            return this._http.put("enquiry/api/paymentReceiptPostSanctions/update", paymentReceipt);
        }
    }

    /**
     * deletePaymentReceipt()
     */
    public deletePaymentReceipt(rejectedByCustomerId: string, preOrPost: string) {
        if (preOrPost === 'pre') {
            return this._http.delete("enquiry/api/paymentReceiptPreSanctions/delete/" + rejectedByCustomerId);
        }
        else {
            return this._http.delete("enquiry/api/paymentReceiptPostSanctions/delete/" + rejectedByCustomerId);
        }
    }

    /**
     * getSanctionLetters()
     */
    public getSanctionLetters(): Observable<any> {
        return this._http.get("enquiry/api/sanctionLetters/search/findBySanctionId?sanctionId=" + 
            this._sanction.value.id);
    }

    /**
     * createSanctionLetter()
     */
    public createSanctionLetter(sanctionLetter: any): Observable<any> {
        return this._http.post("enquiry/api/sanctionLetters/create", sanctionLetter);
    }

    /**
     * updateSanctionLetter()
     */
    public updateSanctionLetter(sanctionLetter: any): Observable<any> {
        return this._http.put("enquiry/api/sanctionLetters/update", sanctionLetter);
    }

    /**
     * deleteSanctionLetter()
     */
    public deleteSanctionLetter(sanctionLetterId: string) {
        return this._http.delete("enquiry/api/sanctionLetters/delete/" + sanctionLetterId);
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

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    /**
     * getSanctionTypes()
     */
    public getSanctionTypes(): Observable<any> {
        return this._http.get('http://localhost:8080/enquiry/api/sanctionTypes?size=100&sort=code');
    }

    /**
     * getFeeTypes()
     */
    public getFeeTypes(): Observable<any> {
        return this._http.get('http://localhost:8080/enquiry/api/feeTypes?size=100&sort=code');
    }
}
