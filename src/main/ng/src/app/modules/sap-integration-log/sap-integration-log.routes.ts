import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { SapIntegrationLogReportComponent } from './sap-integration-log-report/sap-integration-log-report.component';

/** Menu "Administration" > SAP Integration Log. */
export default [
    {
        path: 'sap-integration-log',
        component: SapIntegrationLogReportComponent,
        canActivate: [routeInterceptor]
    }
] as Routes;
