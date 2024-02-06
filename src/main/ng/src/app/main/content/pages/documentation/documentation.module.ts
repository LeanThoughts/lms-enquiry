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
import { DocumentationService } from './documentation.service';
import { DocumentationComponent } from './documentation.component';
import { DocumentationReasonForDelayComponent } from './reasonForDelay/reasonForDelay.component';
import { DocumentationReasonForDelayUpdateDialogComponent } from './reasonForDelayUpdate/reasonForDelayUpdate.component';
import { LegalCounselComponent } from './legalCounsel/legalCounsel.component';
import { LegalCounselUpdateDialogComponent } from './legalCounselUpdate/legalCounselUpdate.component';
import { LegalCounselReportComponent } from './legalCounselReport/legalCounselReport.component';
import { LegalCounselReportUpdateDialogComponent } from './legalCounselReportUpdate/legalCounselReportUpdate.component';
import { LLCFeeAndLegalCounselFeeComponent } from './llcFeeAndLegalCounselFee/llcFeeAndLegalCounselFee.component';
import { LLCFeeUpdateDialogComponent } from './llcFeeUpdate/llcFeeUpdate.component';
import { LLCLegalCounselFeeUpdateDialogComponent } from './llcLegalCounselFeeUpdate/llcLegalCounselFeeUpdate.component';
import { NodalOfficersComponent } from './nodalOfficers/nodalOfficers.component';
import { NodalOfficerUpdateDialogComponent } from './nodalOfficerUpdate/nodalOfficerUpdate.component';

const routes = [
    {
        path: 'documentation',
        component: DocumentationComponent
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
        DocumentationComponent,
        DocumentationReasonForDelayComponent,
        DocumentationReasonForDelayUpdateDialogComponent,
        LegalCounselComponent,
        LegalCounselUpdateDialogComponent,
        LegalCounselReportComponent,
        LegalCounselReportUpdateDialogComponent,
        LLCFeeAndLegalCounselFeeComponent,
        LLCFeeUpdateDialogComponent,
        LLCLegalCounselFeeUpdateDialogComponent,
        NodalOfficersComponent,
        NodalOfficerUpdateDialogComponent
    ],
    providers: [
        DocumentationService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        DocumentationComponent,
        DocumentationReasonForDelayComponent,
        DocumentationReasonForDelayUpdateDialogComponent,
        LegalCounselComponent,
        LegalCounselUpdateDialogComponent,
        LegalCounselReportComponent,
        LegalCounselReportUpdateDialogComponent,
        LLCFeeAndLegalCounselFeeComponent,
        LLCFeeUpdateDialogComponent,
        LLCLegalCounselFeeUpdateDialogComponent,
        NodalOfficersComponent,
        NodalOfficerUpdateDialogComponent
    ],
    entryComponents: [
        DocumentationReasonForDelayUpdateDialogComponent,
        LegalCounselUpdateDialogComponent,
        LegalCounselReportUpdateDialogComponent,
        LLCFeeUpdateDialogComponent,
        LLCLegalCounselFeeUpdateDialogComponent,
        NodalOfficerUpdateDialogComponent
    ]
})
export class DocumentationModule {
}
