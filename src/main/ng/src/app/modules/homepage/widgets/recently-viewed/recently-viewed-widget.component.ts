import { Component } from '@angular/core';
import { LoanContractSearchService, RecentlyViewedLoanContract } from '../../../loan-contract-search/loan-contract-search.service';
import { getTaskAgeLabel } from '../../../inbox/inbox.constants';
import { HomepageService } from '../../homepage.service';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

@Component({
    selector: 'app-recently-viewed-widget',
    imports: [
        DashboardCardComponent
    ],
    templateUrl: './recently-viewed-widget.component.html'
})
export class RecentlyViewedWidgetComponent {

    recentlyViewed: RecentlyViewedLoanContract[];

    constructor(private homepageService: HomepageService,
                loanContractSearchService: LoanContractSearchService) {
        this.recentlyViewed = loanContractSearchService.getRecentlyViewed();
    }

    getViewedLabel(item: RecentlyViewedLoanContract): string {
        const label = getTaskAgeLabel(item.viewedOn);
        return label ? 'Viewed ' + label.toLowerCase() : '';
    }

    /**
     * Open the loan contract search for the loan contract
     */
    open(item: RecentlyViewedLoanContract): void {
        this.homepageService.openLoanContractSearch({ enquiryNumber: item.enquiryNo });
    }
}
