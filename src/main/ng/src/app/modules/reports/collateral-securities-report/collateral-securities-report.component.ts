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
import { describeLines } from '../shared/select-option.model';

import {
    CollateralSecuritiesColumn,
    CollateralSecuritiesReport,
    CollateralSecuritiesRow,
    CollateralSecuritiesSelection,
    COMPLIANCE_FILTERS,
    compareCells,
    defaultCollateralSecuritiesSelection,
    displayAmount,
    displayDate
} from './collateral-securities.model';
import { CollateralSecuritiesService } from './collateral-securities.service';
import { ReportElapsedComponent, ReportRunStatsComponent } from '../shared/report-run-stats.component';
import { recordCountText } from '../shared/report-timing';

/**
 * Collateral Securities Report (Reports › Collaterals): the selection screen of the SAP Collateral Securities Report —
 * company code, loan number and loan partner ranges and the compliance filter — and its result from
 * ZPFS_LMS_REPORTS_SRV (CollateralSecurityReportSet), one row per collateral condition. Every column is sortable and can
 * be hidden (also as a saved layout); a quick filter narrows the rows; the result can be downloaded as Excel.
 */
@Component({
    selector: 'app-collateral-securities-report',
    templateUrl: './collateral-securities-report.component.html',
    styleUrls: ['../shared/report-page.base.scss', '../shared/report-page.result.scss', './collateral-securities-report.component.scss'],
    imports: [FormsModule, SelectOptionComponent, ReportVariantsComponent, ReportRunStatsComponent, ReportElapsedComponent, ButtonComponent, IconComponent, MessageStripComponent, FormModule]
})
export class CollateralSecuritiesReportComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    /** The columns of the result: the properties of the entity, in business order. */
    get columns(): CollateralSecuritiesColumn[] {
        return this.report ? this.report.columns : [];
    }

    readonly complianceFilters = COMPLIANCE_FILTERS;
    selection: CollateralSecuritiesSelection = defaultCollateralSecuritiesSelection();
    report: CollateralSecuritiesReport | null = null;
    rows: CollateralSecuritiesRow[] = [];
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
    private readonly storageKey = 'reports.collateral-securities.hidden-columns';
    columnPanelOpen = false;
    hiddenKeys = new Set<string>(this.loadHidden());

    get visibleColumns() {
        return this.columns.filter(c => !this.hiddenKeys.has(c.key));
    }

    get hiddenCount(): number {
        return this.columns.length - this.visibleColumns.length;
    }

    isHidden(column: { key: string }): boolean {
        return this.hiddenKeys.has(column.key);
    }

    toggleColumnPanel(event: Event): void {
        event.stopPropagation();
        this.columnPanelOpen = !this.columnPanelOpen;
    }

    /** Hides or shows a column; at least one column always stays visible. */
    toggleColumn(column: { key: string }): void {
        const key = column.key;
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

    // ---- layouts: the column choice saved under a name, like a variant (stored as report key collateral-securities_layout) ----
    readonly currentLayout = () => layoutParameters(this.columns.length ? this.columns.filter(c => this.hiddenKeys.has(c.key)).map(c => c.key) : [...this.hiddenKeys]);

    onLayout(load: VariantLoad): void {
        const hidden = layoutHidden(load.parameters, this.columns.map(c => c.key));
        if (hidden) {
            this.hiddenKeys = new Set(hidden);
            this.saveHidden();
        }
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

    constructor(private service: CollateralSecuritiesService, private messageService: MessageService, private router: Router) { }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    toggleFilter(): void {
        this.filterOpen = !this.filterOpen;
    }

    // ---- variants: the whole selection screen saved under a name ----
    private readonly variantSkip: string[] = [];
    readonly currentParameters = () => variantParameters(this.selection, this.variantSkip);

    onVariant(load: VariantLoad): void {
        this.selection = applyParameters(defaultCollateralSecuritiesSelection(), load.parameters, this.variantSkip);
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
        if (!this.selection.companyCode?.trim()) {
            this.errorMessage = 'Enter the Company Code.';
            return;
        }
        if (!COMPLIANCE_FILTERS.some(f => this.selection[f.key])) {
            this.errorMessage = 'Select at least one filter (Complied, Not Complied, In Process, Partially Complied or Timeline Available).';
            return;
        }
        this.running = true;
        this.service.run(this.selection).pipe(takeUntil(this.destroy$)).subscribe({
            next: report => {
                this.running = false;
                this.report = report;
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
        this.selection = defaultCollateralSecuritiesSelection();
        this.errorMessage = '';
    }

    back(): void {
        this.router.navigate(['/reports']);
    }

    /** Header click: sorts by the column, a second click reverses, a third click removes the sort. */
    sort(column: CollateralSecuritiesColumn): void {
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

    sortIcon(column: CollateralSecuritiesColumn): string {
        if (this.sortKey !== column.key) {
            return 'sort';
        }
        return this.sortDirection === 'asc' ? 'sort-ascending' : 'sort-descending';
    }

    ariaSort(column: CollateralSecuritiesColumn): string {
        if (this.sortKey !== column.key) {
            return 'none';
        }
        return this.sortDirection === 'asc' ? 'ascending' : 'descending';
    }

    onQuickFilter(): void {
        this.applyView();
    }

    cell(row: CollateralSecuritiesRow, column: CollateralSecuritiesColumn): string {
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

    isAmountColumn(column: CollateralSecuritiesColumn): boolean {
        return column.type === 'amount' || column.type === 'number';
    }

    /** The active selection as short chips, e.g. "Company Code PFS". */
    get criteria(): string[] {
        const s = this.selection;
        const chips: string[] = [`Company Code ${s.companyCode}`];
        const contract = describeLines(s.contract);
        if (contract) { chips.push(`Loan ${contract}`); }
        const partner = describeLines(s.partner);
        if (partner) { chips.push(`Partner ${partner}`); }
        const off = COMPLIANCE_FILTERS.filter(f => !s[f.key]);
        if (off.length) {
            chips.push(`Only ${COMPLIANCE_FILTERS.filter(f => s[f.key]).map(f => f.label).join(', ') || '–'}`);
        }
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
        return recordCountText(shown, total);
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
                link.download = `collateral-securities-${new Date().toISOString().slice(0, 10)}.xlsx`;
                link.click();
                URL.revokeObjectURL(url);
            },
            error: error => {
                this.exporting = false;
                this.messageService.showError(this.service.errorMessage(error, 'The Excel file could not be created.'));
            }
        });
    }

    private compare(a: string | null, b: string | null, column: CollateralSecuritiesColumn): number {
        // amounts and numbers numerically, dates as yyyy-MM-dd, texts with numbers in them naturally; empty last
        return compareCells(a, b, this.isAmountColumn(column) ? 'number' : 'text');
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
            rows = rows.sort((a, b) => sign * this.compare(a[key], b[key], column));
        }
        this.rows = rows;
    }
}
