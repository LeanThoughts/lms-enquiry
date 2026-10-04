import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { 
    ButtonComponent, 
    DatePickerModule, 
    DynamicPageComponent, 
    DynamicPageContentComponent, 
    DynamicPageGlobalActionsComponent, 
    DynamicPageHeaderComponent, 
    LayoutGridModule, 
    PaginationModule, 
    PanelModule,
    SelectModule,
    TableModule,
    ToolbarComponent
} from '@fundamental-ngx/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SelectionModel } from '@angular/cdk/collections';
import { LoanContractListComponent } from "./loan-contract-list/loan-contract-list.component";
import { firstValueFrom, Subject } from 'rxjs';
import { LoanContractSearchService } from './loan-contract-search.service';
import { takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ProcessEnquiryService } from './functional-stage/process-enquiry/process-enquiry.service';
import { MessageService } from '../../message.service';
import { IccInprincipleApprovalService } from './functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service';
import { RiskAssessmentService } from './functional-stage/risk-assessment/risk-assessment.service';
import { ApplicationFeeService } from './functional-stage/application-fee/application-fee.service';
import { BmcApprovalService } from './functional-stage/bmc-approval/bmc-approval.service';
import { BoardApprovalService } from './functional-stage/board-approval/board-approval.service';
import { SanctionService } from './functional-stage/sanction/sanction.service';

