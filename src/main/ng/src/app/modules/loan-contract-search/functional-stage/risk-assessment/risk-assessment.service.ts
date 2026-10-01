import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { IccInprincipleApprovalService } from '../icc-inprinciple-approval/icc-inprinciple-approval.service';

@Injectable({
  providedIn: 'root'
})
export class RiskAssessmentService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    /**
     * Constructor
     */
    constructor(private http: HttpClient, private iccInprincipleApprovalService: IccInprincipleApprovalService) {
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            approvalByIcc: this.getApprovalByIcc(route.params['loanApplicationId']),
            documentType: this.getDocumentTypes()
        });
    }

    /**
     * Get Approval By ICC of the loan application. Used as the minimum date of assessment.
     */
    getApprovalByIcc(loanApplicationId: string): Observable<any> {
        return this.iccInprincipleApprovalService.getIccInprincipleApproval(loanApplicationId).pipe(
            switchMap((iccApproval: any) => this.iccInprincipleApprovalService.getApprovalByIcc(iccApproval.id)),
            map((approvalsByIcc: any[]) => approvalsByIcc?.[0] ?? null),
            catchError(() => of(null))
        );
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
     * Get Risk Assessment
     */
    public getRiskAssessment(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/riskAssessments/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }

    /**
     * Get Preliminary Risk Assessment
     */
    public getPreliminaryRiskAssessment(riskAssessmentId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/preliminaryRiskAssessments/search/findByRiskAssessmentId?riskAssessmentId=' + riskAssessmentId).pipe(
            map((response: any) => [response]),
            catchError((error) => of(null))
        );
    }

    /**
     * Create Preliminary Risk Assessment
     */
    public createPreliminaryRiskAssessment(preliminaryRiskAssessment: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/preliminaryRiskAssessments/create', preliminaryRiskAssessment);
    }

    /**
     * Update Preliminary Risk Assessment
     */
    public updatePreliminaryRiskAssessment(preliminaryRiskAssessment: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/preliminaryRiskAssessments/update', preliminaryRiskAssessment);
    }

    /**
     * Send Prelim Risk Assessment for approval
     */
    public sendRiskAssessmentForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        const requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Prelim Risk Assessment'
        };
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }
}
