import { Component, OnDestroy, OnInit } from '@angular/core';
import { 
    ButtonComponent, 
    DynamicPageComponent, 
    DynamicPageContentComponent, 
    DynamicPageGlobalActionsComponent, 
    DynamicPageHeaderComponent, 
    ToolbarComponent 
} from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { ApplicationFeeService } from './application-fee.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../../message.service';
import { AuthService } from '../../../auth/auth.service';
import { InvoicingDetailsComponent } from './invoicing-details/invoicing-details.component';
import { ApplicationFeeProjectDetailsComponent } from './project-details/application-fee-project-details.component';

@Component({
    selector: 'app-application-fee',
    imports: [
        // Dynamic Page Components
        DynamicPageHeaderComponent,
        DynamicPageComponent,
        DynamicPageGlobalActionsComponent,
        ToolbarComponent,
        DynamicPageContentComponent,
        // Other Components and Modules
        ButtonComponent,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericListComponent,
        InvoicingDetailsComponent,
        ApplicationFeeProjectDetailsComponent
    ],
    templateUrl: './application-fee.component.html'
})
export class ApplicationFeeComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';

    selectedEnquiry: any;
    selectedApplicationFee: any;

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private applicationFeeService: ApplicationFeeService,
        private messageService: MessageService,
        private authService: AuthService
    ) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];

        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.applicationFeeService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedApplicationFee = entity;
            // Nothing to send until the application fee has been changed since it was last approved
            this.disableSendForApproval = !entity?.modified;
        });
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Set the title
        this.title = this.getTitle();
    }

    /**
     * Get the title for the page
     */
    private getTitle(): string {
        let title = 'Application Fee';
        title += (this.selectedEnquiry.loanContractId) ? ` (Loan Contract: ${this.selectedEnquiry.loanContractId}` : ` (Enquiry No: ${this.selectedEnquiry.enquiryNo?.id}`;
        title += ` / ${this.selectedEnquiry.projectName})`;
        return title;
    }

    /**
     * Send for approval. Customer and invoicing details are mandatory.
     */
    sendForApproval(): void {
        const invoicingDetailsMissingMessage = 'Invoicing details not found. Please complete Customer and Invoicing details before sending for approval.';
        this.disableSendForApproval = true;

        this.applicationFeeService.getInvoicingDetails(this.selectedApplicationFee.id).subscribe((invoicingDetail: any) => {
            if (!invoicingDetail) {
                this.disableSendForApproval = false;
                this.messageService.showError(invoicingDetailsMissingMessage);
                return;
            }

            const { firstName = '', lastName = '', email = '' } = this.authService.currentUser ?? {};
            const name = `${firstName} ${lastName}`.trim();
            this.messageService.showInfo('Please wait while attempting to send application fee details for approval.', 25000);

            this.applicationFeeService.sendApplicationFeeForApproval(this.selectedApplicationFee.id, name, email).subscribe({
                next: (response) => {
                    this.applicationFeeService.selectedEntity$.next(response);
                    this.messageService.showSuccess('Application Fee is sent for approval.');
                },
                error: () => {
                    this.disableSendForApproval = false;
                    this.messageService.showError('Errors occurred while sending for approval. Please try again later or contact your system administrator.');
                }
            });
        });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
