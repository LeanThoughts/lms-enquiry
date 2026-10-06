import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import {
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    FormModule,
    IconComponent,
    MessageStripComponent,
    ToolbarComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import {
    ConfirmDialogComponent,
    ConfirmDialogData
} from '../../collateral-management/confirm-dialog/confirm-dialog.component';
import { BupaFieldStatusService } from '../bupa-field-status/bupa-field-status.service';
import {
    EntitySetField,
    EntitySetFieldStatus,
    EntitySetOverview,
    FIELD_STATUSES,
    RoleSummary,
    statusLabel
} from '../bupa-field-status/bupa-field-status.model';
import {
    FieldStatusDialogComponent,
    FieldStatusDialogData
} from '../bupa-field-status/field-status-dialog/field-status-dialog.component';

interface Group {
    keyFieldValue: string;
    rows: EntitySetField[];
}

/**
 * Configuration app "BP Entity Set Fields": the field status (Display only, Optional, Mandatory, Hide), key field
 * flag and minimum entries of the fields of one entity set (e.g. bank details, identifications) for one business
 * partner role (BupaRoleEntitySetFieldStatus by bupaRoleCode and entitySet). Fields are grouped by key field value.
 */
@Component({
    selector: 'app-bupa-entity-set-field-status',
    templateUrl: './bupa-entity-set-field-status.component.html',
    styleUrl: './bupa-entity-set-field-status.component.scss',
    imports: [DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule,
        RouterLink]
})
export class BupaEntitySetFieldStatusComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly statuses = FIELD_STATUSES;
    readonly statusLabel = statusLabel;

    overview: EntitySetOverview | null = null;
    data: EntitySetFieldStatus | null = null;
    roleCode = '';
    entitySet = '';

    keyFilter = '';
    fieldSearch = '';
    statusFilter: number | null = null;

    edit = false;
    selected = new Set<number>();
    waitMessage = '';
    busy = '';
    loadFailed = false;
    noEditAccessDismissed = false;

    private original = new Map<number, EntitySetField>();
    groups: Group[] = [];
    visibleCount = 0;
    statusCounts: number[] = [0, 0, 0, 0];
    changedCount = 0;

    constructor(
        private service: BupaFieldStatusService,
        private messageService: MessageService,
        private dialogService: DialogService,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit(): void {
        const params = this.route.snapshot.queryParamMap;
        this.load(params.get('role') ?? '', params.get('entitySet') ?? '');
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    // ------------------------------------------------------------------------------------------- page state

    get canChange(): boolean {
        return !!this.overview?.canChange;
    }

    get noEditAccessText(): string {
        const role = this.overview?.userRole?.trim() || '(none)';
        return `No edit access for the user with the role ${role}. Opening the business partner entity set fields in display mode`;
    }

    get roleDescription(): string {
        return this.overview?.roles.find(role => role.code === this.roleCode)?.description ?? '';
    }

    /** Number of fields of a role in the chosen entity set (or in all entity sets). */
    count(roleCode: string, entitySet = this.entitySet): number {
        const counts = this.overview?.counts[roleCode] ?? {};
        return entitySet ? counts[entitySet] ?? 0 : Object.values(counts).reduce((sum, value) => sum + value, 0);
    }

    /** Key field value with its description, e.g. "Z00001 · PAN Card" (identification categories). */
    keyLabel(value: string): string {
        const description = this.data?.keyFieldDescriptions?.[value];
        return description ? `${value} · ${description}` : value;
    }

    get keyValues(): string[] {
        return Array.from(new Set((this.data?.fields ?? []).map(row => row.keyFieldValue))).sort();
    }

    get allVisibleSelected(): boolean {
        const ids = this.visibleIds();
        return ids.length > 0 && ids.every(id => this.selected.has(id));
    }

    // ------------------------------------------------------------------------------------------- loading

    load(preferredRole = this.roleCode, preferredSet = this.entitySet): void {
        this.waitMessage = 'Loading the business partner roles and entity sets. Please wait';
        this.loadFailed = false;
        this.service.getEntitySetOverview().pipe(takeUntil(this.destroy$)).subscribe({
            next: overview => {
                this.overview = overview;
                this.waitMessage = '';
                const set = overview.entitySets.includes(preferredSet) ? preferredSet : (overview.entitySets[0] ?? '');
                const role = overview.roles.some(entry => entry.code === preferredRole) ? preferredRole
                    : (overview.roles.find(entry => this.count(entry.code, set) > 0)?.code ?? overview.roles[0]?.code ?? '');
                this.roleCode = role;
                this.entitySet = set;
                this.loadEntitySet();
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.service.errorMessage(error, 'The entity set fields could not be loaded.'));
            }
        });
    }

    refresh(): void {
        this.confirmDiscard(() => this.load());
    }

    chooseRole(code: string, select?: HTMLSelectElement): void {
        if (code === this.roleCode) {
            return;
        }
        this.confirmDiscard(() => {
            this.roleCode = code;
            this.loadEntitySet();
        }, () => select && (select.value = this.roleCode));
    }

    chooseEntitySet(entitySet: string, select?: HTMLSelectElement): void {
        if (entitySet === this.entitySet) {
            return;
        }
        this.confirmDiscard(() => {
            this.entitySet = entitySet;
            this.keyFilter = '';
            this.loadEntitySet();
        }, () => select && (select.value = this.entitySet));
    }

    private loadEntitySet(): void {
        if (!this.roleCode || !this.entitySet) {
            this.data = null;
            return;
        }
        const role = this.roleCode;
        const set = this.entitySet;
        this.busy = `Loading ${set} of role ${role}. Please wait`;
        this.router.navigate([], { relativeTo: this.route, queryParams: { role, entitySet: set }, replaceUrl: true });
        this.service.getEntitySet(role, set).pipe(takeUntil(this.destroy$)).subscribe({
            next: data => {
                this.busy = '';
                this.edit = false;
                this.apply(data);
            },
            error: error => {
                this.busy = '';
                this.messageService.showError(this.service.errorMessage(error, `${set} of role ${role} could not be loaded.`));
            }
        });
    }

    private apply(data: EntitySetFieldStatus): void {
        this.data = data;
        this.original = new Map(data.fields.map(row => [row.id, { ...row }]));
        this.selected.clear();
        if (this.overview) {
            const counts = this.overview.counts[data.roleCode] ?? (this.overview.counts[data.roleCode] = {});
            counts[data.entitySet] = data.fields.length;
        }
        if (this.keyFilter && !this.keyValues.includes(this.keyFilter)) {
            this.keyFilter = '';
        }
        this.refreshView();
    }

    // ------------------------------------------------------------------------------------------- filtering

    setKeyFilter(value: string): void {
        this.keyFilter = value;
        this.refreshView();
    }

    setFieldSearch(value: string): void {
        this.fieldSearch = value;
        this.refreshView();
    }

    setStatusFilter(value: number): void {
        this.statusFilter = this.statusFilter === value ? null : value;
        this.refreshView();
    }

    refreshView(): void {
        const search = this.fieldSearch.trim().toLowerCase();
        const counts = [0, 0, 0, 0];
        const groups = new Map<string, EntitySetField[]>();
        let visible = 0;
        for (const row of this.data?.fields ?? []) {
            if (this.keyFilter && row.keyFieldValue !== this.keyFilter) {
                continue;
            }
            if (search && !row.fieldName.toLowerCase().includes(search)) {
                continue;
            }
            if (row.fieldStatus >= 0 && row.fieldStatus <= 3) {
                counts[row.fieldStatus]++;
            }
            if (this.statusFilter !== null && row.fieldStatus !== this.statusFilter) {
                continue;
            }
            groups.set(row.keyFieldValue, [...(groups.get(row.keyFieldValue) ?? []), row]);
            visible++;
        }
        this.groups = Array.from(groups, ([keyFieldValue, rows]) => ({ keyFieldValue, rows }));
        this.statusCounts = counts;
        this.visibleCount = visible;
        this.changedCount = this.changes().length;
    }

    // ------------------------------------------------------------------------------------------- editing

    change(): void {
        this.edit = true;
    }

    cancel(): void {
        this.data?.fields.forEach(row => Object.assign(row, this.original.get(row.id)));
        this.edit = false;
        this.selected.clear();
        this.refreshView();
    }

    isChanged(row: EntitySetField): boolean {
        const original = this.original.get(row.id);
        return !!original && (original.fieldStatus !== row.fieldStatus || original.keyField !== row.keyField
            || (original.minimumEntries ?? 0) !== (row.minimumEntries ?? 0));
    }

    setStatus(row: EntitySetField, value: string): void {
        row.fieldStatus = Number(value);
        this.refreshView();
    }

    setKeyField(row: EntitySetField, value: boolean): void {
        row.keyField = value;
        this.refreshView();
    }

    setMinimum(row: EntitySetField, value: string): void {
        const number = value === '' ? 0 : Number(value);
        row.minimumEntries = Number.isFinite(number) ? number : row.minimumEntries;
        this.refreshView();
    }

    toggleSelected(id: number): void {
        this.selected.has(id) ? this.selected.delete(id) : this.selected.add(id);
    }

    toggleAllVisible(): void {
        const ids = this.visibleIds();
        const select = !this.allVisibleSelected;
        ids.forEach(id => select ? this.selected.add(id) : this.selected.delete(id));
    }

    setSelectedStatus(status: number): void {
        this.data?.fields.filter(row => this.selected.has(row.id)).forEach(row => row.fieldStatus = status);
        this.refreshView();
    }

    save(): void {
        if (!this.data) {
            return;
        }
        const changes = this.changes();
        if (!changes.length) {
            this.edit = false;
            return;
        }
        const invalid = this.data.fields.find(row => {
            const minimum = row.minimumEntries ?? 0;
            return !Number.isInteger(minimum) || minimum < 0 || minimum > 999;
        });
        if (invalid) {
            this.messageService.showError(`Minimum entries of ${invalid.keyFieldValue}.${invalid.fieldName} must be a whole number between 0 and 999.`);
            return;
        }
        const { roleCode, entitySet } = this.data;
        const count = changes.length;
        this.busy = `Saving ${count} change${count === 1 ? '' : 's'}. Please wait`;
        this.service.saveEntitySet(roleCode, entitySet, changes).pipe(takeUntil(this.destroy$)).subscribe({
            next: data => {
                this.busy = '';
                this.edit = false;
                this.apply(data);
                this.messageService.showSuccess(`${entitySet} of role ${roleCode} saved (${count} field${count === 1 ? '' : 's'}).`);
            },
            error: error => {
                this.busy = '';
                this.messageService.showError(this.service.errorMessage(error, 'The field status could not be saved.'));
            }
        });
    }

    // ------------------------------------------------------------------------------------------- add, copy, delete

    openDialog(mode: 'set' | 'copy'): void {
        if (!this.data || !this.overview) {
            return;
        }
        const roles: RoleSummary[] = this.overview.roles.map(entry => ({
            ...entry, entityFieldCount: 0, entitySetFieldCount: this.count(entry.code)
        }));
        const data: FieldStatusDialogData = {
            mode,
            roleCode: this.data.roleCode,
            roleDescription: this.data.roleDescription,
            entities: [],
            entitySets: this.overview.entitySets,
            roles,
            fixedEntitySet: this.data.entitySet,
            keyFieldValues: this.data.keyFieldValues,
            keyFieldDescriptions: this.data.keyFieldDescriptions ?? {},
            initialKeyFieldValue: this.keyFilter
        };
        this.dialogService.open(FieldStatusDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (result: unknown) => {
                    if (result && typeof result === 'object') {
                        const before = this.data!.fields.length;
                        this.apply(result as EntitySetFieldStatus);
                        const added = this.data!.fields.length - before;
                        this.messageService.showSuccess(mode === 'copy'
                            ? `${added} field${added === 1 ? '' : 's'} copied to role ${this.data!.roleCode}.`
                            : 'Field added.');
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    remove(row: EntitySetField): void {
        const name = `${this.keyLabel(row.keyFieldValue)} · ${row.fieldName}`;
        const data: ConfirmDialogData = {
            title: 'Delete Field',
            message: `Delete ${name} from ${this.entitySet} for role ${this.roleCode}?`,
            confirmLabel: 'Delete',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed !== true) {
                        return;
                    }
                    this.busy = `Deleting ${name}. Please wait`;
                    this.service.deleteFieldOfEntitySet(row.id).pipe(takeUntil(this.destroy$)).subscribe({
                        next: result => {
                            this.busy = '';
                            this.apply(result);
                            this.messageService.showSuccess(`${name} deleted.`);
                        },
                        error: error => {
                            this.busy = '';
                            this.messageService.showError(this.service.errorMessage(error, 'The field could not be deleted.'));
                        }
                    });
                },
                error: () => { /* dismissed */ }
            });
    }

    // ------------------------------------------------------------------------------------------- helpers

    private changes(): Partial<EntitySetField>[] {
        return (this.data?.fields ?? []).filter(row => this.isChanged(row)).map(row => ({
            id: row.id, fieldStatus: row.fieldStatus, keyField: row.keyField, minimumEntries: row.minimumEntries ?? 0
        }));
    }

    private visibleIds(): number[] {
        return this.groups.flatMap(group => group.rows.map(row => row.id));
    }

    /** Runs the action at once, or after the user agreed to discard unsaved changes; otherwise runs revert. */
    private confirmDiscard(action: () => void, revert?: () => void): void {
        if (!this.edit || this.changedCount === 0) {
            this.edit = false;
            action();
            return;
        }
        const data: ConfirmDialogData = {
            title: 'Discard Changes',
            message: `You have ${this.changedCount} unsaved change${this.changedCount === 1 ? '' : 's'} in ${this.entitySet} of role ${this.roleCode}. Discard them?`,
            confirmLabel: 'Discard',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed === true) {
                        this.cancel();
                        action();
                    } else {
                        revert?.();
                    }
                },
                error: () => revert?.()
            });
    }
}
