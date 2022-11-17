import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
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

        this._financialUpdateForm = _formBuilder.group({
            fiscalPeriod: [ this._financial.fiscalPeriod || '' ],

            revenue: [ this._financial.revenue || '' ],
            depreciation: [ this._financial.depreciation || '' ],
            pbt: [ this._financial.pbt || '' ],
            netCashAccruals: [ this._financial.netCashAccruals || '' ],
            ebitda: [ this._financial.ebitda || '' ],
            interestExpense: [ this._financial.interestExpense || '' ],
            pat: [ this._financial.pat || '' ],

            wcDebt: [ this._financial.wcDebt || '' ],
            totalOutstandingLiabilities: [ this._financial.totalOutstandingLiabilities || '' ],
            adjustedTangibleNetWorth: [ this._financial.adjustedTangibleNetWorth || '' ],
            subAsso: [ this._financial.subAsso || '' ],
            cpltd: [ this._financial.cpltd || '' ],
            shareCapital: [ this._financial.shareCapital || '' ],
            cashAndBankBalance: [ this._financial.cashAndBankBalance || '' ],
            netFixedAssets: [ this._financial.netFixedAssets || '' ],
            ltDebt: [ this._financial.ltDebt || '' ],
            reservesAndSurplus: [ this._financial.reservesAndSurplus || '' ],
            currentAssets: [ this._financial.currentAssets || '' ],
            quasiEquity: [ this._financial.quasiEquity || '' ],
            totalDebt: [ this._financial.totalDebt || '' ],
            tangibleNetWorth: [ this._financial.tangibleNetWorth || '' ],
            currentLiabilities: [ this._financial.currentLiabilities || '' ],

            ebitdaMarginPercentage: [ this._financial.ebitdaMarginPercentage || '' ],
            totalDebtEbitda: [ this._financial.totalDebtEbitda || '' ],
            totalDebtTnw: [ this._financial.totalDebtTnw || '' ],
            currentRatio: [ this._financial.currentRatio || '' ],
            ebitdaInterest: [ this._financial.ebitdaInterest || '' ],
            termDebtEbitda: [ this._financial.termDebtEbitda || '' ],
            tnw: [ this._financial.tnw || '' ],
            cashDscr: [ this._financial.cashDscr || '' ],
            dscr: [ this._financial.dscr || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._financialUpdateForm.valid) {
            var formValues = this._financialUpdateForm.value;

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
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }
}
