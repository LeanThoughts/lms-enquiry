import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IccInprincipleApprovalService implements Resolve<any> {

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
            iccFurtherDetails: this.getIccFurtherDetails(route.params['iccApprovalId'])
        });
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
}
