import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SanctionService } from '../sanction.service';

@Component({
    selector: 'fuse-payment-receipt-post-sanction-update-dialog',
    templateUrl: './paymentReceiptPostSanctionUpdate.component.html',
    styleUrls: ['./paymentReceiptPostSanctionUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PaymentReceiptPostSanctionUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Payment Receipt - Pre Sanction';

    loanApplicationId = '';
    selectedPaymentReceipt: any;

    paymentReceiptForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _sanctionService: SanctionService,
        public _dialogRef: MatDialogRef<PaymentReceiptPostSanctionUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedPaymentReceipt = Object.assign({}, _dialogData.selectedPaymentReceipt);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedPaymentReceipt !== undefined) {
            if (_dialogData.operation === 'updatePaymentReceipt') {
                this.dialogTitle = 'Modify Payment Receipt - Pre Sanction';
            }
        }
        this.paymentReceiptForm = this._formBuilder.group({
            proformaInvoiceNumber: [this.selectedPaymentReceipt.proformaInvoiceNumber || ''],
            proformaInvoiceDate: [this.selectedPaymentReceipt.proformaInvoiceDate || ''],
            feeInvoice: [this.selectedPaymentReceipt.feeInvoice || ''],
            amount: [this.selectedPaymentReceipt.amount || ''],
            payee: [this.selectedPaymentReceipt.payee || ''],
            amountReceived: [this.selectedPaymentReceipt.amountReceived || ''],
            dateOfTransfer: [this.selectedPaymentReceipt.dateOfTransfer || ''],
            rtgsNeftNumber: [this.selectedPaymentReceipt.rtgsNeftNumber || ''],
            referenceNumber: [this.selectedPaymentReceipt.referenceNumber || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        // if (this.paymentReceiptForm.valid) {
        //     var paymentReceipt = this.paymentReceiptForm.value;
                
        //     // To solve the utc time zone issue
        //     var dt = new Date(paymentReceipt.date);
        //     paymentReceipt.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        //     if (this._dialogData.operation === 'addReason') {
        //         paymentReceipt.loanApplicationId = this.loanApplicationId;
        //         this._sanctionService.createSanctionReasonForDelay(paymentReceipt).subscribe(() => {
        //             this._matSnackBar.open('Reason for delay added successfully.', 'OK', { duration: 7000 });
        //             this._dialogRef.close({ 'refresh': true });
        //         });
        //     }
        //     else {
        //         this.selectedPaymentReceipt.reason = paymentReceipt.reason;
        //         this.selectedPaymentReceipt.date = paymentReceipt.date;
        //         this._sanctionService.updateSanctionReasonForDelay(this.selectedPaymentReceipt).subscribe(() => {
        //             this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
        //             this._dialogRef.close({ 'refresh': true });
        //         });            
        //     }
        // }
    }
}
