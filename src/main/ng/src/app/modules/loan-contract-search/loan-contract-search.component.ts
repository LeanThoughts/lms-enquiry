import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../common/component-ngx/component-ngx.component';
import { 
    ButtonComponent, 
    DatePickerModule, 
    LayoutGridModule, 
    PaginationModule, 
    PanelModule,
    SelectModule,
    TableModule
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

@Component({
    selector: 'app-loan-contract-search',
    imports: [
        ButtonComponent,
        CommonModule,
        ComponentNgxComponent,
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
    constructor(

        private loanContractSearchService: LoanContractSearchService,
        private processEnquiryService: ProcessEnquiryService,
        private iccInprincipleApprovalService: IccInprincipleApprovalService,
        private router: Router,
        private messageService: MessageService,
    ) {}

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
        // Check if the loan contract id is not set, then redirect to process enquiry, 
        // else do not redirect and show error message that the loan has already completed the enquiry phase
        if (!this.loanContractSearchService.selectedEnquiry$.value.loanApplication.loanContractId) {
            this.processEnquiryService.getEnquiryAction(this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id).subscribe({
                next: (enquiryAction) => {
                    this.router.navigate(['/process-enquiry', enquiryAction.id, 'loanApplication', 
                        this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id]);
                },
                error: (error) => {
                    this.router.navigate(['/process-enquiry', '', 'loanApplication', this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id]);
                }
            });
        }
        else {
            this.messageService.showError('Selected loan has already completed the enquiry phase !');
        }
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
                this.router.navigate(['/icc-inprinciple-approval', iccInprincipleApproval.id, 'loanApplication', loanApplication.id]);
            } 
            catch (error: any) {
                console.log('error in fetching icc inprinciple approval', error);
                if (error.status === 404) {
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
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
