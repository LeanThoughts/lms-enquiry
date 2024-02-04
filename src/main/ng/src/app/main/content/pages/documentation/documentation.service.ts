import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class DocumentationService {

    _documentation: BehaviorSubject<any> = new BehaviorSubject({});

    _selectedLegalCounsel: BehaviorSubject<any> = new BehaviorSubject({});
    
    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * getDocumentation()
     */
    public getDocumentation(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/documentations/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getReasonForDelays()
     */
    public getReasonForDelays(): Observable<any> {
        return this._http.get("enquiry/api/documentationReasonForDelays/search/findByDocumentationIdAndDeleteFlag?documentationId=" + 
            this._documentation.value.id + '&deleteFlag=false');
    }

    /**
     * createDocumentationReasonForDelay()
     */
    public createDocumentationReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/documentationReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateDocumentationReasonForDelay()
     */
    public updateDocumentationReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/documentationReasonForDelays/update", reasonForDelay);
    }

    /**
     * deleteDocumentationReasonForDelay()
     */
    public deleteDocumentationReasonForDelay(documentationReasonForDelayId: string) {
        return this._http.delete("enquiry/api/documentationReasonForDelays/delete/" + documentationReasonForDelayId);
    }

    /**
     * getLegalCounsels()
     */
    public getLegalCounsels(): Observable<any> {
        return this._http.get("enquiry/api/legalCounsels/search/findByDocumentationIdAndDeleteFlag?documentationId=" + 
            this._documentation.value.id + '&deleteFlag=false');
    }

    /**
     * createLegalCounsel()
     */
    public createLegalCounsel(legalCounsel: any): Observable<any> {
        return this._http.post("enquiry/api/legalCounsels/create", legalCounsel);
    }

    /**
     * updateLegalCounsel()
     */
    public updateLegalCounsel(legalCounsel: any): Observable<any> {
        return this._http.put("enquiry/api/legalCounsels/update", legalCounsel);
    }

    /**
     * deleteLegalCounsel()
     */
    public deleteLegalCounsel(legalCounselId: string) {
        return this._http.delete("enquiry/api/legalCounsels/delete/" + legalCounselId);
    }

    /**
     * getLegalCounselReports()
     */
    public getLegalCounselReports(legalCounselId: string): Observable<any> {
        return this._http.get("enquiry/api/legalCounselReports/search/findByLegalCounselIdAndDeleteFlag?legalCounselId=" + 
                legalCounselId + '&deleteFlag=false');
    }

    /**
     * createLegalCounselReport()
     */
    public createLegalCounselReport(legalCounselReport: any): Observable<any> {
        return this._http.post("enquiry/api/legalCounselReports/create", legalCounselReport);
    }

    /**
     * updateLegalCounselReport()
     */
    public updateLegalCounselReport(legalCounselReport: any): Observable<any> {
        return this._http.put("enquiry/api/legalCounselReports/update", legalCounselReport);
    }

    /**
     * deleteLegalCounselReport()
     */
    public deleteLegalCounselReport(legalCounselReportId: string) {
        return this._http.delete("enquiry/api/legalCounselReports/delete/" + legalCounselReportId);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }
}
