import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, catchError, forkJoin, map, Observable, of } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { LoanContractSearchService } from '../../../loan-contract-search.service';
import { collateralTypes } from '../../../../../app.constants';

@Injectable({
  providedIn: 'root'
})
export class ProjectProposalService implements Resolve<any> {

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    /**
     * Constructor
     */
    constructor(private http: HttpClient,
        private loanContractSearchService: LoanContractSearchService
    ) {       
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (state.url.includes('update-project-proposal')) {
            return forkJoin({
                projectProposal: this.getProjectProposal(route.params['projectProposalId']),
                documentType: this.getDocumentTypes(),
                proposalStatus: this.getProposalStatuses(),
                status: this.getProposalStatuses(),
                state: this.loanContractSearchService.getStates(),
                loanClass: this.loanContractSearchService.getLoanClasses(true),
                projectType: this.loanContractSearchService.getProjectTypes(true),
                financingType: this.loanContractSearchService.getFinancingTypes(true),
                assistanceType: this.loanContractSearchService.getAssistanceTypes(true),
                loanType: this.loanContractSearchService.getLoanTypes(true),
                purposeOfLoan: this.loanContractSearchService.getPurposeOfLoans(true),
                projectTypeCoreSector: this.loanContractSearchService.getProjectTypeCoreSectors(true),
                projectCapacityUnit: this.loanContractSearchService.getProjectCapacityUnits(),
                moratoriumPeriodUnit: this.loanContractSearchService.getDurationUnits(),
                constructionPeriodUnit: this.loanContractSearchService.getDurationUnits(),
                projectDetail: this.getProjectDetail(route.params['projectProposalId']),
                creditRating: this.getCreditRatings(),
                creditRatingAgency: this.getCreditRatingAgencies(),
                projectCost: this.getProjectProposalProjectCost(route.params['projectProposalId']),
                otherLoanDetails: this.getProjectProposalOtherLoanDetails(route.params['projectProposalId']),
                collateralType: this.getCollateralTypes(),
                dealGuaranteeTimeline: this.getDealGuaranteeTimeline(route.params['projectProposalId']),
                environmentalSystemCategory: this.getEnvironmentalSystemCategories(),
                policyExposure: this.getPolicyExposures(),
            });
        }
        else {
            return forkJoin({
                documentType: this.getDocumentTypes(),
                proposalStatus: this.getProposalStatuses(),
                projectProposal: of(this.selectedEntity$.value)
            });
        }
    }

