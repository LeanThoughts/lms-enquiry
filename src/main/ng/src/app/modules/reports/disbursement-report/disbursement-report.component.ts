import { Component, HostListener, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonComponent, FormModule, IconComponent, MessageStripComponent } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { ReportVariantsComponent } from '../shared/report-variants.component';
import { VariantLoad, applyParameters, variantParameters } from '../shared/report-variant.model';
import { SelectOptionComponent } from '../shared/select-option.component';
import { describeLines } from '../shared/select-option.model';

import {
    DisbursementColumn,
    DisbursementReport,
    DisbursementRow,
    DisbursementSelection,
    PERIOD_TYPES,
    amountValue,
    compareCells,
    defaultDisbursementSelection,
    displayDate,
    isAmount
} from './disbursement.model';
import { DisbursementService } from './disbursement.service';

/**
 * Loans Disbursement - Periodwise and Comparison Report (Reports › Loans Management): the selection screen of the SAP
 * report — company code, type (Report / Comparative), fiscal year from / to, abstract report (period), loan contract
 * and business partner ranges, project type and the two switches — and its result from ZPFS_LMS_REPORTS_SRV
 * (DisbursementCompRptSet). SAP returns cells (RowNo, Fieldlabel, Value); the table has one row per RowNo and one
 * column per Fieldlabel. Every column is sortable and can be hidden; a quick filter narrows the rows; the result can
 * be downloaded as Excel.
 */
