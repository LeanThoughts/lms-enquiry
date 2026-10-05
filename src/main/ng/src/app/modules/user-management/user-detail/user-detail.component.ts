import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
    AvatarComponent,
    BusyIndicatorComponent,
    ButtonComponent,
    DialogService,
    DynamicPageModule,
    IconComponent,
    LayoutGridModule,
    ObjectStatusComponent,
    PanelModule,
    ToolbarComponent,
    ToolbarItemDirective,
    ToolbarSeparatorComponent
} from '@fundamental-ngx/core';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { Subject } from 'rxjs';
import { switchMap, takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { UserManagementService } from '../user-management.service';
import { UserFormDialogComponent } from '../user-form-dialog/user-form-dialog.component';
import { Department, formatDate, fullName, User, UserManagementResolvedData, UserRole } from '../user-management.model';

/**
 * Screen 2 – User profile (object header + tabs).
 */
@Component({
    selector: 'app-user-detail',
    templateUrl: './user-detail.component.html',
    styleUrl: './user-detail.component.scss',
    imports: [
        DynamicPageModule,
        ToolbarComponent,
        ToolbarItemDirective,
        ToolbarSeparatorComponent,
        ButtonComponent,
        AvatarComponent,
        ObjectStatusComponent,
        IconComponent,
        LayoutGridModule,
        PanelModule,
        BusyIndicatorComponent,
        IconTabBarComponent,
        IconTabBarTabComponent
    ]
})
export class UserDetailComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly fullName = fullName;
    readonly formatDate = formatDate;

    user: User | null = null;
    roles: UserRole[] = [];
    departments: Department[] = [];
    loading = true;

    constructor(
        private activatedRoute: ActivatedRoute,
        private userManagementService: UserManagementService,
        private messageService: MessageService,
        private dialogService: DialogService,
        public router: Router
    ) {}

    ngOnInit(): void {
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe(data => {
            const resolved = data['routeResolvedData'] as UserManagementResolvedData;
            this.roles = resolved?.roles ?? [];
            this.departments = resolved?.departments ?? [];
        });

        this.activatedRoute.paramMap.pipe(
            switchMap(params => {
                this.loading = true;
                return this.userManagementService.getUser(params.get('id') ?? '');
            }),
            takeUntil(this.destroy$)
        ).subscribe({
            next: user => {
                this.user = user;
                this.loading = false;
            },
            error: error => {
                this.loading = false;
                this.messageService.showError(this.userManagementService.errorMessage(error, 'The user could not be loaded.'));
                this.backToList();
            }
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    backToList(): void {
        this.router.navigate(['/user-management']);
    }

    edit(): void {
        if (!this.user) {
            return;
        }
        const dialogRef = this.dialogService.open(UserFormDialogComponent, {
            data: { operation: 'edit', user: this.user, roles: this.roles, departments: this.departments },
            width: '50rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: unknown) => {
                if (result && typeof result === 'object') {
                    this.user = result as User;
                }
            },
            error: () => { /* dialog dismissed */ }
        });
    }

    toggleStatus(): void {
        if (!this.user) {
            return;
        }
        const activate = !this.user.status;
        this.userManagementService.setUserStatus(this.user.id, activate).pipe(takeUntil(this.destroy$)).subscribe({
            next: updated => {
                this.user = updated;
                this.messageService.showSuccess(`${fullName(updated)} ${activate ? 'activated' : 'deactivated'}.`);
            },
            error: error => this.messageService.showError(
                this.userManagementService.errorMessage(error, 'The user status could not be changed.'))
        });
    }

    yesNo(value: boolean | null | undefined): string {
        return value ? 'Yes' : 'No';
    }

    stamp(date?: string | null, time?: string | null, by?: string | null): string {
        if (!date) {
            return '–';
        }
        const when = time ? `${date} ${time.substring(0, 5)}` : date;
        return by ? `${when} · ${by}` : when;
    }
}
