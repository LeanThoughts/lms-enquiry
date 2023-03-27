import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-proposal-details-update',
  templateUrl: './proposal-details-update.component.html',
  styleUrls: ['./proposal-details-update.component.scss']
})
export class ProposalDetailsUpdateComponent {

    dialogTitle = "Update Proposal Details";

    _proposalDetailsForm: FormGroup;
    _proposalDetail: any;

    /**
     * constructor()
     * @param _formBuilder
     * @param _loanAppraisalService
     * @param _dialogRef
     * @param _dialogData
     */
    constructor(_formBuilder: FormBuilder,
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ProposalDetailsUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._proposalDetail = Object.assign({}, _dialogData.proposalDetail);
        console.log('this._proposalDetail', this._proposalDetail);

        this._proposalDetailsForm = _formBuilder.group({
            rateOfInterestPreCod: [ this._proposalDetail.rateOfInterestPreCod || undefined,
                [Validators.pattern(MonitoringRegEx.holdingPercentage)] ],
            rateOfInterestPostCod: [ this._proposalDetail.rateOfInterestPostCod || undefined,
                [Validators.pattern(MonitoringRegEx.holdingPercentage)] ],
            spreadReset: [ this._proposalDetail.spreadReset || undefined,
                [Validators.pattern(MonitoringRegEx.holdingPercentage)] ],
            spreadResetUnit: [ this._proposalDetail.spreadResetUnit || undefined ],
            effectiveRateOfInterest: [ this._proposalDetail.effectiveRateOfInterest || undefined,
                [Validators.pattern(MonitoringRegEx.holdingPercentage)] ],
            constructionPeriod: [ this._proposalDetail.constructionPeriod || undefined,
                [Validators.pattern(MonitoringRegEx.threeDigitsOnly)] ],
            constructionPeriodUnit: [this._proposalDetail.constructionPeriodUnit || undefined ],
            moratoriumPeriod: [ this._proposalDetail.moratoriumPeriod || undefined,
                [Validators.pattern(MonitoringRegEx.threeDigitsOnly)] ],
            moratoriumPeriodUnit: [ this._proposalDetail.moratoriumPeriodUnit || undefined ],
            repaymentPeriod: [ this._proposalDetail.repaymentPeriod || undefined,
                [Validators.pattern(MonitoringRegEx.threeDigitsOnly)] ],
            repaymentPeriodUnit: [ this._proposalDetail.repaymentPeriodUnit || undefined ],
            tenor: [ this._proposalDetail.tenor || undefined,
                [Validators.pattern(MonitoringRegEx.threeDigitsOnly)] ],
            tenorUnit: [ this._proposalDetail.tenorUnit || undefined ],
            availabilityPeriod: [ this._proposalDetail.availabilityPeriod || undefined,
                [Validators.pattern(MonitoringRegEx.threeDigitsOnly)] ],
            availabilityPeriodUnit: [ this._proposalDetail.availabilityPeriodUnit || undefined ],
            prePaymentCharges: [ this._proposalDetail.prePaymentCharges || undefined,
                [Validators.pattern(MonitoringRegEx.thirteenCommaTwo)] ],
            feeDetailsSchedule: [ this._proposalDetail.feeDetailsSchedule || undefined ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._proposalDetailsForm.valid) {
            var formValues = this._proposalDetailsForm.value;
            if (JSON.stringify(this._proposalDetail) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new proposal detail');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createProposalDetail(formValues).subscribe(response => {
                    this._matSnackBar.open('Proposal details created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'proposalDetail': response });
                });
            }
            else {
                console.log('updating proposal detail');
                this._proposalDetail.rateOfInterestPreCod = formValues.rateOfInterestPreCod;
                this._proposalDetail.rateOfInterestPostCod = formValues.rateOfInterestPostCod;
                this._proposalDetail.spreadReset = formValues.spreadReset;
                this._proposalDetail.spreadResetUnit = formValues.spreadResetUnit;
                this._proposalDetail.effectiveRateOfInterest = formValues.effectiveRateOfInterest;
                this._proposalDetail.constructionPeriod = formValues.constructionPeriod;
                this._proposalDetail.constructionPeriodUnit = formValues.constructionPeriodUnit;
                this._proposalDetail.moratoriumPeriod = formValues.moratoriumPeriod;
                this._proposalDetail.moratoriumPeriodUnit = formValues.moratoriumPeriodUnit;
                this._proposalDetail.repaymentPeriod = formValues.repaymentPeriod;
                this._proposalDetail.repaymentPeriodUnit = formValues.repaymentPeriodUnit;
                this._proposalDetail.tenor = formValues.tenor;
                this._proposalDetail.tenorUnit = formValues.tenorUnit;
                this._proposalDetail.availabilityPeriod = formValues.availabilityPeriod;
                this._proposalDetail.availabilityPeriodUnit = formValues.availabilityPeriodUnit;
                this._proposalDetail.prePaymentCharges = formValues.prePaymentCharges;
                this._proposalDetail.feeDetailsSchedule = formValues.feeDetailsSchedule;
                this._loanAppraisalService.updateProposalDetail(this._proposalDetail).subscribe(response => {
                    this._matSnackBar.open('Proposal details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'proposalDetail': response });
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
