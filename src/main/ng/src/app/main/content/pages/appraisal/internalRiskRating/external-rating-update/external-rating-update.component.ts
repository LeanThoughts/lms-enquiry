import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-external-rating-update',
  templateUrl: './external-rating-update.component.html',
  styleUrls: ['./external-rating-update.component.scss']
})
export class ExternalRatingUpdateComponent {

    dialogTitle = "Add External Rating";

    selectedRating: any;

    ratings: any;
    ratingForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ExternalRatingUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        this.selectedRating = Object.assign({}, _dialogData.selectedRating);

        if (this._dialogData.operation === 'modifyRating') {
            this.dialogTitle = 'Modify External Rating';
        }

        this._loanAppraisalService.getRatings().subscribe(response => {
            this.ratings = response._embedded.ratings;
        });

        this.ratingForm = this._formBuilder.group({
            serialNumber: [ this.selectedRating.serialNumber || '' ],
            validityDate: [ this.selectedRating.validityDate || '' ],
            rating: [ this.selectedRating.rating || '' ],
            ratingAgency: [ this.selectedRating.ratingAgency || '' ],
            creditStanding: [ this.selectedRating.creditStanding || '' ],
            creditStandingInstruction: [ this.selectedRating.creditStandingInstruction || '' ],
            creditStandingText: [ this.selectedRating.creditStandingText || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.ratingForm.valid) {
            var formData = this.ratingForm.value;
            
            var dt = new Date(formData.validityDate);
            formData.validityDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
    
            this.selectedRating.rating = formData.rating;
            this.selectedRating.ratingAgency = formData.ratingAgency;
            this.selectedRating.creditStanding = formData.creditStanding;
            this.selectedRating.creditStandingInstruction = formData.creditStandingInstruction;
            this.selectedRating.creditStandingText = formData.creditStandingText;
            this.selectedRating.validityDate = formData.validityDate;
            
            if (this._dialogData.operation === 'modifyRating') {
                console.log('modifyRating');
                this._loanAppraisalService.updateExternalRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('External rating updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('createRating');
                this.selectedRating.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createExternalRating(this.selectedRating).subscribe(response => {
                    this._matSnackBar.open('External rating created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            } 
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(refresh: boolean): void {
        this._dialogRef.close({ 'refresh': refresh });
    }
}
