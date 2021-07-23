import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar, MatCheckboxChange } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { RateOfInterestModel } from 'app/main/content/model/rateOfInterest.model';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-rate-of-interest-update-dialog',
    templateUrl: './rateOfInterestUpdate.component.html',
    styleUrls: ['./rateOfInterestUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class RateOfInterestUpdateDialogComponent {

    dialogTitle = 'Add New Rate Of Interest';

    selectedRateOfInterest: RateOfInterestModel ;

    rateOfInterestDisplayForm: FormGroup;
    rateOfInterestUpdateForm: FormGroup;
  
    conditionTypes: any;
    referenceInterestRates: any;
    referenceInterestRateSigns: any;
    paymentForms: any;
    interestCalculationMethods: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<RateOfInterestUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        _loanMonitoringService.getConditionTypes().subscribe(data => {
            this.conditionTypes = data;
        });

        _loanMonitoringService.getReferenceInterestRates().subscribe(data => {
            this.referenceInterestRates = data;
        });

        _loanMonitoringService.getReferenceInterestRateSigns().subscribe(data => {
            this.referenceInterestRateSigns = data;
        });

        _loanMonitoringService.getPaymentForms().subscribe(data => {
            this.paymentForms = data;
        });

        _loanMonitoringService.getInterestCalculationMethods().subscribe(data => {
            this.interestCalculationMethods = data;
        });

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedRateOfInterest !== undefined) {
            this.selectedRateOfInterest = Object.assign({}, _dialogData.selectedRateOfInterest);
            this.dialogTitle = 'Modify Rate Of Interest';
        }
        else {
            this.selectedRateOfInterest = new RateOfInterestModel({});
        }

        this.rateOfInterestDisplayForm = _formBuilder.group({
            sanctionPreCod: [''],
            sanctionPostCod: [''],
            presentRoi: [''],
        });

        this.rateOfInterestUpdateForm = _formBuilder.group({
            conditionType: [this.selectedRateOfInterest.conditionType],
            validFromDate: [this.selectedRateOfInterest.validFromDate],
            interestTypeIndicator: [this.selectedRateOfInterest.interestTypeIndicator],
            referenceInterestRate: [this.selectedRateOfInterest.referenceInterestRate],
            refInterestSign: [this.selectedRateOfInterest.refInterestSign],
            interestRate: [this.selectedRateOfInterest.interestRate, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            calculationDate: [this.selectedRateOfInterest.calculationDate],
            isCalculationDateOnMonthEnd: [this.selectedRateOfInterest.isCalculationDateOnMonthEnd],
            dueDate: [this.selectedRateOfInterest.dueDate],
            isDueDateOnMonthEnd: [this.selectedRateOfInterest.isDueDateOnMonthEnd],
            interestPaymentFrequency: [this.selectedRateOfInterest.interestPaymentFrequency, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            paymentForm: [this.selectedRateOfInterest.paymentForm],
            interestCalculationMethod: [this.selectedRateOfInterest.interestCalculationMethod]
        });

        this.rateOfInterestUpdateForm.controls.interestRate.value
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.rateOfInterestUpdateForm.valid) {
            var rateOfInterest: RateOfInterestModel = new RateOfInterestModel(this.rateOfInterestUpdateForm.value);

            const dt1 = new Date(rateOfInterest.validFromDate);
            rateOfInterest.validFromDate = new Date(Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate()));
            rateOfInterest.validFromDate.setDate(rateOfInterest.validFromDate.getDate() + 1);

            const dt2 = new Date(rateOfInterest.calculationDate);
            rateOfInterest.calculationDate = new Date(Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()));
            rateOfInterest.calculationDate.setDate(rateOfInterest.calculationDate.getDate() + 1);

            const dt3 = new Date(rateOfInterest.dueDate);
            rateOfInterest.dueDate = new Date(Date.UTC(dt3.getFullYear(), dt3.getMonth(), dt3.getDate()));
            rateOfInterest.dueDate.setDate(rateOfInterest.dueDate.getDate() + 1);

            if (this._dialogData.operation === 'addRateOfInterest') {
                this._loanMonitoringService.saveRateOfInterest(rateOfInterest, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Rate Of Interest details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedRateOfInterest.conditionType = rateOfInterest.conditionType;
                this.selectedRateOfInterest.validFromDate = rateOfInterest.validFromDate;
                this.selectedRateOfInterest.interestTypeIndicator = rateOfInterest.interestTypeIndicator;
                this.selectedRateOfInterest.referenceInterestRate = rateOfInterest.referenceInterestRate;
                this.selectedRateOfInterest.refInterestSign = rateOfInterest.refInterestSign;
                this.selectedRateOfInterest.interestRate = rateOfInterest.interestRate;
                this.selectedRateOfInterest.calculationDate = rateOfInterest.calculationDate;
                this.selectedRateOfInterest.isCalculationDateOnMonthEnd = rateOfInterest.isCalculationDateOnMonthEnd;
                this.selectedRateOfInterest.dueDate = rateOfInterest.dueDate;
                this.selectedRateOfInterest.isDueDateOnMonthEnd = rateOfInterest.isDueDateOnMonthEnd;
                this.selectedRateOfInterest.interestPaymentFrequency = rateOfInterest.interestPaymentFrequency;
                this.selectedRateOfInterest.paymentForm = rateOfInterest.paymentForm;
                this.selectedRateOfInterest.interestCalculationMethod = rateOfInterest.interestCalculationMethod;
                this._loanMonitoringService.updateRateOfInterest(this.selectedRateOfInterest).subscribe(() => {
                    this._matSnackBar.open('Rate Of Interest details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * setCalculationDate()
     */
    setCalculationDate(event: MatCheckboxChange): void {
        const validFromDate = new Date(this.rateOfInterestUpdateForm.controls.validFromDate.value);
        if (event.checked && validFromDate.getFullYear() !== 1970) {
            const d = new Date(validFromDate.getFullYear(), validFromDate.getMonth() + 1, 0);
            this.rateOfInterestUpdateForm.controls.calculationDate.setValue(d);
        }
    }

    /**
     * setDueDate()
     */
    setDueDate(event: MatCheckboxChange): void {
        const validFromDate = new Date(this.rateOfInterestUpdateForm.controls.validFromDate.value);
        if (event.checked && validFromDate.getFullYear() !== 1970) {
            const d = new Date(validFromDate.getFullYear(), validFromDate.getMonth() + 1, 0);
            this.rateOfInterestUpdateForm.controls.dueDate.setValue(d);
        }
    }
}
