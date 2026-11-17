import { Component, HostListener, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonComponent, FormModule, IconComponent, MessageStripComponent } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { ReportVariantsComponent } from '../shared/report-variants.component';
import { VariantLoad, layoutHidden, layoutParameters, applyParameters, variantParameters } from '../shared/report-variant.model';
import { SelectOptionComponent } from '../shared/select-option.component';
import { dateRangeError, describeLines } from '../shared/select-option.model';

import {
    DEFAULT_WORKFLOW_STATUS_SELECTION,
    RELEASE_OBJECTS,
    WORKFLOW_STATUS_COLUMNS,
    WorkflowStatusColumn,
    WorkflowStatusReport,
    WorkflowStatusRow,
    WorkflowStatusSelection,
    compareCells,
    displayDate
} from './workflow-status.model';
import { WorkflowStatusService } from './workflow-status.service';

/**
 * Workflow Status Report (Reports › Miscellaneous): the selection screen of the SAP report "Loans: Workflow Status
 * Report" — company code, release object, contract number and business partner ranges, date range — and its result
 * from ZPFS_LMS_REPORTS_SRV (WorkflowStatusSet). Every column of the result is sortable; a quick filter narrows
 * the rows; the result can be downloaded as Excel.
 */
@Component({
    selector: 'app-workflow-status-report',
    templateUrl: './workflow-status-report.component.html',
    styleUrls: ['../shared/report-page.base.scss', '../shared/report-page.result.scss', './workflow-status-report.component.scss'],
    imports: [FormsModule, SelectOptionComponent, ReportVariantsComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule]
})
export class WorkflowStatusReportComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly columns = WORKFLOW_STATUS_COLUMNS;
    readonly releaseObjects = RELEASE_OBJECTS;
    readonly displayDate = displayDate;

    selection: WorkflowStatusSelection = { ...DEFAULT_WORKFLOW_STATUS_SELECTION };
    report: WorkflowStatusReport | null = null;
    rows: WorkflowStatusRow[] = [];
    sortKey: keyof WorkflowStatusRow | null = null;
    sortDirection: 'asc' | 'desc' = 'asc';
    quickFilter = '';

    running = false;
    /** The selection criteria section; collapsed after a successful run so the result gets the room. */
    filterOpen = true;
    exporting = false;
    errorMessage = '';
    lastRun = '';

    // ---- column picker: hide / show any number of columns (remembered per report in this browser) ----
    private readonly storageKey = 'reports.workflow-status.hidden-columns';
    columnPanelOpen = false;
    hiddenKeys = new Set<string>(this.loadHidden());

    get visibleColumns() {
        return this.columns.filter(c => !this.hiddenKeys.has(c.key as string));
    }

    get hiddenCount(): number {
        return this.columns.length - this.visibleColumns.length;
    }

    isHidden(column: { key: unknown }): boolean {
        return this.hiddenKeys.has(column.key as string);
    }

    toggleColumnPanel(event: Event): void {
        event.stopPropagation();
        this.columnPanelOpen = !this.columnPanelOpen;
    }

    /** Hides or shows a column; at least one column always stays visible. */
    toggleColumn(column: { key: unknown }): void {
        const key = column.key as string;
        if (this.hiddenKeys.has(key)) {
            this.hiddenKeys.delete(key);
        } else if (this.visibleColumns.length > 1) {
            this.hiddenKeys.add(key);
        }
        this.hiddenKeys = new Set(this.hiddenKeys);
        this.saveHidden();
    }

    showAllColumns(): void {
        this.hiddenKeys = new Set<string>();
        this.saveHidden();
    }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: Event): void {
        const target = event.target as HTMLElement | null;
        if (this.columnPanelOpen && !(target && target.closest && target.closest('.ws-cols'))) {
            this.columnPanelOpen = false;
        }
    }

    @HostListener('document:keydown.escape')
    onEscape(): void {
        this.columnPanelOpen = false;
    }

    // ---- layouts: the column choice saved under a name, like a variant (stored as report key workflow-status_layout) ----
    readonly currentLayout = () => layoutParameters(this.columns.filter(c => this.hiddenKeys.has(c.key as string)).map(c => c.key as string));

    onLayout(load: VariantLoad): void {
        const hidden = layoutHidden(load.parameters, this.columns.map(c => c.key as string));
        if (hidden) {
            this.hiddenKeys = new Set(hidden);
            this.saveHidden();
        }
    }

    private loadHidden(): string[] {
        try {
            const raw = localStorage.getItem(this.storageKey);
            const list = raw ? JSON.parse(raw) : [];
            const known = new Set(this.columns.map(c => c.key as string));
            return Array.isArray(list) ? list.filter(k => known.has(k)) : [];
        } catch {
            return [];
        }
    }

    private saveHidden(): void {
        try {
            localStorage.setItem(this.storageKey, JSON.stringify([...this.hiddenKeys]));
        } catch {
            // private mode / blocked storage: the choice simply isn't remembered
        }
    }

    constructor(private service: WorkflowStatusService, private messageService: MessageService, private router: Router) { }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    /** Execute: runs the SAP report for the selection. */
    toggleFilter(): void {
        this.filterOpen = !this.filterOpen;
    }

    // ---- variants: the selection screen saved under a name (not saved: nothing) ----
    private readonly variantSkip: string[] = [];
    readonly currentParameters = () => variantParameters(this.selection, this.variantSkip);

    onVariant(load: VariantLoad): void {
        this.selection = applyParameters({ ...DEFAULT_WORKFLOW_STATUS_SELECTION }, load.parameters, this.variantSkip);
        if (load.run) {
            this.execute();
        }
    }

    execute(): void {
        if (this.running) {
            return;
        }
        this.errorMessage = '';
        const invalid = dateRangeError('Date', this.selection.date);
        if (invalid) {
            this.errorMessage = invalid;
            return;
        }
        this.running = true;
        this.service.run(this.selection).pipe(takeUntil(this.destroy$)).subscribe({
            next: report => {
                this.running = false;
                this.report = report;
                this.filterOpen = false;
                this.lastRun = new Date().toLocaleString();
                this.applyView();
            },
            error: error => {
                this.running = false;
                this.errorMessage = this.service.errorMessage(error, 'The report could not be run.');
                this.messageService.showError(this.errorMessage);
            }
        });
    }

    reset(): void {
        this.selection = { ...DEFAULT_WORKFLOW_STATUS_SELECTION };
        this.errorMessage = '';
    }

    back(): void {
        this.router.navigate(['/reports']);
    }

    /** Header click: sorts by the column, a second click reverses, a third click removes the sort. */
    sort(column: WorkflowStatusColumn): void {
        if (this.sortKey !== column.key) {
            this.sortKey = column.key;
            this.sortDirection = 'asc';
        } else if (this.sortDirection === 'asc') {
            this.sortDirection = 'desc';
        } else {
            this.sortKey = null;
            this.sortDirection = 'asc';
        }
        this.applyView();
    }

    sortIcon(column: WorkflowStatusColumn): string {
        if (this.sortKey !== column.key) {
            return 'sort';
        }
        return this.sortDirection === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(column: WorkflowStatusColumn): string {
        if (this.sortKey !== column.key) {
            return 'none';
        }
        return this.sortDirection === 'asc' ? 'ascending' : 'descending';
    }

    onQuickFilter(): void {
        this.applyView();
    }

    cell(row: WorkflowStatusRow, column: WorkflowStatusColumn): string {
        const value = row[column.key];
        if (value === null || value === undefined) {
            return '';
        }
        return column.type === 'date' ? displayDate(value) : value;
    }

    /** The active selection as short chips, e.g. "Company Code PFS". */
    get criteria(): string[] {
        const s = this.selection;
        const chips: string[] = [];
        if (s.companyCode.trim()) { chips.push(`Company Code ${s.companyCode.trim().toUpperCase()}`); }
        if (s.releaseObject) { chips.push(this.releaseObjects.find(r => r.code === s.releaseObject)?.text ?? s.releaseObject); }
        const add = (label: string, text: string) => { if (text) { chips.push(`${label} ${text}`); } };
        add('Contract', describeLines(s.contract));
        add('Partner', describeLines(s.partner));
        add('Date', describeLines(s.date, v => displayDate(v)));
        return chips;
    }

    /** Colour of a status text: approved / released positive, pending / awaiting critical, rejected negative. */
    statusTone(text: string): 'positive' | 'critical' | 'negative' | 'neutral' {
        const t = text.toLowerCase();
        if (/reject|cancel|declin/.test(t)) { return 'negative'; }
        if (/pending|await|open|progress|initiat/.test(t)) { return 'critical'; }
        if (/approv|releas|complet|done/.test(t)) { return 'positive'; }
        return 'neutral';
    }

    get sortLabel(): string {
        return this.columns.find(c => c.key === this.sortKey)?.label ?? '';
    }

    get resultText(): string {
        if (!this.report) {
            return '';
        }
        const shown = this.rows.length;
        const total = this.report.total;
        return shown === total ? `${total} ${total === 1 ? 'row' : 'rows'}` : `${shown} of ${total} rows`;
    }

    /** Excel download of the report as shown: the rows in their current order and filter. */
    exportExcel(): void {
        if (!this.rows.length || this.exporting) {
            return;
        }
        this.exporting = true;
        this.service.exportExcel(this.selection, this.rows, this.visibleColumns.map(c => c.label)).pipe(takeUntil(this.destroy$)).subscribe({
            next: blob => {
                this.exporting = false;
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = `workflow-status-report-${new Date().toISOString().slice(0, 10)}.xlsx`;
                link.click();
                URL.revokeObjectURL(url);
            },
            error: error => {
                this.exporting = false;
                this.messageService.showError(this.service.errorMessage(error, 'The Excel file could not be created.'));
            }
        });
    }

    /** Applies the quick filter and the sort to the report's rows. */
    private applyView(): void {
        if (!this.report) {
            this.rows = [];
            return;
        }
        const needle = this.quickFilter.trim().toLowerCase();
        let rows = needle
            ? this.report.rows.filter(row => this.columns.some(c => this.cell(row, c).toLowerCase().includes(needle)))
            : [...this.report.rows];
        const key = this.sortKey;
        if (key) {
            const column = this.columns.find(c => c.key === key)!;
            const sign = this.sortDirection === 'asc' ? 1 : -1;
            rows = rows.sort((a, b) => sign * compareCells(a[key], b[key], column.type));
        }
        this.rows = rows;
    }
}
