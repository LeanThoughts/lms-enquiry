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
import { BoardApprovalComponent } from './boardApproval.component';
import { BoardApprovalService } from './boardApproval.service';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { BoardApprovalReasonForDelayComponent } from './reasonForDelay/reasonForDelay.component';
import { BoardApprovalReasonForDelayUpdateDialogComponent } from './reasonForDelayUpdate/reasonForDelayUpdate.component';

const routes = [
    {
        path: 'boardApproval',
        component: BoardApprovalComponent
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
      MatCardModule,
    ],
    declarations: [
        BoardApprovalComponent,
        BoardApprovalReasonForDelayComponent,
        BoardApprovalReasonForDelayUpdateDialogComponent
    ],
    providers: [
        BoardApprovalService,
        {
            provide: MAT_DATE_LOCALE,
            useValue: 'en-in'
        },
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
    ],
    exports: [
        BoardApprovalComponent,
        BoardApprovalReasonForDelayComponent,
        BoardApprovalReasonForDelayUpdateDialogComponent
    ],
    entryComponents: [
        BoardApprovalReasonForDelayUpdateDialogComponent
    ]
})
export class BoardApprovalModule {
}
