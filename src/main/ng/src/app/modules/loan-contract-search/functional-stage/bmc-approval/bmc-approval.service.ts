import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BmcApprovalService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

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
            projectAppraisalCompletion: this.getProjectAppraisalCompletion(route.params['loanApplicationId']),
            documentTypeMinutes: this.getDocumentTypes(),
            documentTypeMailFromCS: this.getDocumentTypes()
        });
    }

    /**
     * Upload vault document
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/upload', file);
    }

    /**
     * Get document types
     */
    getDocumentTypes(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/documentTypes');
    }

    /**
     * Get the loan appraisal of the loan application
     */
    public getLoanAppraisal(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/loanAppraisals/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get the project appraisal completion of the loan application. BMC clearance cannot be given before the agenda note
     * is approved by the MD and CEO.
     */
    getProjectAppraisalCompletion(loanApplicationId: string): Observable<any> {
        return this.getLoanAppraisal(loanApplicationId).pipe(
            switchMap((loanAppraisal: any) => this.http.get<any>(environment.primaryApiHost +
                '/projectAppraisalCompletions/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisal.id)),
            catchError(() => of(null))
        );
    }

    /**
     * Get BMC ICC Approval
     */
    public getBmcIccApproval(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcIccApprovals/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Send BMC ICC Approval for approval
     */
    public sendBmcIccApprovalForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        const requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'BMCApproval'
        };
        return this.http.post<any>(environment.primaryApiHost + '/bmcIccApprovals/sendForApproval', requestObj);
    }

    /**
     * Get Further Details
     */
    public getFurtherDetails(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcIccFurtherDetails/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => response._embedded.bmcIccFurtherDetails),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Further Detail
     */
    public createFurtherDetail(furtherDetail: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcIccFurtherDetails/create', furtherDetail);
    }

    /**
     * Update Further Detail
     */
    public updateFurtherDetail(furtherDetail: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcIccFurtherDetails/update', furtherDetail);
    }

    /**
     * Delete Further Detail
     */
    public deleteFurtherDetail(furtherDetailId: string): Observable<any> {
        return this.http.delete(environment.primaryApiHost + '/bmcIccFurtherDetails/delete/' + furtherDetailId);
    }

    /**
     * Get Reasons For Delay
     */
    public getReasonsForDelay(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcIccReasonForDelays/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => response._embedded.bmcIccReasonForDelays),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Reason For Delay
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcIccReasonForDelays/create', reasonForDelay);
    }

    /**
     * Update Reason For Delay
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcIccReasonForDelays/update', reasonForDelay);
    }

    /**
     * Get Rejected By ICC
     */
    public getRejectedByIcc(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcRejectedByIccs/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => [response]),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Rejected By ICC
     */
    public createRejectedByIcc(rejectedByIcc: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcRejectedByIccs/create', rejectedByIcc);
    }

    /**
     * Update Rejected By ICC
     */
    public updateRejectedByIcc(rejectedByIcc: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcRejectedByIccs/update', rejectedByIcc);
    }

    /**
     * Get Approval By ICC
     */
    public getApprovalByIcc(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcApprovalByIccs/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => [response]),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Approval By ICC
     */
    public createApprovalByIcc(approvalByIcc: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcApprovalByIccs/create', approvalByIcc);
    }

    /**
     * Update Approval By ICC
     */
    public updateApprovalByIcc(approvalByIcc: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcApprovalByIccs/update', approvalByIcc);
    }

    /**
     * Get Rejected By Customer
     */
    public getRejectedByCustomer(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcRejectedByCustomers/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => [response]),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Rejected By Customer
     */
    public createRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcRejectedByCustomers/create', rejectedByCustomer);
    }

    /**
     * Update Rejected By Customer
     */
    public updateRejectedByCustomer(rejectedByCustomer: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcRejectedByCustomers/update', rejectedByCustomer);
    }

    /**
     * Get Loan Enhancements
     */
    public getLoanEnhancements(bmcIccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/bmcLoanEnhancements/search/findByBmcICCApprovalId?iccApprovalId=' + bmcIccApprovalId).pipe(
            map((response: any) => response._embedded.bmcLoanEnhancements),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Loan Enhancement
     */
    public createLoanEnhancement(loanEnhancement: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/bmcLoanEnhancements/create', loanEnhancement);
    }

    /**
     * Update Loan Enhancement
     */
    public updateLoanEnhancement(loanEnhancement: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/bmcLoanEnhancements/update', loanEnhancement);
    }

    /**
     * Delete Loan Enhancement
     */
    public deleteLoanEnhancement(loanEnhancementId: string): Observable<any> {
        return this.http.delete(environment.primaryApiHost + '/bmcLoanEnhancements/delete/' + loanEnhancementId);
    }
}
