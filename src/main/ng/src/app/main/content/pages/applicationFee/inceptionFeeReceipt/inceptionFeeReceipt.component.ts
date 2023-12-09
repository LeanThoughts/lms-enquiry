import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ApplicationFeeService } from '../applicationFee.service';
import { InceptionFeeReceiptUpdateDialogComponent } from '../inceptionFeeReceiptUpdate/inceptionFeeReceiptUpdate.component';

@Component({
    selector: 'fuse-inception-fee-receipt',
    templateUrl: './inceptionFeeReceipt.component.html',
    styleUrls: ['./inceptionFeeReceipt.component.scss'],
    animations: fuseAnimations
})
export class InceptionFeeReceiptComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'invoiceNumber', 'invoiceDate', 'amount', 'taxAmount', 'totalAmount', 'amountReceived', 'dateOfTransfer', 'rtgsNumber', 
                'remarks', 'referenceNumber'
    ];

    loanApplicationId: string;

    selectedInceptionFee: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _applicationFeeService: ApplicationFeeService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._applicationFeeService.getInceptionFees(this._applicationFeeService._applicationFee.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.inceptionFees);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedInceptionFee = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(InceptionFeeReceiptUpdateDialogComponent, {
            panelClass: 'fuse-inception-fee-receipt-update-dialog',
            width: '850px',
            data: {
                operation: 'addInceptionFee',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._applicationFeeService._applicationFee.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                        this._applicationFeeService._applicationFee.next(data);
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
        const dialogRef = this._matDialog.open(InceptionFeeReceiptUpdateDialogComponent, {
            panelClass: 'fuse-inception-fee-receipt-update-dialog',
            width: '850px',
            data: {
                operation: 'updateInceptionFee',
                loanApplicationId: this.loanApplicationId,
                selectedInceptionFee: this.selectedInceptionFee
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
                this._applicationFeeService.deleteInceptionFee(this.selectedInceptionFee.id).subscribe(() => {
                    this.selectedInceptionFee = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected inception fee details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
