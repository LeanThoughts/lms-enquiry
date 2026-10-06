import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    IconComponent,
    MessageStripComponent,
    ObjectStatusComponent,
    TableModule,
    ToolbarComponent,
    ToolbarSeparatorComponent
} from '@fundamental-ngx/core';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { CollateralService } from '../collateral.service';
import {
    COMPLIANCE_STATUS_STATE,
    CollateralAccess,
    CollateralChecklist,
    CollateralItem,
    LoanSummary,
    ValueLists,
    WORKFLOW_STATUS,
    formatDate,
    valueDescription,
    workflowState
} from '../collateral.model';
import { ConfirmDialogComponent, ConfirmDialogData } from '../confirm-dialog/confirm-dialog.component';

/**
 * Collaterals of a loan (SAP "PFS Check List", checklist conditions). Double-click opens a collateral.
 */
@Component({
    selector: 'app-collateral-list',
    templateUrl: './collateral-list.component.html',
    styleUrl: './collateral-list.component.scss',
    imports: [
        DynamicPageModule,
        ToolbarComponent,
        ToolbarSeparatorComponent,
        ButtonComponent,
        IconComponent,
        MessageStripComponent,
        ObjectStatusComponent,
        TableModule
    ]
})
export class CollateralListComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly formatDate = formatDate;

    loanApplicationId = '';
    /** Set when the page is opened from a workflow task (route collateral-management/checklist/:checklistId). */
    checklistId = '';
    checklist: CollateralChecklist | null = null;
    loan: LoanSummary | null = null;
    items: CollateralItem[] = [];
    lists: ValueLists = {};
    access: CollateralAccess | null = null;
    selected: CollateralItem | null = null;

    /** Message shown with the hourglass while waiting; empty when idle. */
    waitMessage = '';
    loadFailed = false;

    constructor(
        private activatedRoute: ActivatedRoute,
        private collateralService: CollateralService,
        private messageService: MessageService,
        private dialogService: DialogService,
        public router: Router
    ) {}

    ngOnInit(): void {
        this.activatedRoute.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
            this.loanApplicationId = params.get('loanApplicationId') ?? '';
            this.checklistId = params.get('checklistId') ?? '';
            this.load();
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get canWrite(): boolean {
        return !!this.access?.canWrite;
    }

    /** True while the checklist waits for approval: collaterals cannot be created, changed or deleted. */
    get inApproval(): boolean {
        return this.checklist?.workFlowStatusCode === WORKFLOW_STATUS.SENT_FOR_APPROVAL;
    }

    /** Create, change and delete: write role and not waiting for approval. */
    get canChange(): boolean {
        return this.canWrite && !this.inApproval;
    }

    /** Send for Approval: write role, at least one collateral, not already waiting for approval. */
    get canSendForApproval(): boolean {
        return this.canWrite && !!this.checklist?.id && this.items.length > 0 && !this.inApproval;
    }

    get workflowState(): 'positive' | 'negative' | 'critical' | 'informative' {
        return workflowState(this.checklist?.workFlowStatusCode);
    }

    get loanLabel(): string {
        return this.loan?.loanContractId || (this.loan?.enquiryNo ? `Enquiry ${this.loan.enquiryNo}` : '');
    }

    get compliedCount(): number {
        return this.items.filter(item => item.complianceStatus === '1').length;
    }

    load(): void {
        this.waitMessage = this.loanLabel
            ? `Loading collaterals of loan ${this.loanLabel}. Please wait`
            : 'Loading collaterals. Please wait';
        this.loadFailed = false;
        forkJoin({
            checklist: this.checklistId && !this.loanApplicationId
                ? this.collateralService.getChecklistById(this.checklistId)
                : this.collateralService.getChecklist(this.loanApplicationId),
            lists: this.collateralService.getValueLists(),
            access: this.collateralService.getAccess()
        }).pipe(takeUntil(this.destroy$)).subscribe({
            next: ({ checklist, lists, access }) => {
                this.checklist = checklist;
                this.loan = checklist.loan;
                this.loanApplicationId = checklist.loan?.loanApplicationId ?? this.loanApplicationId;
                this.items = checklist.items ?? [];
                this.lists = lists ?? {};
                this.access = access;
                this.selected = this.items.find(item => item.id === this.selected?.id) ?? null;
                this.waitMessage = '';
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.collateralService.errorMessage(error, 'The collaterals could not be loaded.'));
            }
        });
    }

    select(item: CollateralItem): void {
        this.selected = item;
    }

    open(item: CollateralItem | null): void {
        if (item?.id) {
            this.router.navigate(['/collateral-management/item', item.id]);
        }
    }

    create(): void {
        this.router.navigate(['/collateral-management/loan', this.loanApplicationId, 'item', 'new']);
    }

    remove(): void {
        const item = this.selected;
        if (!item?.id) {
            return;
        }
        const data: ConfirmDialogData = {
            title: 'Delete Collateral',
            message: `Delete collateral ${this.itemLabel(item)}? This cannot be undone; a change document is written.`,
            confirmLabel: 'Delete',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed === true) {
                        this.delete(item);
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    /** Starts the approval workflow of the checklist (CollateralWorkFlowController startprocess). */
    sendForApproval(): void {
        const checklistId = this.checklist?.id;
        if (!checklistId) {
            return;
        }
        const data: ConfirmDialogData = {
            title: 'Send for Approval',
            message: `Send the collateral checklist of loan ${this.loanLabel} (${this.items.length} collateral`
                + `${this.items.length === 1 ? '' : 's'}) for approval? The collaterals cannot be changed while the `
                + 'approval is pending.',
            confirmLabel: 'Send for Approval'
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed === true) {
                        this.startWorkflow(checklistId);
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    private startWorkflow(checklistId: string): void {
        this.waitMessage = `Sending the collateral checklist of loan ${this.loanLabel} for approval. Please wait`;
        this.collateralService.sendForApproval(checklistId).pipe(takeUntil(this.destroy$)).subscribe({
            next: checklist => {
                this.checklist = checklist;
                this.items = checklist.items ?? [];
                this.waitMessage = '';
                this.messageService.showSuccess('The collateral checklist was sent for approval. The approver is notified by e-mail.');
            },
            error: error => {
                this.waitMessage = '';
                this.messageService.showError(this.collateralService.errorMessage(error,
                    'The collateral checklist could not be sent for approval.'));
            }
        });
    }

    back(): void {
        this.router.navigate(['/loan-contract-search']);
    }

    statusText(item: CollateralItem): string {
        return item.complianceStatus
            ? `${item.complianceStatus} ${valueDescription(this.lists, 'COMPLIANCE_STATUS', item.complianceStatus)}`
            : '';
    }

    statusState(item: CollateralItem): 'positive' | 'negative' | 'critical' | 'informative' {
        return COMPLIANCE_STATUS_STATE[item.complianceStatus ?? ''] ?? 'informative';
    }

    text(list: string, code: string | null | undefined): string {
        return valueDescription(this.lists, list, code);
    }

    itemLabel(item: CollateralItem): string {
        const number = item.checklistIdNo ? String(item.checklistIdNo) : 'new';
        return item.conditionDescription ? `${number} (${item.conditionDescription})` : number;
    }

    period(item: CollateralItem): string {
        const start = formatDate(item.startDate);
        const end = formatDate(item.endDate);
        return start || end ? `${start || '…'} – ${end || '…'}` : '';
    }

    private delete(item: CollateralItem): void {
        this.waitMessage = `Deleting collateral ${this.itemLabel(item)}. Please wait`;
        this.collateralService.deleteItem(item.id!).pipe(takeUntil(this.destroy$)).subscribe({
            next: () => {
                this.messageService.showSuccess(`Collateral ${this.itemLabel(item)} deleted.`);
                this.selected = null;
                this.load();
            },
            error: error => {
                this.waitMessage = '';
                this.messageService.showError(this.collateralService.errorMessage(error, 'The collateral could not be deleted.'));
            }
        });
    }
}
