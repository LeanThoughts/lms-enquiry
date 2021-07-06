import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
  MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
  MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule,
  MatTabsModule,
  MAT_DATE_LOCALE,
  DateAdapter
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
import { RateOfInterestListComponent } from './rateOfInterest/rateOfInterestList/rateOfInterestList.component';
import { RateOfInterestUpdateDialogComponent } from './rateOfInterest/rateOfInterestUpdate/rateOfInterestUpdate.component';
import { BorrowerFinancialsUpdateDialogComponent } from './borrowerFinancials/borrowerFinancialsUpdate/borrowerFinancialsUpdate.component';
import { BorrowerFinancialsListComponent } from './borrowerFinancials/borrowerFinancialsList/borrowerFinancialsList.component';
import { PromoterFinancialsListComponent } from './promoterFinancials/promoterFinancialsList/promoterFinancialsList.component';
import { PromoterFinancialsUpdateDialogComponent } from './promoterFinancials/promoterFinancialsUpdate/promoterFinancialsUpdate.component';
import { FinancialCovenantsListComponent } from './financialCovenants/financialCovenantsList/financialCovenantsList.component';
import { FinancialCovenantsUpdateDialogComponent } from './financialCovenants/financialCovenantsUpdate/financialCovenantsUpdate.component';
import { PromoterDetailsUpdateDialogComponent } from './promoterDetails/promoterDetailsUpdate/promoterDetailsUpdate.component';
import { PromoterDetailsItemListComponent } from './promoterDetails/promoterDetailsList/promoterDetailsList.component';
import { MAT_DATE_FORMATS } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { OperatingParameterUpdateDialogComponent } from './operatingParameter/operatingParameterUpdate/operatingParameterUpdate.component';
import { OperatingParameterListComponent } from './operatingParameter/operatingParameterList/operatingParameterList.component';
import { OperatingParameterPLFUpdateDialogComponent } from './operatingParameterPLF/operatingParameterPLFUpdate/operatingParameterPLFUpdate.component';
import { OperatingParameterPLFListComponent } from './operatingParameterPLF/operatingParameterPLFList/operatingParameterPLFList.component';
import { ProjectMonitoringDataItemListComponent } from './projectMonitoringData/projectMonitoringDataItemList/projectMonitoringDataItemList.component';
import { ProjectMonitoringDataItemUpdateComponent } from './projectMonitoringData/projectMonitoringDataItemUpdate/projectMonitoringDataItemUpdate.component';

const routes = [
    {
        path: 'loanMonitoring', 
        component: LoanMonitoringComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ],
        resolve: {
            routeResolvedData: LoanMonitoringService
        },
    }
];

const MY_FORMATS = {
    parse: {
        dateInput: ['DD/MM/YYYY'],
    },
    display: {
        dateInput: 'DD/MM/YYYY',
        monthYearLabel: 'MMM YYYY',
        dateA11yLabel: 'LL',
        monthYearA11yLabel: 'MMMM YYYY',
    },
};

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
        SiteVisitUpdateDialogComponent,
        RateOfInterestListComponent,
        RateOfInterestUpdateDialogComponent,
        BorrowerFinancialsListComponent,
        BorrowerFinancialsUpdateDialogComponent,
        PromoterFinancialsListComponent,
        PromoterFinancialsUpdateDialogComponent,
        FinancialCovenantsListComponent,
        FinancialCovenantsUpdateDialogComponent,
        PromoterDetailsItemListComponent,
        PromoterDetailsUpdateDialogComponent,
        OperatingParameterListComponent,
        OperatingParameterUpdateDialogComponent,
        OperatingParameterPLFListComponent,
        OperatingParameterPLFUpdateDialogComponent,
        ProjectMonitoringDataItemListComponent,
        ProjectMonitoringDataItemUpdateComponent
    ],
    providers: [
        LoanEnquiryService,
        LoanMonitoringService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
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
        SiteVisitUpdateDialogComponent,
        RateOfInterestListComponent,
        RateOfInterestUpdateDialogComponent,
        BorrowerFinancialsListComponent,
        BorrowerFinancialsUpdateDialogComponent,
        PromoterFinancialsListComponent,
        PromoterFinancialsUpdateDialogComponent,
        FinancialCovenantsListComponent,
        FinancialCovenantsUpdateDialogComponent,
        PromoterDetailsItemListComponent,
        PromoterDetailsUpdateDialogComponent,
        OperatingParameterListComponent,
        OperatingParameterUpdateDialogComponent,
        OperatingParameterPLFListComponent,
        OperatingParameterPLFUpdateDialogComponent,
        ProjectMonitoringDataItemListComponent,
        ProjectMonitoringDataItemUpdateComponent
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
        SiteVisitUpdateDialogComponent,
        RateOfInterestListComponent,
        RateOfInterestUpdateDialogComponent,
        BorrowerFinancialsListComponent,
        BorrowerFinancialsUpdateDialogComponent,
        PromoterFinancialsListComponent,
        PromoterFinancialsUpdateDialogComponent,
        FinancialCovenantsListComponent,
        FinancialCovenantsUpdateDialogComponent,
        PromoterDetailsItemListComponent,
        PromoterDetailsUpdateDialogComponent,
        OperatingParameterListComponent,
        OperatingParameterUpdateDialogComponent,
        OperatingParameterPLFListComponent,
        OperatingParameterPLFUpdateDialogComponent,
        ProjectMonitoringDataItemListComponent,
        ProjectMonitoringDataItemUpdateComponent
    ]
})
export class LoanMonitoringModule {
}
