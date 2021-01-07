import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
  MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
  MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule,
  MatTabsModule,
  MAT_DATE_LOCALE
} from '@angular/material';
import { LoanMonitoringComponent } from './loanMonitoring.component';
import { EnquiryApplicationRouteGuard } from 'app/enquiryApplication.guard';
import { LIEUpdateDialogComponent } from './lieUpdate/lieUpdate.component';
import { LIEListComponent } from './lieList/lieList.component';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from './loanMonitoring.service';
import { LIEReportAndFeeUpdateDialogComponent } from './lieReportAndFeeUpdate/lieReportAndFeeUpdate.component';
import { LIEReportAndFeeListComponent } from './lieReportAndFeeList/lieReportAndFeeList.component';
import { LFAListComponent } from './lfaList/lfaList.component';
import { LFAUpdateDialogComponent } from './lfaUpdate/lfaUpdate.component';
import { LFAReportAndFeeListComponent } from './lfaReportAndFeeList/lfaReportAndFeeList.component';
import { LFAReportAndFeeUpdateDialogComponent } from './lfaReportAndFeeUpdate/lfaReportAndFeeUpdate.component';
import { TRAUpdateDialogComponent } from './trustRetentionAccount/traUpdate/traUpdate.component';
import { TRAListComponent } from './trustRetentionAccount/traList/traList.component';
import { TRAStatementUpdateDialogComponent } from './trustRetentionAccount/traStatementUpdate/traStatementUpdate.component';
import { TRAStatementListComponent } from './trustRetentionAccount/traStatementList/traStatementList.component';
import { TandCUpdateDialogComponent } from './termsAndConditions/tandcUpdate/tandcUpdate.component';
import { TandCListComponent } from './termsAndConditions/tandcList/tandcList.component';
import { SecurityComplianceUpdateDialogComponent } from './securityCompliance/securityComplianceUpdate/securityComplianceUpdate.component';
import { SecurityComplianceListComponent } from './securityCompliance/securityComplianceList/securityComplianceList.component';
import { SiteVisitUpdateDialogComponent } from './siteVisit/siteVisitUpdate/siteVisitUpdate.component';
import { SiteVisitListComponent } from './siteVisit/siteVisitList/siteVisitList.component';

const routes = [
    {
        path: 'loanMonitoring', component: LoanMonitoringComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ]
    }
];

@NgModule({
    imports: [
      RouterModule.forChild(routes),
      CommonModule,
      FuseSharedModule,
      MatExpansionModule,
      MatInputModule,
      MatButtonModule,
      MatFormFieldModule,
      MatPaginatorModule,
      MatTableModule,
      MatToolbarModule,
      MatIconModule,
      MatSelectModule,
      MatSortModule,
      MatDatepickerModule,
      MatProgressSpinnerModule,
      MatTabsModule
    ],
    declarations: [
        LoanMonitoringComponent,
        LIEListComponent,
        LIEUpdateDialogComponent,
        LIEReportAndFeeListComponent,
        LIEReportAndFeeUpdateDialogComponent,
        LFAListComponent,
        LFAUpdateDialogComponent,
        LFAReportAndFeeListComponent,
        LFAReportAndFeeUpdateDialogComponent,
        TRAListComponent,
        TRAUpdateDialogComponent,
        TRAStatementUpdateDialogComponent,
        TRAStatementListComponent,
        TandCListComponent,
        TandCUpdateDialogComponent,
        SecurityComplianceListComponent,
        SecurityComplianceUpdateDialogComponent,
        SiteVisitListComponent,
        SiteVisitUpdateDialogComponent
    ],
    providers: [
        LoanEnquiryService,
        LoanMonitoringService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        }
    ],
    exports: [
        LoanMonitoringComponent,
        LIEListComponent,
        LIEUpdateDialogComponent,
        LIEReportAndFeeListComponent,
        LIEReportAndFeeUpdateDialogComponent,
        LFAListComponent,
        LFAUpdateDialogComponent,
        LFAReportAndFeeListComponent,
        LFAReportAndFeeUpdateDialogComponent,
        TRAListComponent,
        TRAUpdateDialogComponent,
        TRAStatementUpdateDialogComponent,
        TRAStatementListComponent,
        TandCListComponent,
        TandCUpdateDialogComponent,
        SecurityComplianceListComponent,
        SecurityComplianceUpdateDialogComponent,
        SiteVisitListComponent,
        SiteVisitUpdateDialogComponent
    ],  
    entryComponents: [
        LIEListComponent,
        LIEUpdateDialogComponent,
        LIEReportAndFeeListComponent,
        LIEReportAndFeeUpdateDialogComponent,
        LFAListComponent,
        LFAUpdateDialogComponent,
        LFAReportAndFeeListComponent,
        LFAReportAndFeeUpdateDialogComponent,
        TRAListComponent,
        TRAUpdateDialogComponent,
        TRAStatementUpdateDialogComponent,
        TRAStatementListComponent,
        TandCListComponent,
        TandCUpdateDialogComponent,
        SecurityComplianceListComponent,
        SecurityComplianceUpdateDialogComponent,
        SiteVisitListComponent,
        SiteVisitUpdateDialogComponent
    ]
})
export class LoanMonitoringModule {
}