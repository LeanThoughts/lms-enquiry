import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SanctionService } from '../sanction.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-payment-receipt-pre-sanction-update-dialog',
    templateUrl: './paymentReceiptPreSanctionUpdate.component.html',
    styleUrls: ['./paymentReceiptPreSanctionUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PaymentReceiptPreSanctionUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Payment Receipt - Pre Sanction';

    loanApplicationId = '';
    selectedPaymentReceipt: any;

    paymentReceiptForm: FormGroup;

    feeTypes: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _sanctionService: SanctionService,
        public _dialogRef: MatDialogRef<PaymentReceiptPreSanctionUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedPaymentReceipt = Object.assign({}, _dialogData.selectedPaymentReceipt);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedPaymentReceipt !== undefined) {
            if (_dialogData.operation === 'updatePaymentReceipt') {
                this.dialogTitle = 'Modify Payment Receipt - Pre Sanction';
            }
        }

        this._sanctionService.getFeeTypes().subscribe(data => {
            this.feeTypes = data._embedded.feeTypes;
        });

        this.paymentReceiptForm = this._formBuilder.group({
            proformaInvoiceNumber: [this.selectedPaymentReceipt.proformaInvoiceNumber || ''],
            proformaInvoiceDate: [this.selectedPaymentReceipt.proformaInvoiceDate || ''],
            feeType: [this.selectedPaymentReceipt.feeType || ''],
            amount: [this.selectedPaymentReceipt.amount || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
            payee: [this.selectedPaymentReceipt.payee || ''],
            amountReceived: [this.selectedPaymentReceipt.amountReceived || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
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
        if (this.paymentReceiptForm.valid) {
            var paymentReceipt = this.paymentReceiptForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(paymentReceipt.proformaInvoiceDate);
            paymentReceipt.proformaInvoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(paymentReceipt.dateOfTransfer);
            paymentReceipt.dateOfTransfer = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addPaymentReceipt') {
                paymentReceipt.loanApplicationId = this.loanApplicationId;
                this._sanctionService.createPaymentReceipt(paymentReceipt, 'pre').subscribe(() => {
                    this._matSnackBar.open('Payment receipt added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedPaymentReceipt.proformaInvoiceNumber = paymentReceipt.proformaInvoiceNumber;
                this.selectedPaymentReceipt.proformaInvoiceDate = paymentReceipt.proformaInvoiceDate;
                this.selectedPaymentReceipt.feeType = paymentReceipt.feeType;
                this.selectedPaymentReceipt.amount = paymentReceipt.amount;
                this.selectedPaymentReceipt.payee = paymentReceipt.payee;
                this.selectedPaymentReceipt.amountReceived = paymentReceipt.amountReceived;
                this.selectedPaymentReceipt.dateOfTransfer = paymentReceipt.dateOfTransfer;
                this.selectedPaymentReceipt.rtgsNeftNumber = paymentReceipt.rtgsNeftNumber;
                this.selectedPaymentReceipt.referenceNumber = paymentReceipt.referenceNumber;
                this._sanctionService.updatePaymentReceipt(this.selectedPaymentReceipt, 'pre').subscribe(() => {
                    this._matSnackBar.open('Payment receipt updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