    /**
     * Get policy exposures
     */
    public getPolicyExposures(): Observable<any> {
        const policyExposures = [
            { code: 'E', value: 'EV' },
            { code: 'N', value: 'NEV' }
        ];
        return of(policyExposures);
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
     * Get proposal statuses
     */
    getProposalStatuses(): Observable<any> {
        return new Observable((observer) => {
            const proposalStatuses = new Array<any>();
            proposalStatuses.push({ code: 'Final', description: 'Final' });
            proposalStatuses.push({ code: 'Draft', description: 'Draft' });
            observer.next(proposalStatuses);
            observer.complete();
        });
    }

    /**
     * Get project proposal
     */
    public getProjectProposal(projectProposalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/projectProposals/' + projectProposalId);
    }

    /**
     * Create project proposal
     */
    public createProjectProposal(projectProposal: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/projectProposals/create', projectProposal);
    }

    /**
     * Update project proposal
     */
    public updateProjectProposal(projectProposal: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/projectProposals/update', projectProposal);
    }

    /**
     * Get project detail
     */
    public getProjectDetail(projectProposalId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/projectDetails/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
                .subscribe({
                    next: (response: any) => {
                        observer.next(response);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next(null);
                        observer.complete();
                    }
                });
        });
    }

    /**
     * Create project detail
     */
    public createProjectDetail(projectDetail: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/projectDetails/create', projectDetail);
    }

    /**
     * Update project detail
     */
    public updateProjectDetail(projectDetail: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/projectDetails/update', projectDetail);
    }

    /**
     * Get project proposal by status
     */
    public getProjectProposalByStatus(enquiryActionId: string, proposalStatus: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/projectProposals/search/findByEnquiryActionIdAndProposalStatus?enquiryActionId=' + enquiryActionId
                + '&proposalStatus=' + proposalStatus);
    }
    
    /**
     * Get credit ratings
     */
    public getCreditRatings(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/creditRatingCodes');
    }
    
    /**
     * Get credit rating agencies
     */
    public getCreditRatingAgencies(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/creditRatingAgencies');
    }
    
    /**
     * Get project proposal credit ratings
     */
    public getProjectProposalCreditRatings(projectProposalId: string): Observable<any> {
        return forkJoin({
            creditRatings: this.getCreditRatings(),
            creditRatingAgencies: this.getCreditRatingAgencies(),
            responses: this.http.get(environment.primaryApiHost + '/creditRatings/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
        }).pipe(
            map(({ creditRatings, creditRatingAgencies, responses }: any) => {
                responses._embedded.creditRatings.forEach((response: any) => {
                    response.creditRatingDescription = creditRatings.find((creditRating: any) => creditRating.code === response.creditRating).value;
                    if (response.creditRatingAgency) {
                        response.creditRatingAgencyDescription = creditRatingAgencies.find((creditRatingAgency: any) => creditRatingAgency.code === 
                            response.creditRatingAgency).value;
                    }
                });
                return responses._embedded.creditRatings;
            })
        );
    }

    /**
     * Create project proposal credit rating
     */
    public createProjectProposalCreditRating(creditRating: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/creditRatings/create', creditRating);
    }

    /**
     * Update project proposal credit rating
     */
    public updateProjectProposalCreditRating(creditRating: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/creditRatings/update', creditRating);
    }

    /**
     * Get project proposal project cost
     */
    public getProjectProposalProjectCost(projectProposalId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/projectCosts/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
                .subscribe({
                    next: (response: any) => {
                        observer.next(response);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next(null);
                        observer.complete();
                    }
                });
        });
    }
    
    /**
     * Create project proposal project cost
     */
    public createProjectProposalProjectCost(projectCost: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/projectCosts/create', projectCost);
    }

    /**
     * Update project proposal project cost
     */
    public updateProjectProposalProjectCost(projectCost: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/projectCosts/update', projectCost);
    }

    /**
     * Get project proposal share holders
     */
    public getProjectProposalShareHolders(projectProposalId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/shareHolders/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
                .subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.shareHolders);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next(null);
                        observer.complete();
                    }
                });
        });
    }

    /**
     * Create project proposal share holder
     */
    public createProjectProposalShareHolder(shareHolder: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/shareHolders/create', shareHolder);
    }

    /**
     * Update project proposal share holder
     */
    public updateProjectProposalShareHolder(shareHolder: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/shareHolders/update', shareHolder);
    }

    /**
     * Get project proposal other loan details
     */
    public getProjectProposalOtherLoanDetails(enquiryActionId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/projectProposalOtherDetails/search/findByProjectProposalId?projectProposalId=' + enquiryActionId)
                .subscribe({
                    next: (response: any) => {
                    observer.next(response);
                    observer.complete();
                },
                error: (error: any) => {
                    observer.next(null);
                    observer.complete();
                }
            });
        });
    }
    
    /**
    * Create project proposal other loan details
    */
    public createProjectProposalOtherLoanDetails(otherDetails: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/projectProposalOtherDetails/create', otherDetails);
    }

    /**
     * Update project proposal other loan details
     */
    public updateProjectProposalOtherLoanDetails(otherDetails: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/projectProposalOtherDetails/update', otherDetails);
    }

    /**
     * Get other loan details document
     */
    public getOtherLoanDetailsDocuments(projectProposalId: string): Observable<any> {
        return forkJoin({
            documentTypes: this.getDocumentTypes(),
            responses: this.http.get(environment.primaryApiHost + '/otherDetailsDocuments/search/findByProjectProposalId?projectProposalId=' + 
                projectProposalId)
        }).pipe(
            map(({ documentTypes, responses }: any) => {
                responses._embedded.otherDetailsDocuments.forEach((document: any) => {
                    document.documentTypeName = documentTypes.find((documentType: any) => documentType.code === document.documentType).description;
                });
                return responses._embedded.otherDetailsDocuments;
            })
        );
    }
    
    /**
     * Create other loan details document
     */
    public createOtherLoanDetailsDocument(otherDetialsDocument: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/otherDetailsDocuments/create', otherDetialsDocument);
    }

    /**
     * Update other loan details document
     */
    public updateOtherLoanDetailsDocument(otherDetialsDocument: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/otherDetailsDocuments/update', otherDetialsDocument);
    }
    
    /**
     * Get Promoter Financials
     */
    public getPromoterFinancials(projectProposalId: string): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/promoterBorrowerFinancials/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
                .subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.promoterBorrowerFinancials);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next(null);
                        observer.complete();
                    }
                });
        });
    }

    /**
     * Create Promoter Financials
     */
    public createPromoterFinancials(financial: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/promoterBorrowerFinancials/create', financial);
    }

    /**
     * Update Promoter Financials
     */
    public updatePromoterFinancials(financial: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/promoterBorrowerFinancials/update', financial);
    }

    /**
     * Get collateral types
     */
    public getCollateralTypes(): Observable<any> {
        return new Observable((observer) => {
            const sortedCollateralTypes = [...collateralTypes].sort((a, b) => a.value.localeCompare(b.value));
            observer.next(sortedCollateralTypes);
            observer.complete();
        });
    }

    /**
     * Get collateral details
     */
    public getCollateralDetails(projectProposalId: string): Observable<any> {
        return forkJoin({
            collateralTypes: this.getCollateralTypes(),
            responses: this.http.get(environment.primaryApiHost + '/collateralDetails/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
        }).pipe(
            map(({ collateralTypes, responses }: any) => {
                responses._embedded.collateralDetails.forEach((detail: any) => {
                    detail.collateralTypeDescription = collateralTypes.find((collateralType: any) => collateralType.code === detail.collateralType).value;
                });
                return responses._embedded.collateralDetails;
            })
        );
    }

    /**
     * Create collateral detail
     */
    public createCollateralDetails(collateralDetail: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/collateralDetails/create', collateralDetail);
    }

    /**
     * Update collateral detail
     */
    public updateCollateralDetails(collateralDetail: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/collateralDetails/update', collateralDetail);
    }

    /**
     * Get environmental system categories
     */
    public getEnvironmentalSystemCategories(): Observable<any> {
        return new Observable((observer) => {
            const environmentalSystemCategories = [
                { code: 'Category A', value: 'Category A' },
                { code: 'Category B', value: 'Category B' },
                { code: 'Category C', value: 'Category C' },
            ];
            observer.next(environmentalSystemCategories);
            observer.complete();
        });
    }

    /**
     * Get deal guarantee timeline
     */
    public getDealGuaranteeTimeline(projectProposalId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/dealGuaranteeTimelines/search/findByProjectProposalId?projectProposalId=' + projectProposalId)
            .pipe(
                map((response: any) => response),
                catchError(() => of(null))
            );
    }

    /**
     * Create deal guarantee timeline
     */
    public createDealGuaranteeTimeline(dealGuaranteeTimeline: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/dealGuaranteeTimelines/create', dealGuaranteeTimeline);
    }

    /**
     * Update deal guarantee timeline
     */
    public updateDealGuaranteeTimeline(dealGuaranteeTimeline: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/dealGuaranteeTimelines/update', dealGuaranteeTimeline);
    }    
}
