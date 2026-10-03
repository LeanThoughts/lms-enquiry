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
import { IccInprincipleApprovalService } from './icc-inprinciple-approval.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../../message.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
    selector: 'app-icc-inprinciple-approval',
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
        GenericListComponent
    ],
    templateUrl: './icc-inprinciple-approval.component.html'
})
export class ICCInprincipleApprovalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';
    iccInprincipleApprovalId: string = '';

    selectedEnquiry: any;
    selectedIccInprincipleApproval: any;

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private iccInprincipleApprovalService: IccInprincipleApprovalService,
        private messageService: MessageService,
        private authService: AuthService
    ) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.iccInprincipleApprovalId = this.route.snapshot.params['iccInprincipleApprovalId'];
        console.log('loanApplicationId is', this.loanApplicationId);
        console.log('iccInprincipleApprovalId is', this.iccInprincipleApprovalId);
        
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.iccInprincipleApprovalService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedIccInprincipleApproval = entity;
            // Nothing to send until the ICC approval has been changed since it was last approved
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
        let title = 'ICC In-principle Approval';
        title += (this.selectedEnquiry.loanContractId) ? ` (Loan Contract: ${this.selectedEnquiry.loanContractId}` : ` (Enquiry No: ${this.selectedEnquiry.enquiryNo?.id}`;
        title += ` / ${this.selectedEnquiry.projectName})`;
        return title;
    }

    /**
     * Send for approval
     */
    sendForApproval(): void {
        this.disableSendForApproval = true;
        const { firstName = '', lastName = '', email = '' } = this.authService.currentUser ?? {};
        const name = `${firstName} ${lastName}`.trim();
        this.messageService.showInfo('Please wait while attempting to send the ICC approval for approval.', 25000);

        this.iccInprincipleApprovalService.sendIccApprovalForApproval(this.selectedIccInprincipleApproval.id, name, email).subscribe({
            next: (response) => {
                this.iccInprincipleApprovalService.selectedEntity$.next(response);
                this.messageService.showSuccess('ICC Stage is sent for approval.');
            },
            error: (error) => {
                this.disableSendForApproval = false;
                // The backend reports missing ICC tab entries as a 500 with a user-readable message
                if (error.status === 500 && error.error?.message) {
                    this.messageService.showError(error.error.message);
                } else {
                    this.messageService.showError('Errors occurred. Please try again later or contact your system administrator.');
                }
            }
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
