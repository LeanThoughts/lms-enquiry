import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { BusinessPartnerService } from '../businessPartner.service';

@Component({
    selector: 'fuse-bank-details-update',
    templateUrl: './bankDetailsUpdate.component.html',
    styleUrls: ['./bankDetailsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BankDetailsUpdateComponent implements OnInit {

    dialogTitle = 'Add Bank Details';

    selectedBankDetails: any;

    bankDetailsUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _businessPartnerService: BusinessPartnerService,
                public _dialogRef: MatDialogRef<BankDetailsUpdateComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedBankDetails !== undefined) {
            this.selectedBankDetails = Object.assign({}, _dialogData.selectedBankDetails);
            this.dialogTitle = 'Modify Bank Details';
        }
        else {
            this.selectedBankDetails = {};
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.bankDetailsUpdateForm = this._formBuilder.group({
            serialNumber: [this.selectedBankDetails.serialNumber || null],
            bankKey: [this.selectedBankDetails.bankKey || null],
            bankName: [this.selectedBankDetails.bankName || null],
            ifscCode: [this.selectedBankDetails.ifscCode || null],
            accountNumber: [this.selectedBankDetails.accountNumber || null],
            entryDate: [this.selectedBankDetails.entryDate || null],
            validFrom: [this.selectedBankDetails.validFrom || null],
            validTo: [this.selectedBankDetails.validTo || null],
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.bankDetailsUpdateForm.valid) {
            if (this._dialogData.operation === 'addBankDetails') {
                this._businessPartnerService.createBankDetails(this.selectedBankDetails, this._dialogData.businessPartnerId).
                        subscribe(() => {
                    this._matSnackBar.open('Bank details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedBankDetails.bankKey = this.bankDetailsUpdateForm.value.bankKey;
                this.selectedBankDetails.bankName = this.bankDetailsUpdateForm.value.bankName;
                this.selectedBankDetails.ifscCode = this.bankDetailsUpdateForm.value.ifscCode;
                this.selectedBankDetails.accountNumber = this.bankDetailsUpdateForm.value.accountNumber;
                this.selectedBankDetails.entryDate = this.bankDetailsUpdateForm.value.entryDate;
                this.selectedBankDetails.validFrom = this.bankDetailsUpdateForm.value.validFrom;
                this.selectedBankDetails.validTo = this.bankDetailsUpdateForm.value.validTo;

                this._businessPartnerService.updateBankDetails(this.selectedBankDetails).subscribe(() => {
                    this._matSnackBar.open('Bank details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
