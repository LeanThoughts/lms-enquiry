import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { HomepageService, RecentEnquiry } from '../../homepage.service';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

@Component({
    selector: 'app-recent-enquiries-widget',
    imports: [
        DashboardCardComponent,
        DatePipe
    ],
    templateUrl: './recent-enquiries-widget.component.html'
})
export class RecentEnquiriesWidgetComponent {

    @Input() loading = false;

    /**
     * Latest enquiries of the last 30 days, null if they could not be loaded
     */
    @Input() enquiries: RecentEnquiry[] | null = [];

    @Input() totalCount = 0;

    constructor(private homepageService: HomepageService) {
    }

    /**
     * Open the loan contract search for the enquiry
     */
    openEnquiry(enquiry: RecentEnquiry): void {
        this.homepageService.openLoanContractSearch({ enquiryNumber: enquiry.enquiryNo });
    }
}
