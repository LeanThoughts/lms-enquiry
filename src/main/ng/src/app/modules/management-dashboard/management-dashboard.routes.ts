import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';

/** Management Dashboard: loaded lazily, it is not part of the initial bundle. */
export default [
    {
        path: 'management-dashboard',
        loadComponent: () => import('./management-dashboard.component').then(m => m.ManagementDashboardComponent),
        canActivate: [routeInterceptor],
    },
] as Routes;
