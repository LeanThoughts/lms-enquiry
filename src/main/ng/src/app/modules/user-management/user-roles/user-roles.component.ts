import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
    BusyIndicatorComponent,
    ButtonComponent,
    DynamicPageModule,
    FormModule,
    MessageStripComponent,
    ObjectStatusComponent,
    TableModule,
    ToolbarComponent,
    ToolbarSeparatorComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { UserManagementService } from '../user-management.service';
import { UserRole } from '../user-management.model';

/**
 * Screen 4 – Role master overview (read-only) with user counts and data-quality checks.
 */
@Component({
    selector: 'app-user-roles',
    templateUrl: './user-roles.component.html',
    styleUrl: './user-roles.component.scss',
    imports: [
        FormsModule,
        DynamicPageModule,
        ToolbarComponent,
        ToolbarSeparatorComponent,
        ButtonComponent,
        FormModule,
        TableModule,
        ObjectStatusComponent,
        MessageStripComponent,
        BusyIndicatorComponent
    ]
})
export class UserRolesComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    roles: UserRole[] = [];
    filter = '';
    loading = true;

    constructor(
        private userManagementService: UserManagementService,
        private messageService: MessageService,
        public router: Router
    ) {}

    ngOnInit(): void {
        this.userManagementService.getRoles().pipe(takeUntil(this.destroy$)).subscribe({
            next: roles => {
                this.roles = roles ?? [];
                this.loading = false;
            },
            error: error => {
                this.loading = false;
                this.messageService.showError(this.userManagementService.errorMessage(error, 'Roles could not be loaded.'));
            }
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get filteredRoles(): UserRole[] {
        const text = this.filter.trim().toLowerCase();
        return text
            ? this.roles.filter(role => `${role.code ?? ''} ${role.value ?? ''}`.toLowerCase().includes(text))
            : this.roles;
    }

    get issueCount(): number {
        return this.roles.filter(role => !!role.issue).length;
    }

    /** Opens the user list filtered on this role. */
    showUsers(role: UserRole): void {
        if (!role.code?.trim()) {
            return;
        }
        const current = this.userManagementService.searchCriteria$.value;
        this.userManagementService.searchCriteria$.next({ ...current, query: '', role: role.code, riskDepartment: null });
        this.router.navigate(['/user-management']);
    }

    back(): void {
        this.router.navigate(['/user-management']);
    }
}
