import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from '../businessPartner.service';
import { PartnerService } from '../../administration/partner/partner.service';
import { BusinessPartnerIdentificationUpdateComponent } from '../identificationUpdate/identificationUpdate.component';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-business-partner-identification-list',
    templateUrl: './identificationList.component.html',
    styleUrls: ['./identificationList.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerIdentificationListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    identificationCategories: any;
    businessPartnerId: string;

    displayedColumns = [
        'serialNumber', 'identificationCategory', 'identificationNumber', 'idInstitute', 'idEntryDate', 'idValidFrom', 'idValidTo', 
            'documentName', 'fileReference'
    ];

    selectedIdentificationDetails: any;

    subscription: Subscription;

    /**
     * constructor()
     */
    constructor(private _businessPartnerService: BusinessPartnerService,
                private _partnerService: PartnerService,
                private _dialog: MatDialog, 
                private _snackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute) {
                    
        this.identificationCategories = this._activatedRoute.snapshot.data['routeResolvedData'][4]._embedded.identificationCategories;
        if (this._activatedRoute.snapshot.data['routeResolvedData'][7]) {
            this.dataSource = new MatTableDataSource(this._activatedRoute.snapshot.data['routeResolvedData'][7].
                _embedded.businessPartnerIdentifications);
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
    onSelect(identificationDetails: any): void {
        this.selectedIdentificationDetails = identificationDetails;
    }

    /**
     * addBankDetails()
     */
    addIdentificationDetails(): void {
        if (!this.businessPartnerId) {
            this._snackBar.open('Please select a business partner or create a new one before adding identification details.', 'Close', 
                { duration: 7000 });
        }
        else {
            // Open the dialog.
            const dialogRef = this._dialog.open(BusinessPartnerIdentificationUpdateComponent, {
                panelClass: 'fuse-business-partner-identification-update-dialog',
                width: '900px',
                data: {
                    identificationCategories: this.identificationCategories,
                    operation: 'addIdentificationDetails',
                    businessPartnerId: this.businessPartnerId,
                }
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed().subscribe((result) => { 
                if (result.refresh) {
                    this._businessPartnerService.getBusinessPartnerIdentificationDetails(this.businessPartnerId).subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.businessPartnerIdentifications);
                    });
                }
            });    
        }
    }

    /**
     * updateBankDetails()
     */
    updateIdentificationDetails(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(BusinessPartnerIdentificationUpdateComponent, {
            panelClass: 'fuse-business-partner-identification-update-dialog',
            width: '900px',
            data: {
                identificationCategories: this.identificationCategories,
                operation: 'updateIdentificationDetails',
                businessPartnerId: this.businessPartnerId,
                selectedIdentificationDetails: this.selectedIdentificationDetails,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getBusinessPartnerIdentificationDetails(this.businessPartnerId).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.businessPartnerIdentifications);
                });
            }
        });    
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getIdentificationCategoryDescription()
     */
    getIdentificationCategoryDescription(identificationCategoryId: string): string {
        const identificationCategory = this.identificationCategories.find(identificationCategory => identificationCategory.id === identificationCategoryId);
        return identificationCategory ? identificationCategory.value : '';
    }
}
