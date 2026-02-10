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
// import { TitleComponent } from '@fundamental-ngx/core';

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
        // TitleComponent
    ],
    templateUrl: './project-proposal.component.html'
})
export class ProjectProposalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    loanApplicationId: string = '';
    enquiryActionId: string = '';

    projectProposalOperation: string = 'Update';
    projectDetailOperation: string = 'Create';
    projectCostOperation: string = 'Create';
    otherLoanDetailsOperation: string = 'Create';
    dealGuaranteeOperation: string = 'Create';

    selectedProjectProposal: any = {};
    selectedProjectDetail: any = {};
    selectedProjectCost: any = {};
    selectedOtherLoanDetails: any = {};
    selectedDealGuarantee: any = {};

    selectedEnquiry: any;
    selectedPartner: any;
    selectedEnquiryAction: any;

    private destroy$ = new Subject<void>();

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private processEnquiryService: ProcessEnquiryService,
        private projectProposalService: ProjectProposalService,
        private authService: AuthService
    ) {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.enquiryActionId = this.route.snapshot.params['enquiryActionId'];
        console.log('loanApplicationId is', this.loanApplicationId);
        console.log('enquiryActionId is', this.enquiryActionId);
        
        // Set the selected enquiry and enquiry action
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.selectedPartner = this.loanContractSearchService.selectedEnquiry$.value.partner;
        console.log('selectedEnquiry is', this.selectedEnquiry);
        this.processEnquiryService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedEnquiryAction = entity;
            console.log('selectedEnquiryAction is', this.selectedEnquiryAction);
        });

        // Determine the operation and selected project proposal based on the current url
        let currentUrl = this.route.snapshot.url.join('/');
        console.log('Current URL:', currentUrl);
        if (currentUrl.includes('create-project-proposal')) {
            this.projectProposalOperation = 'Create';
            this.selectedProjectProposal['loanEnquiryNumber'] = this.selectedEnquiry.enquiryNo.id;
        } 
        else {
            // Set the selected project proposal
            var selectedProjectProposal = this.route.snapshot.data['routeResolvedData'].projectProposal;
            selectedProjectProposal['loanEnquiryNumber'] = this.selectedEnquiry.enquiryNo.id;
            this.selectedProjectProposal = selectedProjectProposal;
            if (currentUrl.includes('update-project-proposal')) {
                this.projectProposalOperation = 'Update';
            } 
            else {
                this.projectProposalOperation = 'View';
            }

            // Set the selected project details
            var selectedProjectDetail = this.route.snapshot.data['routeResolvedData'].projectDetail;
            if (!selectedProjectDetail) {
                selectedProjectDetail['projectName'] = this.selectedEnquiry.projectName;
                selectedProjectDetail['borrowerName'] = this.getBorrowerName();
                selectedProjectDetail['promoterName'] = this.selectedEnquiry.groupCompany;
                selectedProjectDetail['loanPurpose'] = this.selectedEnquiry.loanPurpose;
                selectedProjectDetail['projectCapacity'] = this.selectedEnquiry.projectCapacity;
                selectedProjectDetail['projectCapacityUnit'] = (this.selectedEnquiry.projectCapacityUnit && this.selectedEnquiry.projectCapacityUnit.trim())
                        ? this.selectedEnquiry.projectCapacityUnit : null;
                selectedProjectDetail['state'] = this.selectedEnquiry.projectLocationState;
                selectedProjectDetail['district'] = this.selectedEnquiry.projectDistrict;
                selectedProjectDetail['loanType'] = this.selectedEnquiry.loanType;
                selectedProjectDetail['loanClass'] = this.selectedEnquiry.loanClass;
                selectedProjectDetail['assistanceType'] = this.selectedEnquiry.assistanceType;
                selectedProjectDetail['financingType'] = this.selectedEnquiry.financingType;
                selectedProjectDetail['endUseOfFunds'] = this.selectedEnquiry.endUseOfFunds;
                selectedProjectDetail['roi'] = Number(this.selectedEnquiry.expectedInterestRate).toFixed(2);
                selectedProjectDetail['fees'] = this.selectedEnquiry.fees;
                selectedProjectDetail['tenorYear'] = this.selectedEnquiry.tenorYear;
                selectedProjectDetail['tenorMonths'] = this.selectedEnquiry.tenorMonth;
                selectedProjectDetail['moratoriumPeriod'] = this.selectedEnquiry.moratoriumPeriod;
                selectedProjectDetail['moratoriumPeriodUnit'] = (this.selectedEnquiry.moratoriumPeriodUnit && this.selectedEnquiry.moratoriumPeriodUnit.trim())
                        ? this.selectedEnquiry.moratoriumPeriodUnit : null;
                selectedProjectDetail['constructionPeriod'] = this.selectedEnquiry.constructionPeriod;
                selectedProjectDetail['constructionPeriodUnit'] = (this.selectedEnquiry.constructionPeriodUnit 
                        && this.selectedEnquiry.constructionPeriodUnit.trim()) ? this.selectedEnquiry.constructionPeriodUnit : null;
                selectedProjectDetail['projectTypeCoreSector'] = this.selectedEnquiry.projectTypeCoreSector;
                selectedProjectDetail['purposeOfLoan'] = this.selectedEnquiry.purposeOfLoan;
                selectedProjectDetail['projectType'] = this.selectedEnquiry.projectType;
                selectedProjectDetail['loanEnquiryDate'] = this.selectedEnquiry.loanEnquiryDate;
                selectedProjectDetail['policyExposure'] = this.selectedEnquiry.policyExposure;
                selectedProjectDetail['status'] = this.selectedProjectProposal.proposalStatus;
                this.selectedProjectDetail = selectedProjectDetail;
            } 
            else {
                this.projectDetailOperation = 'Update';
                this.selectedProjectDetail = selectedProjectDetail;
            }

            // Set the selected project cost details
            if (this.route.snapshot.data['routeResolvedData'].projectCost) {
                this.projectCostOperation = 'Update';
                this.selectedProjectCost = this.route.snapshot.data['routeResolvedData'].projectCost;
            }

            // Set the other loan details
            if (this.route.snapshot.data['routeResolvedData'].otherLoanDetails) {
                this.otherLoanDetailsOperation = 'Update';
                this.selectedOtherLoanDetails = this.route.snapshot.data['routeResolvedData'].otherLoanDetails;
            }

            // Set the deal guarantee timeline
            if (this.route.snapshot.data['routeResolvedData'].dealGuaranteeTimeline) {
                this.dealGuaranteeOperation = 'Update';
                this.selectedDealGuarantee = this.route.snapshot.data['routeResolvedData'].dealGuaranteeTimeline;
            }
        }
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
        let title = `${this.projectProposalOperation} Project Proposal`;
        title += ' (Enquiry';
        title += (this.selectedEnquiry.loanContractId) ? ` : ${this.selectedEnquiry.loanContractId}` : 
            ` : ${this.selectedEnquiry.enquiryNo}`;
        title += ` / ${this.selectedEnquiry.projectName}`;
        title += ')';
        return title;
    }

    /**
     * Go back
     */
    back(): void {
        this.router.navigate(['/process-enquiry', this.enquiryActionId, 'loanApplication', this.loanApplicationId]);
    }

    /**
     * Get the borrower name
     */
    getBorrowerName(): string {
        let name = this.selectedPartner.partyName1 + ' ';
        if (this.selectedPartner.partyName2) {
            name += this.selectedPartner.partyName2;
        }
        return name.trim();
    }
    
    /**
     * On project proposal create success
     */
    onProjectProposalCreateSuccess(response: any): void {
        // Redirect to the update project proposal route
        this.router.navigate(['/process-enquiry', this.enquiryActionId, 'loanApplication', this.loanApplicationId, 'update-project-proposal', response.id]);
    }

    /**
     * On project detail create success
     */
    onProjectDetailCreateSuccess(response: any): void {
        // Set the selected project detail to the response and set project detail operation to update
        this.selectedProjectDetail = response;
        this.projectDetailOperation = 'Update';
    }

    /**
     * On project detail update success
     */
    onProjectDetailUpdateSuccess(response: any): void {
        // Set the selected project detail to the response
        console.log('response is', response);
        this.selectedProjectDetail = response;
        this.projectProposalService.selectedEntity$.next(response.projectProposal);
        this.projectProposalService.selectedEntity$.next(response.projectProposal.enquiryAction);
    }

    /**
     * On project cost create success
     */
    onProjectCostCreateSuccess(response: any): void {
        // Set the selected project cost to the response
        this.selectedProjectCost = response;
        this.projectCostOperation = 'Update';
    }

    /**
     * On project cost update success
     */
    onProjectCostUpdateSuccess(response: any): void {
        // Set the selected project cost to the response
        this.selectedProjectCost = response;
    }

    /**
     * On other loan details create success
     */
    onOtherLoanDetailsCreateSuccess(response: any): void {
        // Set the selected other loan details to the response
        this.selectedOtherLoanDetails = response;
        this.otherLoanDetailsOperation = 'Update';
    }

    /**
     * On other loan details update success
     */
    onOtherLoanDetailsUpdateSuccess(response: any): void {
        // Set the selected other loan details to the response
        this.selectedOtherLoanDetails = response;
    }

    /**
     * On deal guarantee create success
     */
    onDealGuaranteeCreateSuccess(response: any): void {
        // Set the selected deal guarantee to the response
        this.selectedDealGuarantee = response;
        this.dealGuaranteeOperation = 'Update';
    }
    /**
     * On deal guarantee update success
     */
    onDealGuaranteeUpdateSuccess(response: any): void {
        // Set the selected deal guarantee to the response
        this.selectedDealGuarantee = response;
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    /**
     * Send for approval
     */
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