@Component({
    selector: 'app-disbursement-report',
    templateUrl: './disbursement-report.component.html',
    styleUrls: ['../shared/report-page.base.scss', '../shared/report-page.result.scss', './disbursement-report.component.scss'],
    imports: [FormsModule, SelectOptionComponent, ReportVariantsComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule]
})
export class DisbursementReportComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly periodTypes = PERIOD_TYPES;
    readonly displayDate = displayDate;
    /** Keys of the columns whose values are all amounts: right-aligned and sorted as numbers */
    amountKeys = new Set<string>();

    /** The columns of the result: one per SAP Fieldlabel. */
    get columns(): DisbursementColumn[] {
        return this.report ? this.report.columns : [];
    }

    selection: DisbursementSelection = defaultDisbursementSelection();
    report: DisbursementReport | null = null;
    rows: DisbursementRow[] = [];
    sortKey: string | null = null;
    sortDirection: 'asc' | 'desc' = 'asc';
    quickFilter = '';

    running = false;
    /** The selection criteria section; collapsed after a successful run so the result gets the room. */
    filterOpen = true;
    exporting = false;
    errorMessage = '';
    lastRun = '';

    // ---- column picker: hide / show any number of columns (remembered per report in this browser) ----
    private readonly storageKey = 'reports.disbursement.hidden-columns';
    columnPanelOpen = false;
    hiddenKeys = new Set<string>(this.loadHidden());

    get visibleColumns() {
        return this.columns.filter(c => !this.hiddenKeys.has(c.label));
    }

    get hiddenCount(): number {
        return this.columns.length - this.visibleColumns.length;
    }

    isHidden(column: { label: string }): boolean {
        return this.hiddenKeys.has(column.label);
    }

    toggleColumnPanel(event: Event): void {
        event.stopPropagation();
        this.columnPanelOpen = !this.columnPanelOpen;
    }

    /** Hides or shows a column; at least one column always stays visible. */
    toggleColumn(column: { label: string }): void {
        const key = column.label;
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
            return Array.isArray(list) ? list.filter(k => typeof k === 'string') : [];
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

    constructor(private service: DisbursementService, private messageService: MessageService, private router: Router) { }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    toggleFilter(): void {
        this.filterOpen = !this.filterOpen;
    }

    // ---- variants: the selection screen saved under a name (everything on the screen) ----
    private readonly variantSkip: string[] = [];
    readonly currentParameters = () => variantParameters(this.selection, this.variantSkip);

    onVariant(load: VariantLoad): void {
        this.selection = applyParameters(defaultDisbursementSelection(), load.parameters, this.variantSkip);
        if (load.run) {
            this.execute();
        }
    }

    /** Execute: runs the SAP report for the selection. */
    execute(): void {
        if (this.running) {
            return;
        }
        this.errorMessage = '';
        const s = this.selection;
        for (const [label, year] of [['Fiscal Year', s.fiscalYearFrom], ['Fiscal Year to', s.fiscalYearTo]]) {
            if (year.trim() && !/^\d{4}$/.test(year.trim())) {
                this.errorMessage = `${label}: ${year.trim()} is not a year (yyyy).`;
                return;
            }
        }
        if (s.fiscalYearFrom.trim() && s.fiscalYearTo.trim() && s.fiscalYearTo.trim() < s.fiscalYearFrom.trim()) {
            this.errorMessage = 'Fiscal Year to must not be before Fiscal Year.';
            return;
        }
        this.running = true;
        this.service.run(this.selection).pipe(takeUntil(this.destroy$)).subscribe({
            next: report => {
                this.running = false;
                this.report = report;
                this.amountKeys = this.amountColumns(report);
                this.sortKey = null;
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
        this.selection = defaultDisbursementSelection();
        this.errorMessage = '';
    }

    back(): void {
        this.router.navigate(['/reports']);
    }

    /** Header click: sorts by the column, a second click reverses, a third click removes the sort. */
    sort(column: DisbursementColumn): void {
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

    sortIcon(column: DisbursementColumn): string {
        if (this.sortKey !== column.key) {
            return 'sort';
        }
        return this.sortDirection === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(column: DisbursementColumn): string {
        if (this.sortKey !== column.key) {
            return 'none';
        }
        return this.sortDirection === 'asc' ? 'ascending' : 'descending';
    }

    onQuickFilter(): void {
        this.applyView();
    }

    cell(row: DisbursementRow, column: DisbursementColumn): string {
        const value = row[column.key];
        return value === null || value === undefined ? '' : value;
    }

    isAmountColumn(column: DisbursementColumn): boolean {
        return this.amountKeys.has(column.key);
    }

    private amountColumns(report: DisbursementReport): Set<string> {
        const keys = new Set<string>();
        for (const column of report.columns) {
            const values = report.rows.map(r => r[column.key]).filter((v): v is string => !!v);
            if (values.length && values.every(isAmount)) {
                keys.add(column.key);
            }
        }
        return keys;
    }

    /** The active selection as short chips, e.g. "Company Code PFS". */
    get criteria(): string[] {
        const s = this.selection;
        const chips: string[] = [`Company Code ${s.companyCode}`, s.mode === 'C' ? 'Comparative' : 'Report'];
        const add = (label: string, text: string) => { if (text) { chips.push(`${label} ${text}`); } };
        if (s.fiscalYearFrom.trim()) { chips.push(`Fiscal Year ${s.fiscalYearFrom.trim()}${s.fiscalYearTo.trim() ? ' – ' + s.fiscalYearTo.trim() : ''}`); }
        const period = this.periodTypes.find(p => p.code === s.periodType);
        if (period) { chips.push(period.text); }
        add('Period filter', s.periodFilter.trim());
        add('Contract', describeLines(s.contract));
        add('Partner', describeLines(s.partner));
        add('Project Type', s.projectType.trim());
        if (s.allLoans) { chips.push('All loans'); }
        if (s.suppressZero) { chips.push('Nil disbursements filtered'); }
        return chips;
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
        this.service.exportExcel(this.selection, this.rows, this.visibleColumns).pipe(takeUntil(this.destroy$)).subscribe({
            next: blob => {
                this.exporting = false;
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = `loans-disbursement-report-${new Date().toISOString().slice(0, 10)}.xlsx`;
                link.click();
                URL.revokeObjectURL(url);
            },
            error: error => {
                this.exporting = false;
                this.messageService.showError(this.service.errorMessage(error, 'The Excel file could not be created.'));
            }
        });
    }

    private compare(a: string | null, b: string | null, amount: boolean): number {
        if (amount && a && b) {
            return amountValue(a) - amountValue(b);
        }
        return compareCells(a, b, 'text');
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
            rows = rows.sort((a, b) => sign * this.compare(a[key], b[key], this.amountKeys.has(column.key)));
        }
        this.rows = rows;
    }
}
