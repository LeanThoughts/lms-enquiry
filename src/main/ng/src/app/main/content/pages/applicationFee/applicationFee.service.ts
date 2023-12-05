import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, Observable, forkJoin } from 'rxjs';

@Injectable()
export class ApplicationFeeService {

    _applicationFee: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient) {
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
        return this._http.get('enquiry/api/formalRequests/search/findByApplicationFeeId?applicationFeeId=' + applicationFeeId);
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
}
