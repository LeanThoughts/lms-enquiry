import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FuseSharedModule } from '@fuse/shared.module';
import {
    MatExpansionModule, MatInputModule, MatButtonModule, MatFormFieldModule, MatTableModule, MatSortModule,
    MatPaginatorModule, MatToolbarModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatDatepickerModule,
    MatDialogModule,
    MatSnackBarModule
} from '@angular/material';
import {EnquiryApplicationRouteGuard} from "../../../../enquiryApplication.guard";
import { ReferenceInterestValueComponent } from './referenceInterestValue.component';
import { ReferenceInterestValueService } from './referenceInterestValue.service';
import { ReferenceInterestValueUpdateComponent } from './referenceInterestValueUpdate/referenceInterestValueUpdate.component';

const routes = [
    {
        path: 'referenceInterestRateList',
        component: ReferenceInterestValueComponent,
        resolve: {
            routeResolvedData: ReferenceInterestValueService
        },
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
        MatProgressSpinnerModule,
        MatDialogModule,
        MatSnackBarModule
    ],
    declarations: [
        ReferenceInterestValueComponent,
        ReferenceInterestValueUpdateComponent
    ],
    providers: [
        ReferenceInterestValueService
    ],
    exports: [
        ReferenceInterestValueComponent,
        ReferenceInterestValueUpdateComponent
    ],
    entryComponents: [
        ReferenceInterestValueUpdateComponent
    ]
})
export class ReferenceInterestValueModule {
}