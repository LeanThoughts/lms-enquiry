///<reference path="enquiryReview/enquiryReview.component.ts"/>
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoanApplicationModel } from '../../../model/loanApplication.model';
import { PartnerModel } from '../../../model/partner.model';
import { LoanApplicationResourceModel } from '../../../model/loanApplicationResource.model';
import { catchError } from 'rxjs/operators';
import { LoanEnquiryService } from '../enquiryApplication.service';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import {ApplicantEmail} from "./enquiryReview/enquiryReview.component";

@Injectable()
export class EnquiryAlertsService implements Resolve<any> {

    // loanApplications: BehaviorSubject<LoanApplicationResourceModel[]>;
    loanApplications: BehaviorSubject<EnquiryApplicationModel[]>;

    selectedLoanApplicationId: BehaviorSubject<string>;
    selectedLoanApplicationPartyNumber: BehaviorSubject<string>;


    constructor(private _http: HttpClient, private _loanEnquiryService: LoanEnquiryService) {
    }

    /**
     * resolve()
     * Router resolveer, fetches data before the ui is created.
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        if (route.routeConfig.path === 'enquiryReview') {
            if (this.selectedLoanApplicationId !== undefined) {
                return forkJoin([
                    this._loanEnquiryService.getLoanClasses(), // get loan classes.
                    this._loanEnquiryService.getFinancingTypes(), // get financing types.
                    this._loanEnquiryService.getProjectTypes(), // get project types.
                    this._loanEnquiryService.getStates(), // get states.
                    this._loanEnquiryService.getAssistanceTypes(), // get assistance types.

                    this.getLoanApplication(this.selectedLoanApplicationId.getValue()) ,// get loan application.

                    this._loanEnquiryService.getIndustrySectors(), // Get Industry Sectors
                    this._loanEnquiryService.getUnitOfMeasures(), // Get Unit of Measures Sectors
                    //this.getLoanApplicants(),
                    this.getLoanApplicantsByEmail(),
                    this.getRejectionCategories(),
                    this.getAllPartners()
                ]);
            }
        }
        else {
            return forkJoin([
                this.getEnquiryApplications(1)
            ]);
        }
    }

    /**
     * getEnquiryApplications()
     * Fetches a list of loan applications with a particular status.
     * @param status
     */
    public getEnquiryApplications(status: number): Observable<EnquiryApplicationModel[]> {
        return new Observable(observer => {
            const enquiryApplications = new Array<EnquiryApplicationModel>();
            this._http.get<LoanApplicationResourceModel[]>('enquiry/api/loanApplications?status=' + status).subscribe(result => {
                result.map(loanApplicationResourceModel => {
                    enquiryApplications.push(new EnquiryApplicationModel(loanApplicationResourceModel));
                });
                observer.next(enquiryApplications);
                observer.complete();
            });
        });
    }

  // /**
  //  * getApplicantsOrderedByAlphabet()
  //   */
  // public getLoanApplicants(): Observable<any> {
  //   return this._http.get<ApplicantGroup>('enquiry/api/partners/ordered'  );
  // }

    /**
     * getPartnersOrderedByEmail()
     */
    public getLoanApplicantsByEmail(): Observable<any> {
        return this._http.get<ApplicantEmail>('enquiry/api/partners/byEmail'  );
    }

    public getAllPartners(): Observable<any> {
        return this._http.get('enquiry/api/partners/all');
    }

    /**
     * getLoanApplication()
     * @param enquiryId
     */
    public getLoanApplication(enquiryId: string): Observable<LoanApplicationModel> {
        return this._http.get<LoanApplicationModel>('enquiry/api/loanApplications/' + enquiryId);
    }

    /**
     * getPartner()
     * @param partnerId
     */
    public getPartner(partnerId: string): Observable<PartnerModel> {
        return this._http.get<PartnerModel>('enquiry/api/partners/' + partnerId);
    }


    /**
     * getPartner()
     * @param partnerId
     */
    public getPartnerByPartyNumber(partnerId: string): Observable<PartnerModel> {
        return this._http.get<PartnerModel>('enquiry/api/partner/partyNumber?partyNumber=' + partnerId);
    }

  /**
     * updateLoanApplication()
     * Saves/updates the loan application to the database.
     * @param loanApplication
     * @param partner
     */
    public updateLoanApplication(loanApplication: any, partner: any): Observable<any> {
        return this._http.put('enquiry/api/loanApplications/' + loanApplication.id, { loanApplication, partner });
    }

    /**
     * approveLoanApplication()
     * Updates the loan application to the database and pushes it to SAP
     * @param loanApplication
     * @param partner
     */
    public approveLoanApplication(loanApplication: any, partner: any): Observable<any> {
        return this._http.put('enquiry/api/loanApplications/' + loanApplication.id + '/approve', { loanApplication, partner })
            .pipe(catchError(this.errorHandler));
    }

    /**
     * rejectEnquiry()
     * @param loanApplication
     * @param rejectReason
     */
    public rejectEnquiry(loanApplication: LoanApplicationModel,
                          rejectReason: string,rejectionCategory: string, rejectionDate: string): Observable<any> {
        return this._http.put('enquiry/api/loanApplications/' + loanApplication.id + '/reject',
          {   rejectReason , rejectionCategory,rejectionDate });
    }

    /**
     * getRejectionCategories()
     * returns a list of Rejection Categories
     */
    public getRejectionCategories(): Observable<any> {
        return this._http.get('enquiry/api/rejectionCategories');
    }

    /**
     * cancelEnquiry()
     * this method cancels a loan application.
     * @param loanApplication
     */
    public cancelEnquiry(loanApplication: LoanApplicationModel): Observable<any> {
        return this._http.put('enquiry/api/loanApplications/' + loanApplication.id + '/cancel', {});
    }

    /**
     * getProducts()
     * returns a list of products.
     */
    public getProducts(): Observable<any> {
        return this._http.get('enquiry/api/products');
    }

    errorHandler(errorResponse: HttpErrorResponse): Observable<any> {
        if (errorResponse.error instanceof ErrorEvent) {
            console.log('Client Side Error', errorResponse.error.message);
        } else {
            console.log('Server Side Error', errorResponse);
        }
        return Observable.throw(errorResponse);
    }
}
