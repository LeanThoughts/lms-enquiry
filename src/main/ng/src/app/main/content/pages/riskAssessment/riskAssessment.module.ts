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
import { FormsModule } from '@angular/forms';
import { RiskAssessmentComponent } from './riskAssessment.component';
import { RiskAssessmentService } from './riskAssessment.service';
import { PreliminaryRiskAssessmentComponent } from './riskAssessmentInfo/riskAssessmentInfo.component';
import { PreliminaryRiskAssessmentUpdateDialogComponent } from './riskAssessmentInfoUpdate/riskAssessmentInfoUpdate.component';

const routes = [
    {
        path: 'riskAssessment', 
        component: RiskAssessmentComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ]
        // ,
        // resolve: {
        //     routeResolvedData: RiskAssessmentService
        // }
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
        RiskAssessmentComponent,
        PreliminaryRiskAssessmentComponent,
        PreliminaryRiskAssessmentUpdateDialogComponent
    ],
    providers: [
        LoanEnquiryService,
        RiskAssessmentService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        RiskAssessmentComponent,
        PreliminaryRiskAssessmentComponent,
        PreliminaryRiskAssessmentUpdateDialogComponent
    ],  
    entryComponents: [
        PreliminaryRiskAssessmentUpdateDialogComponent
    ]
})
export class RiskAssessmentModule {
}