@Component({
    selector: 'app-loan-contract-search',
    imports: [
        // Dynamic Page Components
        DynamicPageHeaderComponent,
        DynamicPageComponent,
        DynamicPageGlobalActionsComponent,
        ToolbarComponent,
        DynamicPageContentComponent,
        // Other Components and Modules
        ButtonComponent,
        CommonModule,
        DatePickerModule,
        FormsModule,
        LayoutGridModule,
        PaginationModule,
        PanelModule,
        ReactiveFormsModule,
        SelectModule,
        TableModule,
        LoanContractListComponent
    ],
    templateUrl: './loan-contract-search.component.html',
    styleUrl: './loan-contract-search.component.scss'
})
export class LoanContractSearchComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    selectedEnquiry: SelectionModel<any> = new SelectionModel<any>(false, []);

    private static readonly DEFAULT_SUBTITLE = 'Search loan contracts, select one and open a functional stage';

    subtitle: string = LoanContractSearchComponent.DEFAULT_SUBTITLE;

    // Functional stages that can be opened for the selected enquiry, in the order of the loan life cycle
    readonly stages = [
        { label: 'Process Enquiry', open: () => this.redirectToProcessEnquiry() },
        { label: 'ICC Approval', open: () => this.redirectToICCApproval() },
        { label: 'Risk Assessment', open: () => this.redirectToRiskAssessment() },
        { label: 'Application Fee', open: () => this.redirectToApplicationFee() },
        { label: 'BMC Approval', open: () => this.redirectToBmcApproval() },
        { label: 'Board Approval', open: () => this.redirectToBoardApproval() },
        { label: 'Sanction', open: () => this.redirectToSanction() },
    ];

    /**
     * Constructor
     */
    constructor(private loanContractSearchService: LoanContractSearchService,
                private processEnquiryService: ProcessEnquiryService,
                private iccInprincipleApprovalService: IccInprincipleApprovalService,
                private riskAssessmentService: RiskAssessmentService,
                private applicationFeeService: ApplicationFeeService,
                private bmcApprovalService: BmcApprovalService,
                private boardApprovalService: BoardApprovalService,
                private sanctionService: SanctionService,
                public router: Router,
                private messageService: MessageService)
    {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        this.loanContractSearchService.selectedEnquiry$.pipe(takeUntil(this.destroy$)).subscribe((enquiry) => {
            if (enquiry) {
                this.selectedEnquiry.select(enquiry.loanApplication.enquiryNo.id);
                const loanApplication = enquiry.loanApplication;
                this.subtitle = `Selected: Enquiry No: ${loanApplication.enquiryNo.id}`
                    + (loanApplication.loanContractId ? ` / Loan Contract: ${loanApplication.loanContractId}` : '')
                    + (loanApplication.projectName ? ` / ${loanApplication.projectName}` : '');
            }
            else {
                this.selectedEnquiry.clear();
                this.subtitle = LoanContractSearchComponent.DEFAULT_SUBTITLE;
            }
        });
    }

    /**
     * Redirect to process enquiry
     */
    redirectToProcessEnquiry(): void {
        this.processEnquiryService.getEnquiryAction(this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id).subscribe({
            next: (enquiryAction) => {
                this.processEnquiryService.selectedEntity$.next(enquiryAction);
                this.router.navigate(['/process-enquiry', enquiryAction.id, 'loanApplication', 
                    this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id]);
            },
            error: (error) => {
                this.processEnquiryService.selectedEntity$.next(null);
                this.router.navigate(['/process-enquiry', '', 'loanApplication', this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id]);
            }
        });
    }

    /**
     * Redirect to ICC approval
     */
    async redirectToICCApproval(): Promise<void> {
        try {
            // Determine the loan application id from the selected enquiry
            const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;

            // Fetch enquiry action
            const enquiryAction = await firstValueFrom(
                this.processEnquiryService.getEnquiryAction(loanApplication.id)
            );
    
            // Early validation of functionalStatus and workFlowStatusCode
            const functionalStatus: number = loanApplication.functionalStatus;
            const workFlowStatusCode: number = enquiryAction.workFlowStatusCode;
            if (functionalStatus < 1 || workFlowStatusCode !== 3) {
                this.messageService.showError('Enquiry stage is still under process. ICC In-principle approval cannot be started until enquiry ' +
                    'stage is completed.');
                return;
            }
    
            // Fetch enquiry completion details only if above conditions are met. If no 404 error, then redirect to ICC Inprinciple Approval stage
            console.log('fetching enquiry completion details');
            await firstValueFrom(
                this.processEnquiryService.getEnquiryCompletionDetails(enquiryAction.id)
            );
            console.log('enquiry completion details fetched');
            try {
                const iccInprincipleApproval = await firstValueFrom(
                    this.iccInprincipleApprovalService.getIccInprincipleApproval(loanApplication.id)
                );
                console.log('icc inprinciple approval fetched');
                this.iccInprincipleApprovalService.selectedEntity$.next(iccInprincipleApproval);
                this.router.navigate(['/icc-inprinciple-approval', iccInprincipleApproval.id, 'loanApplication', loanApplication.id]);
            } 
            catch (error: any) {
                console.log('error in fetching icc inprinciple approval', error);
                if (error.status === 404) {
                    this.iccInprincipleApprovalService.selectedEntity$.next(null);
                    this.router.navigate(['/icc-inprinciple-approval', '', 'loanApplication', loanApplication.id]);
                }
            }
        } 
        catch (error: any) {
            console.error('Error in redirectToICCApproval:', error);
            const message =
                error.status === 404
                    ? 'Enquiry process and Enquiry completion should be completed and approved before ICC In-principle approval.'
                    : 'An error occurred while processing the enquiry.';
            this.messageService.showError(message);
        }
    }

    /**
     * Redirect to Prelim Risk Assessment
     */
    async redirectToRiskAssessment(): Promise<void> {
        const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        const iccIncompleteMessage = 'Please complete ICC In-principle approval before starting Prelim Risk Assessment.';

        let iccApproval: any;
        try {
            iccApproval = await firstValueFrom(this.iccInprincipleApprovalService.getIccInprincipleApproval(loanApplication.id));
        }
        catch (error: any) {
            this.messageService.showError(iccIncompleteMessage);
            return;
        }

        const approvalsByIcc = await firstValueFrom(this.iccInprincipleApprovalService.getApprovalByIcc(iccApproval.id));
        const approvalByIcc = approvalsByIcc?.[0];
        if (!approvalByIcc?.edApprovalDate || !approvalByIcc?.cfoApprovalDate) {
            this.messageService.showError('ED Approval Date and CFO Approval Date must be set before proceeding to Risk Assessment.');
            return;
        }

        // Functional status 2 - ICC In-Principle Approval Stage, 10 - Prelim Risk Assessment Stage. Workflow status 3 - Approved.
        const functionalStatus: number = Number(loanApplication.functionalStatus);
        if ((functionalStatus !== 2 && functionalStatus !== 10) || iccApproval.workFlowStatusCode !== 3) {
            this.messageService.showError(iccIncompleteMessage);
            return;
        }

        try {
            const riskAssessment = await firstValueFrom(this.riskAssessmentService.getRiskAssessment(loanApplication.id));
            this.riskAssessmentService.selectedEntity$.next(riskAssessment);
            this.router.navigate(['/risk-assessment', riskAssessment.id, 'loanApplication', loanApplication.id]);
        }
        catch (error: any) {
            if (error.status === 404) {
                this.riskAssessmentService.selectedEntity$.next(null);
                this.router.navigate(['/risk-assessment', '', 'loanApplication', loanApplication.id]);
            }
            else {
                this.messageService.showError('An error occurred while processing the enquiry.');
            }
        }
    }

    /**
     * Redirect to Application Fee
     */
    async redirectToApplicationFee(): Promise<void> {
        const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        const riskAssessmentIncompleteMessage = 'Please complete Prelim Risk Assessment approval before starting Application Fee.';

        let riskAssessment: any;
        try {
            riskAssessment = await firstValueFrom(this.riskAssessmentService.getRiskAssessment(loanApplication.id));
        }
        catch (error: any) {
            this.messageService.showError(error.status === 404 ? riskAssessmentIncompleteMessage : 'An error occurred while processing the enquiry.');
            return;
        }

        // Functional status 10 - Prelim Risk Assessment Stage. Workflow status 3 - Approved.
        if (Number(loanApplication.functionalStatus) < 10 || riskAssessment.workFlowStatusCode !== 3) {
            this.messageService.showError(riskAssessmentIncompleteMessage);
            return;
        }

        try {
            const applicationFee = await firstValueFrom(this.applicationFeeService.getApplicationFee(loanApplication.id));
            this.applicationFeeService.selectedEntity$.next(applicationFee);
            this.router.navigate(['/application-fee', applicationFee.id, 'loanApplication', loanApplication.id]);
        }
        catch (error: any) {
            if (error.status === 404) {
                this.applicationFeeService.selectedEntity$.next(null);
                this.router.navigate(['/application-fee', '', 'loanApplication', loanApplication.id]);
            }
            else {
                this.messageService.showError('An error occurred while processing the enquiry.');
            }
        }
    }

    /**
     * Redirect to BMC Approval
     */
    async redirectToBmcApproval(): Promise<void> {
        const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;

        try {
            await firstValueFrom(this.bmcApprovalService.getLoanAppraisal(loanApplication.id));
        }
        catch (error: any) {
            this.messageService.showError(error.status === 404 ? 'Appraisal stage is not completed for loan enquiry.' : 
                'An error occurred while processing the enquiry.');
            return;
        }

        try {
            const bmcApproval = await firstValueFrom(this.bmcApprovalService.getBmcIccApproval(loanApplication.id));
            this.bmcApprovalService.selectedEntity$.next(bmcApproval);
            this.router.navigate(['/bmc-approval', bmcApproval.id, 'loanApplication', loanApplication.id]);
        }
        catch (error: any) {
            if (error.status === 404) {
                this.bmcApprovalService.selectedEntity$.next(null);
                this.router.navigate(['/bmc-approval', '', 'loanApplication', loanApplication.id]);
            }
            else {
                this.messageService.showError('An error occurred while processing the enquiry.');
            }
        }
    }

    /**
     * Redirect to Board Approval
     */
    redirectToBoardApproval(): void {
        const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.boardApprovalService.getBoardApproval(loanApplication.id).subscribe({
            next: (boardApproval) => {
                this.boardApprovalService.selectedEntity$.next(boardApproval);
                this.router.navigate(['/board-approval', boardApproval.id, 'loanApplication', loanApplication.id]);
            },
            error: (error) => {
                if (error.status === 404) {
                    this.boardApprovalService.selectedEntity$.next(null);
                    this.router.navigate(['/board-approval', '', 'loanApplication', loanApplication.id]);
                }
                else {
                    this.messageService.showError('An error occurred while processing the enquiry.');
                }
            }
        });
    }

    /**
     * Redirect to Sanction
     */
    async redirectToSanction(): Promise<void> {
        const loanApplication = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        const boardApprovalIncompleteMessage = 'Board approval workflow not completed for loan.';

        // Functional status 1 - Enquiry, 2 - ICC, 3 - Appraisal, 4 - Board Approval, 5 - Sanction, 11 - Application Fee
        const functionalStatus: number = Number(loanApplication.functionalStatus);
        if (functionalStatus === 4) {
            let boardApproval: any;
            try {
                boardApproval = await firstValueFrom(this.sanctionService.getBoardApproval(loanApplication.id));
            }
            catch (error: any) {
                this.messageService.showError(error.status === 404 ? boardApprovalIncompleteMessage : 'An error occurred while processing the enquiry.');
                return;
            }
            // Workflow status 1 - Draft, 2 - Under Approval, 3 - Approved, 4 - Rejected
            if (boardApproval.workFlowStatusCode === 4) {
                this.messageService.showError('Board Approval is rejected. Sanction not possible.');
                return;
            }
            if (boardApproval.workFlowStatusCode !== 3) {
                this.messageService.showError(boardApprovalIncompleteMessage);
                return;
            }
        }
        else if (functionalStatus === 11) {
            const approvalByBoards = await firstValueFrom(this.sanctionService.getApprovalByBoards(loanApplication.id));
            if (approvalByBoards.length === 0) {
                this.messageService.showError(boardApprovalIncompleteMessage);
                return;
            }
        }
        else if (![1, 2, 3, 5].includes(functionalStatus)) {
            this.messageService.showError('Sanction cannot be started at the current stage of the loan.');
            return;
        }

        try {
            const sanction = await firstValueFrom(this.sanctionService.getSanction(loanApplication.id));
            this.sanctionService.selectedEntity$.next(sanction);
            this.router.navigate(['/sanction', sanction.id, 'loanApplication', loanApplication.id]);
        }
        catch (error: any) {
            if (error.status === 404) {
                this.sanctionService.selectedEntity$.next(null);
                this.router.navigate(['/sanction', '', 'loanApplication', loanApplication.id]);
            }
            else {
                this.messageService.showError('An error occurred while processing the enquiry.');
            }
        }
    }
    
    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}