import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { modulePermissionGuard } from '../auth/module-permission.guard';
import { SapIntegrationLogReportComponent } from './sap-integration-log-report/sap-integration-log-report.component';

/** Menu "Administration" > SAP Integration Log. */
export default [
    {
        path: 'sap-integration-log',
        component: SapIntegrationLogReportComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'SapIntegrationLog.Display' }
    }
] as Routes;
