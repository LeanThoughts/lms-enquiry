import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { ReportsHomeComponent } from './reports-home/reports-home.component';
import { BalanceListReportComponent } from './balance-list-report/balance-list-report.component';
import { DisbursementReportComponent } from './disbursement-report/disbursement-report.component';
import { IccApprovalReportComponent } from './icc-approval-report/icc-approval-report.component';
import { KycReportComponent } from './kyc-report/kyc-report.component';
import { WorkflowStatusReportComponent } from './workflow-status-report/workflow-status-report.component';

/**
 * Menu "Reports": the catalogue of reports (four modules) and the reports themselves. To add a report: a route
 * below 'reports' here and an entry in reports.model.ts.
 */
export default [
    { path: 'reports', component: ReportsHomeComponent, canActivate: [routeInterceptor] },
    // Loans Management
    { path: 'reports/icc-approval', component: IccApprovalReportComponent, canActivate: [routeInterceptor] },
    { path: 'reports/kyc', component: KycReportComponent, canActivate: [routeInterceptor] },
    { path: 'reports/balance-list', component: BalanceListReportComponent, canActivate: [routeInterceptor] },
    { path: 'reports/disbursement', component: DisbursementReportComponent, canActivate: [routeInterceptor] },
    // Miscellaneous
    { path: 'reports/workflow-status', component: WorkflowStatusReportComponent, canActivate: [routeInterceptor] }
] as Routes;
