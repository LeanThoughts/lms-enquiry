import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar, MatSelectChange } from '@angular/material';
import { BusinessPartnerService } from '../businessPartner.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';

@Component({
    selector: 'fuse-business-partner-identification-update-dialog',
    templateUrl: './identificationUpdate.component.html',
    styleUrls: ['./identificationUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BusinessPartnerIdentificationUpdateComponent implements OnInit {

    dialogTitle = 'Add Identification Details';

    documentTypes: any[] = [];

    selectedIdentificationDetails: any;

    identificationCategories: any;
    identificationDetailsUpdateForm: FormGroup;

    countries: any;
    regions: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                private _businessPartnerService: BusinessPartnerService,
                public _dialogRef: MatDialogRef<BusinessPartnerIdentificationUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar,
                private _loanEnquiryService: LoanEnquiryService) {

        // Fetch list of identification and other details from the dialog's data attribute.
        this.documentTypes = this._loanEnquiryService.documentTypes;
        this.identificationCategories = this._dialogData.identificationCategories;
        if (_dialogData.selectedIdentificationDetails !== undefined) {
            this.selectedIdentificationDetails = Object.assign({}, _dialogData.selectedIdentificationDetails);
            this.dialogTitle = 'Modify Identification Details';
        }
        else {
            this.selectedIdentificationDetails = {};
        }

        this._businessPartnerService.getCountries().subscribe(response => {
            this.countries = response._embedded.countries;
        });

        this._businessPartnerService.getRegions(this.selectedIdentificationDetails.country).subscribe(response => { 
            this.regions = response._embedded.regions;
        });
    }

    onCountrySelect(event) {
        this._businessPartnerService.getRegions(event.value).subscribe(response => {
            this.regions = response._embedded.regions;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.identificationDetailsUpdateForm = this._formBuilder.group({
            serialNumber: [this.selectedIdentificationDetails.serialNumber || null],
            identificationCategoryCode: [this.selectedIdentificationDetails.identificationCategoryCode || null],
            identificationNumber: [this.selectedIdentificationDetails.identificationNumber || null],
            idInstitute: [this.selectedIdentificationDetails.idInstitute || null],
            idEntryDate: [this.selectedIdentificationDetails.idEntryDate || null],
            idValidFromDate: [this.selectedIdentificationDetails.idValidFromDate || null],
            idValidToDate: [this.selectedIdentificationDetails.idValidToDate || null],
            documentName: [this.selectedIdentificationDetails.documentName || null],
            documentType: [this.selectedIdentificationDetails.documentType || null],
            file: [''],
            country: [this.selectedIdentificationDetails.country || null],
            region: [this.selectedIdentificationDetails.region || null]
        });
    }

    /**
     * onIdentificationCategorySelect()
     */
    onIdentificationCategorySelect(event) {
        console.log(event);
        const identificationCategory = this.identificationCategories.find(category => category.code === event.value);
        console.debug(identificationCategory);
        if (identificationCategory.code === 'Z00002' && identificationCategory.panNumberValidation) {
            this.identificationDetailsUpdateForm.get('identificationNumber').setValidators([Validators.pattern(EnquiryApplicationRegEx.pan)]);
        }
        else {
            this.identificationDetailsUpdateForm.get('identificationNumber').clearValidators();
        }
        this.identificationDetailsUpdateForm.get('identificationNumber').updateValueAndValidity();
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.identificationDetailsUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.identificationDetailsUpdateForm.valid) {
            if (this.identificationDetailsUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.identificationDetailsUpdateForm.get('file').value);
                this._businessPartnerService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveIdentificationDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addIdentificationDetails') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveIdentificationDetails('');
                }
            }
        }
    }

    /**
     * saveIdentificationDetails()
     */
    saveIdentificationDetails(fileReference: string) {
        if (this.identificationDetailsUpdateForm.valid) {
            // solve the utc time zone issue
            var identificationDetails = this.identificationDetailsUpdateForm.value;

            const convertToUTCDate = (date) => {
                if (!date) return null;
                const dt = new Date(date);
                return new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            };
            identificationDetails.idEntryDate = convertToUTCDate(identificationDetails.idEntryDate);
            identificationDetails.idValidFromDate = convertToUTCDate(identificationDetails.idValidFromDate);
            identificationDetails.idValidToDate = convertToUTCDate(identificationDetails.idValidToDate);
            console.log(this._dialogData.operation);

            if (this._dialogData.operation === 'addIdentificationDetails') {
                identificationDetails.fileReference = fileReference;
                this._businessPartnerService.createBusinessPartnerIdentificationDetails(identificationDetails, this._dialogData.businessPartnerId).
                        subscribe(() => {
                    this._matSnackBar.open('Identification details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                }, error => {
                    this._matSnackBar.open(error.error.message, 'Close', {duration: 7000});
                });
            }
            else {
                this.selectedIdentificationDetails.identificationCategoryCode = identificationDetails.identificationCategoryCode;
                this.selectedIdentificationDetails.identificationNumber = identificationDetails.identificationNumber;
                this.selectedIdentificationDetails.idInstitute = identificationDetails.idInstitute;
                this.selectedIdentificationDetails.idEntryDate = identificationDetails.idEntryDate;
                this.selectedIdentificationDetails.idValidFromDate = identificationDetails.idValidFromDate;
                this.selectedIdentificationDetails.idValidToDate = identificationDetails.idValidToDate;
                this.selectedIdentificationDetails.documentName = identificationDetails.documentName;
                this.selectedIdentificationDetails.documentType = identificationDetails.documentType;
                this.selectedIdentificationDetails.country = identificationDetails.country;
                this.selectedIdentificationDetails.region = identificationDetails.region;
                this._businessPartnerService.updateBusinessPartnerIdentificationDetails(this.selectedIdentificationDetails).subscribe(() => {
                    this._matSnackBar.open('Identification details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                }, error => {
                    this._matSnackBar.open(error.error.message, 'Close', {duration: 7000});
                });
            }
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
