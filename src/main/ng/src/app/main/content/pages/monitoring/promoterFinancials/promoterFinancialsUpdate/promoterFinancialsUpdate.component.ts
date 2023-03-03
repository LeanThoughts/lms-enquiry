import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { PromoterFinancialsModel } from 'app/main/content/model/promoterFinancials.model';
import { Observable } from 'rxjs';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-promoter-financials-update-dialog',
    templateUrl: './promoterFinancialsUpdate.component.html',
    styleUrls: ['./promoterFinancialsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PromoterFinancialsUpdateDialogComponent {

    dialogTitle = 'Add New Promoter Financials';

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
            this.selectedFinancials = Object.assign({}, _dialogData.selectedFinancials);
            this.dialogTitle = 'Modify Promoter Financials';
        }
        else {
            this.selectedFinancials = new PromoterFinancialsModel({});
        }

        this.financialsUpdateForm = _formBuilder.group({
            fiscalYear: [this.selectedFinancials.fiscalYear || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            turnover: [this.selectedFinancials.turnover || 0, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            pat: [this.selectedFinancials.pat || 0, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            netWorth: [this.selectedFinancials.netWorth || 0, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            dateOfExternalRating: [this.selectedFinancials.dateOfExternalRating || ''],
            nextDueDateOfExternalRating: [this.selectedFinancials.nextDueDateOfExternalRating || ''],
            overAllRating: [this.selectedFinancials.overAllRating || ''],
            annualReturnFile: [''],
            ratingFile: [''],
            remarks: [this.selectedFinancials.remarks || '']
        });
    }

    /**
     * onAnnualReturnDocument()
     * @param event 
     */
    onAnnualReturnDocument(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.financialsUpdateForm.get('annualReturnFile').setValue(file);
        }
    }
    
    /**
     * onRatingDocument()
     * @param event 
     */
    onRatingDocument(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.financialsUpdateForm.get('ratingFile').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.financialsUpdateForm.valid) {
            var financials: PromoterFinancialsModel = new PromoterFinancialsModel(this.financialsUpdateForm.value);
            if (this._dialogData.operation === 'addFinancials') {
                const uploads = Observable.forkJoin(
                    this._loanMonitoringService.uploadDocument(this.financialsUpdateForm.get('annualReturnFile').value),
                    this._loanMonitoringService.uploadDocument(this.financialsUpdateForm.get('ratingFile').value)
                );
                uploads.subscribe(([res1, res2]) => {
                    financials.annualReturnFileReference = res1.fileReference;
                    financials.ratingFileReference = res2.fileReference;
                    this._loanMonitoringService.savePromoterFinancials(financials, this._dialogData.loanApplicationId).subscribe(() => {
                        this._matSnackBar.open('Promoter Financial details added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                const uploads = Observable.forkJoin(
                    this._loanMonitoringService.uploadDocument(this.financialsUpdateForm.get('annualReturnFile').value),
                    this._loanMonitoringService.uploadDocument(this.financialsUpdateForm.get('ratingFile').value)
                );
                uploads.subscribe(([res1, res2]) => {
                    if (res1.fileReference !== '') {
                        this.selectedFinancials.annualReturnFileReference = res1.fileReference;
                    }
                    if (res2.fileReference !== '') {
                        this.selectedFinancials.ratingFileReference = res2.fileReference;
                    }
                    this.selectedFinancials.fiscalYear = financials.fiscalYear;
                    this.selectedFinancials.turnover = financials.turnover;
                    this.selectedFinancials.pat = financials.pat;
                    this.selectedFinancials.netWorth = financials.netWorth;
                    this.selectedFinancials.dateOfExternalRating = financials.dateOfExternalRating;
                    this.selectedFinancials.nextDueDateOfExternalRating = financials.nextDueDateOfExternalRating;
                    this.selectedFinancials.overAllRating = financials.overAllRating;
                    this.selectedFinancials.remarks = financials.remarks;
                    this._loanMonitoringService.updatePromoterFinancials(this.selectedFinancials).subscribe(() => {
                        this._matSnackBar.open('Promoter Financial details updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
        }
    }
}
