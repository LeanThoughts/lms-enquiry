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
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { BupaFieldStatusService } from '../bupa-field-status.service';
import { EntitySetFieldStatus, FIELD_STATUSES, RoleFieldStatus, RoleSummary } from '../bupa-field-status.model';

/** Data passed to {@link FieldStatusDialogComponent}. */
export interface FieldStatusDialogData {
    /** entity: add an entity field; set: add an entity set field; copy: copy the rows of another role. */
    mode: 'entity' | 'set' | 'copy';
    roleCode: string;
    roleDescription: string | null;
    /** Known entities / entity sets (suggestions). */
    entities: string[];
    entitySets: string[];
    /** Roles that can be copied from. */
    roles: RoleSummary[];
    /** Values to start with, e.g. the entity filtered in the page. */
    initialEntity?: string;
    /** App "BP Entity Set Fields": the entity set is fixed; Add and Copy work on this entity set only. */
    fixedEntitySet?: string;
    /** Key field values used in the fixed entity set (suggestions). */
    keyFieldValues?: string[];
    /** Descriptions of the key field values (e.g. identification categories), shown with the suggestions. */
    keyFieldDescriptions?: Record<string, string>;
    /** Initial key field value, e.g. the one filtered in the page. */
    initialKeyFieldValue?: string;
}

/** Add Field / Add Entity Set Field / Copy from Role. Saves directly and closes with the role's rows. */
@Component({
    selector: 'app-field-status-dialog',
    templateUrl: './field-status-dialog.component.html',
    styleUrl: './field-status-dialog.component.scss',
    imports: [FormsModule, DialogModule, BarModule, FormModule, IconComponent, MessageStripComponent, TitleComponent]
})
export class FieldStatusDialogComponent implements OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly data: FieldStatusDialogData;
    readonly statuses = FIELD_STATUSES;

    entity = '';
    entitySet = '';
    keyFieldValue = '';
    fieldName = '';
    keyField = false;
    minimumEntries: number | null = 0;
    fieldStatus = 1;
    sourceRoleCode = '';

    saving = false;
    submitted = false;
    errorMessage = '';

    constructor(public dialogRef: DialogRef, private service: BupaFieldStatusService) {
        this.data = dialogRef.data as FieldStatusDialogData;
        if (this.data.mode === 'entity') {
            this.entity = this.data.initialEntity ?? '';
        } else if (this.data.mode === 'set') {
            this.entitySet = this.data.fixedEntitySet ?? this.data.initialEntity ?? '';
            this.keyFieldValue = this.data.initialKeyFieldValue ?? '';
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get title(): string {
        switch (this.data.mode) {
            case 'entity':
                return 'Add Field';
            case 'set':
                return this.data.fixedEntitySet ? 'Add Field' : 'Add Entity Set Field';
            default:
                return this.data.fixedEntitySet ? 'Copy ' + this.data.fixedEntitySet + ' from Role' : 'Copy from Role';
        }
    }

    get copyRoles(): RoleSummary[] {
        return this.data.roles.filter(role => role.code !== this.data.roleCode
            && role.entityFieldCount + role.entitySetFieldCount > 0);
    }

    /** Description of the key field value being entered, e.g. "PAN Card" for Z00001. */
    get keyFieldDescription(): string {
        return this.data.keyFieldDescriptions?.[this.keyFieldValue.trim()] ?? '';
    }

    missing(value: string | null | undefined): boolean {
        return this.submitted && !(value ?? '').trim();
    }

    close(): void {
        if (!this.saving) {
            this.dialogRef.dismiss();
        }
    }

    save(): void {
        this.submitted = true;
        this.errorMessage = '';
        let request$: Observable<RoleFieldStatus | EntitySetFieldStatus>;
        const fixedSet = this.data.fixedEntitySet;
        const role = this.data.roleCode;
        if (this.data.mode === 'entity') {
            if (!this.entity.trim() || !this.fieldName.trim()) {
                this.errorMessage = 'Fill in the required fields.';
                return;
            }
            request$ = this.service.addEntityField(role, {
                entity: this.entity.trim(), fieldName: this.fieldName.trim(), fieldStatus: Number(this.fieldStatus)
            });
        } else if (this.data.mode === 'set') {
            const minimum = this.minimumEntries === null || this.minimumEntries === undefined ? 0 : Number(this.minimumEntries);
            if (!this.entitySet.trim() || !this.keyFieldValue.trim() || !this.fieldName.trim()) {
                this.errorMessage = 'Fill in the required fields.';
                return;
            }
            if (!Number.isInteger(minimum) || minimum < 0 || minimum > 999) {
                this.errorMessage = 'Minimum entries must be a whole number between 0 and 999.';
                return;
            }
            const field = {
                entitySet: this.entitySet.trim(), keyFieldValue: this.keyFieldValue.trim(), fieldName: this.fieldName.trim(),
                keyField: this.keyField, minimumEntries: minimum, fieldStatus: Number(this.fieldStatus)
            };
            request$ = fixedSet ? this.service.addFieldToEntitySet(role, fixedSet, field)
                : this.service.addEntitySetField(role, field);
        } else {
            if (!this.sourceRoleCode) {
                this.errorMessage = 'Choose the role to copy from.';
                return;
            }
            request$ = fixedSet ? this.service.copyEntitySet(role, fixedSet, this.sourceRoleCode)
                : this.service.copyFromRole(role, this.sourceRoleCode, true);
        }
        this.saving = true;
        request$.pipe(takeUntil(this.destroy$)).subscribe({
            next: result => {
                this.saving = false;
                this.dialogRef.close(result);
            },
            error: error => {
                this.saving = false;
                this.errorMessage = this.service.errorMessage(error, 'The change could not be saved.');
            }
        });
    }
}
