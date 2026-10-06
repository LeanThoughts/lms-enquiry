import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
    CardModule,
    DialogService,
    DynamicPageComponent,
    DynamicPageContentComponent,
    DynamicPageGlobalActionsComponent,
    DynamicPageHeaderComponent,
    FormControlComponent,
    IconComponent,
    SegmentedButtonComponent,
    TableModule,
    ToolbarComponent,
} from '@fundamental-ngx/core';
import { ButtonComponent } from '@fundamental-ngx/core';
import { InboxService } from './inbox.service';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, takeUntil } from 'rxjs/operators';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { MessageService } from '../../message.service';
import { CustomDialogComponent } from '../../custom-dialog.component';
import { BusinessPartnerSearchService } from '../business-partner-search/business-partner-search.service';
import { ProcessEnquiryService } from '../loan-contract-search/functional-stage/process-enquiry/process-enquiry.service';
import { LoanContractSearchService } from '../loan-contract-search/loan-contract-search.service';
import { IccInprincipleApprovalService } from '../loan-contract-search/functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service';
import { RiskAssessmentService } from '../loan-contract-search/functional-stage/risk-assessment/risk-assessment.service';
import { ApplicationFeeService } from '../loan-contract-search/functional-stage/application-fee/application-fee.service';
import { BoardApprovalService } from '../loan-contract-search/functional-stage/board-approval/board-approval.service';
import { SanctionService } from '../loan-contract-search/functional-stage/sanction/sanction.service';
import { ReferenceInterestRateService } from '../reference-interest-rate/reference-interest-rate.service';
import { getProcessColor, getProcessLabel, getTaskAgeInDays, getTaskAgeLabel, TASK_OVERDUE_DAYS } from './inbox.constants';

interface StageReview {
    route: string;
    selectedEntity$: BehaviorSubject<any>;
    getEntity: (loanApplicationId: string) => Observable<any>;
}

interface ProcessSummary {
    processName: string;
    count: number;
}

type InboxViewMode = 'table' | 'cards';

const VIEW_MODE_STORAGE_KEY = 'inbox.viewMode';

