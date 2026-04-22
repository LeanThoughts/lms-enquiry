import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, LayoutGridModule } from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { ProcessEnquiryService } from './process-enquiry.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';

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
                private processEnquiryService: ProcessEnquiryService) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.enquiryActionId = this.route.snapshot.params['enquiryActionId'];
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.processEnquiryService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            console.log('selectedEnquiryAction is', entity);
            this.selectedEnquiryAction = entity;
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
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}