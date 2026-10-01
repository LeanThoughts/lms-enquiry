import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, LayoutGridModule } from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { SanctionService } from './sanction.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../../message.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
    selector: 'app-sanction',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        LayoutGridModule,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericListComponent
    ],
    templateUrl: './sanction.component.html'
})
export class SanctionComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';

    selectedEnquiry: any;
    selectedSanction: any;

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private sanctionService: SanctionService,
        private messageService: MessageService,
        private authService: AuthService
    ) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];

        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.sanctionService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedSanction = entity;
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
        let title = 'Sanction';
        title += (this.selectedEnquiry.loanContractId) ? ` : ${this.selectedEnquiry.loanContractId}` : ` : ${this.selectedEnquiry.enquiryNo}`;
        title += ` / ${this.selectedEnquiry.projectName}`;
        return title;
    }

    /**
     * Send for approval
     */
    sendForApproval(): void {
        this.disableSendForApproval = true;
        const { firstName = '', lastName = '', email = '' } = this.authService.currentUser ?? {};
        const name = `${firstName} ${lastName}`.trim();
        this.messageService.showInfo('Please wait while attempting to send the sanction for approval.', 25000);

        this.sanctionService.sendSanctionForApproval(this.selectedSanction.id, name, email).subscribe({
            next: (response) => {
                this.sanctionService.selectedEntity$.next(response);
                this.messageService.showSuccess('Sanction is sent for approval.');
            },
            error: () => {
                this.disableSendForApproval = false;
                this.messageService.showError('Errors occurred. Please try again later or contact your system administrator.');
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
