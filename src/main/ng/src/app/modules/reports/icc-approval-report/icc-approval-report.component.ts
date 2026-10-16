import { Component, HostListener, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonComponent, FormModule, IconComponent, MessageStripComponent } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { SelectOptionComponent } from '../shared/select-option.component';
import { describeLines } from '../shared/select-option.model';

import {
    DEFAULT_ICC_APPROVAL_SELECTION,
    QUARTERS,
    ICC_APPROVAL_COLUMNS,
    IccApprovalColumn,
    IccApprovalReport,
    IccApprovalRow,
    IccApprovalSelection,
    compareCells,
    displayDate
} from './icc-approval.model';
import { IccApprovalService } from './icc-approval.service';

/**
 * ICC Approval Status Report (Reports › Loans Management): the selection screen of the SAP report "Loans : ICC
 * Approval Status Report" — company code, fiscal year, quarter, loan contract range — and its result from
 * ZPFS_LMS_REPORTS_SRV (MISIccApprovalStatRpt). Every column of the result is sortable; a quick filter narrows the
 * rows; the result can be downloaded as Excel.
 */
@Component({
    selector: 'app-icc-approval-report',
    templateUrl: './icc-approval-report.component.html',
    styleUrls: ['../shared/report-page.base.scss', '../shared/report-page.result.scss', './icc-approval-report.component.scss'],
    imports: [FormsModule, SelectOptionComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule]
})
export class IccApprovalReportComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly columns = ICC_APPROVAL_COLUMNS;
    readonly quarters = QUARTERS;
    readonly displayDate = displayDate;

    selection: IccApprovalSelection = { ...DEFAULT_ICC_APPROVAL_SELECTION };
    report: IccApprovalReport | null = null;
    rows: IccApprovalRow[] = [];
    sortKey: keyof IccApprovalRow | null = null;
    sortDirection: 'asc' | 'desc' = 'asc';
    quickFilter = '';

    running = false;
    /** The selection criteria section; collapsed after a successful run so the result gets the room. */
    filterOpen = true;
    exporting = false;
    errorMessage = '';
    lastRun = '';

    // ---- column picker: hide / show any number of columns (remembered per report in this browser) ----
    private readonly storageKey = 'reports.icc-approval.hidden-columns';
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

    constructor(private service: IccApprovalService, private messageService: MessageService, private router: Router) { }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    /** Execute: runs the SAP report for the selection. */
    toggleFilter(): void {
        this.filterOpen = !this.filterOpen;
    }

    execute(): void {
        if (this.running) {
            return;
        }
        this.errorMessage = '';
        const year = this.selection.fiscalYear.trim();
        if (year && !/^\d{4}$/.test(year)) {
            this.errorMessage = 'Fiscal Year must have four digits, e.g. 2026.';
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
        this.selection = { ...DEFAULT_ICC_APPROVAL_SELECTION };
        this.errorMessage = '';
    }

    back(): void {
        this.router.navigate(['/reports']);
    }

    /** Header click: sorts by the column, a second click reverses, a third click removes the sort. */
    sort(column: IccApprovalColumn): void {
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

    sortIcon(column: IccApprovalColumn): string {
        if (this.sortKey !== column.key) {
            return 'sort';
        }
        return this.sortDirection === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(column: IccApprovalColumn): string {
        if (this.sortKey !== column.key) {
            return 'none';
        }
        return this.sortDirection === 'asc' ? 'ascending' : 'descending';
    }

    onQuickFilter(): void {
        this.applyView();
    }

    cell(row: IccApprovalRow, column: IccApprovalColumn): string {
        const value = row[column.key];
        if (value === null || value === undefined) {
            return '';
        }
        if (column.type === 'date') {
            return displayDate(value);
        }
        if (column.decimal) {
            const n = Number(value);
            return Number.isNaN(n) ? value : n.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        }
        return value;
    }

    /** The active selection as short chips, e.g. "Company Code PFS". */
    get criteria(): string[] {
        const s = this.selection;
        const chips: string[] = [];
        if (s.companyCode.trim()) { chips.push(`Company Code ${s.companyCode.trim().toUpperCase()}`); }
        if (s.fiscalYear.trim()) { chips.push(`Fiscal Year ${s.fiscalYear.trim()}`); }
        if (s.quarter) { chips.push(this.quarters.find(q => q.code === s.quarter)?.text ?? `Quarter ${s.quarter}`); }
        const contract = describeLines(s.contract);
        if (contract) { chips.push(`Contract ${contract}`); }
        return chips;
    }

    /** Colour of a status text: approved / released positive, pending / awaiting critical, rejected negative. */
    statusTone(text: string): 'positive' | 'critical' | 'negative' | 'neutral' {
        const t = text.toLowerCase();
        if (/reject|cancel|declin/.test(t)) { return 'negative'; }
        if (/pending|await|open|progress|initiat/.test(t)) { return 'critical'; }
        if (/approv|releas|complet|done|clear/.test(t)) { return 'positive'; }
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
                link.download = `icc-approval-status-report-${new Date().toISOString().slice(0, 10)}.xlsx`;
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
