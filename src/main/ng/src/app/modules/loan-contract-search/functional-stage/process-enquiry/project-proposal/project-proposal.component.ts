import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, LayoutGridModule } from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { ProcessEnquiryService } from '../process-enquiry.service';
import { LoanContractSearchService } from '../../../loan-contract-search.service';
import { GenericUpdateComponent } from '../../../../generic/generic-update/generic-update.component';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { GenericListComponent } from '../../../../generic/generic-list/generic-list.component';
import { ProjectProposalService } from './project-proposal.service';
import { AuthService } from '../../../../auth/auth.service';

@Component({
    selector: 'app-project-proposal',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        LayoutGridModule,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericUpdateComponent,
        GenericListComponent,
    ],
    templateUrl: './project-proposal.component.html'
})
export class ProjectProposalComponent implements OnInit, OnDestroy {

    // State variables
    disableSendForApproval = false;
    title = '';
    loanApplicationId = '';
    enquiryActionId = '';

    projectProposalOperation = 'Update';
    projectDetailOperation = 'Create';
    projectCostOperation = 'Create';
    otherLoanDetailsOperation = 'Create';
    dealGuaranteeOperation = 'Create';

    selectedProjectProposal: any = {};
    selectedProjectDetail: any = {};
    selectedProjectCost: any = {};
    selectedOtherLoanDetails: any = {};
    selectedDealGuarantee: any = {};
    selectedEnquiry: any;
    selectedPartner: any;
    selectedEnquiryAction: any;

