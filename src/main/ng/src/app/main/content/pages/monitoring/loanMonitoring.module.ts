import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
  MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
  MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule,
  MatTabsModule,
  MAT_DATE_LOCALE,
  MatAutocompleteModule,
  MatCardModule,
  MatRadioModule,
  MatCheckboxModule,
  MatDialogModule
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
import { OperatingParameterUpdateDialogComponent } from './operatingParameter/operatingParameterUpdate/operatingParameterUpdate.component';
import { OperatingParameterListComponent } from './operatingParameter/operatingParameterList/operatingParameterList.component';
import { OperatingParameterPLFUpdateDialogComponent } from './operatingParameterPLF/operatingParameterPLFUpdate/operatingParameterPLFUpdate.component';
import { OperatingParameterPLFListComponent } from './operatingParameterPLF/operatingParameterPLFList/operatingParameterPLFList.component';
import { ProjectMonitoringDataItemListComponent } from './projectMonitoringData/projectMonitoringDataItemList/projectMonitoringDataItemList.component';
import { ProjectMonitoringDataItemUpdateComponent } from './projectMonitoringData/projectMonitoringDataItemUpdate/projectMonitoringDataItemUpdate.component';
import { LIAListComponent } from './liaList/liaList.component';
import { LIAUpdateDialogComponent } from './liaUpdate/liaUpdate.component';
import { LIAReportAndFeeListComponent } from './liaReportAndFeeList/liaReportAndFeeList.component';
import { LIAReportAndFeeUpdateDialogComponent } from './liaReportAndFeeUpdate/liaReportAndFeeUpdate.component';
import { LLCListComponent } from './llcList/llcList.component';
import { LLCUpdateDialogComponent } from './llcUpdate/llcUpdate.component';
import { LLCReportAndFeeListComponent } from './llcReportAndFeeList/llcReportAndFeeList.component';
import { LLCReportAndFeeUpdateDialogComponent } from './llcReportAndFeeUpdate/llcReportAndFeeUpdate.component';
import { CLAListComponent } from './claList/claList.component';
import { CLAUpdateDialogComponent } from './claUpdate/claUpdate.component';
import { CLAReportAndFeeListComponent } from './claReportAndFeeList/claReportAndFeeList.component';
import { CLAReportAndFeeUpdateDialogComponent } from './claReportAndFeeUpdate/claReportAndFeeUpdate.component';
import { NPAUpdateComponent } from './npaDetails/npaUpdate/npaUpdate.component';
import { NPADetailUpdateDialogComponent } from './npaDetails/npaDetailsUpdate/npaDetailsUpdate.component';
import { ValuerListComponent } from './valuerList/valuerList.component';
import { ValuerUpdateDialogComponent } from './valuerUpdate/valuerUpdate.component';
import { ValuerReportAndFeeUpdateDialogComponent } from './valuerReportAndFeeUpdate/valuerReportAndFeeUpdate.component';
import { ValuerReportAndFeeListComponent } from './valuerReportAndFeeList/valuerReportAndFeeList.component';
import { ProjectMonitoringDataItemHistoryComponent } from './projectMonitoringData/projectMonitoringDataItemHistory/projectMonitoringDataItemHistory.component';
import { LoanDocumentationListComponent } from './loanDocumentationList/loanDocumentationList.component';
import { LoanDocumentationUpdateDialogComponent } from './loanDocumentationUpdate/loanDocumentationUpdate.component';

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

@NgModule({
    imports: [
      RouterModule.forChild(routes),
      CommonModule,
      FuseSharedModule,
      MatExpansionModule,
      MatInputModule,
      MatButtonModule,
      MatDialogModule,
      MatFormFieldModule,
      MatPaginatorModule,
      MatTableModule,
      MatToolbarModule,
      MatIconModule,
      MatSelectModule,
      MatSortModule,
      MatDatepickerModule,
      MatProgressSpinnerModule,
      MatTabsModule,
      MatAutocompleteModule,
      MatRadioModule,
      MatCheckboxModule,
      MatCardModule,
    ],
    declarations: [
        LoanMonitoringComponent,
        CLAListComponent,
        CLAUpdateDialogComponent,
        CLAReportAndFeeListComponent,
        CLAReportAndFeeUpdateDialogComponent,
        LLCListComponent,
        LLCUpdateDialogComponent,
        LLCReportAndFeeListComponent,
        LLCReportAndFeeUpdateDialogComponent,
        LIAListComponent,
        LIAUpdateDialogComponent,
        LIAReportAndFeeListComponent,
        LIAReportAndFeeUpdateDialogComponent,
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
        ProjectMonitoringDataItemUpdateComponent,
        NPAUpdateComponent,
        NPADetailUpdateDialogComponent,
        ValuerListComponent,
        ValuerUpdateDialogComponent,
        ValuerReportAndFeeListComponent,
        ValuerReportAndFeeUpdateDialogComponent,
        ProjectMonitoringDataItemHistoryComponent,
        LoanDocumentationListComponent,
        LoanDocumentationUpdateDialogComponent
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
        CLAListComponent,
        CLAUpdateDialogComponent,
        CLAReportAndFeeListComponent,
        CLAReportAndFeeUpdateDialogComponent,
        LLCListComponent,
        LLCUpdateDialogComponent,
        LLCReportAndFeeListComponent,
        LLCReportAndFeeUpdateDialogComponent,
        LIAListComponent,
        LIAUpdateDialogComponent,
        LIAReportAndFeeListComponent,
        LIAReportAndFeeUpdateDialogComponent,
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
        ProjectMonitoringDataItemUpdateComponent,
        ValuerListComponent,
        ValuerUpdateDialogComponent,
        ValuerReportAndFeeListComponent,
        ValuerReportAndFeeUpdateDialogComponent,
        ProjectMonitoringDataItemHistoryComponent,
        LoanDocumentationListComponent,
        LoanDocumentationUpdateDialogComponent
    ],
    entryComponents: [
        CLAUpdateDialogComponent,
        CLAReportAndFeeUpdateDialogComponent,
        LLCUpdateDialogComponent,
        LLCReportAndFeeUpdateDialogComponent,
        LIAReportAndFeeUpdateDialogComponent,
        LIAUpdateDialogComponent,
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
        ProjectMonitoringDataItemUpdateComponent,
        NPAUpdateComponent,
        NPADetailUpdateDialogComponent,
        ValuerUpdateDialogComponent,
        ValuerReportAndFeeUpdateDialogComponent,
        ProjectMonitoringDataItemHistoryComponent,
        LoanDocumentationListComponent,
        LoanDocumentationUpdateDialogComponent
    ]
})
export class LoanMonitoringModule {
}
