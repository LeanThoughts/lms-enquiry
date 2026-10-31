import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    FormModule,
    IconComponent,
    MessageStripComponent,
    PaginationModule,
    ToolbarComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import {
    ConfirmDialogComponent,
    ConfirmDialogData
} from '../../collateral-management/confirm-dialog/confirm-dialog.component';
import { BP_PAGE_SIZES, BpRow, BpTableField, BpTablePage, fieldOptions } from './bp-table.model';
import { BpTableService } from './bp-table.service';
import { BpRowDialogComponent, BpRowDialogData } from './bp-row-dialog/bp-row-dialog.component';

/**
 * Configuration app of a business partner configuration table (route configuration/bp-tables/:table): the rows the
 * CommandLineRunner config of the table delivers, paged on the server with a search. Change mode: add, change and
 * delete rows (ZLM023, see configuration.roles).
 */
@Component({
    selector: 'app-bp-table',
    templateUrl: './bp-table.component.html',
    styleUrl: './bp-table.component.scss',
    imports: [DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule,
        PaginationModule]
})
export class BpTableComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();
    private readonly search$ = new Subject<string>();

    readonly pageSizes = BP_PAGE_SIZES;

    tableKey = '';
    data: BpTablePage | null = null;
    page = 0;
    size = 25;
    search = '';
    /** Column filters (column = value), e.g. listName of the collateral value lists; kept in the URL */
    filters: Record<string, string> = {};
    /** Column sort chosen by clicking a header ('' = the table's own order) */
    sortField = '';
    sortDir: 'asc' | 'desc' = 'asc';

    /** Change mode (the Configuration workspace shows "Edit") */
    edit = false;
    loading = false;
    loadFailed = false;
    busy = '';
    noEditAccessDismissed = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private service: BpTableService,
        private dialogService: DialogService,
        private messageService: MessageService
    ) { }

    ngOnInit(): void {
        this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
            // the same component shows every table: start fresh
            this.tableKey = params.get('table') ?? '';
            this.data = null;
            this.page = 0;
            this.search = '';
            this.filters = this.filtersFromUrl();
            this.sortField = '';
            this.sortDir = 'asc';
            this.edit = false;
            this.noEditAccessDismissed = false;
            this.load();
        });
        this.search$.pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$)).subscribe(text => {
            this.search = text;
            this.page = 0;
            this.load();
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get columns(): BpTableField[] {
        return this.data?.table.fields.filter(field => field.visible) ?? [];
    }

    /** Columns with a filter dropdown above the table */
    get filterFields(): BpTableField[] {
        return this.data?.table.fields.filter(field => field.filter) ?? [];
    }

    filterOptions(field: BpTableField) {
        return this.data ? fieldOptions(field, this.data.references) ?? [] : [];
    }

    /** Filters the table by a column value ('' = all); the first page is shown */
    setFilter(field: BpTableField, value: string): void {
        const filters = { ...this.filters };
        if (value) {
            filters[field.name] = value;
        } else {
            delete filters[field.name];
        }
        this.filters = filters;
        this.page = 0;
        this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { [field.name]: value || null },
            queryParamsHandling: 'merge',
            replaceUrl: true
        });
        this.load();
    }

    private filtersFromUrl(): Record<string, string> {
        const filters: Record<string, string> = {};
        const query = this.route.snapshot.queryParamMap;
        for (const key of query.keys) {
            const value = query.get(key);
            if (value && !['page', 'size', 'search'].includes(key)) {
                filters[key] = value;
            }
        }
        return filters;
    }

    get canChange(): boolean {
        return !!this.data?.canChange;
    }

    get noEditAccessText(): string {
        const role = this.data?.userRole?.trim() || '(none)';
        return `No edit access for the user with the role ${role}. Opening ${this.data?.table.title ?? 'the configuration'} in display mode`;
    }

    /** "26–50 of 245" */
    get rangeText(): string {
        if (!this.data || !this.data.total) {
            return '';
        }
        const from = this.data.page * this.data.size + 1;
        const to = Math.min(this.data.total, from + this.data.rows.length - 1);
        return `${from}–${to} of ${this.data.total}`;
    }

    load(): void {
        const table = this.tableKey;
        this.loading = true;
        this.loadFailed = false;
        this.service.getPage(table, this.page, this.size, this.search, this.filters, this.sortField, this.sortDir).pipe(takeUntil(this.destroy$)).subscribe({
            next: data => {
                if (table !== this.tableKey) {
                    return;
                }
                this.loading = false;
                this.data = data;
                this.page = data.page;
                this.size = data.size;
                if (data.filters) {
                    // the server ignores unknown filters: show what it applied
                    this.filters = { ...data.filters };
                }
                if (!data.canChange) {
                    this.edit = false;
                }
            },
            error: error => {
                this.loading = false;
                this.loadFailed = !this.data;
                this.messageService.showError(this.service.errorMessage(error, 'The configuration could not be loaded.'));
            }
        });
    }

    /** Header click: sorts by the column, a second click reverses, a third returns to the table's own order */
    toggleSort(field: BpTableField): void {
        if (this.sortField !== field.name) {
            this.sortField = field.name;
            this.sortDir = 'asc';
        } else if (this.sortDir === 'asc') {
            this.sortDir = 'desc';
        } else {
            this.sortField = '';
            this.sortDir = 'asc';
        }
        this.page = 0;
        this.load();
    }

    sortIcon(field: BpTableField): string {
        if (this.sortField !== field.name) {
            return 'sort';
        }
        return this.sortDir === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(field: BpTableField): string | null {
        if (this.sortField !== field.name) {
            return null;
        }
        return this.sortDir === 'asc' ? 'ascending' : 'descending';
    }

    setSearch(text: string): void {
        this.search$.next(text);
    }

    goToPage(page: number): void {
        // fd-pagination counts from 1
        const index = Math.max(0, page - 1);
        if (index !== this.page) {
            this.page = index;
            this.load();
        }
    }

    setPageSize(size: number): void {
        if (size && size !== this.size) {
            this.size = size;
            this.page = 0;
            this.load();
        }
    }

    change(): void {
        this.edit = true;
    }

    done(): void {
        this.edit = false;
    }

    /** Text of a cell: description of a reference / fixed value, else the value */
    cell(row: BpRow, field: BpTableField): string {
        const value = row.values[field.name];
        if (value === null || value === undefined || value === '') {
            return '';
        }
        const label = row.labels?.[field.name];
        if (label) {
            return label;
        }
        const options = this.data ? fieldOptions(field, this.data.references) : null;
        return options?.find(option => option.value === String(value))?.label ?? String(value);
    }

    rowName(row: BpRow): string {
        const keys = this.columns.filter(field => field.idField || field.businessKey);
        return keys.map(field => `${field.label} ${row.values[field.name] ?? ''}`).join(' / ');
    }

    openDialog(row?: BpRow): void {
        if (!this.data) {
            return;
        }
        const data: BpRowDialogData = { table: this.data.table, references: this.data.references, row,
            defaults: row ? undefined : { ...this.filters } };
        this.dialogService.open(BpRowDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (saved: BpRow) => {
                    if (saved) {
                        this.messageService.showSuccess(`${this.rowName(saved)} ${row ? 'saved' : 'added'}.`);
                        this.load();
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    remove(row: BpRow): void {
        if (!this.data) {
            return;
        }
        const name = this.rowName(row);
        const data: ConfirmDialogData = {
            title: 'Delete Entry',
            message: `Delete ${name} from ${this.data.table.title}? If the portal delivers this entry `
                + `(${this.data.table.configClass}), it is added again at the next start of the portal.`,
            confirmLabel: 'Delete',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed !== true || !this.data) {
                        return;
                    }
                    this.busy = `Deleting ${name}. Please wait`;
                    this.service.delete(this.data.table.key, row.id).pipe(takeUntil(this.destroy$)).subscribe({
                        next: () => {
                            this.busy = '';
                            this.messageService.showSuccess(`${name} deleted.`);
                            this.load();
                        },
                        error: error => {
                            this.busy = '';
                            this.messageService.showError(this.service.errorMessage(error, `${name} could not be deleted.`));
                        }
                    });
                },
                error: () => { /* dismissed */ }
            });
    }
}