@Component({
    selector: 'app-inbox',
    imports: [
        ButtonComponent,
        CardModule,
        CommonModule,
        FormsModule,
        SegmentedButtonComponent,
        DynamicPageComponent,
        DynamicPageContentComponent,
        DynamicPageGlobalActionsComponent,
        DynamicPageHeaderComponent,
        FormControlComponent,
        IconComponent,
        TableModule,
        ToolbarComponent,
    ],
    templateUrl: './inbox.component.html',
    styleUrl: './inbox.component.scss'
})
export class InboxComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    isApproveDisabled = false;
    isRejectDisabled = false;
    tasks: any[] = [];
    filteredTasks: any[] = [];
    processSummary: ProcessSummary[] = [];
    selectedProcess: string | null = null;
    searchTerm = '';
    viewMode: InboxViewMode = localStorage.getItem(VIEW_MODE_STORAGE_KEY) === 'cards' ? 'cards' : 'table';

    /**
     * Constructor
     */
    constructor(private activatedRoute: ActivatedRoute,
                private dialogService: DialogService,
                private inboxService: InboxService,
                private messageService: MessageService,
                private router: Router,
                private businessPartnerService: BusinessPartnerSearchService,
                private loanContractSearchService: LoanContractSearchService,
                private processEnquiryService: ProcessEnquiryService,
                private iccInprincipleApprovalService: IccInprincipleApprovalService,
                private riskAssessmentService: RiskAssessmentService,
                private applicationFeeService: ApplicationFeeService,
                private boardApprovalService: BoardApprovalService,
                private sanctionService: SanctionService,
                private referenceInterestRateService: ReferenceInterestRateService) {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        this.selectedProcess = this.activatedRoute.snapshot.queryParamMap.get('process');
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((data: any) => {
            this.setTasks(data.routeResolver.tasks);
        });
    }

    /**
     * Refresh tasks
     */
    refreshTasks() {
        this.inboxService.getTasks().subscribe((data: any) => {
            this.setTasks(data);
        });
    }

    /**
     * Store the tasks and rebuild the per process summary and the filtered list
     */
    private setTasks(tasks: any[]): void {
        this.tasks = tasks || [];
        const counts = new Map<string, number>();
        this.tasks.forEach(task => counts.set(task.processName, (counts.get(task.processName) || 0) + 1));
        this.processSummary = Array.from(counts, ([processName, count]) => ({ processName, count }));
        if (this.selectedProcess !== null && !counts.has(this.selectedProcess)) {
            this.selectedProcess = null;
        }
        this.applyFilters();
    }

    /**
     * Switch between the table and card views and remember the choice
     */
    setViewMode(viewMode: InboxViewMode): void {
        if (viewMode !== 'table' && viewMode !== 'cards') {
            return;
        }
        this.viewMode = viewMode;
        localStorage.setItem(VIEW_MODE_STORAGE_KEY, viewMode);
    }

    /**
     * Filter tasks by process
     */
    selectProcess(processName: string | null): void {
        this.selectedProcess = processName;
        this.applyFilters();
    }

    /**
     * Filter tasks by search term
     */
    onSearch(searchTerm: string): void {
        this.searchTerm = searchTerm;
        this.applyFilters();
    }

    private applyFilters(): void {
        const term = this.searchTerm.trim().toLowerCase();
        this.filteredTasks = this.tasks.filter(task =>
            (this.selectedProcess === null || task.processName === this.selectedProcess) &&
            (term === '' || [task.projectName, task.lanContractId, task.requestorName, task.requestorEmail, task.processName]
                .some(value => value && value.toString().toLowerCase().includes(term))));
    }

    getProcessLabel(processName: string): string {
        return getProcessLabel(processName);
    }

    getProcessColor(processName: string): number {
        return getProcessColor(processName);
    }

    isOverdue(requestDate: string): boolean {
        return getTaskAgeInDays(requestDate) > TASK_OVERDUE_DAYS;
    }

    getInitials(name: string): string {
        if (!name) {
            return '?';
        }
        const parts = name.trim().split(/\s+/);
        return (parts[0].charAt(0) + (parts.length > 1 ? parts[parts.length - 1].charAt(0) : '')).toUpperCase();
    }

    getAgeLabel(requestDate: string): string {
        return getTaskAgeLabel(requestDate);
    }

    /**
     * Approve task
     */
    approveTask(task: any): void {
        this.isApproveDisabled = true;
        let workFlowProcessRequestResource = {
            'businessProcessId': task.businessProcessId,
            'processName': task.processName,
            'processInstanceId': task.id,
            'rejectionReason': ''
        }
        this.messageService.showInfo('Approval in Process.', 25000);
        this.inboxService.approveTask(workFlowProcessRequestResource).subscribe({
            next: (response) => {
                this.isApproveDisabled = false;
                this.refreshTasks();
                this.messageService.showSuccess('Selected task is approved and email notification was sent to requestor');
            },
            error: (error) => {
                this.isApproveDisabled = false;
                this.messageService.showError(error.message + '!! Error approving task. Please try again. If the problem persists, '
                    + 'please contact the administrator.');
            }
        });
    }

    /**
     * Review task
     */
    reviewTask(task: any): void {
        if (task.processName === 'BusinessPartner') {
            this.messageService.showInfo('Review in Process.');
            this.businessPartnerService.getBusinesPartner(task.businessProcessId).subscribe({
                next: (data: any) => {
                    const businessPartner = data[0];
                    this.businessPartnerService.selectedEntity$.next(businessPartner);
                    this.router.navigate(['/business-partners/update', businessPartner.id]);
                },
                error: (error: any) => this.showReviewError(error)
            });
        }
        else if (task.processName === 'CollateralManagement') {
            this.router.navigate(['/collateral-management/checklist', task.businessProcessId]);
        }
        else if (task.processName === 'ReferenceInterestRateValue') {
            this.referenceInterestRateService.referenceInterestRateTypeCode = task.lanContractId.split(':')[0];
            this.router.navigate(['/reference-interest-rates']);
        }
        else {
            const stageReview = this.getStageReview(task.processName);
            if (!stageReview) {
                this.messageService.showError('Review of ' + task.processName + ' tasks is not available.');
                return;
            }
            this.messageService.showInfo('Review in Process.');
            this.inboxService.getLoanApplicationByLoanContractId(task.lanContractId).pipe(
                switchMap((response: any) => {
                    this.loanContractSearchService.selectedEnquiry$.next(response);
                    return stageReview.getEntity(response.loanApplication.id);
                })
            ).subscribe({
                next: (entity: any) => {
                    const loanApplicationId = this.loanContractSearchService.selectedEnquiry$.value.loanApplication.id;
                    stageReview.selectedEntity$.next(entity);
                    this.router.navigate([stageReview.route, entity.id, 'loanApplication', loanApplicationId]);
                },
                error: (error: any) => this.showReviewError(error)
            });
        }
    }

    /**
     * Return the stage page and entity lookup for a workflow process name
     */
    private getStageReview(processName: string): StageReview | null {
        switch (processName) {
            case 'Process Enquiry':
                return {
                    route: '/process-enquiry',
                    selectedEntity$: this.processEnquiryService.selectedEntity$,
                    getEntity: (id) => this.processEnquiryService.getEnquiryAction(id)
                };
            case 'ICC In-Principal Approval':
                return {
                    route: '/icc-inprinciple-approval',
                    selectedEntity$: this.iccInprincipleApprovalService.selectedEntity$,
                    getEntity: (id) => this.iccInprincipleApprovalService.getIccInprincipleApproval(id)
                };
            case 'Prelim Risk Assessment':
                return {
                    route: '/risk-assessment',
                    selectedEntity$: this.riskAssessmentService.selectedEntity$,
                    getEntity: (id) => this.riskAssessmentService.getRiskAssessment(id)
                };
            case 'Application Fee':
                return {
                    route: '/application-fee',
                    selectedEntity$: this.applicationFeeService.selectedEntity$,
                    getEntity: (id) => this.applicationFeeService.getApplicationFee(id)
                };
            case 'Board Approval':
                return {
                    route: '/board-approval',
                    selectedEntity$: this.boardApprovalService.selectedEntity$,
                    getEntity: (id) => this.boardApprovalService.getBoardApproval(id)
                };
            case 'Sanction':
                return {
                    route: '/sanction',
                    selectedEntity$: this.sanctionService.selectedEntity$,
                    getEntity: (id) => this.sanctionService.getSanction(id)
                };
            default:
                return null;
        }
    }

    /**
     * Show review error
     */
    private showReviewError(error: any): void {
        this.messageService.showError(error.message + '!! Error opening task for review. Please try again. If the problem persists, '
            + 'please contact the administrator.');
    }

    /**
     * Reject task
     */
    rejectTask(task: any): void {
        const dialogRef = this.dialogService.open(CustomDialogComponent, {
            data: {
                title: 'Reject Task',
                description: 'Please enter the rejection reason for the task.',
                displayInput: true,
                inputRequired: true,
                facts: []
            },
            width: '500px'
        });
        dialogRef.afterClosed.subscribe({
            next: (result: any) => {
                if (result.continue) {
                    let workFlowProcessRequestResource = {
                        'businessProcessId': task.businessProcessId,
                        'processName': task.processName,
                        'processInstanceId': task.id,
                        'rejectionReason': result.inputValue
                    }
                    this.isRejectDisabled = true;
                    this.messageService.showInfo('Reject in Process.', 25000);
                    this.inboxService.rejectTask(workFlowProcessRequestResource).subscribe({
                        next: (response) => {
                            this.isRejectDisabled = false;
                            this.messageService.showSuccess('Selected task is rejected and email notification was sent to requestor');
                            this.refreshTasks();
                        },
                        error: (error) => {
                            this.isRejectDisabled = false;
                            this.messageService.showError(error.message + '!! Error rejecting task. Please try again. '
                                + 'If the problem persists, please contact the administrator.');
                        }
                    });
    
                }
            },
            error: (error: any) => {
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