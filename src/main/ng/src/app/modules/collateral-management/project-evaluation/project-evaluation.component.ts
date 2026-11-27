import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    IconComponent,
    MessageStripComponent,
    ObjectStatusComponent,
    ToolbarComponent,
    ToolbarSeparatorComponent
} from '@fundamental-ngx/core';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { ChildGridComponent } from '../child-grid/child-grid.component';
import { CollateralFieldComponent } from '../collateral-field/collateral-field.component';
import { CollateralService } from '../collateral.service';
import {
    CollateralAccess,
    FieldDef,
    GRIDS,
    LoanSummary,
    ProjectEvaluation,
    ProjectEvaluationData,
    ValueLists,
    WORKFLOW_STATUS,
    formatDate,
    technicalInfoOf,
    workflowState
} from '../collateral.model';
import { ConfirmDialogComponent, ConfirmDialogData } from '../confirm-dialog/confirm-dialog.component';
import { TechnicalInfoDialogComponent, TechnicalInfoDialogData } from '../technical-info-dialog/technical-info-dialog.component';

/**
 * Project (collateral) valuation of a loan, as on the SAP loan contract tab Project(Coll.) Valuation (table
 * ZLMS_COLL_EVAL): the Major Collateral Type of the loan and its valuations, created, changed and deleted in a dialog
 * (New / Change / Delete Valuation). Opened from the collaterals of the loan with "Maintain Project Valuation".
 * <p>
 * The valuations are approved with the loan's collateral checklist: every signed-in user can display; the collateral
 * write roles can change, except while the checklist waits for approval.
 */
