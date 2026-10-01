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

    /**
     * Constructor
     */
    constructor(private loanContractSearchService: LoanContractSearchService,
                private processEnquiryService: ProcessEnquiryService,
                private iccInprincipleApprovalService: IccInprincipleApprovalService,
                private riskAssessmentService: RiskAssessmentService,
                private applicationFeeService: ApplicationFeeService,
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
            }
            else {
                this.selectedEnquiry.clear();
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
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}