import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { ProcessEnquiryService } from '../process-enquiry/process-enquiry.service';

@Injectable({
  providedIn: 'root'
})
export class IccInprincipleApprovalService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    /**
     * Constructor
     */
    constructor(private http: HttpClient, private processEnquiryService: ProcessEnquiryService) {         
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            iccFurtherDetails: this.getIccFurtherDetails(route.params['iccInprincipleApprovalId']),
            enquiryCompletion: this.getEnquiryCompletionDetails(route.params['loanApplicationId']).pipe(
                map((enquiryCompletions: any[]) => enquiryCompletions?.[0] ?? null),
                catchError(() => of(null))
            ),
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
     * Get Enquiry Completion Details
     */
    getEnquiryCompletionDetails(loanApplicationId: string): Observable<any> {
        return this.processEnquiryService.getEnquiryAction(loanApplicationId).pipe(
            switchMap((response: any) => this.processEnquiryService.getEnquiryCompletionDetails(response.id))
        );
    }

    /**
     * Get ICC In-principle Approval
     */
    public getIccInprincipleApproval(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/iCCApprovals/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get ICC Further Details
     */
    public getIccFurtherDetails(iccApprovalId: string): Observable<any> {
        console.log('getIccFurtherDetails is called for iccApprovalId', iccApprovalId);
        return this.http.get(environment.primaryApiHost + '/iCCFurtherDetails/search/findByIccApprovalId?iccApprovalId=' + iccApprovalId).pipe(
            map((response: any) => response._embedded.iCCFurtherDetails),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Further Detail
     */
    public createFurtherDetail(furtherDetail: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/iCCFurtherDetails/create', furtherDetail);
    }

    /**
     * Update Further Detail
     */
    public updateFurtherDetail(furtherDetail: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/iCCFurtherDetails/update', furtherDetail);
    }
        
    /**
     * Delete Further Detail
     */
    public deleteFurtherDetail(furtherDetailId: string): Observable<any> {
        return this.http.delete(environment.primaryApiHost + '/iCCFurtherDetails/delete/' + furtherDetailId);
    }

    /**
     * Get Reason For Delay
     */
    public getReasonsForDelay(iccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/iCCReasonForDelays/search/findByIccApprovalId?iccApprovalId=' + iccApprovalId).pipe(
            map((response: any) => response._embedded.iCCReasonForDelays),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Reason For Delay
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/iCCReasonForDelays/create', reasonForDelay);
    }

    /**
     * Update Reason For Delay
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/iCCReasonForDelays/update', reasonForDelay);
    }
    
    /**
     * Get Rejected By ICC
     */
    public getRejectedByIcc(iccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/rejectedByICCs/search/findByIccApprovalId?iccApprovalId=' + iccApprovalId).pipe(
            map((response: any) => [response]), // Convert to array to match the expected format
            catchError((error) => of(null))
        );
    }

    /**
     * Create Rejected By ICC
     */
    public createRejectedByIcc(rejectedByICC: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/rejectedByICCs/create', rejectedByICC);
    }

    /**
     * Update Rejected By ICC
     */
    public updateRejectedByIcc(rejectedByICC: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/rejectedByICCs/update', rejectedByICC);
    }

    /**
     * Get Approval By ICC
     */
    public getApprovalByIcc(iccApprovalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/approvalByICCs/search/findByIccApprovalId?iccApprovalId=' + iccApprovalId).pipe(
            map((response: any) => [response]),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Approval By ICC
     */
    public createApprovalByIcc(approvalByICC: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/approvalByICCs/create', approvalByICC);
    }

    /**
     * Update Approval By ICC
     */
    public updateApprovalByIcc(approvalByICC: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/approvalByICCs/update', approvalByICC);
    }
}