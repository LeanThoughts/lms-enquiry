import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { OutstandingApprovalsReportComponent } from './outstanding-approvals-report/outstanding-approvals-report.component';

/** Menu "Administration" > Outstanding Approvals. */
export default [
    {
        path: 'outstanding-approvals',
        component: OutstandingApprovalsReportComponent,
        canActivate: [routeInterceptor]
    }
] as Routes;
