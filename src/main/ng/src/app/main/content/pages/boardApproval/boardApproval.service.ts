import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class BoardApprovalService {

    _boardApproval: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * getBoardApproval()
     */
    public getBoardApproval(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/boardApprovals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getReasonForDelays()
     */
    public getReasonForDelays(): Observable<any> {
        return this._http.get("enquiry/api/boardApprovalReasonForDelays/search/findByBoardApprovalId?boardApprovalId=" + 
            this._boardApproval.value.id);
    }

    /**
     * createBoardApprovalReasonForDelay()
     */
    public createBoardApprovalReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/boardApprovalReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateBoardApprovalReasonForDelay()
     */
    public updateBoardApprovalReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/boardApprovalReasonForDelays/update", reasonForDelay);
    }

    /**
     * deleteBoardApprovalReasonForDelay()
     */
    public deleteBoardApprovalReasonForDelay(boardApprovalReasonForDelayId: string) {
        return this._http.delete("enquiry/api/boardApprovalReasonForDelays/delete/" + boardApprovalReasonForDelayId);
    }
}
