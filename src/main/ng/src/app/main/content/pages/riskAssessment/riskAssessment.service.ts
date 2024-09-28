import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class RiskAssessmentService implements Resolve<any> {

    _riskAssessment: BehaviorSubject<any> = new BehaviorSubject({});
        
    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient) {
    }

    /**
     * resolve()
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        console.log('in resolve :: riskAssessment id is ', this._riskAssessment.value.id);
        return forkJoin([
        ]);
    }
    
    /**
     * getICCApproval()
     */
    public getRiskAssessment(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/riskAssessments/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }
    
    /**
     * getPreliminaryRiskAssessment()
     */
    getPreliminaryRiskAssessment(id: any) {
        return this._http.get("enquiry/api/preliminaryRiskAssessments/search/findByRiskAssessmentId?riskAssessmentId=" + id);
    }

    updatePreliminaryRiskAssessment(riskAssessment: any) {
        return this._http.put("enquiry/api/preliminaryRiskAssessments/update", riskAssessment);
    }

    createPreliminaryRiskAssessment(riskAssessment: any) {
        return this._http.post("enquiry/api/preliminaryRiskAssessments/create", riskAssessment);
    }

    /**
     * sendAppraisalForApproval()
     */
    public sendRiskAssessmentForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Prelim Risk Assessment'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}
