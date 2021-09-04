import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { FinancialCovenantsModel } from 'app/main/content/model/financialCovenants.model';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-financial-covenants-update-dialog',
    templateUrl: './financialCovenantsUpdate.component.html',
    styleUrls: ['./financialCovenantsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class FinancialCovenantsUpdateDialogComponent {

    dialogTitle: string;

    selectedFinancialCovenants: FinancialCovenantsModel ;

    financialCovenantsUpdateForm: FormGroup;
  
    financialCovenantsType = LoanMonitoringConstants.financialCovenantsType;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<FinancialCovenantsUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedFinancialCovenants !== undefined) {
            this.selectedFinancialCovenants = Object.assign({}, _dialogData.selectedFinancialCovenants);
            this.dialogTitle = 'Modify Financial Covenants';
        }
        else {
            this.selectedFinancialCovenants = new FinancialCovenantsModel({});
            this.dialogTitle = 'Add New Financial Covenants';
        }

        this.financialCovenantsUpdateForm = _formBuilder.group({
            financialCovenantType: [this.selectedFinancialCovenants.financialCovenantType],
            financialYear: [this.selectedFinancialCovenants.financialYear, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            debtEquityRatio: [this.selectedFinancialCovenants.debtEquityRatio, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            dscr: [this.selectedFinancialCovenants.dscr, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            tolTnw: [this.selectedFinancialCovenants.tolTnw, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            remarksForDeviation: [this.selectedFinancialCovenants.remarksForDeviation],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.financialCovenantsUpdateForm.valid) {
            var financialCovenants: FinancialCovenantsModel = new FinancialCovenantsModel(this.financialCovenantsUpdateForm.value);
            if (this._dialogData.operation === 'addFinancialCovenants') {
                this._loanMonitoringService.saveFinancialCovenants(financialCovenants, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Financial Covenant details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedFinancialCovenants.financialCovenantType = financialCovenants.financialCovenantType;
                this.selectedFinancialCovenants.financialYear = financialCovenants.financialYear;
                this.selectedFinancialCovenants.debtEquityRatio  = financialCovenants.debtEquityRatio;
                this.selectedFinancialCovenants.dscr  = financialCovenants.dscr;
                this.selectedFinancialCovenants.tolTnw  = financialCovenants.tolTnw;
                this.selectedFinancialCovenants.remarksForDeviation  = financialCovenants.remarksForDeviation;
                this._loanMonitoringService.updateFinancialCovenants(this.selectedFinancialCovenants).subscribe(() => {
                    this._matSnackBar.open('Financial Covenant details updated successfully.', 'OK', { duration: 7000 });
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
