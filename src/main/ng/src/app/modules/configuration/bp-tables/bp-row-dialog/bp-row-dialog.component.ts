import { Component, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
    BarModule,
    DialogModule,
    DialogRef,
    FormModule,
    IconComponent,
    MessageStripComponent,
    TitleComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { BpOption, BpRow, BpTableDefinition, BpTableField, fieldOptions } from '../bp-table.model';
import { BpTableService } from '../bp-table.service';

export interface BpRowDialogData {
    table: BpTableDefinition;
    references: Record<string, BpOption[]>;
    /** The row to change; none to add a row */
    row?: BpRow;
}

/** Adds or changes a row of a business partner configuration table. Closes with the saved row. */
@Component({
    selector: 'app-bp-row-dialog',
    templateUrl: './bp-row-dialog.component.html',
    styleUrl: './bp-row-dialog.component.scss',
    imports: [FormsModule, DialogModule, BarModule, FormModule, IconComponent, MessageStripComponent, TitleComponent]
})
export class BpRowDialogComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly data: BpRowDialogData;
    readonly fields: BpTableField[];
    values: Record<string, string | boolean> = {};

    saving = false;
    submitted = false;
    errorMessage = '';

    constructor(public dialogRef: DialogRef, private service: BpTableService) {
        this.data = dialogRef.data as BpRowDialogData;
        this.fields = this.data.table.fields.filter(field => field.visible);
        for (const field of this.fields) {
            const value = this.data.row?.values[field.name];
            this.values[field.name] = field.type === 'BOOLEAN' ? value === true : value === null || value === undefined ? '' : String(value);
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get isNew(): boolean {
        return !this.data.row;
    }

    /** Keys can only be entered for a new row */
    readOnly(field: BpTableField): boolean {
        return !this.isNew && !field.changeable;
    }

    options(field: BpTableField): BpOption[] | null {
        const options = fieldOptions(field, this.data.references);
        if (!options) {
            return null;
        }
        // keep a stored value that is not (or no longer) in the list
        const current = String(this.values[field.name] ?? '');
        if (current && !options.some(option => option.value === current)) {
            return [{ value: current, label: this.data.row?.labels[field.name] ?? current }, ...options];
        }
        return options;
    }

    missing(field: BpTableField): boolean {
        return this.submitted && field.required && field.type !== 'BOOLEAN' && !String(this.values[field.name] ?? '').trim();
    }

    wide(field: BpTableField): boolean {
        return field.maxLength > 40 || !!field.referenceTable;
    }

    close(): void {
        if (!this.saving) {
            this.dialogRef.dismiss();
        }
    }

    save(): void {
        this.submitted = true;
        this.errorMessage = '';
        if (this.fields.some(field => !this.readOnly(field) && this.missing(field))) {
            this.errorMessage = 'Fill in the required fields.';
            return;
        }
        const values: Record<string, unknown> = {};
        for (const field of this.fields) {
            if (this.readOnly(field)) {
                continue;
            }
            const value = this.values[field.name];
            values[field.name] = typeof value === 'string' ? value.trim() : value;
        }
        const table = this.data.table.key;
        const request$ = this.data.row
            ? this.service.update(table, this.data.row.id, values)
            : this.service.create(table, values);
        this.saving = true;
        request$.pipe(takeUntil(this.destroy$)).subscribe({
            next: row => {
                this.saving = false;
                this.dialogRef.close(row);
            },
            error: error => {
                this.saving = false;
                this.errorMessage = this.service.errorMessage(error, 'The entry could not be saved.');
            }
        });
    }
}
