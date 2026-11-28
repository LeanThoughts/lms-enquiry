import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { ManagementDashboardComponent } from './management-dashboard.component';

export default [
    {
        path: 'management-dashboard',
        component: ManagementDashboardComponent,
        canActivate: [routeInterceptor],
    },
] as Routes;
