import { LowerCasePipe } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
import { debounceTime, takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { ConfirmDialogComponent, ConfirmDialogData } from '../../collateral-management/confirm-dialog/confirm-dialog.component';
import {
    EMPTY_SAP_LOG_FILTER,
    SAP_LOG_PAGE_SIZES,
    SapLogFilter,
    SapLogReportPage,
    SapLogRow,
    formatSize,
    urlPath
} from '../sap-integration-log.model';
import { SapIntegrationLogService } from '../sap-integration-log.service';
import { SapLogDetailDialogComponent, SapLogDetailDialogData } from '../sap-log-detail-dialog/sap-log-detail-dialog.component';

/**
 * Administration report "SAP Integration Log": every HTTP request the portal sent to SAP (all gateway services),
 * with totals, filters, paging, the full request/response of a call, CSV export and a purge by age.
 */
@Component({
    selector: 'app-sap-integration-log-report',
    templateUrl: './sap-integration-log-report.component.html',
    styleUrl: './sap-integration-log-report.component.scss',
    imports: [LowerCasePipe, FormsModule, DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent,
        MessageStripComponent, FormModule, PaginationModule]
})
export class SapIntegrationLogReportComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();
    private readonly search$ = new Subject<void>();

    readonly pageSizes = SAP_LOG_PAGE_SIZES;
    readonly methods = ['GET', 'POST', 'PUT', 'DELETE'];
    readonly formatSize = formatSize;
    readonly urlPath = urlPath;

    data: SapLogReportPage | null = null;
    filter: SapLogFilter = { ...EMPTY_SAP_LOG_FILTER };
    page = 0;
    size = 25;
    purgeDays = 90;

    loading = false;
    loadFailed = false;
    exporting = false;
    purging = false;
    openingId: number | null = null;

    constructor(private service: SapIntegrationLogService,
                private messageService: MessageService,
                private dialogService: DialogService) { }

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

    get successRate(): string {
        const s = this.data?.summary;
        if (!s || !s.total) {
            return '–';
        }
        return `${Math.round(s.succeeded * 100 / s.total)}%`;
    }

    get hasFilter(): boolean {
        const f = this.filter;
        return !!(f.from || f.to || f.method || f.outcome || f.businessProcess || f.serviceName || f.search.trim());
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
                if (data.retentionDays > 0) {
                    this.purgeDays = data.retentionDays;
                }
            },
            error: error => {
                this.loading = false;
                this.loadFailed = !this.data;
                this.messageService.showError(this.service.errorMessage(error, 'The SAP integration log could not be loaded.'));
            }
        });
    }

    onFilterChange(): void {
        this.search$.next();
    }

    clearFilters(): void {
        this.filter = { ...EMPTY_SAP_LOG_FILTER };
        this.page = 0;
        this.load();
    }

    showFailedOnly(): void {
        this.filter = { ...this.filter, outcome: 'failed' };
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

    /** Short description of the target: the entity set (and key) rather than the whole URL. */
    target(row: SapLogRow): string {
        const path = urlPath(row.url);
        const index = row.entitySet ? path.indexOf(row.entitySet) : -1;
        return index >= 0 ? path.substring(index) : path;
    }

    open(row: SapLogRow): void {
        if (this.openingId !== null) {
            return;
        }
        this.openingId = row.id;
        this.service.getDetail(row.id).pipe(takeUntil(this.destroy$)).subscribe({
            next: detail => {
                this.openingId = null;
                const data: SapLogDetailDialogData = { detail };
                this.dialogService.open(SapLogDetailDialogComponent, { data, responsivePadding: true, width: '64rem' });
            },
            error: error => {
                this.openingId = null;
                this.messageService.showError(this.service.errorMessage(error, 'The SAP call could not be loaded.'));
            }
        });
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
                link.download = `sap-integration-log-${new Date().toISOString().slice(0, 10)}.csv`;
                link.click();
                URL.revokeObjectURL(url);
            },
            error: error => {
                this.exporting = false;
                this.messageService.showError(this.service.errorMessage(error, 'The export could not be created.'));
            }
        });
    }

    purge(): void {
        if (!this.data?.canPurge || this.purging) {
            return;
        }
        const days = Math.max(0, Math.floor(this.purgeDays || 0));
        const data: ConfirmDialogData = {
            title: 'Delete Log Entries',
            message: days > 0
                ? `Delete all SAP calls logged more than ${days} days ago? This cannot be undone.`
                : 'Delete the whole SAP integration log? This cannot be undone.',
            confirmLabel: 'Delete',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed) {
                        this.runPurge(days);
                    }
                },
                error: () => undefined
            });
    }

    private runPurge(days: number): void {
        this.purging = true;
        this.service.purge(days).pipe(takeUntil(this.destroy$)).subscribe({
            next: result => {
                this.purging = false;
                this.messageService.showSuccess(`${result.deleted} log ${result.deleted === 1 ? 'entry' : 'entries'} deleted.`);
                this.page = 0;
                this.load();
            },
            error: error => {
                this.purging = false;
                this.messageService.showError(this.service.errorMessage(error, 'The log could not be purged.'));
            }
        });
    }
}
