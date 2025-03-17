import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-term-loan-risk-rating-update',
  templateUrl: './term-loan-risk-rating-update.component.html',
  styleUrls: ['./term-loan-risk-rating-update.component.scss']
})
export class TermLoanRiskRatingUpdateComponent {

    dialogTitle = "Add Term Loan Risk Rating";
    disableSubmitButton = false;
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
                public _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<TermLoanRiskRatingUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        this.selectedRating = Object.assign({}, _dialogData.selectedRating);

        this.ratingForm = this._formBuilder.group({
            year: [ this.selectedRating.year || '' ],
            financialRatio: [ this.selectedRating.financialRatio || '' ],
            approvalRisk: [ this.selectedRating.approvalRisk || '' ],
            offTakeRisk: [ this.selectedRating.offTakeRisk || '' ],
            fuelRisk: [ this.selectedRating.fuelRisk || '' ],
            reputationRisk: [ this.selectedRating.reputationRisk || '' ],
            financingStructure: [ this.selectedRating.financingStructure || '' ],
            sponsorSupport: [ this.selectedRating.sponsorSupport || '' ],
            securityPackage: [ this.selectedRating.securityPackage || '' ],
            constructionRisk: [ this.selectedRating.constructionRisk || '' ],
            exposure: [ this.selectedRating.exposure || '' ],
            technologyRisk: [ this.selectedRating.technologyRisk || '' ],
            overallRisk: [ this.selectedRating.overallRisk || '' ]
        });

        if (this._dialogData.operation === 'modifyRating') {
            this.dialogTitle = 'Modify Term Loan Risk Rating';
            // this.selectedRating = Object.assign({}, _dialogData.selectedRating);
        }
        else if (this._dialogData.operation === 'viewRating') {
            this.dialogTitle = 'View Term Loan Risk Rating';
            this.disableSubmitButton = true;
            this.ratingForm.disable();
        }
    }

    /**
     * submit()
     */
    submit(): void {
        console.log('checking if valid');
        if (this.ratingForm.valid) {
            this.disableSubmitButton = true;
            console.log('is valid');
            var formData = this.ratingForm.value;
            this.selectedRating.year = formData.year;
            this.selectedRating.financialRatio = formData.financialRatio;
            this.selectedRating.approvalRisk = formData.approvalRisk;
            this.selectedRating.offTakeRisk = formData.offTakeRisk;
            this.selectedRating.fuelRisk = formData.fuelRisk;
            this.selectedRating.reputationRisk = formData.reputationRisk;
            this.selectedRating.financingStructure = formData.financingStructure;
            this.selectedRating.sponsorSupport = formData.sponsorSupport;
            this.selectedRating.securityPackage = formData.securityPackage;
            this.selectedRating.constructionRisk = formData.constructionRisk;
            this.selectedRating.exposure = formData.exposure;
            this.selectedRating.technologyRisk = formData.technologyRisk;
            this.selectedRating.overallRisk = formData.overallRisk;
            if (this._dialogData.operation === 'modifyRating') {
                console.log('modifyRating');
                this._loanAppraisalService.updateTermLoanRiskRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('Term loan risk rating updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                    this.disableSubmitButton = false;
                });
            }
            else {
                console.log('createRating');
                this.selectedRating.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createTermLoanRiskRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('Term loan risk rating created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                    this.disableSubmitButton = false;
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

    /**
     * validateFiscalYear()
     */
    validateFiscalYear(): void {
        var year = this.ratingForm.get('year').value;
        if (year.length === 2) {
            year = '20' + year;
        }
        this.ratingForm.controls.year.setValue(year);
    }
}
