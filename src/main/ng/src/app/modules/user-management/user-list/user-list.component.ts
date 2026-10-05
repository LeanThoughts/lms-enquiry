import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
    AvatarComponent,
    BusyIndicatorComponent,
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    FormModule,
    IconComponent,
    InfoLabelComponent,
    LayoutGridModule,
    ObjectStatusComponent,
    PanelModule,
    SegmentedButtonComponent,
    SelectModule,
    TableModule,
    ToolbarComponent,
    ToolbarItemDirective,
    ToolbarSeparatorComponent,
    ToolbarSpacerDirective
} from '@fundamental-ngx/core';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { UserManagementService } from '../user-management.service';
import { UserFormDialogComponent } from '../user-form-dialog/user-form-dialog.component';
import {
    Department,
    formatDate,
    fullName,
    User,
    UserFormDialogData,
    UserManagementResolvedData,
    UserRole,
    UserStatusFilter
} from '../user-management.model';

/**
 * Screen 1 – User list with filter bar, users table and quick view of the selected user.
 */
@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrl: './user-list.component.scss',
    imports: [
        FormsModule,
        ReactiveFormsModule,
        DynamicPageModule,
        ToolbarComponent,
        ToolbarItemDirective,
        ToolbarSeparatorComponent,
        ButtonComponent,
        FormModule,
        SelectModule,
        SegmentedButtonComponent,
        LayoutGridModule,
        TableModule,
        AvatarComponent,
        ObjectStatusComponent,
        InfoLabelComponent,
        IconComponent,
        PanelModule,
        BusyIndicatorComponent
    ]
})
export class UserListComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly fullName = fullName;
    readonly formatDate = formatDate;

    roles: UserRole[] = [];
    departments: Department[] = [];
    users: User[] = [];
    selectedUser: User | null = null;
    loading = false;
    /** True once the first user list has arrived; until then the full-page loading message is shown. */
    initialLoadDone = false;

    readonly loadingMessage = 'Loading list of users. Please wait';

    searchForm = new FormGroup({
        query: new FormControl('', { nonNullable: true }),
        role: new FormControl('', { nonNullable: true }),
        riskDepartment: new FormControl('', { nonNullable: true }),
        status: new FormControl<UserStatusFilter>('all', { nonNullable: true })
    });

    constructor(
        private userManagementService: UserManagementService,
        private messageService: MessageService,
        private dialogService: DialogService,
        public router: Router
    ) {}

    ngOnInit(): void {
        // Master data loads in parallel with the users, so the page (and its loading message) shows at once.
        forkJoin({
            roles: this.userManagementService.getRoles(),
            departments: this.userManagementService.getDepartments()
        }).pipe(takeUntil(this.destroy$)).subscribe({
            next: (masterData: UserManagementResolvedData) => {
                this.roles = (masterData.roles ?? []).filter(role => !!role.code?.trim());
                this.departments = masterData.departments ?? [];
            },
            error: error => this.messageService.showError(
                this.userManagementService.errorMessage(error, 'Roles and departments could not be loaded.'))
        });

        const criteria = this.userManagementService.searchCriteria$.value;
        this.searchForm.patchValue({
            query: criteria.query ?? '',
            role: criteria.role ?? '',
            riskDepartment: criteria.riskDepartment ?? '',
            status: criteria.status
        });
        this.search();

        // The status switch filters immediately
        this.searchForm.controls.status.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.search());
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get activeCount(): number {
        return this.users.filter(user => user.status).length;
    }

    get inactiveCount(): number {
        return this.users.length - this.activeCount;
    }

    search(): void {
        const value = this.searchForm.getRawValue();
        const criteria = {
            query: value.query,
            role: value.role || null,
            riskDepartment: value.riskDepartment || null,
            status: value.status
        };
        this.userManagementService.searchCriteria$.next(criteria);

        this.loading = true;
        this.userManagementService.searchUsers(criteria).pipe(takeUntil(this.destroy$)).subscribe({
            next: users => {
                this.loading = false;
                this.initialLoadDone = true;
                this.users = users ?? [];
                // Keep the selection when the user is still in the result
                this.selectedUser = this.users.find(user => user.id === this.selectedUser?.id) ?? null;
            },
            error: error => {
                this.loading = false;
                this.initialLoadDone = true;
                this.users = [];
                this.selectedUser = null;
                this.messageService.showError(this.userManagementService.errorMessage(error, 'Users could not be loaded.'));
            }
        });
    }

    clear(): void {
        this.searchForm.reset({ query: '', role: '', riskDepartment: '', status: 'all' }, { emitEvent: false });
        this.search();
    }

    select(user: User): void {
        this.selectedUser = user;
    }

    openProfile(user: User | null): void {
        if (user) {
            this.router.navigate(['/user-management/users', user.id]);
        }
    }

    createUser(): void {
        this.openForm({ operation: 'create', roles: this.roles, departments: this.departments });
    }

    editUser(user: User | null): void {
        if (user) {
            this.openForm({ operation: 'edit', user, roles: this.roles, departments: this.departments });
        }
    }

    toggleStatus(user: User | null): void {
        if (!user) {
            return;
        }
        const activate = !user.status;
        this.userManagementService.setUserStatus(user.id, activate).pipe(takeUntil(this.destroy$)).subscribe({
            next: updated => {
                this.messageService.showSuccess(`${fullName(updated)} ${activate ? 'activated' : 'deactivated'}.`);
                this.selectedUser = updated;
                this.search();
            },
            error: error => this.messageService.showError(
                this.userManagementService.errorMessage(error, 'The user status could not be changed.'))
        });
    }

    manageRoles(): void {
        this.router.navigate(['/user-management/roles']);
    }

    roleLabel(user: User): string {
        return user.roleDescription || user.role || '–';
    }

    private openForm(data: UserFormDialogData): void {
        const dialogRef = this.dialogService.open(UserFormDialogComponent, { data, width: '50rem' });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: unknown) => {
                if (result && typeof result === 'object') {
                    this.selectedUser = result as User;
                    this.search();
                }
            },
            error: () => { /* dialog dismissed */ }
        });
    }
}
