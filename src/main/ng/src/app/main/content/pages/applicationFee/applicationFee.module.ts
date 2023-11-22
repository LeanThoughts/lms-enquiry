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
import { ApplicationFeeService } from './applicationFee.service';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { ApplicationFeeComponent } from './applicationFee.component';
import { TermSheetComponent } from './termSheet/termSheet.component';
import { TermSheetUpdateDialogComponent } from './termSheetUpdate/termSheetUpdate.component';
import { FormalRequestComponent } from './formalRequest/formalRequest.component';
import { FormalRequestUpdateDialogComponent } from './formalRequestUpdate/formalRequestUpdate.component';
import { ApplicationFeeReceiptUpdateDialogComponent } from './applicationFeeReceiptUpdate/applicationFeeReceiptUpdate.component';
import { ApplicationFeeReceiptComponent } from './applicationFeeReceipt/applicationFeeReceipt.component';
import { InvoicingDetailsComponent } from './invoicingDetails/invoicingDetails.component';

const routes = [
    {
        path: 'applicationFee',
        component: ApplicationFeeComponent
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
        ApplicationFeeComponent,
        TermSheetComponent,
        TermSheetUpdateDialogComponent,
        FormalRequestComponent,
        FormalRequestUpdateDialogComponent,
        ApplicationFeeReceiptComponent,
        ApplicationFeeReceiptUpdateDialogComponent,
        InvoicingDetailsComponent
    ],
    providers: [
        ApplicationFeeService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        ApplicationFeeComponent,
        TermSheetComponent,
        TermSheetUpdateDialogComponent,
        FormalRequestComponent,
        FormalRequestUpdateDialogComponent,
        ApplicationFeeReceiptComponent,
        ApplicationFeeReceiptUpdateDialogComponent,
        InvoicingDetailsComponent
    ],
    entryComponents: [
        TermSheetUpdateDialogComponent,
        FormalRequestUpdateDialogComponent,
        ApplicationFeeReceiptUpdateDialogComponent
    ]
})
export class ApplicationFeeModule {
}
