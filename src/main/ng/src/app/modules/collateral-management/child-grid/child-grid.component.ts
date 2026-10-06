import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { ButtonComponent, DialogService, IconComponent, TableModule } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { CollateralService } from '../collateral.service';
import { GridColumn, GridDef, ValueLists, formatDate, valueText } from '../collateral.model';
import { ChildDialogComponent, ChildDialogData } from '../child-dialog/child-dialog.component';
import { ConfirmDialogComponent, ConfirmDialogData } from '../confirm-dialog/confirm-dialog.component';

type Row = Record<string, unknown>;

/**
 * Grid of a child table of a collateral (Coverage, RoC, CERSAI, NeSL, Documents, Securities Positions). Create and Change open the dialog in change
 * mode; double-click (or the arrow) opens it in display mode. Rows are saved directly when the dialog is saved.
 */
@Component({
    selector: 'app-collateral-child-grid',
    templateUrl: './child-grid.component.html',
    styleUrl: './child-grid.component.scss',
    imports: [ButtonComponent, IconComponent, TableModule]
})
export class ChildGridComponent implements OnDestroy {

    @Input({ required: true }) grid!: GridDef;
    @Input() rows: Row[] = [];
    @Input() lists: ValueLists = {};
    /** Id of the collateral; null while it is not saved yet. */
    @Input() itemId: string | null = null;
    /** Create, Change and Delete are offered in change mode of the collateral only. */
    @Input() editable = false;
    /** Whether the user may change rows at all (write role). */
    @Input() canWrite = false;

    @Output() rowsChange = new EventEmitter<Row[]>();

    private readonly destroy$ = new Subject<void>();

    selected: Row | null = null;
    deleting = false;

    constructor(
        private collateralService: CollateralService,
        private messageService: MessageService,
        private dialogService: DialogService
    ) {}

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get canChange(): boolean {
        return this.editable && this.canWrite && !!this.itemId;
    }

    isSelected(row: Row): boolean {
        return !!this.selected && this.selected['id'] === row['id'];
    }

    select(row: Row): void {
        this.selected = row;
    }

    cell(row: Row, column: GridColumn): string {
        const value = row[column.key];
        if (value === null || value === undefined || value === '') {
            return '';
        }
        switch (column.type) {
            case 'date':
                return formatDate(value);
            case 'code':
                return valueText(this.lists, column.list, value);
            case 'number':
                return Number(value).toLocaleString('en-IN', {
                    minimumFractionDigits: column.decimals ?? 0,
                    maximumFractionDigits: column.decimals ?? 3
                });
            default:
                return String(value);
        }
    }

    /** Download link of the uploaded document of a row (column type 'file'). */
    fileUrl(row: Row): string {
        return this.collateralService.downloadUrl(String(row['fileReference']), row['fileName'] as string | null);
    }

    create(): void {
        this.openDialog(null, 'edit');
    }

    change(): void {
        if (this.selected) {
            this.openDialog(this.selected, 'edit');
        }
    }

    display(row: Row | null = this.selected): void {
        if (row) {
            this.selected = row;
            this.openDialog(row, 'display');
        }
    }

    remove(): void {
        const row = this.selected;
        if (!row) {
            return;
        }
        const data: ConfirmDialogData = {
            title: `Delete ${this.grid.rowLabel}`,
            message: `Delete the selected ${this.grid.rowLabel}? A change document is written.`,
            confirmLabel: 'Delete',
            destructive: true
        };
        this.dialogService.open(ConfirmDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: confirmed => {
                    if (confirmed === true) {
                        this.delete(row);
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    private delete(row: Row): void {
        this.deleting = true;
        this.collateralService.deleteChild(this.grid.type, String(row['id'])).pipe(takeUntil(this.destroy$)).subscribe({
            next: () => {
                this.deleting = false;
                this.selected = null;
                this.rowsChange.emit(this.rows.filter(candidate => candidate['id'] !== row['id']));
                this.messageService.showSuccess(`${this.capitalized} deleted.`);
            },
            error: error => {
                this.deleting = false;
                this.messageService.showError(this.collateralService.errorMessage(error,
                    `The ${this.grid.rowLabel} could not be deleted.`));
            }
        });
    }

    private openDialog(row: Row | null, mode: 'display' | 'edit'): void {
        if (!this.itemId) {
            return;
        }
        const data: ChildDialogData = {
            grid: this.grid,
            itemId: this.itemId,
            row,
            mode,
            canEdit: this.canChange,
            lists: this.lists
        };
        this.dialogService.open(ChildDialogComponent, { data, responsivePadding: true, width: '52rem' }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (saved: unknown) => {
                    if (saved && typeof saved === 'object') {
                        const savedRow = saved as Row;
                        const exists = this.rows.some(candidate => candidate['id'] === savedRow['id']);
                        this.rowsChange.emit(exists
                            ? this.rows.map(candidate => candidate['id'] === savedRow['id'] ? savedRow : candidate)
                            : [...this.rows, savedRow]);
                        this.selected = savedRow;
                        this.messageService.showSuccess(`${this.capitalized} saved.`);
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    private get capitalized(): string {
        const label = this.grid.rowLabel;
        return label.charAt(0).toUpperCase() + label.slice(1);
    }
}
