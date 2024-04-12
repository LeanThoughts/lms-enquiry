import { Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';

@Injectable()
export class LoanAppraisalService implements Resolve<any> {

    _loanAppraisal: any;
    _loanAppraisalBehaviourSubject: BehaviorSubject<any> = new BehaviorSubject({});
    
    _selectedSecurityTrustee: BehaviorSubject<any> = new BehaviorSubject({});

    refreshKYCPartnerList: BehaviorSubject<any> = new BehaviorSubject({'refresh': false});
    
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
        console.log('in resolve :: loanAppraisalId is ', this._loanAppraisal.id);
        return forkJoin([
            this.getLoanOfficers(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getLoanOfficersForKyc(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getBanks(),
            this.getSyndicateConsortiums(this._loanAppraisal.id),
            this.getProposalDetail(this._loanAppraisal.id),
            this.getFurtherDetail(this._loanAppraisal.id),
            this.getSiteVisits(this._loanEnquiryService.selectedLoanApplicationId.value),
            this.getProjectAppraisalCompletion(this._loanAppraisal.id),
            this.getReasonForDelay(this._loanAppraisal.id),
            this.getCustomerRejection(this._loanAppraisal.id),
            this.getProjectData(this._loanAppraisal.id),
            this._loanEnquiryService.getUnitOfMeasures(),
            this._loanEnquiryService.getProjectTypes(),
            this.getBusinessPartners('ZLM015'),
            this.getBusinessPartners('ZLM028'),
            this.getMainLocationDetail(this._loanAppraisal.id),
            this.getSubLocations(this._loanAppraisal.id)
        ]);
    }
    
    /**
     * getBanks()
     */
    public getBanks(): Observable<any> {
        return this._http.get('enquiry/api/bankmasters/all');
    }

    /**
     * getLaonAppraisal()
     */
    public getLaonAppraisal(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanAppraisals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    }

    /**
     * getPartnersByRoleType()
     */
    public getPartnersByRole(role: string): Observable<any> {
        return this._http.get("enquiry/api/partners/partyRole/" + role);
    }
    
    /**
     * getAppraisalOfficers()
     */
    public getLoanOfficers(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdOrderBySerialNumberDesc?loanApplicationId=" 
                + loanApplicationId);
    }

    /**
     * getLoanOfficersForKyc()
     */
    public getLoanOfficersForKyc(loanApplicationId: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdAndKycRequired?loanApplicationId=" 
                + loanApplicationId + '&kycRequired=true');
    }

    /**
     * getLoanOfficersByRoleType()
     */
     public getLoanOfficersByRoleType(loanApplicationId: string, roleType: string): Observable<any> {
        return this._http.get("enquiry/api/loanPartners/search/findByLoanApplicationIdAndRoleType?loanApplicationId=" 
                + loanApplicationId + '&roleType=' + roleType);
    }

    /**
     * deleteLoanOfficer()
     */
    public deleteLoanOfficer(partnerId: string): Observable<any> {
        return this._http.delete("enquiry/api/loanPartners/delete/" + partnerId);
    }

    /**
     * createAppraisalOfficer()
     */
    public createLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.post("enquiry/api/loanPartners/create", loanOfficer);
    }

    /**
     * updateLoanOfficer()
     */
    public updateLoanOfficer(loanOfficer: any): Observable<any> {
        return this._http.put("enquiry/api/loanPartners/update", loanOfficer);
    }

    /**
     * getKYCDocuments()
     */
    public getKYCDocuments(loanPartnerId: string): Observable<any> {
        return this._http.get("enquiry/api/knowYourCustomers/search/findByLoanPartnerId?loanPartnerId=" + loanPartnerId);
    }

    /**
     * updateKYC()
     */
    public updateKYC(kyc: string): Observable<any> {
        return this._http.put("enquiry/api/knowYourCustomers/update", kyc);
    }

    /**
     * getSyndicateConsortiums()
     */
    public getSyndicateConsortiums(loanAppraisalId: string): Observable<any> {
        if (loanAppraisalId === '') {
            return Observable.of({});
        }
        else {
            return this._http.get("enquiry/api/syndicateConsortiums/search/findByLoanAppraisalIdOrderBySerialNumberDesc?loanAppraisalId=" 
                    + loanAppraisalId);
        }
    }

    /**
     * createSyndicateConsortium()
     */
    public createSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.post("enquiry/api/syndicateConsortiums/create", syndicateConsortium);
    }

    /**
     * updateSyndicateConsortium()
     */
    public updateSyndicateConsortium(syndicateConsortium: any): Observable<any> {
        return this._http.put("enquiry/api/syndicateConsortiums/update", syndicateConsortium);
    }

    /**
     * deleteLoanOfficer()
     */
    public deleteSyndicateConsortium(bankId: string): Observable<any> {
        return this._http.delete("enquiry/api/syndicateConsortiums/delete/" + bankId);
    }
        
    /**
     * getProposalDetail()
     */
    public getProposalDetail(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/proposalDetails/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createProposalDetail()
     */
    public createProposalDetail(proposalDetail: any): Observable<any> {
        return this._http.post("enquiry/api/proposalDetails/create", proposalDetail);
    }

    /**
     * updateProposalDetail()
     */
    public updateProposalDetail(proposalDetail: any): Observable<any> {
        return this._http.put("enquiry/api/proposalDetails/update", proposalDetail);
    }

    /**
     * getFurtherDetails()
     */
    public getFurtherDetail(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/furtherDetails/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * getMainLocationDetail()
     */
    public getMainLocationDetail(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/mainLocationDetails/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * updateFurtherDetail()
     */
    public updateFurtherDetail(furtherDetail: any): Observable<any> {
        return this._http.put("enquiry/api/furtherDetails/update", furtherDetail);
    }

    /**
     * updateMainLocationDetail()
     */
    public updateMainLocationDetail(mainLocationDetail: any): Observable<any> {
        return this._http.put("enquiry/api/mainLocationDetails/update", mainLocationDetail);
    }

    /**
     * getSubLocations()
     */
    public getSubLocations(loanAppraisalId: string): Observable<any> {
        return this._http.get("enquiry/api/subLocationDetails/search/findByLoanAppraisalId?loanAppraisalId=" + loanAppraisalId);
    }

    /**
     * createSubLocationDetail()
     */
    public createSubLocationDetail(subLocationDetail: any): Observable<any> {
        return this._http.post("enquiry/api/subLocationDetails/create", subLocationDetail);
    }

    /**
     * updateSubLocationDetail()
     */
    public updateSubLocationDetail(subLocationDetail: any): Observable<any> {
        return this._http.put("enquiry/api/subLocationDetails/update", subLocationDetail);
    }

    /**
     * getSiteVisits()
     */
    getSiteVisits(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/siteVisits/search/findByLoanApplicationIdAndSiteVisitTypeOrderBySerialNumberDesc?loanApplicationId=' 
                + loanApplicationId + '&siteVisitType=Site Visit');
    }

    /**
     * deleteSiteVisit()
     */
    public deleteSiteVisit(id: string): Observable<any> {
        return this._http.delete("enquiry/api/siteVisits/delete/" + id);
    }

    /**
     * getProjectAppraisalCompletion()
     */
    public getProjectAppraisalCompletion(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/projectAppraisalCompletions/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createProjectAppraisalCompletion()
     */
    public createProjectAppraisalCompletion(proposalDetail: any): Observable<any> {
        return this._http.post("enquiry/api/projectAppraisalCompletions/create", proposalDetail);
    }

    /**
     * updateProjectAppraisalCompletion()
     */
    public updateProjectAppraisalCompletion(proposalDetail: any): Observable<any> {
        return this._http.put("enquiry/api/projectAppraisalCompletions/update", proposalDetail);
    }

    /**
     * getCorporateLoanRiskRatings()
     */
    public getCorporateLoanRiskRatings(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/corporateLoanRiskRatings/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createCorporateLoanRiskRating()
     */
    public createCorporateLoanRiskRating(termLoanRiskRating: any): Observable<any> {
        return this._http.post("enquiry/api/corporateLoanRiskRatings/create", termLoanRiskRating);
    }
    
    /**
     * updateCorporateLoanRiskRating()
     */
    public updateCorporateLoanRiskRating(termLoanRiskRating: any): Observable<any> {
        return this._http.put("enquiry/api/corporateLoanRiskRatings/update", termLoanRiskRating);
    }

    /**
     * deleteCorporateLoanRiskRating()
     */
    public deleteCorporateLoanRiskRating(id: string): Observable<any> {
        return this._http.delete("enquiry/api/corporateLoanRiskRatings/delete/" + id);
    }
        
    /**
     * getTermLoanRiskRatings()
     */
     public getTermLoanRiskRatings(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/termLoanRiskRatings/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createTermLoanRiskRating()
     */
    public createTermLoanRiskRating(termLoanRiskRating: any): Observable<any> {
        return this._http.post("enquiry/api/termLoanRiskRatings/create", termLoanRiskRating);
    }

    /**
     * deleteTermLoanRiskRating()
     */
    public deleteTermLoanRiskRating(id: string): Observable<any> {
        return this._http.delete("enquiry/api/termLoanRiskRatings/delete/" + id);
    }

    /**
     * updateTermLoanRiskRating
     */
    public updateTermLoanRiskRating(termLoanRiskRating: any): Observable<any> {
        return this._http.put("enquiry/api/termLoanRiskRatings/update", termLoanRiskRating);
    }

    /**
     * getRatings()
     */
    public getRatings(): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/externalRatingTypes').subscribe(
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
     * getExternalRatings()
     */
    public getExternalRatings(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/externalRatings/search/findByLoanAppraisalIdOrderBySerialNumber?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createExternalRating()
     */
    public createExternalRating(externalRating: any): Observable<any> {
        return this._http.post("enquiry/api/externalRatings/create", externalRating);
    }
    
    /**
     * updateExternalRating()
     */
    public updateExternalRating(externalRating: any): Observable<any> {
        return this._http.put("enquiry/api/externalRatings/update", externalRating);
    }

    /**
     * deleteCExternalRating()
     */
    public deleteExternalRating(id: string): Observable<any> {
        return this._http.delete("enquiry/api/externalRatings/delete/" + id);
    }
    
    /**
     * getReasonForDelay()
     */
    public getReasonForDelay(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/reasonForDelays/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
        return this._http.post("enquiry/api/reasonForDelays/create", reasonForDelay);
    }

    /**
     * updateReasonForDelay()
     */
    public updateReasonForDelay(reasonForDelay: any): Observable<any> {
        return this._http.put("enquiry/api/reasonForDelays/update", reasonForDelay);
    }

    /**
     * getCustomerRejection()
     */
    public getCustomerRejection(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/customerRejections/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createCustomerRejection()
     */
    public createCustomerRejection(customerRejection: any): Observable<any> {
        return this._http.post("enquiry/api/customerRejections/create", customerRejection);
    }

    /**
     * updateCustomerRejection()
     */
    public updateCustomerRejection(customerRejection: any): Observable<any> {
        return this._http.put("enquiry/api/customerRejections/update", customerRejection);
    }

    /**
     * getProjectData()
     */
    public getProjectData(loanAppraisalId: string): Observable<any> {
        return new Observable((observer) => {
            this._http.get('enquiry/api/projectDatas/search/findByLoanAppraisalId?loanAppraisalId=' + loanAppraisalId).subscribe(
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
     * createProjectData()
     */
    public createProjectData(projectData: any): Observable<any> {
        return this._http.post("enquiry/api/projectDatas/create", projectData);
    }

    /**
     * updateProjectData()
     */
    public updateProjectData(projectData: any): Observable<any> {
        return this._http.put("enquiry/api/projectDatas/update", projectData);
    }

    // Security Trustee CRUD methods ...

    public getSecurityTrustees(loanApplicationId: string): Observable<any> {
        return this._http.get('enquiry/api/loanApplications/' + loanApplicationId + '/securityTrustees');
    }

    public saveSecurityTrustee(securityTrustee: any, loanApplicationId: any, module: string): Observable<any> {
        const url = "enquiry/api/securityTrustees/create";
        return this._http.post(url, { 'loanApplicationId':loanApplicationId, 'securityTrustee':securityTrustee, 'moduleName':module });
    }

    public updateSecurityTrustee(securityTrustee: any, module: string): Observable<any> {
        const url = "enquiry/api/securityTrustees/update/" + securityTrustee.id;
        return this._http.put(url, { 'loanApplicationId':'', 'securityTrustee':securityTrustee, 'moduleName':module });
    }

    public deleteSecurityTrustee(securityTrustee: any, module: string): Observable<any> {
        const url = "enquiry/api/securityTrustees/" + securityTrustee.id + "/moduleName/" + module;
        return this._http.delete(url);
    }

    // Security Trustee Report and Fee CRUD methods ...

    public getSecurityTrusteeReportAndFees(securityTrusteeId: string): Observable<any> {
        return this._http.get('enquiry/api/securityTrustees/' + securityTrusteeId + '/securityTrusteeReportAndFees');
    }

    public saveSecurityTrusteeReportAndFee(securityTrusteeReportAndFee: any, securityTrusteeId: string, module: string): Observable<any> {
        const url = "enquiry/api/securityTrusteeReportAndFees/create";
        return this._http.post(url, { 'securityTrusteeId': securityTrusteeId, 'securityTrusteeReportAndFee': securityTrusteeReportAndFee, 'moduleName':module });
    }

    public updateSecurityTrusteeReportAndFee(securityTrusteeReportAndFee: any, module: string): Observable<any> {
        const url = "enquiry/api/securityTrusteeReportAndFees/update/" + securityTrusteeReportAndFee.id;
        return this._http.put(url, { 'securityTrusteeId': '', 'securityTrusteeReportAndFee': securityTrusteeReportAndFee, 'moduleName':module });
    }

    public deleteSecurityTrusteeReportAndFee(securityTrusteeReportAndFee: any, module: string): Observable<any> {
        const url = "enquiry/api/securityTrusteeReportAndFees/" + securityTrusteeReportAndFee.id + "/moduleName/" + module;
        return this._http.delete(url);
    }
    
    /**
     * getBusinessPartners()
     */
    public getBusinessPartners(role: string): Observable<any> {
        return this._http.get("enquiry/api/partners/role/" + role);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }    

    /**
     * getRiskModelSummaryForLoanContractId()
     */
    public getRiskModelSummaryForLoanContractId(loanContractId: string): Observable<any> {
        return this._http.get('enquiry/api/termLoanRiskRatings/riskModelSummary/' + loanContractId);
    }

    /**
     * sendAppraisalForApproval()
     */
    public sendAppraisalForApproval(businessProcessId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': businessProcessId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'Appraisal'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}
