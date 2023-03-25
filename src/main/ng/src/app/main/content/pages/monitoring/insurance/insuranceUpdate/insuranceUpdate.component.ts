import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-insurance-update-dialog',
    templateUrl: './insuranceUpdate.component.html',
    styleUrls: ['./insuranceUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class InsuranceUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Insurance Details';

    selectedInsurance: any;

    insuranceUpdateForm: FormGroup;

    allowUpdates = true;

    documentTypes = LoanMonitoringConstants.documentTypes;
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<InsuranceUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedInsurance = Object.assign({}, _dialogData.selectedInsurance);

        if (_dialogData.selectedInsurance !== undefined) {
            if (_dialogData.operation === 'updateInsurance') {
                this.dialogTitle = 'Modify Insurance Details';
            }
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.insuranceUpdateForm = this._formBuilder.group({
            validFrom: [this.selectedInsurance.validFrom || ''],
            validTo: [this.selectedInsurance.validTo || ''],
            documentType: [this.selectedInsurance.documentType || ''],
            documentTitle: [this.selectedInsurance.documentTitle || ''],
            file: ['']
        }); 

        console.log('testing in onInit()');
        console.log(this.selectedInsurance);
        console.log(this.selectedInsurance.fileReference);
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.insuranceUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.insuranceUpdateForm.valid) {
            if (this.insuranceUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.insuranceUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveInsuranceDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addInsurance') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveInsuranceDetails('');
                }
            }
        }
    }

    saveInsuranceDetails(fileReference: string) {
        if (this.insuranceUpdateForm.valid) {

            // To solve the utc time zone issue
            var insurance = this.insuranceUpdateForm.value;
            var dt = new Date(insurance.validFrom);
            insurance.validFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(insurance.validTo);
            insurance.validTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addInsurance') {
                insurance.fileReference = fileReference;
                this._loanMonitoringService.saveInsurance(insurance, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Insurance details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedInsurance.fileReference = fileReference;
                }
                this.selectedInsurance.validFrom  = insurance.validFrom;
                this.selectedInsurance.validTo = insurance.validTo;
                this.selectedInsurance.documentTitle = insurance.documentTitle;
                this.selectedInsurance.documentType = insurance.documentType;
                this._loanMonitoringService.updateInsurance(this.selectedInsurance).subscribe(() => {
                    this._matSnackBar.open('Insurance details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
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
