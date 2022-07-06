import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-loan-appraisal-kyc-update',
  templateUrl: './loan-appraisal-kyc-update.component.html',
  styleUrls: ['./loan-appraisal-kyc-update.component.scss']
})
export class LoanAppraisalKYCUpdateComponent implements OnInit {

    dialogTitle = "KYC Document Upload";

    loanAppraisalKYCForm: FormGroup;

    selectedLoanAppraisalKYC: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(private _formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<LoanAppraisalKYCUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        this.selectedLoanAppraisalKYC = Object.assign({}, _dialogData.kycDocument);
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
        this.loanAppraisalKYCForm = this._formBuilder.group({
            documentName: [ this.selectedLoanAppraisalKYC.documentName || '' ],
            dateOfCompletion: [ this.selectedLoanAppraisalKYC.dateOfCompletion || '' ],
            remarks: [ this.selectedLoanAppraisalKYC.remarks || '' ],
            file: [''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.loanAppraisalKYCForm.valid) {

            var formValues = this.loanAppraisalKYCForm.value;
            
            var dt = new Date(formValues.dateOfCompletion);
            this.selectedLoanAppraisalKYC.dateOfCompletion = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            this.selectedLoanAppraisalKYC.remarks = formValues.remarks;

            if (this.loanAppraisalKYCForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.loanAppraisalKYCForm.get('file').value);      
                this._loanAppraisalService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.selectedLoanAppraisalKYC.fileReference = response.fileReference;
                        this._loanAppraisalService.updateKYC(this.selectedLoanAppraisalKYC).subscribe(() => {
                            this._matSnackBar.open('KYC updated successfully.', 'OK', { duration: 7000 });
                            this._dialogRef.close({ 'refresh': true });
                        });
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else
            {
                this._matSnackBar.open('Please select a file to upload.', 'OK', { duration: 7000 });
            };
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }

    /**
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.loanAppraisalKYCForm.get('file').setValue(file);
        }
    }
}
