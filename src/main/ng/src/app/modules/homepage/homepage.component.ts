import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IconComponent } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { statesOfIndia } from '../../app.constants';
import { AuthService } from '../auth/auth.service';
import { BusinessPartnerSearchService } from '../business-partner-search/business-partner-search.service';
import { Dashboard, HomepageService } from './homepage.service';
import { PendingTasksWidgetComponent } from './widgets/pending-tasks/pending-tasks-widget.component';
import { PipelineWidgetComponent } from './widgets/pipeline/pipeline-widget.component';
import { PortfolioWidgetComponent } from './widgets/portfolio/portfolio-widget.component';
import { RecentEnquiriesWidgetComponent } from './widgets/recent-enquiries/recent-enquiries-widget.component';
import { RecentlyViewedWidgetComponent } from './widgets/recently-viewed/recently-viewed-widget.component';
import { SapSyncWidgetComponent } from './widgets/sap-sync/sap-sync-widget.component';

interface QuickAction {
    label: string;
    description: string;
    icon: string;
    open: () => void;
    borrowerAccess: boolean;
    showTaskCount?: boolean;
}

// Role of loan applicant users (main loan partner)
const BORROWER_ROLE = 'TR0100';

@Component({
    selector: 'app-homepage',
    standalone: true,
    templateUrl: './homepage.component.html',
    styleUrl: './homepage.component.scss',
    imports: [
        DatePipe,
        IconComponent,
        PendingTasksWidgetComponent,
        PipelineWidgetComponent,
        PortfolioWidgetComponent,
        RecentEnquiriesWidgetComponent,
        RecentlyViewedWidgetComponent,
        SapSyncWidgetComponent
    ]
})
export class HomepageComponent implements OnInit, OnDestroy {

    readonly today = new Date();

    dashboard: Dashboard | null = null;
    loadingDashboard = true;
    tasks: any[] | null = null;
    loadingTasks = true;

    readonly quickActions: QuickAction[] = [
        {
            label: 'Search Loan Contracts', description: 'Find enquiries and loan contracts', icon: 'loan', borrowerAccess: true,
            open: () => this.router.navigate(['/loan-contract-search'])
        },
        {
            label: 'Create Business Partner', description: 'Add a borrower or other partner', icon: 'add-employee', borrowerAccess: false,
            open: () => {
                this.businessPartnerSearchService.selectedEntity$.next(null);
                this.router.navigate(['/business-partners/profile/create']);
            }
        },
        {
            label: 'Upload Enquiries', description: 'Import loan enquiries from Excel', icon: 'upload-to-cloud', borrowerAccess: false,
            open: () => this.router.navigate(['/enquiry-upload'])
        },
        {
            label: 'Reference Interest Rates', description: 'Maintain rate values', icon: 'trend-up', borrowerAccess: false,
            open: () => this.router.navigate(['/reference-interest-rates'])
        },
        {
            label: 'Inbox', description: 'Review tasks waiting for you', icon: 'inbox', borrowerAccess: true, showTaskCount: true,
            open: () => this.router.navigate(['/inbox'])
        }
    ];

    readonly describeState = (code: string): string => statesOfIndia.find(state => state.code === code)?.value || code;

    private readonly destroy$ = new Subject<void>();

    /**
     * Constructor
     */
    constructor(private authService: AuthService,
                private businessPartnerSearchService: BusinessPartnerSearchService,
                private homepageService: HomepageService,
                private router: Router) {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        if (!this.authService.currentUser) {
            this.authService.isAuthenticated().pipe(takeUntil(this.destroy$)).subscribe();
        }

        this.homepageService.getDashboard().pipe(takeUntil(this.destroy$)).subscribe(dashboard => {
            this.dashboard = dashboard;
            this.loadingDashboard = false;
        });

        this.homepageService.getTasks().pipe(takeUntil(this.destroy$)).subscribe(tasks => {
            this.tasks = tasks;
            this.loadingTasks = false;
        });
    }

    get user(): any {
        return this.authService.currentUser;
    }

    get isBorrower(): boolean {
        return this.user?.role === BORROWER_ROLE;
    }

    get visibleQuickActions(): QuickAction[] {
        return this.isBorrower ? this.quickActions.filter(action => action.borrowerAccess) : this.quickActions;
    }

    get greeting(): string {
        const hour = new Date().getHours();
        return hour < 12 ? 'Good morning' : hour < 17 ? 'Good afternoon' : 'Good evening';
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
