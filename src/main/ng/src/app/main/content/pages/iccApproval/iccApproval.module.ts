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
    MatDialogModule,
    DateAdapter,
    MAT_DATE_FORMATS
} from '@angular/material';
import { ICCApprovalComponent } from './iccApproval.component';
import { ICCApprovalService } from './iccApproval.service';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { ICCFurtherDetailUpdateDialogComponent } from './iccFurtherDetailUpdate/iccFurtherDetailUpdate.component';
import { ICCFurtherDetailsComponent } from './iccFurtherDetails/iccFurtherDetails.component';
import { RejectedByICCComponent } from './rejectedByICC/rejectedByICC.component';
import { RejectedByICCUpdateDialogComponent } from './rejectedByICCUpdate/rejectedByICCUpdate.component';
import { ICCApprovalUpdateDialogComponent } from './iccApprovalUpdate/iccApprovalUpdate.component';
import { ICCReasonForDelayComponent } from './iccReasonForDelay/iccReasonForDelay.component';
import { ICCReasonForDelayUpdateDialogComponent } from './iccReasonForDelayUpdate/iccReasonForDelayUpdate.component';
import { ICCApprovalMeetingComponent } from './iccApproval/iccApproval.component';
import { ICCRejectedByCustomerComponent } from './iccRejectedByCustomer/iccRejectedByCustomer.component';
import { ICCRejectedByCustomerUpdateDialogComponent } from './iccRejectedByCustomerUpdate/iccRejectedByCustomerUpdate.component';
import { LoanEnhancementComponent } from './loanEnhancement/loanEnhancement.component';
import { LoanEnhancementUpdateDialogComponent } from './loanEnhancementUpdate/loanEnhancementUpdate.component';

const routes = [
    {
        path: 'iccApprovalStage',
        component: ICCApprovalComponent
        // resolve: {
        //     routeResolvedData: ICCApprovalService
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
        ICCApprovalComponent,
        ICCFurtherDetailsComponent,
        ICCFurtherDetailUpdateDialogComponent,
        RejectedByICCComponent,
        RejectedByICCUpdateDialogComponent,
        ICCApprovalMeetingComponent,
        ICCApprovalUpdateDialogComponent,
        ICCReasonForDelayComponent,
        ICCReasonForDelayUpdateDialogComponent,
        ICCRejectedByCustomerComponent,
        ICCRejectedByCustomerUpdateDialogComponent,
        LoanEnhancementComponent,
        LoanEnhancementUpdateDialogComponent
    ],
    providers: [
        ICCApprovalService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        ICCApprovalComponent,
        ICCFurtherDetailsComponent,
        ICCFurtherDetailUpdateDialogComponent,
        RejectedByICCComponent,
        RejectedByICCUpdateDialogComponent,
        ICCApprovalMeetingComponent,
        ICCApprovalUpdateDialogComponent,
        ICCReasonForDelayComponent,
        ICCReasonForDelayUpdateDialogComponent,
        ICCRejectedByCustomerComponent,
        ICCRejectedByCustomerUpdateDialogComponent,
        LoanEnhancementComponent,
        LoanEnhancementUpdateDialogComponent
    ],
    entryComponents: [
        ICCFurtherDetailUpdateDialogComponent,
        RejectedByICCUpdateDialogComponent,
        ICCApprovalUpdateDialogComponent,
        ICCReasonForDelayUpdateDialogComponent,
        ICCRejectedByCustomerUpdateDialogComponent,
        LoanEnhancementUpdateDialogComponent
    ]
})
export class ICCApprovalModule {
}
