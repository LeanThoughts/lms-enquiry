import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from '../businessPartner.service';
import { PartnerService } from '../../administration/partner/partner.service';
import { BusinessPartnerBankDetailsUpdateComponent } from '../bankDetailsUpdate/bankDetailsUpdate.component';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { BusinessPartnerIndustryUpdateComponent } from '../businessPartnerIndustryUpdate/businessPartnerIndustryUpdate.component';

@Component({
    selector: 'fuse-business-partner-industry-list',
    templateUrl: './businessPartnerIndustryList.component.html',
    styleUrls: ['./businessPartnerIndustryList.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerIndustryListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    businessPartnerIndustries: any;
    businessPartnerId: string;

    industrySystems: any;

    displayedColumns = [
        'serialNumber', 'industrySystem', 'industry'
    ];

    selectedBusinessPartnerIndustry: any;

    subscription: Subscription;

    /**
     * constructor()
     */
    constructor(private _businessPartnerService: BusinessPartnerService,
                private _partnerService: PartnerService,
                private _dialog: MatDialog, 
                private _snackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute) {
                    
        this.industrySystems = this._activatedRoute.snapshot.data['routeResolvedData'][3];

        this.subscription = this._partnerService.selectedPartner.subscribe(partner => {
            this.businessPartnerId = partner ? partner.id : null;
        });
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    /**
     * onSelect()
     */
    onSelect(businessPartnerIndustry: any): void {
        this.selectedBusinessPartnerIndustry = businessPartnerIndustry;
    }

    /**
     * addBankDetails()
     */
    addBusinessPartnerIndustry(): void {
        if (!this.businessPartnerId) {
            this._snackBar.open('Please select a business partner or create a new one before adding business partner industry.', 'Close', 
                { duration: 7000 });
        }
        else {
            // Open the dialog.
            const dialogRef = this._dialog.open(BusinessPartnerIndustryUpdateComponent, {
                panelClass: 'fuse-business-partner-industry-update-dialog',
                width: '600px',
                data: {
                    industrySystems: this.industrySystems,
                    operation: 'addBusinessPartnerIndustry',
                    businessPartnerId: this.businessPartnerId,
                }
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getBusinessPartnerIndustries(this.businessPartnerId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                }
            });    
        }
    }

    /**
     * updateBankDetails()
     */
    updateBusinessPartnerIndustry(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(BusinessPartnerIndustryUpdateComponent, {
            panelClass: 'fuse-business-partner-industry-update-dialog',
            width: '600px',
            data: {
                industrySystems: this.industrySystems,
                operation: 'updateBusinessPartnerIndustry',
                businessPartnerId: this.businessPartnerId,
                selectedBusinessPartnerIndustry: this.selectedBusinessPartnerIndustry,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getBusinessPartnerIndustries(this.businessPartnerId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }
}