    private destroy$ = new Subject<void>();

    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private processEnquiryService: ProcessEnquiryService,
        private projectProposalService: ProjectProposalService,
        private authService: AuthService
    ) {
        this.initializeRouteParams();
        this.subscribeToEnquiryAction();
        this.initializeDataBasedOnUrl();
    }

    private initializeRouteParams(): void {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.enquiryActionId = this.route.snapshot.params['enquiryActionId'];
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.selectedPartner = this.loanContractSearchService.selectedEnquiry$.value.partner;
    }

    private subscribeToEnquiryAction(): void {
        this.processEnquiryService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            console.log('updating selectedEnquiryAction in project proposal', entity);
            this.selectedEnquiryAction = entity;
        });
    }

    private initializeDataBasedOnUrl(): void {
        const currentUrl = this.route.snapshot.url.join('/');
        const resolvedData = this.route.snapshot.data['routeResolvedData'];

        if (currentUrl.includes('create-project-proposal')) {
            this.projectProposalOperation = 'Create';
            this.selectedProjectProposal['loanEnquiryNumber'] = this.selectedEnquiry.enquiryNo?.id;
        } 
        else {
            // Project Proposal
            const selectedProjectProposal = { ...resolvedData.projectProposal };
            selectedProjectProposal['loanEnquiryNumber'] = this.selectedEnquiry.enquiryNo?.id;
            this.selectedProjectProposal = selectedProjectProposal;
            this.projectProposalOperation =
                currentUrl.includes('update-project-proposal') ? 'Update' : 'View';

            // Project Details
            let selectedProjectDetail = resolvedData.projectDetail;
            if (!selectedProjectDetail) {
                selectedProjectDetail = this.mapEnquiryToProjectDetail();
            } else {
                this.projectDetailOperation = 'Update';
            }
            this.selectedProjectDetail = selectedProjectDetail;

            // Project Cost
            if (resolvedData.projectCost) {
                this.projectCostOperation = 'Update';
                this.selectedProjectCost = resolvedData.projectCost;
            }

            // Other Loan Details
            if (resolvedData.otherLoanDetails) {
                this.otherLoanDetailsOperation = 'Update';
                this.selectedOtherLoanDetails = resolvedData.otherLoanDetails;
            }

            // Deal Guarantee Timeline
            if (resolvedData.dealGuaranteeTimeline) {
                this.dealGuaranteeOperation = 'Update';
                this.selectedDealGuarantee = resolvedData.dealGuaranteeTimeline;
            }
        }
    }

    private mapEnquiryToProjectDetail(): any {
        const enquiry = this.selectedEnquiry || {};
        return {
            projectName: enquiry.projectName,
            borrowerName: this.getBorrowerName(),
            promoterName: enquiry.groupCompany,
            loanPurpose: enquiry.loanPurpose,
            projectCapacity: enquiry.projectCapacity,
            projectCapacityUnit: enquiry.projectCapacityUnit?.trim() ? enquiry.projectCapacityUnit : null,
            state: enquiry.projectLocationState,
            district: enquiry.projectDistrict,
            loanType: enquiry.loanType,
            loanClass: enquiry.loanClass,
            assistanceType: enquiry.assistanceType,
            financingType: enquiry.financingType,
            endUseOfFunds: enquiry.endUseOfFunds,
            roi: enquiry.expectedInterestRate ? Number(enquiry.expectedInterestRate).toFixed(2) : undefined,
            fees: enquiry.fees,
            tenorYear: enquiry.tenorYear,
            tenorMonths: enquiry.tenorMonth,
            moratoriumPeriod: enquiry.moratoriumPeriod,
            moratoriumPeriodUnit: enquiry.moratoriumPeriodUnit?.trim()
                ? enquiry.moratoriumPeriodUnit
                : null,
            constructionPeriod: enquiry.constructionPeriod,
            constructionPeriodUnit: enquiry.constructionPeriodUnit?.trim()
                ? enquiry.constructionPeriodUnit
                : null,
            projectTypeCoreSector: enquiry.projectTypeCoreSector,
            purposeOfLoan: enquiry.purposeOfLoan,
            projectType: enquiry.projectType,
            loanEnquiryDate: enquiry.loanEnquiryDate,
            policyExposure: enquiry.policyExposure,
            status: this.selectedProjectProposal?.proposalStatus,
        };
    }

    ngOnInit(): void {
        this.title = this.getTitle();
    }

    private getTitle(): string {
        const enquiry = this.selectedEnquiry || {};
        const proposalOp = this.projectProposalOperation;
        const contractId = enquiry.loanContractId
            ? ` : ${enquiry.loanContractId}`
            : ` : ${enquiry.enquiryNo}`;
        return `${proposalOp} Project Proposal (Enquiry${contractId} / ${enquiry.projectName})`;
    }

    back(): void {
        this.router.navigate([
            '/process-enquiry',
            this.enquiryActionId,
            'loanApplication',
            this.loanApplicationId,
        ]);
    }

    getBorrowerName(): string {
        const partner = this.selectedPartner || {};
        let name = partner.partyName1 || '';
        if (partner.partyName2) name += ' ' + partner.partyName2;
        return name.trim();
    }

    /**
     * Handle successful project proposal create
     */
    onProjectProposalCreateSuccess(response: any): void {
        this.router.navigate([
            '/process-enquiry',
            this.enquiryActionId,
            'loanApplication',
            this.loanApplicationId,
            'update-project-proposal',
            response.id,
        ]);
    }

    /**
     * Handle successful project proposal update
     */
    onProjectProposalUpdateSuccess(response: any): void {
        this.projectProposalService.selectedEntity$.next(response.projectProposal);
    }

    onProjectDetailCreateSuccess(response: any): void {
        this.selectedProjectDetail = response;
        this.projectDetailOperation = 'Update';
    }

    /**
     * Handle successful project detail update
     */
    onProjectDetailUpdateSuccess(response: any): void {
        this.selectedProjectDetail = response;
        // Is it really correct to call next() twice? Consider refactoring in future.
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful project cost create
     */
    onProjectCostCreateSuccess(response: any): void {
        this.selectedProjectCost = response;
        this.projectCostOperation = 'Update';
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful project cost update
     */
    onProjectCostUpdateSuccess(response: any): void {
        console.log('updating project cost', response);
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful other loan details create
     */
    onOtherLoanDetailsCreateSuccess(response: any): void {
        this.selectedOtherLoanDetails = response;
        this.otherLoanDetailsOperation = 'Update';
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful other loan details update
     */
    onOtherLoanDetailsUpdateSuccess(response: any): void {
        this.selectedOtherLoanDetails = response;
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful deal guarantee create
     */
    onDealGuaranteeCreateSuccess(response: any): void {
        this.selectedDealGuarantee = response;
        this.dealGuaranteeOperation = 'Update';
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle successful deal guarantee update
     */
    onDealGuaranteeUpdateSuccess(response: any): void {
        this.selectedDealGuarantee = response;
        if (response.projectProposal) {
            this.projectProposalService.selectedEntity$.next(response.projectProposal);
            this.processEnquiryService.selectedEntity$.next(response.projectProposal.enquiryAction);
        }
    }

    /**
     * Handle destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    sendForApproval(): void {
        console.log(this.authService.currentUser);
        // this.disableSendForApproval = true;
        // this.processEnquiryService.getEnquiryCompletionDetails(this.enquiryActionId).subscribe(data => {
        //     if (Object.keys(data).length > 0) {
        //         this.projectProposalService.getProjectProposalByStatus(this.enquiryActionId, 'Final').subscribe(pp => {
        //             if (pp._embedded.projectProposals.length > 0) {
        //                 let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        //                 let email = this._appService.currentUser.email;
        //                 this._matSnackBar.open('Please wait while attempting to send enquiry for approval.', 'OK', { duration: 25000 });
        //                 this._enquiryActionService.sendEnquiryActionForApproval(this.enquiryAction.id, name, email).subscribe(
        //                     response => {
        //                         this.enquiryAction = response;
        //                         this._matSnackBar.dismiss();
        //                         this._matSnackBar.open('Enquiry is sent for approval.', 'OK', { duration: 7000 });
        //                     },
        //                     error => {
        //                         this.disableSendForApproval = false;
        //                         this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator',
        //                             'OK', { duration: 7000 });
        //                         this.disableSendForApproval = false;
        //                     });
        //                 this.disableSendForApproval = true;
        //                 this._location.back();
        //             }
        //             else {
        //                 this._matSnackBar.open('Project Proposal with status Final not found. Cannot send enquiry for approval.',
        //                 'OK', { duration: 7000 });
        //                 this.disableSendForApproval = false;
        //             }
        //         });
        //     }
        //     else {
        //         // Activate the 5th tab (index 4) before showing the snackbar
        //         if (this.tabGroup) {
        //             this.tabGroup.selectedIndex = 4;
        //         }
        //         this._matSnackBar.open('Data for enquiry completion is missing. Cannot send enquiry for approval.',
        //             'OK', { duration: 7000 });
        //     }
        // })
    }
}