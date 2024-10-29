import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BusinessPartnerService } from '../businessPartner.service';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';
import { startWith } from 'rxjs/operators';

@Component({
    selector: 'fuse-bank-details-update',
    templateUrl: './bankDetailsUpdate.component.html',
    styleUrls: ['./bankDetailsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BusinessPartnerBankDetailsUpdateComponent implements OnInit {

    dialogTitle = 'Add Bank Details';

    selectedBankDetails: any;

    bankDetailsUpdateForm: FormGroup;

    banks: any;
    bankFilteredOptions: any;
    bankKeyFormControl = new FormControl();

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _businessPartnerService: BusinessPartnerService,
                public _dialogRef: MatDialogRef<BusinessPartnerBankDetailsUpdateComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute
            ) {

        // Fetch list of banks and other details from the dialog's data attribute.
        this.banks = this._dialogData.banks;
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
            // serialNumber: [this.selectedBankDetails.serialNumber || null],
            bankKey: [this.selectedBankDetails.bankKey || null],
            bankName: [this.selectedBankDetails.bankName || null],
            ifscCode: [this.selectedBankDetails.ifscCode || null],
            accountNumber: [this.selectedBankDetails.accountNumber || null],
            entryDate: [this.selectedBankDetails.entryDate || null],
            validFrom: [this.selectedBankDetails.validFrom || null],
            validTo: [this.selectedBankDetails.validTo || null],
        });
        
        this.bankFilteredOptions = this.bankKeyFormControl.valueChanges.pipe(
            startWith(''),
            map(value => value ? this._filterBanks(value) : this.banks.slice())
        );

    }

    /**
     * _filterBanks()
     */
    private _filterBanks(value: string): any {
        const filterValue = value.toLowerCase();
        return this.banks.filter(bank => bank.bankName.toLowerCase().indexOf(filterValue) === 0);
    }

    /**
     * validateBank()
     */
    validateBank($event) {
        const filteredBanks = this.banks.filter(bank => bank.bankKey === $event.target.value);
        console.log('filtered banks', filteredBanks);
        if (filteredBanks.length > 0) {
            this.bankDetailsUpdateForm.controls.bankKey.setValue(this.bankKeyFormControl.value);
            this.bankDetailsUpdateForm.controls.bankName.setValue(filteredBanks[0].bankName);
            // this.bankDetailsUpdateForm.controls.branch.setValue(filteredBanks[0].bankBranch || '');
            // this.bankDetailsUpdateForm.controls.address.setValue(filteredBanks[0].houseNumberAndStreet);
            this.bankDetailsUpdateForm.controls.ifscCode.setValue(filteredBanks[0].bankNumber);
        }
        else {
            this.bankDetailsUpdateForm.controls.bankKey.setValue('');
            this.bankDetailsUpdateForm.controls.bankName.setValue('');
            // this.bankDetailsUpdateForm.controls.branch.setValue('');
            // this.bankDetailsUpdateForm.controls.address.setValue('');
            this.bankDetailsUpdateForm.controls.ifscCode.setValue('');
        }
    }
    
    /**
     * submit()
     */
    submit(): void {
        if (this.bankDetailsUpdateForm.valid) {
            if (this._dialogData.operation === 'addBankDetails') {
                this._businessPartnerService.createBusinessPartnerBankDetails(this.selectedBankDetails, this._dialogData.businessPartnerId).
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

                this._businessPartnerService.updateBusinessPartnerBankDetails(this.selectedBankDetails).subscribe(() => {
                    this._matSnackBar.open('Bank details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
