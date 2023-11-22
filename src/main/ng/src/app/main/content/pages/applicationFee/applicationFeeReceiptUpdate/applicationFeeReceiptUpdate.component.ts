import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../../iccApproval/iccApproval.service';

@Component({
    selector: 'fuse-application-fee-receipt-update-dialog',
    templateUrl: './applicationFeeReceiptUpdate.component.html',
    styleUrls: ['./applicationFeeReceiptUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ApplicationFeeReceiptUpdateDialogComponent implements OnInit {

    dialogTitle = 'New Application Fee';

    loanApplicationId = '';
    selectedApplicationFee: any;

    applicationFeeForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ApplicationFeeReceiptUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedApplicationFee = Object.assign({}, _dialogData.selectedFormalRequest);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedFormalRequest !== undefined) {
            if (_dialogData.operation === 'updateAplicationFee') {
                this.dialogTitle = 'Modify Application Fee';
            }
        }

        this.applicationFeeForm = this._formBuilder.group({
            invoiceNumber: [this.selectedApplicationFee.invoiceNumber || ''],
            invoiceDate: [this.selectedApplicationFee.invoiceDate || ''],
            amount: [this.selectedApplicationFee.amount || ''],
            taxAmount: [this.selectedApplicationFee.taxAmount || ''],
            totalAmount: [this.selectedApplicationFee.totalAmount || ''],
            amountReceived: [this.selectedApplicationFee.amountReceived || ''],
            dateOfTransfer: [this.selectedApplicationFee.dateOfTransfer || ''],
            rtgsNumber: [this.selectedApplicationFee.rtgsNumber || ''],
            remarks: [this.selectedApplicationFee.remarks || ''],
            referenceNumber: [this.selectedApplicationFee.referenceNumber || '']
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
        if (this.applicationFeeForm.valid) {
            var applicationFee = this.applicationFeeForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(applicationFee.invoiceDate);
            applicationFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addAplicationFee') {
                applicationFee.loanApplicationId = this.loanApplicationId;
                // this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc added successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });
            }
            else {
                // this.selectedApplicationFee.uploadDate = applicationFee.uploadDate;
                // this.selectedApplicationFee.documentLetterDate = applicationFee.documentLetterDate;
                // this.selectedApplicationFee.documentReceivedDate = applicationFee.documentReceivedDate;
                // this.selectedApplicationFee.documentName = applicationFee.documentName;
                // this.selectedApplicationFee.document = applicationFee.document;
                // this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc updated successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });            
            }
        }
    }
}
