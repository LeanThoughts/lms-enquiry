import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { SanctionService } from '../sanction.service';
import { PaymentReceiptPreSanctionUpdateDialogComponent } from '../paymentReceiptPreSanctionUpdate/paymentReceiptPreSanctionUpdate.component';

@Component({
    selector: 'fuse-payment-receipt-pre-sanction',
    templateUrl: './paymentReceiptPreSanction.component.html',
    styleUrls: ['./paymentReceiptPreSanction.component.scss'],
    animations: fuseAnimations
})
export class PaymentReceiptPreSanctionComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'proformaInvoiceNumber', 'proformaInvoiceDate', 'feeType', 'amount', 'payee', 'amountReceived', 'dateOfTransfer', 
                'rtgsNeftNumber', 'referenceNumber'
    ];

    loanApplicationId: string;

    selectedPaymentReceipt: any;
    feeTypes: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _sactionService: SanctionService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._sactionService.getFeeTypes().subscribe(data => {
            this.feeTypes = data._embedded.feeTypes;
        });
        this.refreshTable();
    }

    /**
     *
     * @param enquiry
     */
    onSelect(row: any): void {
        this.selectedPaymentReceipt = row;
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._sactionService.getPaymentReceipts('pre').subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.paymentReceiptPreSanctions);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(PaymentReceiptPreSanctionUpdateDialogComponent, {
            panelClass: 'fuse-payment-receipt-pre-sanction-update-dialog',
            width: '850px',
            data: {
                operation: 'addPaymentReceipt',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._sactionService._sanction.value.id !== '') {
                    this.refreshTable(); 
                }
                else {
                    this._sactionService.getSanction(this.loanApplicationId).subscribe(data => {
                        this._sactionService._sanction.next(data);
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
        const dialogRef = this._matDialog.open(PaymentReceiptPreSanctionUpdateDialogComponent, {
            panelClass: 'fuse-payment-receipt-pre-sanction-update-dialog',
            width: '850px',
            data: {
                operation: 'updatePaymentReceipt',
                loanApplicationId: this.loanApplicationId,
                selectedPaymentReceipt: this.selectedPaymentReceipt
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
                this._sactionService.deletePaymentReceipt(this.selectedPaymentReceipt.id, 'pre').subscribe(() => {
                    this.selectedPaymentReceipt = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected payment receipt.', 'OK', { duration: 7000 });
                });
            }
        });
    }

    /**
     * getFeeTypeDescription()
     */
    getFeeTypeDescription(code): string {
        const obj = this.feeTypes.find(feeType => {
            return feeType.code === code;
        });
        return obj.value;
    }    
}
