import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from '../businessPartner.service';
import { PartnerService } from '../../administration/partner/partner.service';
import { BusinessPartnerBankDetailsUpdateComponent } from '../bankDetailsUpdate/bankDetailsUpdate.component';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-business-partner-bank-details-list',
    templateUrl: './bankDetailsList.component.html',
    styleUrls: ['./bankDetailsList.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerBankDetailsListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    banks: any;
    businessPartnerId: string;

    displayedColumns = [
        'serialNumber', 'bankKey', 'ifscCode', 'accountNumber', 'entryDate', 'validFrom', 'validTo'
    ];

    selectedBankDetails: any;

    subscription: Subscription;

    countryCodes: any;

    /**
     * constructor()
     */
    constructor(private _businessPartnerService: BusinessPartnerService,
                private _partnerService: PartnerService,
                private _dialog: MatDialog, 
                private _snackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute) {
                    
        this.banks = this._activatedRoute.snapshot.data['routeResolvedData'][2];
        if (this._activatedRoute.snapshot.data['routeResolvedData'][8]) {
            this.dataSource = new MatTableDataSource(this._activatedRoute.snapshot.data['routeResolvedData'][8].
                _embedded.businessPartnerBankDetails);
        }
        
        if (this._activatedRoute.snapshot.data['routeResolvedData'][12]) {
            this.countryCodes = this._activatedRoute.snapshot.data['routeResolvedData'][12];
        }
        else {
            this.countryCodes = this._activatedRoute.snapshot.data['routeResolvedData'][7];
        }

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
    onSelect(bankDetails: any): void {
        this.selectedBankDetails = bankDetails;
    }

    /**
     * addBankDetails()
     */
    addBankDetails(): void {
        if (!this.businessPartnerId) {
            this._snackBar.open('Please select a business partner or create a new one before adding bank details.', 'Close', 
                { duration: 7000 });
        }
        else {
            // Open the dialog.
            const dialogRef = this._dialog.open(BusinessPartnerBankDetailsUpdateComponent, {
                panelClass: 'fuse-business-partner-bank-details-update-dialog',
                width: '900px',
                data: {
                    banks: this.banks,
                    operation: 'addBankDetails',
                    businessPartnerId: this.businessPartnerId,
                    countryCodes: this.countryCodes
                }
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed().subscribe((result) => { 
                if (result.refresh) {
                    this._businessPartnerService.getBusinessPartnerBankDetails(this.businessPartnerId).subscribe(response => {
                        this.dataSource = new MatTableDataSource(response._embedded.businessPartnerBankDetails);
                    });
                }
            });    
        }
    }

    /**
     * updateBankDetails()
     */
    updateBankDetails(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(BusinessPartnerBankDetailsUpdateComponent, {
            panelClass: 'fuse-business-partner-bank-details-update-dialog',
            width: '900px',
            data: {
                banks: this.banks,
                operation: 'updateBankDetails',
                businessPartnerId: this.businessPartnerId,
                selectedBankDetails: this.selectedBankDetails,
                countryCodes: this.countryCodes
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getBusinessPartnerBankDetails(this.businessPartnerId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.businessPartnerBankDetails);
                });
            }
        });    
    }
}
