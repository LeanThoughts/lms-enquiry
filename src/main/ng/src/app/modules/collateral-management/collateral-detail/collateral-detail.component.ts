import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
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
import { forkJoin, Observable, Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { CollateralService } from '../collateral.service';
import {
    ALL_FIELDS,
    COMPLIANCE_STATUS_STATE,
    CollateralAccess,
    CollateralItem,
    FieldDef,
    GridDef,
    HEADER_FIELDS,
    ITEM_MAX_LENGTH,
    LoanSummary,
    TABS,
    TabDef,
    ValueEntry,
    ValueLists,
    WORKFLOW_STATUS,
    formatDate,
    valueDescription
} from '../collateral.model';
import { buildControls, dateOrderValidator, fromFormValue, toFormValue } from '../collateral-form';
import { ChildGridComponent } from '../child-grid/child-grid.component';
import { CollateralFieldComponent } from '../collateral-field/collateral-field.component';
import { ConfirmDialogComponent, ConfirmDialogData } from '../confirm-dialog/confirm-dialog.component';

/** Fields that are kept in the form but have no field definition (Post Exec upload). */
const EXTRA_CONTROLS = ['postExecutionDocumentName', 'postExecutionDocumentReference'];

/** Values of a new collateral (deck: condition group and category default to 04). */
const NEW_ITEM_DEFAULTS: Partial<CollateralItem> = {
    conditionGroup: '04',
    conditionCategory: '04',
    collateralValueCurrency: 'INR'
};

/**
 * Display / change one collateral: header block (always visible) and the tabs stored in the collateral.
 * Opens in display mode; Change switches to change mode for users with write access.
 */
@Component({
    selector: 'app-collateral-detail',
    templateUrl: './collateral-detail.component.html',
    styleUrl: './collateral-detail.component.scss',
    imports: [
        ReactiveFormsModule,
        DynamicPageModule,
        ToolbarComponent,
        ToolbarSeparatorComponent,
        ButtonComponent,
        IconComponent,
        ObjectStatusComponent,
        MessageStripComponent,
        CollateralFieldComponent,
        ChildGridComponent
    ]
})
export class CollateralDetailComponent implements OnInit, OnDestroy {

    @ViewChild('fileInput') fileInput?: ElementRef<HTMLInputElement>;

    private readonly destroy$ = new Subject<void>();

    readonly headerFields = HEADER_FIELDS;
    readonly tabs = TABS;
    readonly formatDate = formatDate;

    readonly form = buildForm();

    /** The collateral as last loaded or saved; null for a new collateral. */
    item: CollateralItem | null = null;
    loan: LoanSummary | null = null;
    loanApplicationId = '';
    workFlowStatusDescription: string | null = null;
    workFlowStatusCode: number | null = null;
    lists: ValueLists = {};
    agreementTypes: ValueEntry[] | null = null;
    access: CollateralAccess | null = null;
    /** Rows of the child grids, keyed by GridDef.detailKey. */
    children: Record<string, Record<string, unknown>[]> = {};
    /** Names of the partners in Security Trustee, Security Agent and Custodian, keyed by party number. */
    partnerNames: Record<string, string> = {};

    isNew = false;
    edit = false;
    activeTab: TabDef = TABS[0];

    /** Message shown with the hourglass while loading or saving; empty when idle. */
    waitMessage = '';
    saving = false;
    loadFailed = false;
    errorMessage = '';

    constructor(
        private activatedRoute: ActivatedRoute,
        private collateralService: CollateralService,
        private messageService: MessageService,
        private dialogService: DialogService,
        public router: Router
    ) {}

    ngOnInit(): void {
        this.activatedRoute.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
            const itemId = params.get('itemId');
            this.loanApplicationId = params.get('loanApplicationId') ?? '';
            this.isNew = !itemId;
            this.load(itemId);
        });

        // Offer the agreement types allowed for the chosen collateral object
        this.form.controls['collateralObjectType'].valueChanges.pipe(takeUntil(this.destroy$)).subscribe(objectType => {
            if (this.edit) {
                this.loadAgreementTypes(objectType);
            }
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    // ------------------------------------------------------------------------------------------- page state

    /** True while the checklist is sent for approval; collaterals cannot be changed then. */
    get inApproval(): boolean {
        return this.workFlowStatusCode === WORKFLOW_STATUS.SENT_FOR_APPROVAL;
    }

    get canWrite(): boolean {
        return !!this.access?.canWrite && !this.inApproval;
    }

    noEditAccessDismissed = false;

    /** True when the user's role has no change authorization for collaterals (display only). */
    get noEditAccess(): boolean {
        return !!this.access && !this.access.canWrite;
    }

    /** Message for users without change authorization, naming their role. */
    get noEditAccessText(): string {
        const role = this.access?.role?.trim() || '(none)';
        return `No edit access for the user with the role ${role}. Opening collateral details in display mode`;
    }

    get title(): string {
        if (this.isNew) {
            return 'New Collateral';
        }
        const number = this.item?.checklistIdNo || 'New';
        const object = valueDescription(this.lists, 'COLLATERAL_OBJECT_TYPE', this.item?.collateralObjectType);
        return object ? `Collateral ${number} · ${object}` : `Collateral ${number}`;
    }

    get subtitle(): string {
        if (!this.loan) {
            return '';
        }
        const loan = this.loan.loanContractId ? `Loan ${this.loan.loanContractId}` : `Enquiry ${this.loan.enquiryNo ?? '–'}`;
        return [loan, this.loan.borrowerName, this.loan.projectName].filter(Boolean).join(' · ');
    }

    get complianceState(): 'positive' | 'negative' | 'critical' | 'informative' {
        return COMPLIANCE_STATUS_STATE[this.item?.complianceStatus ?? ''] ?? 'informative';
    }

    get complianceText(): string {
        const code = this.item?.complianceStatus;
        return code ? `Compliance: ${code} ${valueDescription(this.lists, 'COMPLIANCE_STATUS', code)}` : 'Compliance: open';
    }

    get audit(): string {
        const item = this.item;
        if (!item) {
            return '';
        }
        const created = item.createdOn ? `Created by ${item.createdByUserName || '–'} on ${formatDate(item.createdOn)}` : '';
        const changed = item.changedOn ? `Changed by ${item.changedByUserName || '–'} on ${formatDate(item.changedOn)}` : '';
        return [created, changed].filter(Boolean).join(' · ');
    }

    /** Dropdown values of a select field; the agreement type list follows the collateral object. */
    options(def: FieldDef): ValueEntry[] {
        if (def.list === 'AGREEMENT_TYPE' && this.edit && this.agreementTypes) {
            return this.agreementTypes;
        }
        return def.list ? this.lists[def.list] ?? [] : [];
    }

    rows(grid: GridDef): Record<string, unknown>[] {
        return this.children[grid.detailKey] ?? [];
    }

    setRows(grid: GridDef, rows: Record<string, unknown>[]): void {
        this.children = { ...this.children, [grid.detailKey]: rows };
    }

    selectTab(tab: TabDef): void {
        this.activeTab = tab;
    }

    tabHasError(tab: TabDef): boolean {
        return this.edit && tab.sections.some(section => section.fields.some(field => {
            const control = this.form.get(field.key);
            return !!control && control.invalid && control.touched;
        }));
    }

    // ------------------------------------------------------------------------------------------- actions

    change(): void {
        if (!this.canWrite || !this.item) {
            return;
        }
        this.edit = true;
        this.errorMessage = '';
        this.loadAgreementTypes(this.form.controls['collateralObjectType'].value);
    }

    cancel(): void {
        if (this.form.dirty) {
            this.confirm({
                title: 'Discard Changes',
                message: 'Discard the changes you made to this collateral?',
                confirmLabel: 'Discard',
                destructive: true
            }, () => this.discard());
        } else {
            this.discard();
        }
    }

    back(): void {
        if (this.edit && this.form.dirty) {
            this.confirm({
                title: 'Discard Changes',
                message: 'Leave the collateral without saving your changes?',
                confirmLabel: 'Leave',
                destructive: true
            }, () => this.toList());
        } else {
            this.toList();
        }
    }

    save(): void {
        this.form.markAllAsTouched();
        if (this.form.invalid) {
            this.errorMessage = 'Check the highlighted fields.';
            this.showFirstInvalidTab();
            return;
        }
        this.errorMessage = '';
        const payload = this.toItem();
        const label = this.isNew ? 'the new collateral' : `collateral ${this.item?.checklistIdNo || ''}`.trim();
        this.waitMessage = `Saving ${label}. Please wait`;
        this.saving = true;

        const request$: Observable<CollateralItem> = this.isNew
            ? this.collateralService.createItem(this.loanApplicationId, payload)
            : this.collateralService.updateItem(this.item!.id!, payload);

        request$.pipe(takeUntil(this.destroy$)).subscribe({
            next: saved => {
                this.saving = false;
                this.waitMessage = '';
                this.messageService.showSuccess(this.isNew ? 'Collateral created.' : 'Collateral saved.');
                if (this.isNew) {
                    this.form.markAsPristine();
                    this.router.navigate(['/collateral-management/item', saved.id], { replaceUrl: true });
                } else {
                    this.item = saved;
                    this.patchForm(saved);
                    this.edit = false;
                }
            },
            error: error => {
                this.saving = false;
                this.waitMessage = '';
                this.errorMessage = this.collateralService.errorMessage(error, 'The collateral could not be saved.');
                this.messageService.showError(this.errorMessage);
            }
        });
    }

    // ------------------------------------------------------------------------------------------- Post Exec upload

    get documentName(): string {
        return this.form.controls['postExecutionDocumentName'].value || '';
    }

    get documentReference(): string {
        return this.form.controls['postExecutionDocumentReference'].value || '';
    }

    get documentUrl(): string {
        return this.documentReference ? this.collateralService.downloadUrl(this.documentReference, this.documentName) : '';
    }

    chooseFile(): void {
        this.fileInput?.nativeElement.click();
    }

    upload(event: Event): void {
        const input = event.target as HTMLInputElement;
        const file = input.files?.[0];
        input.value = '';
        if (!file) {
            return;
        }
        const maxNameLength = ITEM_MAX_LENGTH['postExecutionDocumentName'] ?? 100;
        this.waitMessage = `Uploading ${file.name}. Please wait`;
        this.collateralService.uploadFile(file).pipe(takeUntil(this.destroy$)).subscribe({
            next: reference => {
                this.waitMessage = '';
                this.form.patchValue({
                    postExecutionDocumentReference: reference,
                    postExecutionDocumentName: file.name.substring(0, maxNameLength)
                });
                this.form.markAsDirty();
                this.messageService.showSuccess(`${file.name} uploaded. Save the collateral to keep it.`);
            },
            error: error => {
                this.waitMessage = '';
                this.messageService.showError(this.collateralService.errorMessage(error, 'The document could not be uploaded.'));
            }
        });
    }

    removeDocument(): void {
        this.form.patchValue({ postExecutionDocumentReference: null, postExecutionDocumentName: null });
        this.form.markAsDirty();
    }

    // ------------------------------------------------------------------------------------------- loading

    load(itemId: string | null): void {
        this.waitMessage = this.isNew ? 'Preparing a new collateral. Please wait' : 'Loading collateral. Please wait';
        this.loadFailed = false;
        this.errorMessage = '';

        const detail$ = itemId
            ? this.collateralService.getItem(itemId)
            : this.collateralService.getChecklist(this.loanApplicationId).pipe(map(checklist => ({
                item: { ...NEW_ITEM_DEFAULTS } as CollateralItem,
                loan: checklist.loan,
                workFlowStatusCode: checklist.workFlowStatusCode,
                workFlowStatusDescription: checklist.workFlowStatusDescription
            })));

        forkJoin({
            detail: detail$,
            lists: this.collateralService.getValueLists(),
            access: this.collateralService.getAccess()
        }).pipe(takeUntil(this.destroy$)).subscribe({
            next: ({ detail, lists, access }) => {
                this.lists = lists ?? {};
                this.access = access;
                this.loan = detail.loan;
                this.loanApplicationId = detail.loan?.loanApplicationId ?? this.loanApplicationId;
                this.workFlowStatusDescription = detail.workFlowStatusDescription;
                this.workFlowStatusCode = detail.workFlowStatusCode ?? null;
                this.item = this.isNew ? null : detail.item;
                const rows = detail as unknown as Record<string, Record<string, unknown>[] | undefined>;
                this.children = {
                    coverages: rows['coverages'] ?? [],
                    rocEvents: rows['rocEvents'] ?? [],
                    cersaiEvents: rows['cersaiEvents'] ?? [],
                    neslEvents: rows['neslEvents'] ?? [],
                    documents: rows['documents'] ?? [],
                    securitiesPositions: rows['securitiesPositions'] ?? []
                };
                this.partnerNames = { ...((detail as { partnerNames?: Record<string, string> }).partnerNames ?? {}) };
                this.patchForm(detail.item);
                this.waitMessage = '';

                if (this.isNew) {
                    if (!this.canWrite) {
                        this.messageService.showError(this.inApproval
                            ? 'The checklist is sent for approval. Collaterals cannot be created now.'
                            : `No edit access for the user with the role ${access.role?.trim() || '(none)'}. Collaterals cannot be created.`);
                        this.toList();
                        return;
                    }
                    this.edit = true;
                    this.loadAgreementTypes(detail.item.collateralObjectType ?? null);
                } else {
                    this.edit = false;
                }
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.collateralService.errorMessage(error, 'The collateral could not be loaded.'));
            }
        });
    }

    private loadAgreementTypes(objectType: string | null): void {
        this.collateralService.getAgreementTypes(objectType).pipe(takeUntil(this.destroy$)).subscribe({
            next: types => {
                this.agreementTypes = types;
                const control = this.form.controls['collateralAgreementType'];
                if (control.value && types.length && !types.some(type => type.code === control.value)) {
                    control.setValue(null);
                    control.markAsDirty();
                }
            },
            error: () => this.agreementTypes = null
        });
    }

    // ------------------------------------------------------------------------------------------- form <-> item

    private patchForm(item: CollateralItem): void {
        const record = item as Record<string, unknown>;
        const value = toFormValue(ALL_FIELDS, record);
        for (const key of EXTRA_CONTROLS) {
            value[key] = record[key] ?? null;
        }
        this.form.reset(value);
    }

    /** The loaded collateral with the form values applied (fields without an input are kept as loaded). */
    private toItem(): CollateralItem {
        const result = fromFormValue(ALL_FIELDS, this.form, { ...(this.item ?? {}) } as Record<string, unknown>);
        for (const key of EXTRA_CONTROLS) {
            result[key] = this.form.controls[key].value ?? null;
        }
        return result as CollateralItem;
    }

    private discard(): void {
        if (this.isNew || !this.item) {
            this.toList();
            return;
        }
        this.patchForm(this.item);
        this.edit = false;
        this.errorMessage = '';
    }

    private toList(): void {
        if (this.loanApplicationId) {
            this.router.navigate(['/collateral-management/loan', this.loanApplicationId]);
        } else {
            this.router.navigate(['/loan-contract-search']);
        }
    }

    private showFirstInvalidTab(): void {
        if (HEADER_FIELDS.some(def => this.form.controls[def.key].invalid)) {
            return; // the header block is always visible
        }
        const tab = TABS.find(candidate => this.tabHasError(candidate));
        if (tab) {
            this.activeTab = tab;
        }
    }

    private confirm(data: ConfirmDialogData, onConfirm: () => void): void {
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed === true) {
                        onConfirm();
                    }
                },
                error: () => { /* dismissed */ }
            });
    }
}

function buildForm(): FormGroup {
    const controls = buildControls(ALL_FIELDS);
    for (const key of EXTRA_CONTROLS) {
        controls[key] = new FormControl(null);
    }
    return new FormGroup(controls, {
        validators: [
            dateOrderValidator('validFromDate', 'validToDate', 'Valid to cannot be before Valid from.'),
            dateOrderValidator('startDate', 'endDate', 'End Date cannot be before Start Date.')
        ]
    });
}
