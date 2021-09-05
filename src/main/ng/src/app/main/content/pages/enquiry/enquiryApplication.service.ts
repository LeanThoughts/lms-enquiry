import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StateModel } from '../../model/state.model';
import {ApplicantEmail} from "./enquiryApplication/enquiryApplication.component";
import { AppService } from 'app/app.service';

@Injectable()
export class LoanEnquiryService implements Resolve<any> {

    public enquirySearchList: BehaviorSubject<any>;

    selectedLoanApplicationId: BehaviorSubject<string>;
    selectedLoanApplicationPartyNumber: BehaviorSubject<string>;

    selectedEnquiry: BehaviorSubject<any> = new BehaviorSubject({});
    
    /**
     *
     * @param _http
     */
    constructor(private _http: HttpClient, private _appService: AppService) {
    }

    /**
     * resolve()
     * Router resolveer, fetches data before the ui is created.
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        if (route.routeConfig.path === 'loanContractsList') {
            return forkJoin([
                this.getLoanClasses(), // get loan classes.
                this.getFinancingTypes(), // get financing types.
                this.getProjectTypes(), // get project types.
                this.getStates(), // get states.
                this.getAssistanceTypes(), // get assistance types.
                this.getTechnicalStatus(),   // get technical status.
                this.getUserRoleAuthorizations()
            ]);
        } else {
            return forkJoin([
                this.getLoanClasses(), // get loan classes.
                this.getFinancingTypes(), // get financing types.
                this.getProjectTypes(), // get project types.
                this.getStates(), // get states.
                this.getAssistanceTypes(), // get assistance types.
                this.getPartnerByPrincipal(), // get logged in partner details.
                this.getIndustrySectors(), // Get Industry Sectors
                this.getUnitOfMeasures(),   // Get Units
                this.getLoanApplicantsByEmail(), // Get Loan Applicants by Email
                this.getTechnicalStatus()   // Get Technical Status
            ]);
        }
    }

    
    /**
     * getUserRoleAuthorizations()
     */
    public getUserRoleAuthorizations(): Observable<any> {
        return this._http.get<any>('enquiry/api/authorization?userRole=' + this._appService.currentUser.role);
    }

    /**
     * getPartnersOrderedByEmail()
     */
    public getLoanApplicantsByEmail(): Observable<any> {
        return this._http.get<ApplicantEmail>('enquiry/api/partners/byEmail'  );
    }

    public getPartnerByPrincipal(): Observable<any> {
        return this._http.get('enquiry/api/partners/byPrincipal');
    }

    /**
     * getLoanClasses()
     * returns a list of loan classes.
     */
    public getLoanClasses(): Observable<any> {
        return this._http.get('enquiry/api/loanClasses?sort=code');
    }

    /**
     * getFinancingTypes()
     * returns a list of financing types.
     */
    public getFinancingTypes(): Observable<any> {
        return this._http.get('enquiry/api/financingTypes?sort=code');
    }

    /**
     * getProjectTypes()
     * Returns a list of project types.
     */
    public getProjectTypes(): Observable<any> {
        return this._http.get('enquiry/api/projectTypes?sort=code');
    }

    /**
     * getAssistanceTypes()
     * Returns a list of assistance types.
     */
    public getAssistanceTypes(): Observable<any> {
        return this._http.get('enquiry/api/assistanceTypes');
    }

    /**
     * getIndustrySectors()
     * Returns a list of Industry Sectors.
     */
    public getIndustrySectors(): Observable<any> {
        return this._http.get('enquiry/api/industrySectors?sort=code');
    }

    /**
     * getProjectCapacityUnits()
     * Returns a list of Units .
     */
    public getUnitOfMeasures(): Observable<any> {
        return this._http.get('enquiry/api/unitOfMeasures');
    }

    /**
     * getRejectionCategories()
     * returns a list of Rejection Categories
     */
    public getRejectionCategories(): Observable<any> {
        return this._http.get('enquiry/api/rejectionCategories');
    }
    /**
     * getTechnicalStatus()
     * returns a list of Technical Status
     */
    public getTechnicalStatus(): Observable<any> {
        return this._http.get('enquiry/api/technicalStatus');
    }

    /**
     * getStates()
     * Returns a list of states in the country.
     */
    public getStates(): Observable<Array<Object>> {
        return new Observable((observer) => {
            observer.next(StateModel.getStates());
            observer.complete();
        });
    }

    /**
     * saveLoanApplication()
     * Saves the loan application to the database.
     * @param loanApplication
     * @param partner
     */
    public saveLoanApplication(loanApplication: any, partner: any): Observable<any> {
        return this._http.post('enquiry/api/loanApplications', { loanApplication, partner });
    }

    /**
     * searchLoanEnquiries()
     * Fetches a list of loan applications based on the request parameters.
     * @param request
     */
    public searchLoanEnquiries(request: any): Observable<any> {
        return this._http.put<any>('enquiry/api/loanApplications/search', request);
    }

    /**
     * searchLoanContracts()
     * @param request 
     */
    public searchLoanContracts(request: any): Observable<any> {
        return this._http.put<any>('enquiry/api/loanApplications/loanContracts/search', request);
    }
   
    /**
     * getLoanApplicationByLoanContractId()
     * @param loanContractId
     */
     public getLoanApplicationByLoanContractId(loanContractId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/loanApplications/loanContractId/' + loanContractId);
    }
}
