import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
  MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
  MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule} from '@angular/material';
import { LoanEnquiryService } from '../enquiryApplication.service';
import {EnquiryApplicationRouteGuard} from "../../../../../enquiryApplication.guard";
import { EnquiriesExcelUploadComponent } from './enquiriesExcelUpload.component';
import { EnquiriesUploadListComponent } from './enquiriesUploadList/enquiriesUploadList.component';

const routes = [
    {
        path: 'importEnquiries',
        component: EnquiriesExcelUploadComponent,
        canActivate: [
            EnquiryApplicationRouteGuard
        ]
    }
];

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
      MatProgressSpinnerModule
    ],
    declarations: [
        EnquiriesExcelUploadComponent,
        EnquiriesUploadListComponent
    ],
    providers: [
        LoanEnquiryService
    ],
    exports: [
        EnquiriesExcelUploadComponent,
        EnquiriesUploadListComponent
    ]
})
export class EnquiriesExcelUploadModule {
}
