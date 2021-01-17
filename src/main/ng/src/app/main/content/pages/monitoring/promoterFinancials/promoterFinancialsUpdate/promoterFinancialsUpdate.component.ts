import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { PromoterFinancialsModel } from 'app/main/content/model/promoterFinancials.model';

@Component({
    selector: 'fuse-promoter-financials-update-dialog',
    templateUrl: './promoterFinancialsUpdate.component.html',
    styleUrls: ['./promoterFinancialsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PromoterFinancialsUpdateDialogComponent {

    dialogTitle = 'Promoter Financials';

    selectedFinancials: PromoterFinancialsModel ;

    financialsUpdateForm: FormGroup;
  
    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<PromoterFinancialsUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedFinancials !== undefined) {
            this.selectedFinancials = _dialogData.selectedFinancials;
        }
        else {
            this.selectedFinancials = new PromoterFinancialsModel({});
        }

        this.financialsUpdateForm = _formBuilder.group({
            fiscalYear: [this.selectedFinancials.fiscalYear || ''],
            turnover: [this.selectedFinancials.turnover || 0],
            pat: [this.selectedFinancials.pat || 0],
            netWorth: [this.selectedFinancials.netWorth || 0],
            dateOfExternalRating: [this.selectedFinancials.dateOfExternalRating || ''],
            nextDueDateOfExternalRating: [this.selectedFinancials.nextDueDateOfExternalRating || ''],
            overAllRating: [this.selectedFinancials.overAllRating || ''],
            // documentContentAnnualReturn: [this.selectedFinancials.documentContentAnnualReturn || ''],
            // documentContentRating: [this.selectedFinancials.documentContentRating || '']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.financialsUpdateForm.valid) {
            var financials: PromoterFinancialsModel = new PromoterFinancialsModel(this.financialsUpdateForm.value);
            if (this._dialogData.operation === 'addFinancials') {
                this._loanMonitoringService.savePromoterFinancials(financials, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Promoter Financial details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedFinancials.fiscalYear = financials.fiscalYear;
                this.selectedFinancials.turnover = financials.turnover;
                this.selectedFinancials.pat = financials.pat;
                this.selectedFinancials.netWorth = financials.netWorth;
                this.selectedFinancials.dateOfExternalRating = financials.dateOfExternalRating;
                this.selectedFinancials.nextDueDateOfExternalRating = financials.nextDueDateOfExternalRating;
                this.selectedFinancials.overAllRating = financials.overAllRating;
                // this.selectedFinancials.documentContentAnnualReturn = financials.documentContentAnnualReturn;
                // this.selectedFinancials.documentContentRating = financials.documentContentRating;
                this._loanMonitoringService.updatePromoterFinancials(this.selectedFinancials).subscribe(() => {
                    this._matSnackBar.open('Promoter Financial details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * closeClick()
     */
    closeClick(): void {
        this._dialogRef.close({ 'refresh': false });
    }
}