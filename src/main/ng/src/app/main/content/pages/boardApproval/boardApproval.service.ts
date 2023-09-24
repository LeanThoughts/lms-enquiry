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

    /**
     * getDeferredByBoards()
     */
    public getDeferredByBoards(): Observable<any> {
        return this._http.get("enquiry/api/deferredByBoards/search/findByBoardApprovalId?boardApprovalId=" + 
            this._boardApproval.value.id);
    }

    /**
     * createDeferredByBoard()
     */
    public createDeferredByBoard(deferredByBoard: any): Observable<any> {
        return this._http.post("enquiry/api/deferredByBoards/create", deferredByBoard);
    }

    /**
     * updateDeferredByBoard()
     */
    public updateDeferredByBoard(deferredByBoard: any): Observable<any> {
        return this._http.put("enquiry/api/deferredByBoards/update", deferredByBoard);
    }

    /**
     * deleteDeferredByBoard()
     */
    public deleteDeferredByBoard(deferredByBoardId: string) {
        return this._http.delete("enquiry/api/deferredByBoards/delete/" + deferredByBoardId);
    }

    /**
     * getRejectedByBoards()
     */
    public getRejectedByBoards(): Observable<any> {
        return this._http.get("enquiry/api/rejectedByBoards/search/findByBoardApprovalId?boardApprovalId=" + 
            this._boardApproval.value.id);
    }

    /**
     * createRejectedByBoard()
     */
    public createRejectedByBoard(rejectedByBoard: any): Observable<any> {
        return this._http.post("enquiry/api/rejectedByBoards/create", rejectedByBoard);
    }

    /**
     * updateRejectedByBoard()
     */
    public updateRejectedByBoard(rejectedByBoard: any): Observable<any> {
        return this._http.put("enquiry/api/rejectedByBoards/update", rejectedByBoard);
    }

    /**
     * deleteRejectedByBoard()
     */
    public deleteRejectedByBoard(rejectedByBoardId: string) {
        return this._http.delete("enquiry/api/rejectedByBoards/delete/" + rejectedByBoardId);
    }

    /**
     * getApprovalByBoards()
     */
    public getApprovalByBoards(): Observable<any> {
        return this._http.get("enquiry/api/approvalByBoards/search/findByBoardApprovalId?boardApprovalId=" + 
            this._boardApproval.value.id);
    }

    /**
     * createApprovalByBoard()
     */
    public createApprovalByBoard(approvalByBoard: any): Observable<any> {
        return this._http.post("enquiry/api/approvalByBoards/create", approvalByBoard);
    }

    /**
     * updateApprovalByBoard()
     */
    public updateApprovalByBoard(approvalByBoard: any): Observable<any> {
        return this._http.put("enquiry/api/approvalByBoards/update", approvalByBoard);
    }

    /**
     * deleteApprovalByBoard()
     */
    public deleteApprovalByBoard(approvalByBoardId: string) {
        return this._http.delete("enquiry/api/approvalByBoards/delete/" + approvalByBoardId);
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
        return this._http.get("enquiry/api/boardApprovalRejectedByCustomers/search/findByBoardApprovalId?boardApprovalId=" + 
            this._boardApproval.value.id);
    }

    /**
     * createRejectedByCustomer()
     */
    public createRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.post("enquiry/api/boardApprovalRejectedByCustomers/create", rejectedByCustomer);
    }

    /**
     * updateRejectedByCustomer()
     */
    public updateRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this._http.put("enquiry/api/boardApprovalRejectedByCustomers/update", rejectedByCustomer);
    }

    /**
     * deleteRejectedByCustomer()
     */
    public deleteRejectedByCustomer(rejectedByCustomerId: string) {
        return this._http.delete("enquiry/api/boardApprovalRejectedByCustomers/delete/" + rejectedByCustomerId);
    }

    /**
     * sendBoardApprovalForWorkflowApproval()
     */
    public sendBoardApprovalForWorkflowApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'BoardApproval'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}
