import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';

@Component({
    selector: 'fuse-inception-fee-receipt-update-dialog',
    templateUrl: './inceptionFeeReceiptUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class InceptionFeeReceiptUpdateDialogComponent implements OnInit {

    dialogTitle = 'New Inception Fee';

    loanApplicationId = '';
    selectedInceptionFee: any;

    inceptionFeeForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        public _dialogRef: MatDialogRef<InceptionFeeReceiptUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedInceptionFee = Object.assign({}, _dialogData.selectedInceptionFee);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedInceptionFee !== undefined) {
            if (_dialogData.operation === 'updateInceptionFee') {
                this.dialogTitle = 'Modify Inception Fee';
            }
        }

        this.inceptionFeeForm = this._formBuilder.group({
            invoiceNumber: [this.selectedInceptionFee.invoiceNumber || ''],
            invoiceDate: [this.selectedInceptionFee.invoiceDate || ''],
            amount: [this.selectedInceptionFee.amount || ''],
            taxAmount: [this.selectedInceptionFee.taxAmount || ''],
            totalAmount: [this.selectedInceptionFee.totalAmount || ''],
            amountReceived: [this.selectedInceptionFee.amountReceived || ''],
            dateOfTransfer: [this.selectedInceptionFee.dateOfTransfer || ''],
            rtgsNumber: [this.selectedInceptionFee.rtgsNumber || ''],
            remarks: [this.selectedInceptionFee.remarks || ''],
            referenceNumber: [this.selectedInceptionFee.referenceNumber || '']
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
        if (this.inceptionFeeForm.valid) {
            var inceptionFee = this.inceptionFeeForm.value;
                
            // To solve the utc time zone issue
            // var dt = new Date(inceptionFee.invoiceDate);
            // inceptionFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addInceptionFee') {
                inceptionFee.loanApplicationId = this.loanApplicationId;
                this._applicationFeeService.createInceptionFee(inceptionFee).subscribe(() => {
                    this._matSnackBar.open('Inception fee details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedInceptionFee.rtgsNumber = inceptionFee.rtgsNumber;
                this.selectedInceptionFee.referenceNumber = inceptionFee.referenceNumber;
                this.selectedInceptionFee.remarks = inceptionFee.remarks;
                this._applicationFeeService.updateInceptionFee(this.selectedInceptionFee).subscribe(() => {
                    this._matSnackBar.open('Inception fee details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
