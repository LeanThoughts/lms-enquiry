import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable()
export class ReferenceInterestValueService implements Resolve<any> {

    referenceInterestRateType: string = ''; // This is the reference interest rate type received from the inbox component.

    /**
     * Constructor
     */
    constructor(private _http: HttpClient) {
    }

    /**
     * Route resolver
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return this.getReferenceInterestRateTypes();
    }

    /**
     * getReferenceInterestRateTypes()
     */
    public getReferenceInterestRateTypes(): Observable<any> {
        return this._http.get('enquiry/api/refinterestratetypes');
    }

    /**
     * getReferenceInterestRateValues()
     */
    public getReferenceInterestRateValues(referenceRateTypeId: number): Observable<any> {
        return this._http.get('enquiry/api/referenceInterestRateValues/referenceInterestRateType/' + referenceRateTypeId);
    }

    /**
     * saveReferenceInterestValue()
     */
    public saveReferenceInterestValue(referenceInterestValue: any): Observable<any> {
        return this._http.post('enquiry/api/referenceInterestRateValues/create', referenceInterestValue);
    }

    /**
     * updateReferenceInterestValue()
     */
    public updateReferenceInterestValue(referenceInterestValue: any): Observable<any> {
        return this._http.put('enquiry/api/referenceInterestRateValues/update', referenceInterestValue);
    }

    /**
     * deleteReferenceInterestValue()
     */
    public deleteReferenceInterestValue(referenceInterestValueId: any): Observable<any> {
        return this._http.delete('enquiry/api/referenceInterestRateValues/delete/' + referenceInterestValueId);
    }

    /**
     * sendReferenceInterestValueForApproval()
     */
    public sendReferenceInterestValueForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'ReferenceInterestRateValue'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }


}
