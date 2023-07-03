import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-promoter-borrower-financial-update',
  templateUrl: './promoterBorrowerFinancialUpdate.component.html',
  styleUrls: ['./promoterBorrowerFinancialUpdate.component.scss']
})
export class PromoterBorrowerFinancialUpdateComponent {

    dialogTitle = "Financial Details";

    _financial: any;
    _financialUpdateForm: FormGroup;
    _financials: any;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<PromoterBorrowerFinancialUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch financial details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._financial = Object.assign({}, _dialogData.financial);
        this._financials = _dialogData.financials;
        console.log('_financials', this._financials);
        
        this._financialUpdateForm = _formBuilder.group({
            fiscalPeriod: [ this._financial.fiscalPeriod || '' ],

            revenue: [ this._financial.revenue || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            depreciation: [ this._financial.depreciation || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            pbt: [ this._financial.pbt || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            netCashAccruals: [ this._financial.netCashAccruals || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            ebitda: [ this._financial.ebitda || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            interestExpense: [ this._financial.interestExpense || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            pat: [ this._financial.pat || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],

            wcDebt: [ this._financial.wcDebt || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            totalOutstandingLiabilities: [ this._financial.totalOutstandingLiabilities || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            adjustedTangibleNetWorth: [ this._financial.adjustedTangibleNetWorth || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            subAsso: [ this._financial.subAsso || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            cpltd: [ this._financial.cpltd || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            shareCapital: [ this._financial.shareCapital || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            cashAndBankBalance: [ this._financial.cashAndBankBalance || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]],
            netFixedAssets: [ this._financial.netFixedAssets || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            ltDebt: [ this._financial.ltDebt || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            reservesAndSurplus: [ this._financial.reservesAndSurplus || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            currentAssets: [ this._financial.currentAssets || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            quasiEquity: [ this._financial.quasiEquity || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            totalDebt: [ this._financial.totalDebt || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            tangibleNetWorth: [ this._financial.tangibleNetWorth || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],
            currentLiabilities: [ this._financial.currentLiabilities || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)] ],

            ebitdaMarginPercentage: [ this._financial.ebitdaMarginPercentage || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            totalDebtEbitda: [ this._financial.totalDebtEbitda || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)]],
            totalDebtTnw: [ this._financial.totalDebtTnw || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            currentRatio: [ this._financial.currentRatio || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            ebitdaInterest: [ this._financial.ebitdaInterest || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            termDebtEbitda: [ this._financial.termDebtEbitda || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            tnw: [ this._financial.tnw || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            cashDscr: [ this._financial.cashDscr || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ],
            dscr: [ this._financial.dscr || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)] ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._financialUpdateForm.valid) {
            var formValues = this._financialUpdateForm.value;
            let found = false;
            this._financials.forEach(obj => {
                if (obj.fiscalPeriod === formValues.fiscalPeriod)
                    found = true
            });
            if (!found) {
                if (JSON.stringify(this._financial) === JSON.stringify({})) { // Insert a new record ...
                    console.log('inserting new record');
                    formValues.projectProposalId = this._dialogData.projectProposalId;
                    this._enquiryActionService.createFinancial(formValues).subscribe(response => {
                        this._matSnackBar.open('Financial details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
                else {
                    console.log('updating');
                    this._financial.fiscalPeriod = formValues.fiscalPeriod;
    
                    this._financial.revenue = formValues.revenue;
                    this._financial.depreciation = formValues.depreciation;
                    this._financial.pbt = formValues.pbt;
                    this._financial.netCashAccruals = formValues.netCashAccruals;
                    this._financial.ebitda = formValues.ebitda;
                    this._financial.interestExpense = formValues.interestExpense;
                    this._financial.pat = formValues.pat;
    
                    this._financial.wcDebt = formValues.wcDebt;
                    this._financial.totalOutstandingLiabilities = formValues.totalOutstandingLiabilities;
                    this._financial.adjustedTangibleNetWorth = formValues.adjustedTangibleNetWorth;
                    this._financial.subAsso = formValues.subAsso;
                    this._financial.cpltd = formValues.cpltd;
                    this._financial.shareCapital = formValues.shareCapital;
                    this._financial.cashAndBankBalance = formValues.cashAndBankBalance;
                    this._financial.netFixedAssets = formValues.netFixedAssets;
                    this._financial.ltDebt = formValues.ltDebt;
                    this._financial.reservesAndSurplus = formValues.reservesAndSurplus;
                    this._financial.currentAssets = formValues.currentAssets;
                    this._financial.quasiEquity = formValues.quasiEquity;
                    this._financial.totalDebt = formValues.totalDebt;
                    this._financial.tangibleNetWorth = formValues.tangibleNetWorth;
                    this._financial.currentLiabilities = formValues.currentLiabilities;
    
                    this._financial.ebitdaMarginPercentage = formValues.ebitdaMarginPercentage;
                    this._financial.totalDebtEbitda = formValues.totalDebtEbitda;
                    this._financial.totalDebtTnw = formValues.totalDebtTnw;
                    this._financial.currentRatio = formValues.currentRatio;
                    this._financial.ebitdaInterest = formValues.ebitdaInterest;
                    this._financial.termDebtEbitda = formValues.termDebtEbitda;
                    this._financial.tnw = formValues.tnw;
                    this._financial.cashDscr = formValues.cashDscr;
                    this._financial.dscr = formValues.dscr;
    
                    this._enquiryActionService.updateFinancial(this._financial).subscribe(response => {
                        this._matSnackBar.open('Financial details updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }    
            }
            else {
                this._matSnackBar.open('Data for fiscal year already found. Please select a different fiscal year.', 'OK', { duration: 7000 });
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
