import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { userManagementAccessGuard } from './user-management.guard';
import { UserManagementService } from './user-management.service';
import { UserListComponent } from './user-list/user-list.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { UserRolesComponent } from './user-roles/user-roles.component';

export default [
    {
        path: 'user-management',
        component: UserListComponent,
        // No resolver: the list loads its master data itself so the loading message shows immediately
        canActivate: [routeInterceptor, userManagementAccessGuard]
    },
    {
        path: 'user-management/roles',
        component: UserRolesComponent,
        canActivate: [routeInterceptor, userManagementAccessGuard]
    },
    {
        path: 'user-management/users/:id',
        component: UserDetailComponent,
        canActivate: [routeInterceptor, userManagementAccessGuard],
        resolve: {
            routeResolvedData: UserManagementService
        }
    }
] as Routes;
