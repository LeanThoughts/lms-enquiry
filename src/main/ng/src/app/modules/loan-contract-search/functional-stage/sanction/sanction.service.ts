import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap, tap } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SanctionService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    private sanctionTypes: any[] = [];
    private feeTypes: any[] = [];
    private customerRejectionReasons: any[] = [];

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            approvalByBoards: this.getApprovalByBoards(route.params['loanApplicationId']),
            documentType: this.getDocumentTypes(),
            type: this.getSanctionTypes(),
            feeType: this.getFeeTypes(),
            rejectionCategory: this.getCustomerRejectionReasons()
        }).pipe(
            tap(({ type, feeType, rejectionCategory }) => {
                this.sanctionTypes = type;
                this.feeTypes = feeType;
                this.customerRejectionReasons = rejectionCategory;
            }),
            map((data: any) => ({
                ...data,
                // Earliest board approval meeting. Sanction letters cannot be issued before it.
                approvalByBoard: [...data.approvalByBoards].sort((a: any, b: any) => 
                    String(a.meetingDate).localeCompare(String(b.meetingDate)))[0] ?? null,
                // Rejected by customer stores the meeting number as a string
                approvalByBoardMeetingNumber: data.approvalByBoards.map((approvalByBoard: any) => 
                    ({ meetingNumber: String(approvalByBoard.meetingNumber) }))
            }))
        );
    }

    /**
     * Get the board approval meetings that approved the loan application
     */
    getApprovalByBoards(loanApplicationId: string): Observable<any[]> {
        return this.getBoardApproval(loanApplicationId).pipe(
            switchMap((boardApproval: any) => this.http.get<any>(environment.primaryApiHost + 
                '/approvalByBoards/search/findByBoardApprovalId?boardApprovalId=' + boardApproval.id)),
            map((response: any) => response?._embedded?.approvalByBoards ?? []),
            catchError(() => of([]))
        );
    }

    /**
     * Get the board approval of the loan application
     */
    getBoardApproval(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/boardApprovals/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get document types
     */
    getDocumentTypes(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/documentTypes');
    }

    /**
     * Get sanction types
     */
    getSanctionTypes(): Observable<any[]> {
        return this.http.get<any>(environment.primaryApiHost + '/sanctionTypes?size=100&sort=code').pipe(
            map((response: any) => response?._embedded?.sanctionTypes ?? [])
        );
    }

    /**
     * Get fee types
     */
    getFeeTypes(): Observable<any[]> {
        return this.http.get<any>(environment.primaryApiHost + '/feeTypes?size=100&sort=code').pipe(
            map((response: any) => response?._embedded?.feeTypes ?? [])
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
     * Upload vault document
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/upload', file);
    }

    /**
     * Get Sanction
     */
    public getSanction(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/sanctions/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get Reasons For Delay
     */
    public getReasonsForDelay(sanctionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/sanctionReasonForDelays/search/findBySanctionId?sanctionId=' + sanctionId).pipe(
            map((response: any) => response?._embedded?.sanctionReasonForDelays ?? [])
        );
    }

    /**
     * Create Reason For Delay
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/sanctionReasonForDelays/create', reasonForDelay);
    }

    /**
     * Update Reason For Delay
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/sanctionReasonForDelays/update', reasonForDelay);
    }

    /**
     * Get Payment Receipts - Pre Sanction
     */
    public getPaymentReceiptsPreSanction(sanctionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/paymentReceiptPreSanctions/search/findBySanctionId?sanctionId=' + sanctionId).pipe(
            map((response: any) => this.withFeeTypeDescription(response?._embedded?.paymentReceiptPreSanctions ?? []))
        );
    }

    /**
     * Create Payment Receipt - Pre Sanction
     */
    public createPaymentReceiptPreSanction(paymentReceipt: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/paymentReceiptPreSanctions/create', paymentReceipt);
    }

    /**
     * Update Payment Receipt - Pre Sanction
     */
    public updatePaymentReceiptPreSanction(paymentReceipt: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/paymentReceiptPreSanctions/update', paymentReceipt);
    }

    /**
     * Get Payment Receipts - Post Sanction
     */
    public getPaymentReceiptsPostSanction(sanctionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/paymentReceiptPostSanctions/search/findBySanctionId?sanctionId=' + sanctionId).pipe(
            map((response: any) => this.withFeeTypeDescription(response?._embedded?.paymentReceiptPostSanctions ?? []))
        );
    }

    /**
     * Create Payment Receipt - Post Sanction
     */
    public createPaymentReceiptPostSanction(paymentReceipt: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/paymentReceiptPostSanctions/create', paymentReceipt);
    }

    /**
     * Update Payment Receipt - Post Sanction
     */
    public updatePaymentReceiptPostSanction(paymentReceipt: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/paymentReceiptPostSanctions/update', paymentReceipt);
    }

    /**
     * Get Sanction Letters
     */
    public getSanctionLetters(sanctionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/sanctionLetters/search/findBySanctionId?sanctionId=' + sanctionId).pipe(
            map((response: any) => (response?._embedded?.sanctionLetters ?? []).map((sanctionLetter: any) => ({
                ...sanctionLetter,
                typeDescription: this.sanctionTypes.find((sanctionType: any) => sanctionType.code === sanctionLetter.type)?.value
            })))
        );
    }

    /**
     * Create Sanction Letter
     */
    public createSanctionLetter(sanctionLetter: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/sanctionLetters/create', sanctionLetter);
    }

    /**
     * Update Sanction Letter
     */
    public updateSanctionLetter(sanctionLetter: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/sanctionLetters/update', sanctionLetter);
    }

    /**
     * Get Rejected By Customer
     */
    public getRejectedByCustomers(sanctionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/sanctionRejectedByCustomers/search/findBySanctionId?sanctionId=' + sanctionId).pipe(
            map((response: any) => (response?._embedded?.sanctionRejectedByCustomers ?? []).map((rejectedByCustomer: any) => ({
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
        return this.http.post(environment.primaryApiHost + '/sanctionRejectedByCustomers/create', rejectedByCustomer);
    }

    /**
     * Update Rejected By Customer
     */
    public updateRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/sanctionRejectedByCustomers/update', rejectedByCustomer);
    }

    /**
     * Send Sanction for approval
     */
    public sendSanctionForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        const requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Sanction'
        };
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }

    /**
     * Add the fee type description to payment receipts
     */
    private withFeeTypeDescription(paymentReceipts: any[]): any[] {
        return paymentReceipts.map((paymentReceipt: any) => ({
            ...paymentReceipt,
            feeTypeDescription: this.feeTypes.find((feeType: any) => feeType.code === paymentReceipt.feeType)?.value
        }));
    }
}
