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
    BALANCE_LIST_COLUMNS,
    BalanceListColumn,
    BalanceListReport,
    BalanceListRow,
    BalanceListSelection,
    compareCells,
    defaultBalanceListSelection,
    displayAmount,
    displayDate
} from './balance-list.model';
import { BalanceListService } from './balance-list.service';

/**
 * Loans Balance List (Reports › Loans Management): the selection screen of the SAP report "Balance List" (FNR7) —
 * company code (fixed PFS), contract number, the general selections (product type, loan type, contract currency,
 * account assignment reference, last changed by) and the output control (key date, display currency, balance and
 * total switches) — and its result from ZPFS_LMS_REPORTS_SRV (ZFNR7Report). Every column of the result is sortable
 * and can be hidden; a quick filter narrows the rows; the result can be downloaded as Excel.
 */
@Component({
    selector: 'app-balance-list-report',
    templateUrl: './balance-list-report.component.html',
    styleUrls: ['../shared/report-page.base.scss', '../shared/report-page.result.scss', './balance-list-report.component.scss'],
    imports: [FormsModule, SelectOptionComponent, ReportVariantsComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule]
})
export class BalanceListReportComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly columns = BALANCE_LIST_COLUMNS;
    readonly displayDate = displayDate;

    selection: BalanceListSelection = defaultBalanceListSelection();
    report: BalanceListReport | null = null;
    rows: BalanceListRow[] = [];
    sortKey: keyof BalanceListRow | null = null;
    sortDirection: 'asc' | 'desc' = 'asc';
    quickFilter = '';

    running = false;
    /** The selection criteria section; collapsed after a successful run so the result gets the room. */
    filterOpen = true;
    exporting = false;
    errorMessage = '';
    lastRun = '';

    // ---- column picker: hide / show any number of columns (remembered per report in this browser) ----
    private readonly storageKey = 'reports.balance-list.hidden-columns';
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

    constructor(private service: BalanceListService, private messageService: MessageService, private router: Router) { }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    toggleFilter(): void {
        this.filterOpen = !this.filterOpen;
    }

    // ---- variants: the selection screen saved under a name (not saved: companyCode, keyDate) ----
    private readonly variantSkip: string[] = ['companyCode', 'keyDate'];
    readonly currentParameters = () => variantParameters(this.selection, this.variantSkip);

    onVariant(load: VariantLoad): void {
        this.selection = applyParameters({ ...defaultBalanceListSelection(), keyDate: this.selection.keyDate }, load.parameters, this.variantSkip);
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
        this.selection = defaultBalanceListSelection();
        this.errorMessage = '';
    }

    back(): void {
        this.router.navigate(['/reports']);
    }

    /** Header click: sorts by the column, a second click reverses, a third click removes the sort. */
    sort(column: BalanceListColumn): void {
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

    sortIcon(column: BalanceListColumn): string {
        if (this.sortKey !== column.key) {
            return 'sort';
        }
        return this.sortDirection === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(column: BalanceListColumn): string {
        if (this.sortKey !== column.key) {
            return 'none';
        }
        return this.sortDirection === 'asc' ? 'ascending' : 'descending';
    }

    onQuickFilter(): void {
        this.applyView();
    }

    cell(row: BalanceListRow, column: BalanceListColumn): string {
        const value = row[column.key];
        if (value === null || value === undefined) {
            return '';
        }
        if (column.type === 'date') {
            return displayDate(value);
        }
        if (column.type === 'amount') {
            return displayAmount(value);
        }
        return value;
    }

    /** The active selection as short chips, e.g. "Company Code PFS". */
    get criteria(): string[] {
        const s = this.selection;
        const chips: string[] = [`Company Code ${s.companyCode}`];
        const add = (label: string, text: string) => { if (text) { chips.push(`${label} ${text}`); } };
        add('Contract', describeLines(s.contract));
        add('Product Type', describeLines(s.productType));
        add('Loan Type', describeLines(s.loanType));
        add('Currency', describeLines(s.currency));
        add('Acct Ref.', describeLines(s.acctRef));
        add('Changed by', describeLines(s.changedBy));
        if (s.evaluateCompleted) { chips.push('Incl. completed contracts'); }
        if (s.keyDate) { chips.push(`Key date ${displayDate(s.keyDate)}`); }
        if (s.displayCurrency.trim()) { chips.push(`Display in ${s.displayCurrency.trim().toUpperCase()}`); }
        if (s.nonZeroOnly) { chips.push('Balances <> 0'); }
        if (s.totalsForZero) { chips.push('Totals for zero balances'); }
        if (s.totalsOnly) { chips.push('Totals only'); }
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
        this.service.exportExcel(this.selection, this.rows, this.visibleColumns.map(c => c.label)).pipe(takeUntil(this.destroy$)).subscribe({
            next: blob => {
                this.exporting = false;
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = `loans-balance-list-${new Date().toISOString().slice(0, 10)}.xlsx`;
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
            rows = rows.sort((a, b) => sign * compareCells(a[key], b[key], column.type === 'amount' ? 'number' : column.type));
        }
        this.rows = rows;
    }
}
