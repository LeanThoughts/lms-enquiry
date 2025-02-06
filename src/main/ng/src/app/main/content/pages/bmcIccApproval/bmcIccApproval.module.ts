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
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { BMCApprovalService } from './bmcIccApproval.service';
import { BMCApprovalComponent } from './bmcIccApproval.component';
import { BMCICCFurtherDetailsComponent } from './bmcIccFurtherDetails/bmcIccFurtherDetails.component';
import { BMCICCFurtherDetailUpdateDialogComponent } from './bmcIccFurtherDetailUpdate/bmcIccFurtherDetailUpdate.component';
import { BMCICCApprovalMeetingComponent } from './bmcIccApproval/bmcIccApproval.component';
import { BMCRejectedByICCUpdateDialogComponent } from './bmcRejectedByIccUpdate/bmcRejectedByIccUpdate.component';
import { BMCICCRejectedByCustomerUpdateDialogComponent } from './bmcIccRejectedByCustomerUpdate/bmcIccRejectedByCustomerUpdate.component';
import { BMCICCApprovalUpdateDialogComponent } from './bmcIccApprovalUpdate/bmcIccApprovalUpdate.component';
import { BMCICCReasonForDelayComponent } from './bmcIccReasonForDelay/bmcIccReasonForDelay.component';
import { BMCICCReasonForDelayUpdateDialogComponent } from './bmcIccReasonForDelayUpdate/bmcIccReasonForDelayUpdate.component';
import { BMCICCRejectedByCustomerComponent } from './bmcIccRejectedByCustomer/bmcIccRejectedByCustomer.component';
import { BMCLoanEnhancementComponent } from './bmcIccLoanEnhancement/bmcIccLoanEnhancement.component';
import { BMCLoanEnhancementUpdateDialogComponent } from './bmcIccLoanEnhancementUpdate/bmcIccLoanEnhancementUpdate.component';
import { BMCRejectedByICCComponent } from './bmcRejectedByIcc/bmcRejectedByIcc.component';

const routes = [
    {
        path: 'bmcApprovalStage',
        component: BMCApprovalComponent,
        resolve: {
            routeResolvedData: BMCApprovalService
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
        BMCApprovalComponent,
        BMCICCFurtherDetailsComponent,
        BMCICCFurtherDetailUpdateDialogComponent,
        BMCRejectedByICCComponent,
        BMCRejectedByICCUpdateDialogComponent,
        BMCICCApprovalMeetingComponent,
        BMCICCApprovalUpdateDialogComponent,
        BMCICCReasonForDelayComponent,
        BMCICCReasonForDelayUpdateDialogComponent,
        BMCICCRejectedByCustomerComponent,
        BMCICCRejectedByCustomerUpdateDialogComponent,
        BMCLoanEnhancementComponent,
        BMCLoanEnhancementUpdateDialogComponent
    ],
    providers: [
        BMCApprovalService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        BMCApprovalComponent,
        BMCICCFurtherDetailsComponent,
        BMCICCFurtherDetailUpdateDialogComponent,
        BMCRejectedByICCComponent,
        BMCRejectedByICCUpdateDialogComponent,
        BMCICCApprovalMeetingComponent,
        BMCICCApprovalUpdateDialogComponent,
        BMCICCReasonForDelayComponent,
        BMCICCReasonForDelayUpdateDialogComponent,
        BMCICCRejectedByCustomerComponent,
        BMCICCRejectedByCustomerUpdateDialogComponent,
        BMCLoanEnhancementComponent,
        BMCLoanEnhancementUpdateDialogComponent
    ],
    entryComponents: [
        BMCICCFurtherDetailUpdateDialogComponent,
        BMCRejectedByICCUpdateDialogComponent,
        BMCICCApprovalUpdateDialogComponent,
        BMCICCReasonForDelayUpdateDialogComponent,
        BMCICCRejectedByCustomerUpdateDialogComponent,
        BMCLoanEnhancementUpdateDialogComponent
    ]
})
export class BMCApprovalModule {
}
