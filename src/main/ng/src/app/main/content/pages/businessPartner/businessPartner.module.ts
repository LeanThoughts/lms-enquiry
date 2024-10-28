
import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {
    MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule, MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatIconModule, MatInputModule,
  MatOptionModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSelectModule, MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule
} from "@angular/material";
import {CdkTableModule} from "@angular/cdk/table";
import {BusinessPartnerService} from "./businessPartner.service";
import { BusinessPartnerComponent } from "./businessPartner.component";
import { FuseSharedModule } from "@fuse/shared.module";
import { PartnerUpdateComponent } from "./partnerUpdate/partnerUpdate.component";
import { BusinessPartnerContactDetailsListComponent } from "./contactDetailsList/contactDetailsList.component";
import { BusinessPartnerContactDetailsUpdateDialogComponent } from "./contactDetailsUpdate/contactDetailsUpdate.component";



const routes = [
  {
    path      : 'createBusinessPartner',
    component : BusinessPartnerComponent,
    resolve   : {
      routeResolvedData: BusinessPartnerService
    }
  }
]

@NgModule({
    declarations: [
        BusinessPartnerComponent,
        PartnerUpdateComponent,
        BusinessPartnerContactDetailsListComponent,
        BusinessPartnerContactDetailsUpdateDialogComponent
    ],
    imports     : [
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
        MatOptionModule
      ],
    exports     : [
        BusinessPartnerComponent,
        PartnerUpdateComponent,
        BusinessPartnerContactDetailsListComponent,
        BusinessPartnerContactDetailsUpdateDialogComponent
    ],
    providers   : [
        BusinessPartnerService
    ],
    entryComponents: [
        BusinessPartnerContactDetailsUpdateDialogComponent
    ]
})
export class BusinessPartnerModule
{
}
