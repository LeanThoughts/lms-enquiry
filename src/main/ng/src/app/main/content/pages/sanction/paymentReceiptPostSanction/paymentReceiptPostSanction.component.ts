import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { SanctionService } from '../sanction.service';
import { PaymentReceiptPostSanctionUpdateDialogComponent } from '../paymentReceiptPostSanctionUpdate/paymentReceiptPostSanctionUpdate.component';

@Component({
    selector: 'fuse-payment-receipt-post-sanction',
    templateUrl: './paymentReceiptPostSanction.component.html',
    styleUrls: ['./paymentReceiptPostSanction.component.scss'],
    animations: fuseAnimations
})
export class PaymentReceiptPostSanctionComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'proformaInvoiceNumber', 'proformaInvoiceDate', 'feeInvoice', 'amount', 'payee', 'amountReceived', 'dateOfTransfer', 
                'rtgsNeftNumber', 'referenceNumber'
    ];

    loanApplicationId: string;

    selectedPaymentReceipt: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _sactionService: SanctionService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        // _sactionService.getReasonForDelays().subscribe(data => {
        //     this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
        //     this.dataSource.sort = this.sort;
        // });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(row: any): void {
        this.selectedPaymentReceipt = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(PaymentReceiptPostSanctionUpdateDialogComponent, {
            panelClass: 'fuse-payment-receipt-post-sanction-update-dialog',
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
                    this._sactionService.getReasonForDelays().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                        this.dataSource.sort = this.sort;
                    });    
                }
                else {
                    this._sactionService.getSanction(this.loanApplicationId).subscribe(data => {
                        this._sactionService._sanction.next(data);
                        this._sactionService.getReasonForDelays().subscribe(data => {
                            this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                            this.dataSource.sort = this.sort;
                        });  
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
        // const dialogRef = this._matDialog.open(SanctionReasonForDelayUpdateDialogComponent, {
        //     panelClass: 'fuse-sanction-reason-for-delay-update-dialog',
        //     width: '750px',
        //     data: {
        //         operation: 'updateReason',
        //         loanApplicationId: this.loanApplicationId,
        //         selectedReason: this.selectedPaymentReceipt
        //     }
        // });
        // // Subscribe to the dialog close event to intercept the action taken.
        // dialogRef.afterClosed().subscribe((result) => { 
        //     if (result.refresh) {
        //         this._sactionService.getReasonForDelays().subscribe(data => {
        //             this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
        //             this.dataSource.sort = this.sort;
        //         });
        //     }
        // });
    }

    /**
     * delete()
     */
    delete(): void {
    //     const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
    //     // Subscribe to the dialog close event to intercept the action taken.
    //     dialogRef.afterClosed().subscribe((response) => {
    //         if (response) {
    //             this._sactionService.deleteSanctionReasonForDelay(this.selectedPaymentReceipt.id).subscribe(() => {
    //                 this.selectedPaymentReceipt = undefined;
    //                 this._sactionService.getReasonForDelays().subscribe(data => {
    //                     this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
    //                     this.dataSource.sort = this.sort;
    //                 });
    //             },
    //             (error) => {
    //                 this._matSnackBar.open('Unable to delete selected reason for delay.', 'OK', { duration: 7000 });
    //             });
    //         }
    //     });
    }
}
