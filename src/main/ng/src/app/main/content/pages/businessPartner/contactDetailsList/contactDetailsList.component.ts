import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerContactDetailsUpdateDialogComponent } from '../contactDetailsUpdate/contactDetailsUpdate.component';
import { BusinessPartnerService } from '../businessPartner.service';
import { PartnerService } from '../../administration/partner/partner.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-business-partner-contact-details-list',
    templateUrl: './contactDetailsList.component.html',
    styleUrls: ['./contactDetailsList.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerContactDetailsListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    businessPartnerId: string;

    displayedColumns = [
        'serialNumber', 'selection', 'loanNumber', 'name', 'branchAddress', 'designation', 'department', 'telephoneNumber', 'landLineNumber', 
                'faxNumber', 'email'
    ];

    selectedContactDetails: any;

    subscription: Subscription;
    
    /**
     * constructor()
     */
    constructor(private _businessPartnerService: BusinessPartnerService,
                private _partnerService: PartnerService,
                private _dialog: MatDialog, 
                private _snackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute) {

        if (this._activatedRoute.snapshot.data.routeResolvedData[5]) {
            this.dataSource = new MatTableDataSource(this._activatedRoute.snapshot.data['routeResolvedData'][5].
                _embedded.businessPartnerLoanContacts);
        }
        this.subscription = this._partnerService.selectedPartner.subscribe(partner => {
            this.businessPartnerId = partner ? partner.id : null;
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    /**
     * onSelect()
     */
    onSelect(contactDetails: any): void {
        this.selectedContactDetails = contactDetails;
    }

    /**
     * addContactDetails()
     */
    addContactDetails(): void {
        if (!this.businessPartnerId) {
            this._snackBar.open('Please select a business partner or create a new one before adding contact details.', 'Close', 
                { duration: 7000 });
        }
        else {
            // Open the dialog.
            const dialogRef = this._dialog.open(BusinessPartnerContactDetailsUpdateDialogComponent, {
                panelClass: 'fuse-business-partner-contact-details-update-dialog',
                width: '900px',
                data: {
                    operation: 'addContactDetails',
                    businessPartnerId: this.businessPartnerId,
                }
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                    this._businessPartnerService.getBusinessPartnerContactDetails(this.businessPartnerId).subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.businessPartnerLoanContacts);
                    });
                }
            });    
        }
    }

    /**
     * updateContactDetails()
     */
    updateContactDetails(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(BusinessPartnerContactDetailsUpdateDialogComponent, {
            panelClass: 'fuse-business-partner-contact-details-update-dialog',
            width: '900px',
            data: {
                operation: 'updateContactDetails',
                businessPartnerId: this.businessPartnerId,
                selectedContactDetails: this.selectedContactDetails,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getBusinessPartnerContactDetails(this.businessPartnerId).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.businessPartnerLoanContacts);
                });
            }
        });    
    }
}
