import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BoardApprovalService } from '../boardApproval.service';
import { BoardApprovalRejectedByCustomerUpdateDialogComponent } from '../rejectedByCustomerUpdate/rejectedByCustomerUpdate.component';

@Component({
    selector: 'fuse-ba-rejected-by-customer',
    templateUrl: './rejectedByCustomer.component.html',
    styleUrls: ['./rejectedByCustomer.component.scss'],
    animations: fuseAnimations
})
export class BoardApprovalRejectedByCustomerComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'rejectionCategory', 'details'
    ];

    loanApplicationId: string;

    selectedRejectedByCustomer: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _boardApprovalService: BoardApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._boardApprovalService.getRejectedByCustomers().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.boardApprovalRejectedByCustomers);
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
        const dialogRef = this._matDialog.open(BoardApprovalRejectedByCustomerUpdateDialogComponent, {
            panelClass: 'fuse-ba-rejected-by-customer-update-dialog',
            width: '750px',
            data: {
                operation: 'addRejectedByCustomer',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._boardApprovalService._boardApproval.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._boardApprovalService.getBoardApproval(this.loanApplicationId).subscribe(data => {
                        this._boardApprovalService._boardApproval.next(data);
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
        const dialogRef = this._matDialog.open(BoardApprovalRejectedByCustomerUpdateDialogComponent, {
            panelClass: 'fuse-ba-rejected-by-customer-update-dialog',
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
                this._boardApprovalService.deleteRejectedByCustomer(this.selectedRejectedByCustomer.id).subscribe(() => {
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
