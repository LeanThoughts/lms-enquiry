import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { InboxService } from '../inbox/inbox.service';
import { LoanContractSearchService } from '../loan-contract-search/loan-contract-search.service';

export interface StageCount {
    functionalStatus: number;
    description: string;
    count: number;
    amount: number;
}

export interface SapSyncStatus {
    waiting: number;
    inProgress: number;
    errors: number;
    posted: number;
}

export interface PortfolioSlice {
    code: string;
    description: string;
    count: number;
    amount: number;
}

export interface RecentEnquiry {
    loanApplicationId: string;
    enquiryNo: number;
    loanEnquiryDate: string;
    projectName: string;
    loanContractId: string;
    borrowerName: string;
    functionalStatus: number;
    functionalStatusDescription: string;
}

export interface Dashboard {
    stages: StageCount[];
    sapSync: SapSyncStatus;
    portfolioByProjectType: PortfolioSlice[];
    portfolioByState: PortfolioSlice[];
    recentEnquiryCount: number;
    recentEnquiries: RecentEnquiry[];
}

@Injectable({
    providedIn: 'root'
})
export class HomepageService {

    /**
     * Constructor
     */
    constructor(private http: HttpClient,
                private router: Router,
                private inboxService: InboxService,
                private loanContractSearchService: LoanContractSearchService) {
    }

    /**
     * Get the dashboard aggregates, or null if they could not be loaded
     */
    getDashboard(): Observable<Dashboard | null> {
        return this.http.get<Dashboard>(environment.primaryApiHost + '/dashboard').pipe(
            catchError(() => of(null))
        );
    }

    /**
     * Get the tasks of the logged in user, or null if they could not be loaded
     */
    getTasks(): Observable<any[] | null> {
        return this.inboxService.getTasks().pipe(
            map((tasks: any) => tasks || []),
            catchError(() => of(null))
        );
    }

    /**
     * Open the loan contract search and run it with the given criteria
     */
    openLoanContractSearch(criteria: { functionalStatus?: number, enquiryNumber?: number }): void {
        this.loanContractSearchService.searchFormValue = {
            functionalStatus: criteria.functionalStatus != null ? String(criteria.functionalStatus) : null,
            enquiryNumber: criteria.enquiryNumber != null ? String(criteria.enquiryNumber) : null
        };
        this.loanContractSearchService.runSearchOnLoad = true;
        this.router.navigate(['/loan-contract-search']);
    }
}
