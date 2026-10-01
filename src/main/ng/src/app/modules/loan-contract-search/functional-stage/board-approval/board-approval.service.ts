import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap, tap } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BoardApprovalService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    private customerRejectionReasons: any[] = [];

    // Shared with the route resolved data so the rejected by customer dialog sees newly added board approval meetings
    private approvalByBoardMeetingNumbers: any[] = [];

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const loanApplicationId = route.params['loanApplicationId'];
        const boardApprovalId = route.params['boardApprovalId'];
        this.approvalByBoardMeetingNumbers = [];
        return forkJoin({
            projectAppraisalCompletion: this.getProjectAppraisalCompletion(loanApplicationId),
            rejectionCategory: this.getCustomerRejectionReasons(),
            approvalByBoards: boardApprovalId ? this.getApprovalByBoards(boardApprovalId) : of([])
        }).pipe(
            tap(({ rejectionCategory }) => this.customerRejectionReasons = rejectionCategory),
            map(({ projectAppraisalCompletion, rejectionCategory }) => ({
                projectAppraisalCompletion,
                rejectionCategory,
                approvalByBoardMeetingNumber: this.approvalByBoardMeetingNumbers
            }))
        );
    }

    /**
     * Get the project appraisal completion of the loan application. Board meetings cannot be held before the agenda note
     * is approved by the MD and CEO.
     */
    getProjectAppraisalCompletion(loanApplicationId: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/loanAppraisals/search/findByLoanApplicationId?loanApplicationId=' + 
                loanApplicationId).pipe(
            switchMap((loanAppraisal: any) => this.http.get<any>(environment.primaryApiHost + 
                '/projectAppraisalCompletions/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisal.id)),
            catchError(() => of(null))
        );
    }

    /**
     * Get customer rejection reasons
     */
    getCustomerRejectionReasons(): Observable<any[]> {
        return this.http.get<any>(environment.primaryApiHost + '/customerRejectionReasons').pipe(
            map((response: any) => response?._embedded?.customerRejectionReasons ?? [])
        );
    }

    /**
     * Get Board Approval
     */
    public getBoardApproval(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/boardApprovals/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get Deferred By Boards
     */
    public getDeferredByBoards(boardApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/deferredByBoards/search/findByBoardApprovalId?boardApprovalId=' + boardApprovalId).pipe(
            map((response: any) => response?._embedded?.deferredByBoards ?? [])
        );
    }

    /**
     * Create Deferred By Board
     */
    public createDeferredByBoard(deferredByBoard: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/deferredByBoards/create', deferredByBoard);
    }

    /**
     * Update Deferred By Board
     */
    public updateDeferredByBoard(deferredByBoard: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/deferredByBoards/update', deferredByBoard);
    }

    /**
     * Get Reasons For Delay
     */
    public getReasonsForDelay(boardApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/boardApprovalReasonForDelays/search/findByBoardApprovalId?boardApprovalId=' + 
                boardApprovalId).pipe(
            map((response: any) => response?._embedded?.boardApprovalReasonForDelays ?? [])
        );
    }

    /**
     * Create Reason For Delay
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/boardApprovalReasonForDelays/create', reasonForDelay);
    }

    /**
     * Update Reason For Delay
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/boardApprovalReasonForDelays/update', reasonForDelay);
    }

    /**
     * Get Rejected By Boards
     */
    public getRejectedByBoards(boardApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/rejectedByBoards/search/findByBoardApprovalId?boardApprovalId=' + boardApprovalId).pipe(
            map((response: any) => response?._embedded?.rejectedByBoards ?? [])
        );
    }

    /**
     * Create Rejected By Board
     */
    public createRejectedByBoard(rejectedByBoard: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/rejectedByBoards/create', rejectedByBoard);
    }

    /**
     * Update Rejected By Board
     */
    public updateRejectedByBoard(rejectedByBoard: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/rejectedByBoards/update', rejectedByBoard);
    }

    /**
     * Get Approval By Boards. Also refreshes the meeting numbers offered in the rejected by customer dialog.
     */
    public getApprovalByBoards(boardApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/approvalByBoards/search/findByBoardApprovalId?boardApprovalId=' + boardApprovalId).pipe(
            map((response: any) => response?._embedded?.approvalByBoards ?? []),
            tap((approvalByBoards: any[]) => {
                // Rejected by customer stores the meeting number as a string
                const meetingNumbers = approvalByBoards.map((approvalByBoard: any) => ({ meetingNumber: String(approvalByBoard.meetingNumber) }));
                this.approvalByBoardMeetingNumbers.splice(0, this.approvalByBoardMeetingNumbers.length, ...meetingNumbers);
            })
        );
    }

    /**
     * Create Approval By Board
     */
    public createApprovalByBoard(approvalByBoard: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/approvalByBoards/create', approvalByBoard);
    }

    /**
     * Update Approval By Board
     */
    public updateApprovalByBoard(approvalByBoard: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/approvalByBoards/update', approvalByBoard);
    }

    /**
     * Get Rejected By Customers
     */
    public getRejectedByCustomers(boardApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/boardApprovalRejectedByCustomers/search/findByBoardApprovalId?boardApprovalId=' + 
                boardApprovalId).pipe(
            map((response: any) => (response?._embedded?.boardApprovalRejectedByCustomers ?? []).map((rejectedByCustomer: any) => ({
                ...rejectedByCustomer,
                rejectionCategoryDescription: this.customerRejectionReasons.find((reason: any) => 
                    reason.code === rejectedByCustomer.rejectionCategory)?.value
            })))
        );
    }

    /**
     * Create Rejected By Customer
     */
    public createRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/boardApprovalRejectedByCustomers/create', rejectedByCustomer);
    }

    /**
     * Update Rejected By Customer
     */
    public updateRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/boardApprovalRejectedByCustomers/update', rejectedByCustomer);
    }

    /**
     * Send Board Approval for approval
     */
    public sendBoardApprovalForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        const requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'BoardApproval'
        };
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }
}
