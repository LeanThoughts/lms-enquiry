import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class EnquiryActionService implements Resolve<any> {

    _enquiryAction: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * resolve()
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        console.log('in resolve :: enquiryActionId is ', this._enquiryAction.value.id);
        return forkJoin([
            this.getReasonForDelay(this._enquiryAction.value.id),
            this.getRejectByPFS(this._enquiryAction.value.id),
            this.getRejectByCustomers(this._enquiryAction.value.id),
            this.getEnquiryCompletion(this._enquiryAction.value.id),
            this.getOtherDetails(this._enquiryAction.value.id)
        ]);
    }
    
    /**
     * getEnquiryAction()
     */
    public getEnquiryAction(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/enquiryActions/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getOtherDetails()
     */
    public getOtherDetails(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/otherDetails/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }

    /**
     * createOtherDetails()
     */
    public createOtherDetails(otherDetails: any): Observable<any> {
        return this._http.post("enquiry/api/otherDetails/create", otherDetails);
    }

    /**
     * updateOtherDetails()
     */
    public updateOtherDetails(otherDetails: any): Observable<any> {
        return this._http.put("enquiry/api/otherDetails/update", otherDetails);
    }

    /**
     * getEnquiryCompletion()
     */
    public getEnquiryCompletion(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/enquiryCompletions/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }

    /**
     * createEnquiryCompletion()
     */
    public createEnquiryCompletion(enquiryCompletion: any): Observable<any> {
        return this._http.post("enquiry/api/enquiryCompletions/create", enquiryCompletion);
    }

    /**
     * updateEnquiryCompletion()
     */
    public updateEnquiryCompletion(enquiryCompletion: any): Observable<any> {
        return this._http.put("enquiry/api/enquiryCompletions/update", enquiryCompletion);
    }

    /**
     * getReasonForDelay()
     */
    public getReasonForDelay(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/enquiryActionReasonForDelays/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }
    
    /**
     * createReasonForDelay()
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.post("enquiry/api/enquiryActionReasonForDelays/create", reasonForDelay);
    }

    /**
     * updateReasonForDelay()
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/enquiryActionReasonForDelays/update", reasonForDelay);
    }

    /**
     * getRejectByCustomers()
     */
    public getRejectByCustomers(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/rejectByCustomers/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }

    /**
     * createRejectByCustomers()
     */
    public createRejectByCustomers(rejectByCustomer: any): Observable<any> {
        return this._http.post("enquiry/api/rejectByCustomers/create", rejectByCustomer);
    }

    /**
     * updateRejectByCustomers()
     */
    public updateRejectByCustomers(rejectByCustomer: any): Observable<any> {
        return this._http.put("enquiry/api/rejectByCustomers/update", rejectByCustomer);
    }

    /**
     * getRejectByPFS()
     */
    public getRejectByPFS(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/rejectByPfses/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe(
                (response => {
                    observer.next(response);
                    observer.complete();
                }),
                (error => {
                    observer.next({});
                    observer.complete();
                })
            )
        });
    }
    
    /**
     * createRejectByPFS()
     */
    public createRejectByPFS(rejectByPFS: any): Observable<any> {
        return this._http.post("enquiry/api/rejectByPfses/create", rejectByPFS);
    }

    /**
     * updateRejectByPFS()
     */
    public updateRejectByPFS(rejectByPFS: any): Observable<any> {
        return this._http.put("enquiry/api/rejectByPfses/update", rejectByPFS);
    }
}
