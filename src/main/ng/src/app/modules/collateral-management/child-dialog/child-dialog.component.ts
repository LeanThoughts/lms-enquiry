import { Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import {
    BarModule,
    ButtonComponent,
    DialogModule,
    DialogRef,
    IconComponent,
    MessageStripComponent,
    TitleComponent
} from '@fundamental-ngx/core';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CollateralFieldComponent } from '../collateral-field/collateral-field.component';
import { CollateralService } from '../collateral.service';
import { CHILD_MAX_LENGTH, FieldDef, GridDef, ValueEntry, ValueLists } from '../collateral.model';
import { buildControls, fromFormValue, toFormValue } from '../collateral-form';

/** Data passed to {@link ChildDialogComponent}. */
export interface ChildDialogData {
    grid: GridDef;
    itemId: string;
    /** The row to display or change; null to create one. */
    row: Record<string, unknown> | null;
    mode: 'display' | 'edit';
    /** Whether the user may switch from display to change. */
    canEdit: boolean;
    lists: ValueLists;
}

/**
 * Create / change / display dialog of a child row (Coverage, RoC, CERSAI, NeSL, Documents, Securities Positions).
 * Saves directly to the backend and closes with the saved row. For documents the dialog also uploads the file.
 */
@Component({
    selector: 'app-collateral-child-dialog',
    templateUrl: './child-dialog.component.html',
    styleUrl: './child-dialog.component.scss',
    imports: [ReactiveFormsModule, DialogModule, BarModule, ButtonComponent, IconComponent, TitleComponent,
        MessageStripComponent, CollateralFieldComponent]
})
export class ChildDialogComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly data: ChildDialogData;
    readonly fields: FieldDef[];
    readonly form: FormGroup;

    edit: boolean;
    saving = false;
    errorMessage = '';

    /** Uploaded document of the row (documents only): reference in the portal file storage and file name. */
    fileReference: string | null;
    fileName: string | null;
    /** Name of the file being uploaded, while the upload runs. */
    uploading: string | null = null;

    @ViewChild('fileInput') fileInput?: ElementRef<HTMLInputElement>;

    constructor(public dialogRef: DialogRef, private collateralService: CollateralService) {
        this.data = dialogRef.data as ChildDialogData;
        this.fields = this.data.grid.fields;
        this.form = new FormGroup(buildControls(this.fields));
        this.form.reset(toFormValue(this.fields, this.data.row ?? this.data.grid.defaults ?? {}));
        this.edit = this.data.mode === 'edit';
        this.fileReference = (this.data.row?.['fileReference'] as string | null | undefined) ?? null;
        this.fileName = (this.data.row?.['fileName'] as string | null | undefined) ?? null;
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get isNew(): boolean {
        return !this.data.row?.['id'];
    }

    get title(): string {
        return this.data.grid.dialogTitle + (this.isNew ? ' – New' : '');
    }

    get modeText(): string {
        return this.edit ? (this.isNew ? 'Create' : 'Change') : 'Display';
    }

    options(def: FieldDef): ValueEntry[] {
        return def.list ? this.data.lists[def.list] ?? [] : [];
    }

    change(): void {
        this.edit = true;
    }

    // ------------------------------------------------------------------------------------------- document upload

    /** Component ID of a document that was migrated from SAP and is kept in SAP's document service. */
    get sapDocumentId(): string {
        return (this.data.row?.['bdsDocumentId'] as string | null | undefined) ?? '';
    }

    get fileUrl(): string {
        return this.fileReference ? this.collateralService.downloadUrl(this.fileReference, this.fileName) : '';
    }

    chooseFile(): void {
        this.fileInput?.nativeElement.click();
    }

    upload(event: Event): void {
        const input = event.target as HTMLInputElement;
        const file = input.files?.[0];
        input.value = '';
        if (!file) {
            return;
        }
        this.errorMessage = '';
        this.uploading = file.name;
        this.collateralService.uploadFile(file).pipe(takeUntil(this.destroy$)).subscribe({
            next: reference => {
                this.uploading = null;
                this.fileReference = reference;
                this.fileName = file.name.substring(0, CHILD_MAX_LENGTH['fileName'] ?? 255);
            },
            error: error => {
                this.uploading = null;
                this.errorMessage = this.collateralService.errorMessage(error, 'The document could not be uploaded.');
            }
        });
    }

    removeFile(): void {
        this.fileReference = null;
        this.fileName = null;
    }

    close(): void {
        if (!this.saving) {
            this.dialogRef.dismiss();
        }
    }

    save(): void {
        if (this.uploading) {
            return;
        }
        this.form.markAllAsTouched();
        if (this.form.invalid) {
            this.errorMessage = 'Check the highlighted fields.';
            return;
        }
        this.errorMessage = '';
        this.saving = true;
        const row = fromFormValue(this.fields, this.form, this.data.row ?? {});
        if (this.data.grid.upload) {
            row['fileReference'] = this.fileReference;
            row['fileName'] = this.fileReference ? this.fileName : null;
        }
        const { type } = this.data.grid;
        const request$: Observable<Record<string, unknown>> = this.isNew
            ? this.collateralService.createChild(type, this.data.itemId, row)
            : this.collateralService.updateChild(type, String(this.data.row!['id']), row);
        request$.pipe(takeUntil(this.destroy$)).subscribe({
            next: saved => {
                this.saving = false;
                this.dialogRef.close(saved);
            },
            error: error => {
                this.saving = false;
                this.errorMessage = this.collateralService.errorMessage(error,
                    `The ${this.data.grid.rowLabel} could not be saved.`);
            }
        });
    }
}
