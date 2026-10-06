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
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import {
    ConfirmDialogComponent,
    ConfirmDialogData
} from '../../collateral-management/confirm-dialog/confirm-dialog.component';
import { BupaFieldStatusService } from './bupa-field-status.service';
import {
    EntityField,
    EntitySetField,
    FIELD_STATUSES,
    FieldStatusChanges,
    FieldStatusOverview,
    RoleFieldStatus,
    RoleSummary,
    statusLabel
} from './bupa-field-status.model';
import { FieldStatusDialogComponent, FieldStatusDialogData } from './field-status-dialog/field-status-dialog.component';

type Tab = 'entity' | 'set';
type Row = EntityField | EntitySetField;

interface Group<T> {
    name: string;
    rows: T[];
}

/**
 * Configuration app "Business Partner Field Status": the field status (Display only, Optional, Mandatory, Hide) of
 * the fields of the business partner entities (BupaRoleEntityFieldStatus) per business partner role. Roles are listed
 * on the left; the fields of the chosen role on the right. Entity sets (BupaRoleEntitySetFieldStatus) are maintained
 * by role and entity set in the app "BP Entity Set Fields".
 */
@Component({
    selector: 'app-bupa-field-status',
    templateUrl: './bupa-field-status.component.html',
    styleUrl: './bupa-field-status.component.scss',
    imports: [DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule,
        RouterLink]
})
export class BupaFieldStatusComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly statuses = FIELD_STATUSES;
    readonly statusLabel = statusLabel;

    overview: FieldStatusOverview | null = null;
    role: RoleFieldStatus | null = null;
    selectedCode = '';
    roleSearch = '';

    /** This page shows the entity fields only. */
    readonly tab: Tab = 'entity';
    groupFilter = '';
    fieldSearch = '';
    statusFilter: number | null = null;

    edit = false;
    selected = new Set<number>();
    waitMessage = '';
    roleWait = '';
    loadFailed = false;
    noEditAccessDismissed = false;

    /** Values as loaded, by id, to find the changed rows. */
    private originalEntity = new Map<number, EntityField>();
    private originalSet = new Map<number, EntitySetField>();

    entityGroups: Group<EntityField>[] = [];
    setGroups: Group<EntitySetField>[] = [];
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
        this.loadOverview(this.route.snapshot.queryParamMap.get('role') ?? '');
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
        return `No edit access for the user with the role ${role}. Opening the business partner field status in display mode`;
    }

    get roles(): RoleSummary[] {
        const search = this.roleSearch.trim().toLowerCase();
        const roles = this.overview?.roles ?? [];
        return search
            ? roles.filter(role => role.code.toLowerCase().includes(search) || (role.description ?? '').toLowerCase().includes(search))
            : roles;
    }

    get selectedRole(): RoleSummary | undefined {
        return this.overview?.roles.find(role => role.code === this.selectedCode);
    }

    get groupNames(): string[] {
        if (!this.role) {
            return [];
        }
        const names = this.tab === 'entity'
            ? this.role.entityFields.map(row => row.entity)
            : this.role.entitySetFields.map(row => row.entitySet);
        return Array.from(new Set(names)).sort();
    }

    get rowsOfTab(): Row[] {
        return this.role ? (this.tab === 'entity' ? this.role.entityFields : this.role.entitySetFields) : [];
    }

    get allVisibleSelected(): boolean {
        return this.visibleCount > 0 && this.visibleIds().every(id => this.selected.has(id));
    }

    // ------------------------------------------------------------------------------------------- loading

    loadOverview(preferredRole = this.selectedCode): void {
        this.waitMessage = 'Loading the business partner roles. Please wait';
        this.loadFailed = false;
        this.service.getOverview().pipe(takeUntil(this.destroy$)).subscribe({
            next: overview => {
                this.overview = overview;
                this.waitMessage = '';
                const code = overview.roles.some(role => role.code === preferredRole) ? preferredRole
                    : (overview.roles.find(role => role.entityFieldCount + role.entitySetFieldCount > 0)?.code
                        ?? overview.roles[0]?.code ?? '');
                if (code) {
                    this.loadRole(code);
                }
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.service.errorMessage(error, 'The field status could not be loaded.'));
            }
        });
    }

    refresh(): void {
        this.confirmDiscard(() => this.loadOverview(this.selectedCode));
    }

    chooseRole(code: string): void {
        if (code === this.selectedCode) {
            return;
        }
        this.confirmDiscard(() => this.loadRole(code));
    }

    private loadRole(code: string): void {
        this.selectedCode = code;
        this.roleWait = `Loading the field status of role ${code}. Please wait`;
        this.router.navigate([], { relativeTo: this.route, queryParams: { role: code }, replaceUrl: true });
        this.service.getRole(code).pipe(takeUntil(this.destroy$)).subscribe({
            next: role => {
                this.roleWait = '';
                this.edit = false;
                this.groupFilter = '';
                this.applyRole(role);
            },
            error: error => {
                this.roleWait = '';
                this.messageService.showError(this.service.errorMessage(error, `The field status of role ${code} could not be loaded.`));
            }
        });
    }

    private applyRole(role: RoleFieldStatus): void {
        this.role = role;
        this.originalEntity = new Map(role.entityFields.map(row => [row.id, { ...row }]));
        this.originalSet = new Map(role.entitySetFields.map(row => [row.id, { ...row }]));
        this.selected.clear();
        const summary = this.overview?.roles.find(entry => entry.code === role.roleCode);
        if (summary) {
            summary.entityFieldCount = role.entityFields.length;
            summary.entitySetFieldCount = role.entitySetFields.length;
        }
        if (this.overview) {
            const entities = new Set([...this.overview.entities, ...role.entityFields.map(row => row.entity)]);
            const sets = new Set([...this.overview.entitySets, ...role.entitySetFields.map(row => row.entitySet)]);
            this.overview.entities = Array.from(entities).sort();
            this.overview.entitySets = Array.from(sets).sort();
        }
        this.refreshView();
    }

    // ------------------------------------------------------------------------------------------- filtering


    setGroupFilter(value: string): void {
        this.groupFilter = value;
        this.refreshView();
    }

    setFieldSearch(value: string): void {
        this.fieldSearch = value;
        this.refreshView();
    }

    setStatusFilter(value: number | null): void {
        this.statusFilter = this.statusFilter === value ? null : value;
        this.refreshView();
    }

    /** Recomputes the grouped, filtered rows and the counters (called after every change, not per change detection). */
    refreshView(): void {
        const search = this.fieldSearch.trim().toLowerCase();
        const counts = [0, 0, 0, 0];
        let visible = 0;
        const matches = (row: Row, group: string): boolean => {
            if (this.groupFilter && group !== this.groupFilter) {
                return false;
            }
            const keyText = 'keyFieldValue' in row ? row.keyFieldValue.toLowerCase() : '';
            if (search && !row.fieldName.toLowerCase().includes(search) && !keyText.includes(search)) {
                return false;
            }
            if (row.fieldStatus >= 0 && row.fieldStatus <= 3) {
                counts[row.fieldStatus]++;
            }
            return this.statusFilter === null || row.fieldStatus === this.statusFilter;
        };
        if (this.tab === 'entity') {
            const groups = new Map<string, EntityField[]>();
            for (const row of this.role?.entityFields ?? []) {
                if (matches(row, row.entity)) {
                    groups.set(row.entity, [...(groups.get(row.entity) ?? []), row]);
                    visible++;
                }
            }
            this.entityGroups = Array.from(groups, ([name, rows]) => ({ name, rows }));
        } else {
            const groups = new Map<string, EntitySetField[]>();
            for (const row of this.role?.entitySetFields ?? []) {
                if (matches(row, row.entitySet)) {
                    const name = `${row.entitySet} · ${row.keyFieldValue}`;
                    groups.set(name, [...(groups.get(name) ?? []), row]);
                    visible++;
                }
            }
            this.setGroups = Array.from(groups, ([name, rows]) => ({ name, rows }));
        }
        this.statusCounts = counts;
        this.visibleCount = visible;
        const changes = this.collectChanges();
        this.changedCount = changes.entityFields.length + changes.entitySetFields.length;
    }

    // ------------------------------------------------------------------------------------------- editing

    change(): void {
        this.edit = true;
    }

    cancel(): void {
        if (this.role) {
            this.role.entityFields.forEach(row => Object.assign(row, this.originalEntity.get(row.id)));
            this.role.entitySetFields.forEach(row => Object.assign(row, this.originalSet.get(row.id)));
        }
        this.edit = false;
        this.selected.clear();
        this.refreshView();
    }

    isChanged(row: Row): boolean {
        if ('entity' in row) {
            const original = this.originalEntity.get(row.id);
            return !!original && original.fieldStatus !== row.fieldStatus;
        }
        const original = this.originalSet.get(row.id);
        return !!original && (original.fieldStatus !== row.fieldStatus || original.keyField !== row.keyField
            || (original.minimumEntries ?? 0) !== (row.minimumEntries ?? 0));
    }

    setStatus(row: Row, value: string | number): void {
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
        if (this.allVisibleSelected) {
            ids.forEach(id => this.selected.delete(id));
        } else {
            ids.forEach(id => this.selected.add(id));
        }
    }

    /** Sets the field status of all selected rows. */
    setSelectedStatus(status: number): void {
        for (const row of this.rowsOfTab) {
            if (this.selected.has(row.id)) {
                row.fieldStatus = status;
            }
        }
        this.refreshView();
    }

    save(): void {
        if (!this.role) {
            return;
        }
        const changes = this.collectChanges();
        if (!changes.entityFields.length && !changes.entitySetFields.length) {
            this.edit = false;
            return;
        }
        const invalid = this.role.entitySetFields.find(row => {
            const minimum = row.minimumEntries ?? 0;
            return !Number.isInteger(minimum) || minimum < 0 || minimum > 999;
        });
        if (invalid) {
            this.messageService.showError(`Minimum entries of ${invalid.entitySet} ${invalid.keyFieldValue}.${invalid.fieldName} must be a whole number between 0 and 999.`);
            return;
        }
        const code = this.role.roleCode;
        const count = changes.entityFields.length + changes.entitySetFields.length;
        this.roleWait = `Saving ${count} change${count === 1 ? '' : 's'} of role ${code}. Please wait`;
        this.service.saveChanges(code, changes).pipe(takeUntil(this.destroy$)).subscribe({
            next: role => {
                this.roleWait = '';
                this.edit = false;
                this.applyRole(role);
                this.messageService.showSuccess(`Field status of role ${code} saved (${count} field${count === 1 ? '' : 's'}).`);
            },
            error: error => {
                this.roleWait = '';
                this.messageService.showError(this.service.errorMessage(error, 'The field status could not be saved.'));
            }
        });
    }

    // ------------------------------------------------------------------------------------------- add, copy, delete

    openDialog(mode: 'entity' | 'set' | 'copy'): void {
        if (!this.role || !this.overview) {
            return;
        }
        const data: FieldStatusDialogData = {
            mode,
            roleCode: this.role.roleCode,
            roleDescription: this.role.roleDescription,
            entities: this.overview.entities,
            entitySets: this.overview.entitySets,
            roles: this.overview.roles.map(entry => ({ ...entry, entitySetFieldCount: 0 })),
            initialEntity: mode !== 'copy' ? this.groupFilter : undefined
        };
        this.dialogService.open(FieldStatusDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (result: unknown) => {
                    if (result && typeof result === 'object') {
                        const before = this.role!.entityFields.length + this.role!.entitySetFields.length;
                        this.applyRole(result as RoleFieldStatus);
                        const added = this.role!.entityFields.length + this.role!.entitySetFields.length - before;
                        this.messageService.showSuccess(mode === 'copy'
                            ? `${added} field${added === 1 ? '' : 's'} copied to role ${this.role!.roleCode}.`
                            : 'Field added.');
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    remove(row: Row): void {
        const name = 'entity' in row ? `${row.entity}.${row.fieldName}` : `${row.entitySet} ${row.keyFieldValue}.${row.fieldName}`;
        const data: ConfirmDialogData = {
            title: 'Delete Field',
            message: `Delete the field status of ${name} for role ${this.selectedCode}? The business partner screens then use their default for this field.`,
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
                    const request$: Observable<RoleFieldStatus> = 'entity' in row
                        ? this.service.deleteEntityField(row.id)
                        : this.service.deleteEntitySetField(row.id);
                    this.roleWait = `Deleting ${name}. Please wait`;
                    request$.pipe(takeUntil(this.destroy$)).subscribe({
                        next: role => {
                            this.roleWait = '';
                            this.applyRole(role);
                            this.messageService.showSuccess(`${name} deleted.`);
                        },
                        error: error => {
                            this.roleWait = '';
                            this.messageService.showError(this.service.errorMessage(error, 'The field could not be deleted.'));
                        }
                    });
                },
                error: () => { /* dismissed */ }
            });
    }

    // ------------------------------------------------------------------------------------------- helpers

    private collectChanges(): FieldStatusChanges {
        if (!this.role) {
            return { entityFields: [], entitySetFields: [] };
        }
        return {
            entityFields: this.role.entityFields.filter(row => this.isChanged(row))
                .map(row => ({ id: row.id, fieldStatus: row.fieldStatus })),
            entitySetFields: this.role.entitySetFields.filter(row => this.isChanged(row))
                .map(row => ({ id: row.id, fieldStatus: row.fieldStatus, keyField: row.keyField, minimumEntries: row.minimumEntries ?? 0 }))
        };
    }

    private visibleIds(): number[] {
        return this.tab === 'entity'
            ? this.entityGroups.flatMap(group => group.rows.map(row => row.id))
            : this.setGroups.flatMap(group => group.rows.map(row => row.id));
    }

    /** Runs the action at once, or after the user agreed to discard unsaved changes. */
    private confirmDiscard(action: () => void): void {
        if (!this.edit || this.changedCount === 0) {
            this.edit = false;
            action();
            return;
        }
        const data: ConfirmDialogData = {
            title: 'Discard Changes',
            message: `You have ${this.changedCount} unsaved change${this.changedCount === 1 ? '' : 's'} for role ${this.selectedCode}. Discard them?`,
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
                    }
                },
                error: () => { /* dismissed */ }
            });
    }
}
