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
import { EnquiryActionService } from './enquiryAction.service';
import { FormsModule } from '@angular/forms';
import { EnquiryActionComponent } from './enquiryAction.component';
import { EnquiryActionReasonForDelayComponent } from './enquiryActionReasonForDelay/enquiryActionReasonForDelay.component';
import { EnquiryActionReasonForDelayUpdateComponent } from './enquiryActionReasonForDelayUpdate/enquiryActionReasonForDelayUpdate.component';
import { RejectByPFSComponent } from './rejectByPFS/rejectByPFS.component';
import { RejectByPFSUpdateComponent } from './rejectByPFSUpdate/rejectByPFSUpdate.component';
import { RejectByCustomerComponent } from './rejectByCustomer/rejectByCustomer.component';
import { RejectByCustomerUpdateComponent } from './rejectByCustomerUpdate/rejectByCustomerUpdate.component';
import { EnquiryCompletionComponent } from './enquiryCompletion/enquiryCompletion.component';
import { EnquiryCompletionUpdateComponent } from './enquiryCompletionUpdate/enquiryCompletionUpdate.component';

const routes = [
    {
        path: 'enquiryAction', 
        component: EnquiryActionComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ],
        resolve: {
            routeResolvedData: EnquiryActionService
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
        EnquiryActionComponent,
        EnquiryActionReasonForDelayComponent,
        EnquiryActionReasonForDelayUpdateComponent,
        RejectByPFSComponent,
        RejectByPFSUpdateComponent,
        RejectByCustomerComponent,
        RejectByCustomerUpdateComponent,
        EnquiryCompletionComponent,
        EnquiryCompletionUpdateComponent
    ],
    providers: [
        LoanEnquiryService,
        EnquiryActionService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        EnquiryActionComponent,
        EnquiryActionReasonForDelayComponent,
        EnquiryActionReasonForDelayUpdateComponent,
        RejectByPFSComponent,
        RejectByPFSUpdateComponent,
        RejectByCustomerComponent,
        RejectByCustomerUpdateComponent,
        EnquiryCompletionComponent,
        EnquiryCompletionUpdateComponent
    ],  
    entryComponents: [
        EnquiryActionReasonForDelayUpdateComponent,
        RejectByPFSUpdateComponent,
        RejectByCustomerUpdateComponent,
        EnquiryCompletionUpdateComponent
    ]
})
export class EnquiryActionModule {
}
