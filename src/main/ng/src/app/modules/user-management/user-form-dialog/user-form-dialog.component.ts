import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import {
    BusyIndicatorComponent,
    DialogCloseButtonComponent,
    DialogModule,
    DialogRef,
    FormModule,
    IconComponent,
    LayoutGridModule,
    SelectModule,
    SwitchComponent,
    TitleComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { UserManagementService } from '../user-management.service';
import { Department, formatDate, fullName, User, UserFormDialogData, UserRequest, UserRole } from '../user-management.model';

/** End date of a user without time limit (matches the backend). */
const OPEN_END_DATE = '9999-12-31';

/** Today as ISO yyyy-MM-dd in the browser's time zone. */
function todayIso(): string {
    const now = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}`;
}

/** Separator between role code and description in the role select value (codes are not unique). */
const ROLE_KEY_SEPARATOR = '||';

/**
 * Create / edit user dialog. Closes with the saved User, or 'Cancelled'.
 */
@Component({
    selector: 'app-user-form-dialog',
    templateUrl: './user-form-dialog.component.html',
    styles: [`
        .um-form__section { display: flex; align-items: center; gap: 0.5rem; margin: 1.25rem 0 0.5rem; }
        .um-form__section:first-of-type { margin-top: 0; }
        fd-select { display: block; }
        .um-form__value { display: block; padding-top: 0.375rem; }
        .um-form__hint { margin: 0.5rem 0 0; font-size: 0.8125rem; color: var(--sapContent_LabelColor, #556b82); }
    `],
    imports: [
        ReactiveFormsModule,
        DialogModule,
        DialogCloseButtonComponent,
        TitleComponent,
        FormModule,
        LayoutGridModule,
        SelectModule,
        SwitchComponent,
        IconComponent,
        BusyIndicatorComponent
    ]
})
export class UserFormDialogComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    data: UserFormDialogData;
    title = '';
    roles: UserRole[] = [];
    departments: Department[] = [];
    saving = false;
    serverError: string | null = null;

    form = new FormGroup({
        firstName: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(100)] }),
        lastName: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(100)] }),
        email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
        userName: new FormControl('', { nonNullable: true, validators: [Validators.maxLength(100)] }),
        roleKey: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
        sapBPNumber: new FormControl('', { nonNullable: true }),
        riskDepartment: new FormControl('', { nonNullable: true }),
        departmentHead: new FormControl(false, { nonNullable: true }),
        riskPortalDisplayOnlyAccess: new FormControl(false, { nonNullable: true }),
        status: new FormControl(true, { nonNullable: true })
    });

    constructor(
        public dialogRef: DialogRef,
        private userManagementService: UserManagementService,
        private messageService: MessageService
    ) {
        this.data = this.dialogRef.data as UserFormDialogData;
        // Roles without a code cannot be assigned
        this.roles = (this.data.roles ?? []).filter(role => !!role.code?.trim());
        this.departments = this.data.departments ?? [];
    }

    ngOnInit(): void {
        const user = this.data.user;
        this.title = this.data.operation === 'edit' && user ? `Edit User – ${fullName(user)}` : 'Create User';

        if (user) {
            this.form.patchValue({
                firstName: user.firstName ?? '',
                lastName: user.lastName ?? '',
                email: user.email ?? '',
                userName: user.userName ?? '',
                roleKey: this.findRoleKey(user),
                sapBPNumber: user.sapBPNumber ?? '',
                riskDepartment: user.riskDepartment ?? '',
                departmentHead: !!user.departmentHead,
                riskPortalDisplayOnlyAccess: !!user.riskPortalDisplayOnlyAccess,
                status: !!user.status
            });
        }
        this.updateRiskFlags(this.form.controls.riskDepartment.value);

        this.form.controls.riskDepartment.valueChanges.pipe(takeUntil(this.destroy$))
            .subscribe(value => this.updateRiskFlags(value));
        this.form.controls.email.valueChanges.pipe(takeUntil(this.destroy$))
            .subscribe(() => this.clearServerError('duplicate'));
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    readonly formatDate = formatDate;
    readonly today = formatDate(todayIso());

    get isEdit(): boolean {
        return this.data.operation === 'edit';
    }

    /** End date the user will have after saving, given the Active switch. */
    get endDatePreview(): string {
        const user = this.data.user;
        const active = this.form.controls.status.value;
        if (!user || active === user.status) {
            return formatDate(user?.endDate);
        }
        return formatDate(active ? OPEN_END_DATE : todayIso());
    }

    roleKey(role: UserRole): string {
        return `${role.code}${ROLE_KEY_SEPARATOR}${role.value ?? ''}`;
    }

    /** True when the control is invalid and the user has interacted with it. */
    showError(name: keyof typeof this.form.controls): boolean {
        const control = this.form.controls[name];
        return control.invalid && (control.touched || control.dirty);
    }

    submit(): void {
        this.serverError = null;
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }
        const request = this.toRequest();
        const save$ = this.isEdit && this.data.user
            ? this.userManagementService.updateUser(this.data.user.id, request)
            : this.userManagementService.createUser(request);

        this.saving = true;
        save$.pipe(takeUntil(this.destroy$)).subscribe({
            next: (saved: User) => {
                this.saving = false;
                this.messageService.showSuccess(this.isEdit ? 'User updated.' : 'User created.');
                this.dialogRef.close(saved);
            },
            error: (error: unknown) => {
                this.saving = false;
                if (error instanceof HttpErrorResponse && error.status === 409) {
                    this.form.controls.email.setErrors({ ...(this.form.controls.email.errors ?? {}), duplicate: true });
                    this.form.controls.email.markAsTouched();
                    return;
                }
                this.serverError = this.userManagementService.errorMessage(error, 'The user could not be saved.');
            }
        });
    }

    cancel(): void {
        this.dialogRef.close('Cancelled');
    }

    private toRequest(): UserRequest {
        const value = this.form.getRawValue();
        const [role, roleDescription] = value.roleKey.split(ROLE_KEY_SEPARATOR);
        const hasDepartment = !!value.riskDepartment;
        return {
            firstName: value.firstName.trim(),
            lastName: value.lastName.trim(),
            email: value.email.trim(),
            userName: value.userName.trim() || null,
            role,
            roleDescription: roleDescription || null,
            sapBPNumber: value.sapBPNumber.trim() || null,
            riskDepartment: value.riskDepartment || null,
            departmentHead: hasDepartment && value.departmentHead,
            riskPortalDisplayOnlyAccess: hasDepartment && value.riskPortalDisplayOnlyAccess,
            // New users are always created active; the backend sets the validity dates.
            status: this.isEdit ? value.status : true
        };
    }

    /** Department head / display-only access only apply to risk department users. */
    private updateRiskFlags(riskDepartment: string): void {
        const flags = [this.form.controls.departmentHead, this.form.controls.riskPortalDisplayOnlyAccess];
        if (riskDepartment) {
            flags.forEach(control => control.enable({ emitEvent: false }));
        } else {
            flags.forEach(control => {
                control.setValue(false, { emitEvent: false });
                control.disable({ emitEvent: false });
            });
        }
    }

    private findRoleKey(user: User): string {
        const exact = this.roles.find(role => role.code === user.role && role.value === user.roleDescription);
        const byCode = exact ?? this.roles.find(role => role.code === user.role);
        return byCode ? this.roleKey(byCode) : '';
    }

    private clearServerError(key: string): void {
        const errors = this.form.controls.email.errors;
        if (errors?.[key]) {
            const { [key]: _removed, ...rest } = errors;
            this.form.controls.email.setErrors(Object.keys(rest).length ? rest : null);
        }
    }
}
