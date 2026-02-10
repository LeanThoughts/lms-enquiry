import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReferenceInterestRateService implements Resolve<any> {

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
            referenceRateTypes: this.getReferenceRateTypes() // get reference rate types
        });
    }

    /**
     * Get reference rate types
     */
    getReferenceRateTypes(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/refinterestratetypes');
    }

    /**
     * Get reference interest rate
     */
    public getReferenceInterestRates(referenceRateTypeId: number): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/referenceInterestRateValues/referenceInterestRateType/' + referenceRateTypeId);
    }

    /**
     * Save reference interest rate
     */
    public saveReferenceInterestRate(referenceInterestValue: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/referenceInterestRateValues/create', referenceInterestValue);
    }

    /**
     * Update reference interest rate
     */
    public updateReferenceInterestRate(referenceInterestValue: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/referenceInterestRateValues/update', referenceInterestValue);
    }

    /**
     * Delete reference interest rate
     */
    public deleteReferenceInterestRate(referenceInterestValueId: any): Observable<any> {
        return this.http.delete(environment.primaryApiHost + '/referenceInterestRateValues/delete/' + referenceInterestValueId);
    }

    /**
     * Send reference interest value for approval
     */
    public sendReferenceInterestValueForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'ReferenceInterestRateValue'
        }
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }    
}
