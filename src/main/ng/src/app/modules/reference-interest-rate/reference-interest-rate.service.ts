import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { forkJoin, map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const MODIFICATION_STATUS_DESCRIPTIONS: Record<number, string> = {
    0: 'Not Changed',
    1: 'Changed',
    2: 'Marked for Deletion'
};

@Injectable({
  providedIn: 'root'
})
export class ReferenceInterestRateService implements Resolve<any> {

    referenceInterestRateTypeCode: string | null = null;

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
     * Get the reference interest values of a reference rate type, flattened for the generic list
     */
    public getReferenceInterestRates(referenceRateTypeId: number | string): Observable<any[]> {
        return this.http.get<any[]>(environment.primaryApiHost + '/referenceInterestRateValues/referenceInterestRateType/' + referenceRateTypeId)
            .pipe(map((values) => (values || []).map((value) => ({
                ...value,
                referenceInterestRateCode: value.referenceInterestRate?.code,
                referenceInterestRateDescription: value.referenceInterestRate?.description,
                modificationStatusDescription: MODIFICATION_STATUS_DESCRIPTIONS[value.modificationStatus] ?? ''
            }))));
    }

    /**
     * Save reference interest rate
     */
    public saveReferenceInterestRate(referenceInterestValue: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/referenceInterestRateValues/create', {
            referenceInterestRate: referenceInterestValue.referenceInterestRate,
            validFromDate: referenceInterestValue.validFromDate,
            interestRate: referenceInterestValue.interestRate
        });
    }

    /**
     * Update reference interest rate. Only the interest rate can be changed.
     */
    public updateReferenceInterestRate(referenceInterestValue: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/referenceInterestRateValues/update', {
            id: referenceInterestValue.id,
            referenceInterestRate: referenceInterestValue.referenceInterestRate?.id ?? referenceInterestValue.referenceInterestRate,
            validFromDate: referenceInterestValue.validFromDate,
            interestRate: referenceInterestValue.interestRate
        });
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