@Component({
    selector: 'app-project-evaluation',
    templateUrl: './project-evaluation.component.html',
    styleUrl: './project-evaluation.component.scss',
    imports: [
        DynamicPageModule,
        ToolbarComponent,
        ToolbarSeparatorComponent,
        ButtonComponent,
        IconComponent,
        MessageStripComponent,
        ObjectStatusComponent,
        CollateralFieldComponent,
        ChildGridComponent
    ]
})
export class ProjectEvaluationComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly grid = GRIDS['project-evaluations'];
    readonly majorTypeDef: FieldDef = {
        key: 'majorCollateralType', label: 'Major Collateral Type', type: 'select', list: 'MAJOR_COLLATERAL_TYPE', span: 4
    };
    readonly majorTypeForm = new FormGroup({ majorCollateralType: new FormControl<string | null>(null) });

    loanApplicationId = '';
    loan: LoanSummary | null = null;
    data: ProjectEvaluationData | null = null;
    evaluations: ProjectEvaluation[] = [];
    lists: ValueLists = {};
    access: CollateralAccess | null = null;
    majorCollateralType: string | null = null;

    /** Message shown with the hourglass while waiting; empty when idle. */
    waitMessage = '';
    loadFailed = false;
    savingMajorType = false;

    constructor(
        private activatedRoute: ActivatedRoute,
        private collateralService: CollateralService,
        private messageService: MessageService,
        private dialogService: DialogService,
        public router: Router
    ) {}

    ngOnInit(): void {
        this.majorTypeForm.controls.majorCollateralType.valueChanges.pipe(takeUntil(this.destroy$))
            .subscribe(value => this.saveMajorType(value ?? null));
        this.activatedRoute.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
            this.loanApplicationId = params.get('loanApplicationId') ?? '';
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

    /** True while the collateral checklist waits for approval: valuations cannot be created, changed or deleted. */
    get inApproval(): boolean {
        return this.data?.checklistWorkFlowStatusCode === WORKFLOW_STATUS.SENT_FOR_APPROVAL;
    }

    /** New, Change and Delete Valuation and the Major Collateral Type: write role and not waiting for approval. */
    get canChange(): boolean {
        return this.canWrite && (this.data?.changeable ?? true) && !this.inApproval;
    }

    /** Send for Approval: write role, at least one valuation (the checklist exists then), not already waiting. */
    get canSendForApproval(): boolean {
        return this.canWrite && !!this.data?.checklistId && this.evaluations.length > 0 && !this.inApproval;
    }

    get workflowState(): 'positive' | 'negative' | 'critical' | 'informative' {
        return workflowState(this.data?.checklistWorkFlowStatusCode);
    }

    get loanLabel(): string {
        return this.loan?.loanContractId || (this.loan?.enquiryNo ? `Enquiry ${this.loan.enquiryNo}` : '');
    }

    /** The valuation with the latest valuation date (the highest line item among equal dates). */
    get latest(): ProjectEvaluation | null {
        return [...this.evaluations]
            .filter(evaluation => !!evaluation.valuationDate)
            .sort((a, b) => String(b.valuationDate).localeCompare(String(a.valuationDate))
                || (b.itemNumber ?? 0) - (a.itemNumber ?? 0))[0] ?? null;
    }

    formatAmount(value: number | null | undefined, currency?: string | null): string {
        if (value === null || value === undefined) {
            return '–';
        }
        const amount = Number(value).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        return currency ? `${currency} ${amount}` : amount;
    }

    formatDate(value: unknown): string {
        return formatDate(value);
    }

    text(list: string, code: string | null | undefined): string {
        const entry = (this.lists[list] ?? []).find(value => value.code === code);
        return entry?.description ?? code ?? '';
    }

    load(): void {
        this.waitMessage = this.loanLabel
            ? `Loading the project valuation of loan ${this.loanLabel}. Please wait`
            : 'Loading the project valuation. Please wait';
        this.loadFailed = false;
        forkJoin({
            data: this.collateralService.getProjectEvaluations(this.loanApplicationId),
            lists: this.collateralService.getValueLists(),
            access: this.collateralService.getAccess()
        }).pipe(takeUntil(this.destroy$)).subscribe({
            next: ({ data, lists, access }) => {
                this.apply(data);
                this.lists = lists ?? {};
                this.access = access;
                this.waitMessage = '';
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.collateralService.errorMessage(error,
                    'The project valuation could not be loaded.'));
            }
        });
    }

    /** After a row was created, changed or deleted in the grid: the line items and the type may have moved. */
    onRowsChange(rows: ProjectEvaluation[]): void {
        this.evaluations = rows;
    }

    /** Technical Info: the checklist the valuations are approved with. */
    technicalInfo(): void {
        if (!this.data?.checklistId) {
            return;
        }
        const data: TechnicalInfoDialogData = {
            title: `Collateral checklist of ${this.loanLabel} (valuations are approved with it)`.trim(),
            info: technicalInfoOf({ id: this.data.checklistId })
        };
        this.dialogService.open(TechnicalInfoDialogComponent, { data, responsivePadding: true });
    }

    /**
     * Starts the approval workflow of the loan's collateral checklist, which the valuations belong to: the approver
     * approves the collaterals and the valuations together, and the approved valuations are then posted to SAP.
     */
    sendForApproval(): void {
        const checklistId = this.data?.checklistId;
        if (!checklistId) {
            return;
        }
        const count = this.evaluations.length;
        const data: ConfirmDialogData = {
            title: 'Send for Approval',
            message: `Send the collateral checklist of loan ${this.loanLabel} with its ${count} valuation`
                + `${count === 1 ? '' : 's'} for approval? The valuations and the collaterals cannot be changed while `
                + 'the approval is pending; the approved valuations are posted to SAP.',
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
        this.waitMessage = `Sending the valuations of loan ${this.loanLabel} for approval. Please wait`;
        this.collateralService.sendForApproval(checklistId).pipe(takeUntil(this.destroy$)).subscribe({
            next: () => {
                this.messageService.showSuccess('The valuations were sent for approval with the collateral checklist. '
                    + 'The approver is notified by e-mail.');
                this.load();
            },
            error: error => {
                this.waitMessage = '';
                this.messageService.showError(this.collateralService.errorMessage(error,
                    'The valuations could not be sent for approval.'));
            }
        });
    }

    back(): void {
        this.router.navigate(['/collateral-management/loan', this.loanApplicationId]);
    }

    private apply(data: ProjectEvaluationData): void {
        this.data = data;
        this.loan = data.loan ?? null;
        this.evaluations = data.evaluations ?? [];
        this.setMajorType(data.majorCollateralType ?? null);
    }

    /** Shows a value without saving it again. */
    private setMajorType(value: string | null): void {
        this.majorCollateralType = value;
        this.majorTypeForm.controls.majorCollateralType.setValue(value, { emitEvent: false });
    }

    private saveMajorType(value: string | null): void {
        if (!this.canChange || value === this.majorCollateralType) {
            return;
        }
        this.savingMajorType = true;
        this.collateralService.setMajorCollateralType(this.loanApplicationId, value).pipe(takeUntil(this.destroy$)).subscribe({
            next: data => {
                this.savingMajorType = false;
                this.apply(data);
                this.messageService.showSuccess('Major Collateral Type saved.');
            },
            error: error => {
                this.savingMajorType = false;
                this.setMajorType(this.majorCollateralType);
                this.messageService.showError(this.collateralService.errorMessage(error,
                    'The Major Collateral Type could not be saved.'));
            }
        });
    }
}
