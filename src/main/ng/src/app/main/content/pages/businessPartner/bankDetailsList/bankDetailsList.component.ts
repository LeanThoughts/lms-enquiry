import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from '../businessPartner.service';
import { PartnerService } from '../../administration/partner/partner.service';
import { BusinessPartnerBankDetailsUpdateComponent } from '../bankDetailsUpdate/bankDetailsUpdate.component';

@Component({
    selector: 'fuse-business-partner-bank-details-list',
    templateUrl: './bankDetailsList.component.html',
    styleUrls: ['./bankDetailsList.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerBankDetailsListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    businessPartnerId: string;

    displayedColumns = [
        'serialNumber', 'selection', 'loanNumber', 'name', 'branchAddress', 'designation', 'department', 'telephoneNumber', 'landLineNumber', 
                'faxNumber', 'email'
    ];

    selectedContactDetails: any;

    /**
     * constructor()
     */
    constructor(private _businessPartnerService: BusinessPartnerService,
                private _partnerService: PartnerService,
                private _dialog: MatDialog, 
                private _snackBar: MatSnackBar) {
                    
        this.businessPartnerId = _partnerService.selectedPartner.value ? _partnerService.selectedPartner.value.id : null;
        console.log('this.businessPartnerId', this.businessPartnerId);
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
            const dialogRef = this._dialog.open(BusinessPartnerBankDetailsUpdateComponent, {
                panelClass: 'fuse-business-partner-bank-details-update-dialog',
                width: '750px',
                data: {
                operation: 'addBankDetails',
                loanApplicationId: this.businessPartnerId,
                }
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getContactDetails(this.businessPartnerId).subscribe(data => {
                        this.dataSource.data = data;
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
        const dialogRef = this._dialog.open(BusinessPartnerBankDetailsUpdateComponent, {
            panelClass: 'fuse-business-partner-bank-details-update-dialog',
            width: '750px',
            data: {
                operation: 'updateContactDetails',
                businessPartnerId: this.businessPartnerId,
                selectedContactDetails: this.selectedContactDetails,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._businessPartnerService.getContactDetails(this.businessPartnerId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }
}
