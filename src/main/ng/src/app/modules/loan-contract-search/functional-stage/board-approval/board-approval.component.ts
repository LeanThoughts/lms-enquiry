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
import { BoardApprovalService } from './board-approval.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../../message.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
    selector: 'app-board-approval',
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
    templateUrl: './board-approval.component.html'
})
export class BoardApprovalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';

    selectedEnquiry: any;
    selectedBoardApproval: any;

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private boardApprovalService: BoardApprovalService,
        private messageService: MessageService,
        private authService: AuthService
    ) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];

        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.boardApprovalService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedBoardApproval = entity;
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
        let title = 'Board Approval';
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
        this.messageService.showInfo('Please wait while attempting to send the board approval for approval.', 25000);

        this.boardApprovalService.sendBoardApprovalForApproval(this.selectedBoardApproval.id, name, email).subscribe({
            next: (response) => {
                this.boardApprovalService.selectedEntity$.next(response);
                this.messageService.showSuccess('Board approval is sent for approval.');
            },
            error: (error: any) => {
                this.disableSendForApproval = false;
                this.messageService.showError(error?.error?.message || 'Errors occurred. Please try again later or contact your system administrator.');
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
