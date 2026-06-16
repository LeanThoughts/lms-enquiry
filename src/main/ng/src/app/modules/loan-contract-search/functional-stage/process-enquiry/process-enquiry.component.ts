import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, LayoutGridModule } from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { ProcessEnquiryService } from './process-enquiry.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../../message.service';
import { AuthService } from '../../../auth/auth.service';
import { ProjectProposalService } from './project-proposal/project-proposal.service';

@Component({
    selector: 'app-process-enquiry',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        LayoutGridModule,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericListComponent
    ],
    templateUrl: './process-enquiry.component.html'
})
export class ProcessEnquiryComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';
    enquiryActionId: string = '';

    selectedEnquiry: any;
    selectedEnquiryAction: any;

    /**
     * Constructor
     */
    constructor(private route: ActivatedRoute,
                public router: Router,
                private loanContractSearchService: LoanContractSearchService,
                private processEnquiryService: ProcessEnquiryService,
                private projectProposalService: ProjectProposalService,
                private messageService: MessageService,
                private authService: AuthService) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;

        this.processEnquiryService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            console.log('selectedEnquiryAction is', entity);
            this.selectedEnquiryAction = entity;
        });

        this.enquiryActionId = this.route.snapshot.params['enquiryActionId'];
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
        let title = 'Process Enquiry';
        title += (this.selectedEnquiry.loanContractId) ? ` : ${this.selectedEnquiry.loanContractId}` : 
            ` : ${this.selectedEnquiry.enquiryNo}`;
        title += ` / ${this.selectedEnquiry.projectName}`;
        return title;
    }

    /**
     * Send for approval
     */
    sendForApproval() {
        this.disableSendForApproval = true;
        this.processEnquiryService.getEnquiryCompletionDetails(this.enquiryActionId).subscribe({
            next: (enquiryCompletion) => {
                this.projectProposalService.getProjectProposalByStatus(this.enquiryActionId, 'Final').subscribe({
                    next: (projectProposals) => {
                        const proposals = projectProposals?._embedded?.projectProposals || [];
                        if (proposals.length > 0) {
                            const { firstName = '', lastName = '', email = '' } = this.authService.currentUser;
                            const name = `${firstName} ${lastName}`.trim();
                            this.messageService.showInfo('Please wait while attempting to send enquiry for approval.', 25000);

                            this.processEnquiryService.sendEnquiryActionForApproval(this.enquiryActionId, name, email).subscribe({
                                next: (response) => {
                                    this.processEnquiryService.selectedEntity$.next(response);
                                    this.messageService.showSuccess('Enquiry is sent for approval.');
                                },
                                error: () => {
                                    this.messageService.showError('Errors occurred. Please try again later or contact your system administrator.');
                                    this.disableSendForApproval = false;
                                }
                            });
                        } else {
                            this.messageService.showError('Project Proposal with status Final not found. Cannot send enquiry for approval.');
                            this.disableSendForApproval = false;
                        }
                    },
                    error: () => {
                        this.messageService.showError('Errors occurred. Please try again later or contact your system administrator.');
                        this.disableSendForApproval = false;
                    }
                });
        
            },
            error: () => {
                this.messageService.showError('Enquiry completion details not found. Cannot send enquiry for approval.');
                this.disableSendForApproval = false;
            }
        });
    }

    /**
     * On update project proposal click
     */
    onUpdateProjectProposalClick(projectProposal: any) {
        console.log('redirecting to update project proposal with projectProposal is', projectProposal);
        this.router.navigate(['/process-enquiry', this.enquiryActionId, 'loanApplication', this.loanApplicationId, 'update-project-proposal', 
            projectProposal.id]);
    }

    /**
     * On view project proposal click
     */
    onViewProjectProposalClick(projectProposal: any) {
        console.log('redirecting to view project proposal with projectProposal is', projectProposal);
        this.projectProposalService.selectedEntity$.next(projectProposal);
        this.router.navigate(['/process-enquiry', this.enquiryActionId, 'loanApplication', this.loanApplicationId, 'view-project-proposal', 
            projectProposal.id]);
    }
    
    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}