import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { SanctionService } from '../sanction.service';
import { SanctionRejectedByCustomerUpdateDialogComponent } from '../rejectedByCustomerUpdate/rejectedByCustomerUpdate.component';

@Component({
    selector: 'fuse-sanction-rejected-by-customer',
    templateUrl: './rejectedByCustomer.component.html',
    styleUrls: ['./rejectedByCustomer.component.scss'],
    animations: fuseAnimations
})
export class SanctionRejectedByCustomerComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'rejectionCategory', 'details'
    ];

    loanApplicationId: string;

    selectedRejectedByCustomer: any;

    disableAdd = true;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _sanctionService: SanctionService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._sanctionService.getRejectedByCustomers().subscribe(data => {
            if (data._embedded.sanctionRejectedByCustomers.length === 0)
                this.disableAdd = false;
            else
                this.disableAdd = true;
            this.dataSource = new MatTableDataSource(data._embedded.sanctionRejectedByCustomers);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedRejectedByCustomer = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(SanctionRejectedByCustomerUpdateDialogComponent, {
            panelClass: 'fuse-sanction-rejected-by-customer-update-dialog',
            width: '750px',
            data: {
                operation: 'addRejectedByCustomer',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._sanctionService._sanction.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._sanctionService.getSanction(this.loanApplicationId).subscribe(data => {
                        this._sanctionService._sanction.next(data);
                        this.refreshTable(); 
                    });
                }
            }
        });    
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(SanctionRejectedByCustomerUpdateDialogComponent, {
            panelClass: 'fuse-sanction-rejected-by-customer-update-dialog',
            width: '750px',
            data: {
                operation: 'updateRejectedByCustomer',
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByCustomer: this.selectedRejectedByCustomer
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.refreshTable();
            }
        });
    }

    /**
     * delete()
     */
    delete(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._sanctionService.deleteRejectedByCustomer(this.selectedRejectedByCustomer.id).subscribe(() => {
                    this.selectedRejectedByCustomer = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected rejected by customer details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
