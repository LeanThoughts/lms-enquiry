import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProcessEnquiryService implements Resolve<any> {

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
            rejectionCategory: this.getRejectionCategoryValues(),
            rating: this.getCreditRatingCodes(),
            creditStanding: this.getCreditRatingAgencies(),
            productType: this.getProductTypes(),
            term: this.getTerms()
        });
    }

    /**
     * Get Enquiry Action by Loan Application Id
     */
    public getEnquiryAction(loanApplicationId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/enquiryActions/search/findByLoanApplicationId?loanApplicationId=' + loanApplicationId);
    }
    
    /**
     * Get Enquiry Action by Enquiry Action Id
     */
    public getEnquiryActionByEnquiryActionId(enquiryActionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/enquiryActions/' + enquiryActionId);
    }

    /**
     * Get PFS Rejection Category Values
     */
    getRejectionCategoryValues(): Observable<any> {
        return new Observable((observer) => {
            let values = [
                {code: '1', value: 'Rejected by Borrower'},
                {code: '2', value: 'Rejected by BD'},
                {code: '3', value: 'Rejected by ICC'},
                {code: '4', value: 'Rejected by Appraisal'},
                {code: '5', value: 'Rejected by Board'},
            ];
            observer.next(values);
            observer.complete();
        });
    }

    /**
     * Get Credit Rating Codes
     */
    public getCreditRatingCodes(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/creditRatingCodes');
    }

    /**
     * Get Credit Rating Agencies
     */
    public getCreditRatingAgencies(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/creditRatingAgencies');
    }
    
    /**
     * Get Product Types
     */
    public getProductTypes(): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/products?sort=code').subscribe({
                next: (response: any) => {
                    observer.next(response._embedded.products);
                    observer.complete();
                },
                error: (error: any) => {
                    observer.next([]);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Get Terms
     */
    public getTerms(): Observable<any> {
        return new Observable((observer) => {
            let values = [
                {code: '1', value: 'Short Term (Less than 1 Year)'},
                {code: '2', value: 'Medium Term (Between 1 and 5 years)'},
                {code: '3', value: 'Long Term(Greater than 5 years)'},
            ];
            observer.next(values);
            observer.complete();
        });
    }

    /**
     * Get Rejected By PFS
     */
    public getRejectedByPFS(enquiryActionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/rejectByPfses/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).pipe(
            map((response: any) => {
                console.log('response is', response);
                if (response.rejectionCategory === '2') {
                    response['categoryDescription'] = 'Rejected by BD';
                } else if (response.rejectionCategory === '3') {
                    response['categoryDescription'] = 'Rejected by ICC';
                } else if (response.rejectionCategory === '4') {
                    response['categoryDescription'] = 'Rejected by Appraisal';
                } else if (response.rejectionCategory === '5') {
                    response['categoryDescription'] = 'Rejected by Board';
                }
                return [response];
            }),
            catchError((error) => {
                console.error('Error getting rejected by PFS', error);
                return of([]);
            })
        );
    }
    
    /**
     * Create Rejected By PFS
     */
    public createRejectedByPFS(rejectByPFS: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/rejectByPfses/create', rejectByPFS);
    }

    /**
     * Update Rejected By PFS
     */
    public updateRejectedByPFS(rejectByPFS: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/rejectByPfses/update', rejectByPFS);
    }

    /**
     * Get Other Details
     */
    public getOtherDetails(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/otherDetails/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).subscribe({
                next: (response: any) => {
                    observer.next([response]);
                    observer.complete();
                },
                error: (error: any) => {
                    observer.next([]);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Create Other Details
     */
    public createOtherDetails(otherDetails: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/otherDetails/create', otherDetails);
    }

    /**
     * Update Other Details
     */
    public updateOtherDetails(otherDetails: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/otherDetails/update', otherDetails);
    }

    /**
     * Get Reason For Delay
     */
    public getReasonForDelay(enquiryActionId: string): Observable<any> {
        console.log('In getReasonForDelay');
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/enquiryActionReasonForDelays/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId)
                .subscribe({
                    next: (response: any) => {
                        observer.next([response]);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
        });
    }

    /**
     * Create Reason For Delay
     */
    public createReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/enquiryActionReasonForDelays/create', reasonForDelay);
    }

    /**
     * Update Reason For Delay
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/enquiryActionReasonForDelays/update', reasonForDelay);
    }

    /**
     * Get Enquiry Completion Details
     */
    public getEnquiryCompletionDetails(enquiryActionId: string): Observable<any> {
        return forkJoin({
            productTypes: this.getProductTypes(),
            terms: this.getTerms(),
            response: this.http.get<any>(environment.primaryApiHost + '/enquiryCompletions/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId)
        }).pipe(
            map(({ productTypes, terms, response }) => {
                response['productTypeDescription'] = productTypes.find((productType: any) => productType.code === response.productType)?.name;
                if (response.term) {
                    response['termDescription'] = terms.find((term: any) => term.code === response.term)?.value;
                }
                return [response];
            })
        );
    }

    /**
     * Create Enquiry Completion Details
     */
    public createEnquiryCompletionDetails(enquiryCompletion: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/enquiryCompletions/create', enquiryCompletion);
    }

    /**
     * Update Enquiry Completion Details
     */
    public updateEnquiryCompletionDetails(enquiryCompletion: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/enquiryCompletions/update', enquiryCompletion);
    }

    /**
     * Get Rejected By Customer
     */
    public getRejectedByCustomer(enquiryActionId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/rejectByCustomers/search/findByEnquiryActionId?enquiryActionId=' + enquiryActionId).pipe(
                map((response: any) => {
                    response['rejectionCategory'] = '1';
                    response['categoryDescription'] = 'Rejected by Borrower';
                    return [response];
                }),
                catchError((error) => {
                    console.error('Error getting rejected by Customer', error);
                    return of([]);
                })
            );
        }
        
    /**
     * Create Rejected By Customer
     */
    public createRejectedByCustomer(rejectByCustomer: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/rejectByCustomers/create', rejectByCustomer);
    }

    /**
     * Update Rejected By Customer
     */
    public updateRejectedByCustomer(rejectByCustomer: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/rejectByCustomers/update', rejectByCustomer);
    }
    
    /**
     * Get Project Proposals
     */
    public getProjectProposals(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/projectProposals/search/findByEnquiryActionIdOrderBySerialNumber?enquiryActionId=' + enquiryActionId)
            .subscribe({
                next: (response: any) => {
                    observer.next(response._embedded.projectProposals);
                    observer.complete();
                },
                error: (error: any) => {
                    observer.next([]);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Get project proposal by status
     */
    public getProjectProposalByStatus(enquiryActionId: string, proposalStatus: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/projectProposals/search/findByEnquiryActionIdAndProposalStatus?enquiryActionId=' + enquiryActionId
                + '&proposalStatus=' + proposalStatus);
    }

    /**
     * Send enquiry action for approval
     */
    public sendEnquiryActionForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'EnquiryAction'
        }
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }
}