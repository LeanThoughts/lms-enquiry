import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
  MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
  MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule,
  MatTabsModule,
  MAT_DATE_LOCALE,
  DateAdapter,
  MatDialogModule,
  MatCheckboxModule,
  MatAutocompleteModule,
  MatStepperModule
} from '@angular/material';
import { EnquiryApplicationRouteGuard } from 'app/enquiryApplication.guard';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { MAT_DATE_FORMATS } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { LoanAppraisalService } from './loanAppraisal.service';
import { LoanAppraisalComponent } from './loanAppraisal.component';
import { LoanPartnerUpdateComponent } from './loan-partner/loan-partner-update/loan-partner-update.component';
import { FormsModule } from '@angular/forms';
import { LoanPartnerListComponent } from './loan-partner/loan-partner-list/loan-partner-list.component';
import { LoanAppraisalKYCListComponent } from './loan-appraisal-kyc/loan-appraisal-kyc-list/loan-appraisal-kyc-list.component';
import { LoanAppraisalKYCUpdateComponent } from './loan-appraisal-kyc/loan-appraisal-kyc-update/loan-appraisal-kyc-update.component';
import { SyndicateConsortiumListComponent } from './syndicate-consortium/syndicate-consortium-list/syndicate-consortium-list.component';
import { SyndicateConsortiumUpdateComponent } from './syndicate-consortium/syndicate-consortium-update/syndicate-consortium-update.component';
import { ProposalDetailsComponent } from './proposal-details/proposal-details/proposal-details.component';
import { ProposalDetailsUpdateComponent } from './proposal-details/proposal-details-update/proposal-details-update.component';
import { FurtherDetailsSiteVisitListComponent } from './further-details/furtherDetailsSiteVisitList/furtherDetailsSiteVisitList.component';
import { ProjectAppraisalCompletionUpdateComponent } from './projectAppraisalCompletionUpdate/projectAppraisalCompletionUpdate.component';
import { FurtherDetailsSiteVisitUpdateComponent } from './further-details/furtherDetailsSiteVisitUpdate/furtherDetailsSiteVisitUpdate.component';
import { ReasonForDelayComponent } from './reasonForDelay/reasonForDelay.component';
import { CustomerRejectionUpdateComponent } from './customerRejectionUpdate/customerRejectionUpdate.component';
import { ProjectDataUpdateComponent } from './projectDataUpdate/projectDataUpdate.component';
import { ConfirmationDialogComponent } from './confirmationDialog/confirmationDialog.component';
import { ProjectAppraisalCompletionDetails } from './projectAppraisalCompletionDetails/projectAppraisalCompletionDetails.component';
import { ProjectDataComponent } from './projectData/projectData.component';
import { InternalRiskRatingComponent } from './internalRiskRating/internal-risk-rating.component';
import { TermLoanRiskRatingListComponent } from './internalRiskRating/term-loan-risk-rating-list/term-loan-risk-rating-list.component';
import { CorporateLoanRiskRatingListComponent } from './internalRiskRating/corporate-loan-risk-rating-list/corporate-loan-risk-rating-list.component';
import { TermLoanRiskRatingUpdateComponent } from './internalRiskRating/term-loan-risk-rating-update/term-loan-risk-rating-update.component';
import { CorporateLoanRiskRatingUpdateComponent } from './internalRiskRating/corporate-loan-risk-rating-update/corporate-loan-risk-rating-update.component';
import { ReasonForDelayUpdateComponent } from './reasonForDelayUpdate/reasonForDelayUpdate.component';
import { CustomerRejectionComponent } from './customerRejection/customerRejection.component';
import { RiskEvaluationsOverviewComponent } from './riskReport/risk-evaluations-overview/risk-evaluations-overview.component';

const routes = [
    {
        path: 'loanAppraisal', 
        component: LoanAppraisalComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ],
        resolve: {
            routeResolvedData: LoanAppraisalService
        }
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
        MatTabsModule,
        MatDialogModule,
        FormsModule,
        MatCheckboxModule,
        MatAutocompleteModule,
        MatStepperModule
    ],
    declarations: [
        ConfirmationDialogComponent,
        LoanAppraisalComponent,
        LoanPartnerUpdateComponent,
        LoanPartnerListComponent,
        LoanAppraisalKYCListComponent,
        LoanAppraisalKYCUpdateComponent,
        SyndicateConsortiumListComponent,
        SyndicateConsortiumUpdateComponent,
        ProposalDetailsComponent,
        ProposalDetailsUpdateComponent,
        FurtherDetailsSiteVisitListComponent,
        FurtherDetailsSiteVisitUpdateComponent,
        ProjectAppraisalCompletionUpdateComponent,
        ReasonForDelayComponent,
        ReasonForDelayUpdateComponent,
        CustomerRejectionComponent,
        CustomerRejectionUpdateComponent,
        ProjectDataComponent,
        ProjectDataUpdateComponent,
        ProjectAppraisalCompletionDetails,
        InternalRiskRatingComponent,
        TermLoanRiskRatingListComponent,
        TermLoanRiskRatingUpdateComponent,
        CorporateLoanRiskRatingListComponent,
        CorporateLoanRiskRatingUpdateComponent,
        RiskEvaluationsOverviewComponent
    ],
    providers: [
        LoanEnquiryService,
        LoanAppraisalService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        LoanAppraisalComponent,
    ],  
    entryComponents: [
        ConfirmationDialogComponent,
        LoanPartnerUpdateComponent,
        LoanAppraisalKYCUpdateComponent,
        SyndicateConsortiumUpdateComponent,
        ProposalDetailsUpdateComponent,
        FurtherDetailsSiteVisitUpdateComponent,
        ProjectAppraisalCompletionUpdateComponent,
        ReasonForDelayUpdateComponent,
        CustomerRejectionUpdateComponent,
        ProjectDataUpdateComponent,
        TermLoanRiskRatingUpdateComponent,
        CorporateLoanRiskRatingUpdateComponent
    ]
})
export class LoanAppraisalModule {
}
