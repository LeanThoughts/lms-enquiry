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

    dialogTitle = "Add Loan Appraisal KYC";

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
        console.log('_dialogData', _dialogData);
        this.selectedLoanAppraisalKYC = Object.assign({}, _dialogData.loanAppraisalKYC);

        // Change diglog title and fetch partners based on the role assigned ...
        if (this._dialogData.operation === 'modifyOfficer') {
            this.dialogTitle = 'Modify Loan Appraisal KYC';
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
        this.loanAppraisalKYCForm = this._formBuilder.group({
            serialNumber: [ this.selectedLoanAppraisalKYC.serialNumber || '' ],
            partnerType: [ this.selectedLoanAppraisalKYC.partnerType || '' ],
            kycType: [ this.selectedLoanAppraisalKYC.kycType || '' ],
            status: [ this.selectedLoanAppraisalKYC.status || '' ],
            dateOfCompletion: [ this.selectedLoanAppraisalKYC.dateOfCompletion || '' ],
            remarks: [ this.selectedLoanAppraisalKYC.remarks || '' ],
            file: [''],
            documentName: [ this.selectedLoanAppraisalKYC.documentName || '' ]
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
            this.selectedLoanAppraisalKYC.documentName = formValues.documentName;
            if (this._dialogData.operation === 'modifyAppraisalKYC') {
                if (this.loanAppraisalKYCForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.loanAppraisalKYCForm.get('file').value);      
                    this._loanAppraisalService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedLoanAppraisalKYC.fileReference = response.fileReference;
                            this._loanAppraisalService.updateLoanAppraisalKYC(this.selectedLoanAppraisalKYC).subscribe(() => {
                                this._matSnackBar.open('Loan Appraisal KYC updated successfully.', 'OK', { duration: 7000 });
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
                    this._loanAppraisalService.updateLoanAppraisalKYC(this.selectedLoanAppraisalKYC).subscribe(() => {
                        this._matSnackBar.open('Loan Appraisal KYC updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
            else {
                this.selectedLoanAppraisalKYC.loanApplicationId = this._dialogData.loanApplicationId;
                if (this.loanAppraisalKYCForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.loanAppraisalKYCForm.get('file').value);      
                    this._loanAppraisalService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedLoanAppraisalKYC.fileReference = response.fileReference;
                            this._loanAppraisalService.createLoanAppraisalKYC(this.selectedLoanAppraisalKYC).subscribe(() => {
                                this._matSnackBar.open('Loan Appraisal KYC added successfully.', 'OK', { duration: 7000 });
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
                    this._loanAppraisalService.createLoanAppraisalKYC(this.selectedLoanAppraisalKYC).subscribe(() => {
                        this._matSnackBar.open('Loan Appraisal KYC added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
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
