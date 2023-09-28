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

import { SanctionService } from './sanction.service';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { SanctionReasonForDelayComponent } from './reasonForDelay/reasonForDelay.component';
import { SanctionComponent } from './sanction.component';
import { SanctionReasonForDelayUpdateDialogComponent } from './reasonForDelayUpdate/reasonForDelayUpdate.component';
import { SanctionRejectedByCustomerComponent } from './rejectedByCustomer/rejectedByCustomer.component';
import { SanctionRejectedByCustomerUpdateDialogComponent } from './rejectedByCustomerUpdate/rejectedByCustomerUpdate.component';
import { PaymentReceiptPreSanctionComponent } from './paymentReceiptPreSanction/paymentReceiptPreSanction.component';
import { PaymentReceiptPreSanctionUpdateDialogComponent } from './paymentReceiptPreSanctionUpdate/paymentReceiptPreSanctionUpdate.component';
import { PaymentReceiptPostSanctionComponent } from './paymentReceiptPostSanction/paymentReceiptPostSanction.component';
import { PaymentReceiptPostSanctionUpdateDialogComponent } from './paymentReceiptPostSanctionUpdate/paymentReceiptPostSanctionUpdate.component';
import { sanctionLetterComponent } from './sanctionLetter/sanctionLetter.component';
import { SanctionLetterUpdateDialogComponent } from './sanctionLetterUpdate/sanctionLetterUpdate.component';

const routes = [
    {
        path: 'sanction',
        component: SanctionComponent
        // resolve: {
        //     routeResolvedData: BoardApprovalService
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
      MatCardModule
    ],
    declarations: [
        SanctionComponent,
        SanctionReasonForDelayComponent,
        SanctionReasonForDelayUpdateDialogComponent,
        SanctionRejectedByCustomerComponent,
        SanctionRejectedByCustomerUpdateDialogComponent,
        PaymentReceiptPreSanctionComponent,
        PaymentReceiptPreSanctionUpdateDialogComponent,
        PaymentReceiptPostSanctionComponent,
        PaymentReceiptPostSanctionUpdateDialogComponent,
        sanctionLetterComponent,
        SanctionLetterUpdateDialogComponent
    ],
    providers: [
        SanctionService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        SanctionComponent,
        SanctionReasonForDelayComponent,
        SanctionReasonForDelayUpdateDialogComponent,
        SanctionRejectedByCustomerComponent,
        SanctionRejectedByCustomerUpdateDialogComponent,
        PaymentReceiptPreSanctionComponent,
        PaymentReceiptPreSanctionUpdateDialogComponent,
        PaymentReceiptPostSanctionComponent,
        PaymentReceiptPostSanctionUpdateDialogComponent,
        sanctionLetterComponent,
        SanctionLetterUpdateDialogComponent
    ],
    entryComponents: [
        SanctionReasonForDelayUpdateDialogComponent,
        SanctionRejectedByCustomerUpdateDialogComponent,
        PaymentReceiptPreSanctionUpdateDialogComponent,
        PaymentReceiptPostSanctionUpdateDialogComponent,
        SanctionLetterUpdateDialogComponent
    ]
})
export class SanctionModule {
}
