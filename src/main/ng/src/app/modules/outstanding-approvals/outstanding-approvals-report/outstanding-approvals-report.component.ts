import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
    ButtonComponent,
    DynamicPageModule,
    FormModule,
    IconComponent,
    MessageStripComponent,
    PaginationModule,
    ToolbarComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { APPROVAL_PAGE_SIZES, ApprovalFilter, ApprovalReportPage, ApprovalRow } from '../outstanding-approvals.model';
import { OutstandingApprovalsService } from '../outstanding-approvals.service';

/**
 * Administration report "Outstanding Workflow Approvals": the active approval tasks from Activiti (ACT_RU_TASK and
 * related tables), with totals, filters, paging and CSV export. Read-only.
 */
@Component({
    selector: 'app-outstanding-approvals-report',
    templateUrl: './outstanding-approvals-report.component.html',
    styleUrl: './outstanding-approvals-report.component.scss',
    imports: [FormsModule, DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent, MessageStripComponent,
        FormModule, PaginationModule]
})
export class OutstandingApprovalsReportComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();
    private readonly search$ = new Subject<void>();

    readonly pageSizes = APPROVAL_PAGE_SIZES;

    data: ApprovalReportPage | null = null;
    filter: ApprovalFilter = { processName: '', approver: '', requestor: '', search: '', minAgeDays: null };
    page = 0;
    size = 25;

    loading = false;
    loadFailed = false;
    exporting = false;

    constructor(private service: OutstandingApprovalsService, private messageService: MessageService) { }

    ngOnInit(): void {
        this.load();
        this.search$.pipe(debounceTime(300), takeUntil(this.destroy$)).subscribe(() => {
            this.page = 0;
            this.load();
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get canView(): boolean {
        return !!this.data?.canView;
    }

    get rangeText(): string {
        if (!this.data || !this.data.total) {
            return '';
        }
        const from = this.data.page * this.data.size + 1;
        const to = Math.min(this.data.total, from + this.data.rows.length - 1);
        return `${from}–${to} of ${this.data.total}`;
    }

    load(): void {
        this.loading = true;
        this.loadFailed = false;
        this.service.getReport(this.filter, this.page, this.size).pipe(takeUntil(this.destroy$)).subscribe({
            next: data => {
                this.loading = false;
                this.data = data;
                this.page = data.page;
                this.size = data.size;
            },
            error: error => {
                this.loading = false;
                this.loadFailed = !this.data;
                this.messageService.showError(this.service.errorMessage(error, 'The report could not be loaded.'));
            }
        });
    }

    onFilterChange(): void {
        this.search$.next();
    }

    clearFilters(): void {
        this.filter = { processName: '', approver: '', requestor: '', search: '', minAgeDays: null };
        this.page = 0;
        this.load();
    }

    goToPage(page: number): void {
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

    isOverdue(row: ApprovalRow): boolean {
        return !!this.data && row.ageDays >= this.data.summary.overdueThresholdDays;
    }

    exportCsv(): void {
        if (!this.data || !this.data.total) {
            return;
        }
        this.exporting = true;
        this.service.exportCsv(this.filter).pipe(takeUntil(this.destroy$)).subscribe({
            next: blob => {
                this.exporting = false;
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = `outstanding-approvals-${new Date().toISOString().slice(0, 10)}.csv`;
                link.click();
                URL.revokeObjectURL(url);
            },
            error: error => {
                this.exporting = false;
                this.messageService.showError(this.service.errorMessage(error, 'The export could not be created.'));
            }
        });
    }
}
