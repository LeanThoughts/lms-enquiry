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
  MatCheckboxModule
} from '@angular/material';
import { EnquiryApplicationRouteGuard } from 'app/enquiryApplication.guard';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { MAT_DATE_FORMATS } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { LoanAppraisalService } from './loanAppraisal.service';
import { LoanAppraisalComponent } from './loanAppraisal.component';
import { LoanPartnerUpdateComponent } from './loan-partner/loan-partner-update/loan-partner-update.component';
import { FormsModule } from '@angular/forms';
import { LoanPartnersComponent } from './loan-partner/loan-partners/loan-partners.component';
import { LoanAppraisalKYCListComponent } from './loan-appraisal-kyc/loan-appraisal-kyc-list/loan-appraisal-kyc-list.component';
import { LoanAppraisalKYCUpdateComponent } from './loan-appraisal-kyc/loan-appraisal-kyc-update/loan-appraisal-kyc-update.component';
import { SyndicateConsortiumListComponent } from './syndicate-consortium/syndicate-consortium-list/syndicate-consortium-list.component';
import { SyndicateConsortiumUpdateComponent } from './syndicate-consortium/syndicate-consortium-update/syndicate-consortium-update.component';

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
      MatCheckboxModule
    ],
    declarations: [
        LoanAppraisalComponent,
        LoanPartnerUpdateComponent,
        LoanPartnersComponent,
        LoanAppraisalKYCListComponent,
        LoanAppraisalKYCUpdateComponent,
        SyndicateConsortiumListComponent,
        SyndicateConsortiumUpdateComponent
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
        LoanPartnersComponent,
        LoanPartnerUpdateComponent,
        LoanAppraisalKYCListComponent,
        LoanAppraisalKYCUpdateComponent,
        SyndicateConsortiumListComponent,
        SyndicateConsortiumUpdateComponent
    ]
})
export class LoanAppraisalModule {
}
