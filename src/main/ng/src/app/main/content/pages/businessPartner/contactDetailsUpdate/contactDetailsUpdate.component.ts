import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { BusinessPartnerService } from '../businessPartner.service';

@Component({
    selector: 'fuse-business-partner-contact-details-update-dialog',
    templateUrl: './contactDetailsUpdate.component.html',
    styleUrls: ['./contactDetailsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BusinessPartnerContactDetailsUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Contact Details';

    selectedContactDetails: any;

    contactDetailsUpdateForm: FormGroup;

    selection = new FormControl(false);
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _businessPartnerService: BusinessPartnerService,
                public _dialogRef: MatDialogRef<BusinessPartnerContactDetailsUpdateDialogComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedContactDetails !== undefined) {
            this.selectedContactDetails = Object.assign({}, _dialogData.selectedContactDetails);
            this.dialogTitle = 'Modify Contact Details';
        }
        else {
            this.selectedContactDetails = {};
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.contactDetailsUpdateForm = this._formBuilder.group({
            serialNumber: [this.selectedContactDetails.serialNumber || null],
            selection: [this.selectedContactDetails.selection || false],
            loanNumber: [this.selectedContactDetails.loanNumber || null],
            name: [this.selectedContactDetails.name || null],
            branchAddress: [this.selectedContactDetails.branchAddress || null],
            designation: [this.selectedContactDetails.designation || null],
            department: [this.selectedContactDetails.department || null],
            telephoneNumber: [this.selectedContactDetails.telephoneNumber || null],
            landLineNumber: [this.selectedContactDetails.landLineNumber || null],
            faxNumber: [this.selectedContactDetails.faxNumber || null],
            email: [this.selectedContactDetails.email || null, [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.contactDetailsUpdateForm.valid) {
            if (this._dialogData.operation === 'addContactDetails') {
                this._businessPartnerService.createBusinessPartnerContactDetails(this.selectedContactDetails, this._dialogData.businessPartnerId).
                        subscribe(() => {
                    this._matSnackBar.open('Contact details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedContactDetails.loanNumber  = this.contactDetailsUpdateForm.value.loanNumber;
                this.selectedContactDetails.name = this.contactDetailsUpdateForm.value.name;
                this.selectedContactDetails.branchAddress = this.contactDetailsUpdateForm.value.branchAddress;
                this.selectedContactDetails.designation = this.contactDetailsUpdateForm.value.designation;
                this.selectedContactDetails.department = this.contactDetailsUpdateForm.value.department;
                this.selectedContactDetails.telephoneNumber = this.contactDetailsUpdateForm.value.telephoneNumber;
                this.selectedContactDetails.landLineNumber = this.contactDetailsUpdateForm.value.landLineNumber;
                this.selectedContactDetails.faxNumber = this.contactDetailsUpdateForm.value.faxNumber;
                this.selectedContactDetails.email = this.contactDetailsUpdateForm.value.email;

                this._businessPartnerService.updateBusinessPartnerContactDetails(this.selectedContactDetails).subscribe(() => {
                    this._matSnackBar.open('Contact details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
