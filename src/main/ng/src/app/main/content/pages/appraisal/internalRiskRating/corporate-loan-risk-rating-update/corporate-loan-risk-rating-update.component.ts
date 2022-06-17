import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-corporate-loan-risk-rating-update',
  templateUrl: './corporate-loan-risk-rating-update.component.html',
  styleUrls: ['./corporate-loan-risk-rating-update.component.scss']
})
export class CorporateLoanRiskRatingUpdateComponent {

    dialogTitle = "Add Corporate Loan Risk Rating";

    selectedRating: any;

    ratingForm: FormGroup;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(private _formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<CorporateLoanRiskRatingUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        this.selectedRating = Object.assign({}, _dialogData.selectedRating);

        if (this._dialogData.operation === 'modifyRating') {
            this.dialogTitle = 'Modify Corporate Loan Risk Rating';
            // this.selectedRating = Object.assign({}, _dialogData.selectedRating);
        }

        this.ratingForm = this._formBuilder.group({
            year: [ this.selectedRating.year || '' ],
            financialRatio: [ this.selectedRating.financialRatio || '' ],
            purposeOfLoan: [ this.selectedRating.purposeOfLoan || '' ],
            financingStructure: [ this.selectedRating.financingStructure || '' ],
            repaymentCapability: [ this.selectedRating.repaymentCapability || '' ],
            corporateGovernancePractice: [ this.selectedRating.corporateGovernancePractice || '' ],
            conductOfLoan: [ this.selectedRating.conductOfLoan || '' ],
            deviationWithOperationalPolicy: [ this.selectedRating.deviationWithOperationalPolicy || '' ],
            exposure: [ this.selectedRating.exposure || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        console.log('checking if valid');
        if (this.ratingForm.valid) {
            console.log('is valid');
            var formData = this.ratingForm.value;
            this.selectedRating.year = formData.year;
            this.selectedRating.financialRatio = formData.financialRatio;
            this.selectedRating.purposeOfLoan = formData.purposeOfLoan;
            this.selectedRating.financingStructure = formData.financingStructure;
            this.selectedRating.repaymentCapability = formData.repaymentCapability;
            this.selectedRating.corporateGovernancePractice = formData.corporateGovernancePractice;
            this.selectedRating.conductOfLoan = formData.conductOfLoan;
            this.selectedRating.deviationWithOperationalPolicy = formData.deviationWithOperationalPolicy;
            this.selectedRating.exposure = formData.exposure;
            if (this._dialogData.operation === 'modifyRating') {
                console.log('modifyRating');
                this._loanAppraisalService.updateCorporateLoanRiskRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('Corporate loan risk rating updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('createRating');
                this.selectedRating.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createCorporateLoanRiskRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('Corporate loan risk rating created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            } 
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }
}
