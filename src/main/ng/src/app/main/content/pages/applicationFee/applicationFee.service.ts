import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, Observable, forkJoin } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class ApplicationFeeService {

    _applicationFee: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return forkJoin([
            this.getInvoicingDetails(this._applicationFee.value.id),
            this._loanEnquiryService.getLoanApplication(this._loanEnquiryService.selectedLoanApplicationId.value),
            this._loanEnquiryService.getProjectTypes(),
            this.getMeetingNumbers(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getAllPartners()
        ]);
    }

    /**
     * getAllPartners()
     */
    public getAllPartners(): Observable<any> {
        return this._http.get('enquiry/api/partners/all');
    }
    
    /**
     * getApplicationFee()
     */
    public getApplicationFee(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/applicationFees/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    /**
     * getFormalRequests()
     */
    public getFormalRequests(applicationFeeId: string): Observable<any> {
        return this._http.get('enquiry/api/formalRequests/search/findByApplicationFeeIdOrderBySerialNumber?applicationFeeId=' + applicationFeeId);
    }

    /**
     * deleteFormalRequest()
     */
    public deleteFormalRequest(formalRequestId: string): Observable<any> {
        return this._http.delete("enquiry/api/formalRequests/delete/" + formalRequestId);
    }

    /**
     * createFormalRequest()
     */
    public createFormalRequest(formalRequest: any): Observable<any> {
        return this._http.post("enquiry/api/formalRequests/create", formalRequest);
    }

    /**
     * updateFormalRequest()
     */
    public updateFormalRequest(formalRequest: any): Observable<any> {
        return this._http.put("enquiry/api/formalRequests/update", formalRequest);
    }

    /**
     * getTermSheets()
     */
    public getTermSheets(applicationFeeId: string): Observable<any> {
        return this._http.get('enquiry/api/termSheets/search/findByApplicationFeeIdOrderBySerialNumber?applicationFeeId=' + applicationFeeId);
    }

    /**
     * deleteTermSheet()
     */
    public deleteTermSheet(termSheetId: string): Observable<any> {
        return this._http.delete("enquiry/api/termSheets/delete/" + termSheetId);
    }

    /**
     * createTermSheet()
     */
    public createTermSheet(termSheet: any): Observable<any> {
        return this._http.post("enquiry/api/termSheets/create", termSheet);
    }

    /**
     * updateTermSheet()
     */
    public updateTermSheet(termSheet: any): Observable<any> {
        return this._http.put("enquiry/api/termSheets/update", termSheet);
    }

    /**
     * getInvoicingDetails()
     */
    public getInvoicingDetails(applicationFeeId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/invoicingDetails/search/findByApplicationFeeId?applicationFeeId=' + applicationFeeId).subscribe(
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
     * createInvoicingDetail()
     */
    public createInvoicingDetail(invoicingDetail: any): Observable<any> {
        return this._http.post("enquiry/api/invoicingDetails/create", invoicingDetail);
    }

    /**
     * updateInvoicingDetail()
     */
    public updateInvoicingDetail(invoicingDetail: any): Observable<any> {
        return this._http.put("enquiry/api/invoicingDetails/update", invoicingDetail);
    }

    /**
     * getMeetingNumbers()
     */
    public getMeetingNumbers(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/invoicingDetails/meetingNumbers/" + loanApplicationId);
    }

    /**
     * getInceptionFees()
     */
    public getInceptionFees(applicationFeeId: string): Observable<any> {
        return this._http.get('enquiry/api/inceptionFees/search/findByApplicationFeeId?applicationFeeId=' + applicationFeeId);
    }

    /**
     * deleteInceptionFee()
     */
    public deleteInceptionFee(inceptionFeeId: string): Observable<any> {
        return this._http.delete("enquiry/api/inceptionFees/delete/" + inceptionFeeId);
    }

    /**
     * createInceptionFee()
     */
    public createInceptionFee(inceptionFee: any): Observable<any> {
        return this._http.post("enquiry/api/inceptionFees/create", inceptionFee);
    }

    /**
     * updateInceptionFee()
     */
    public updateInceptionFee(inceptionFee: any): Observable<any> {
        return this._http.put("enquiry/api/inceptionFees/update", inceptionFee);
    }

    /**
     * sendApplicationFeeForApproval()
     */
    public sendApplicationFeeForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'ApplicationFee'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }

    /**
     * getPartner()
     */
    public getPartner(href: string): Observable<any> {
        return this._http.get(href);
    }
}
