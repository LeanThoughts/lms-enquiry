import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { RiskAssessmentService } from '../riskAssessment.service';

@Component({
    selector: 'fuse-preliminary-risk-assessment-update-dialog',
    templateUrl: './riskAssessmentInfoUpdate.component.html',
    styleUrls: ['./riskAssessmentInfoUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PreliminaryRiskAssessmentUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Preliminary Risk Assessment Details';

    loanApplicationId = '';

    preliminaryRiskAssessment: any;
    riskAssessmentForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _riskAssessmentService: RiskAssessmentService,
        public _dialogRef: MatDialogRef<PreliminaryRiskAssessmentUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.preliminaryRiskAssessment = Object.assign({}, _dialogData.preliminaryRiskAssessment);
        this.loanApplicationId = _dialogData.loanApplicationId;

        if (this.preliminaryRiskAssessment.id !== undefined) {
            this.dialogTitle = 'Modify Preliminary Risk Assessment Details';
        }

        this.riskAssessmentForm = this._formBuilder.group({
            dateOfAssessment: [this.preliminaryRiskAssessment.dateOfAssessment],
            remarksByRiskDepartment: [this.preliminaryRiskAssessment.remarksByRiskDepartment || ''],
            remarks: [this.preliminaryRiskAssessment.remarks || ''],
            mdApprovalDate: [this.preliminaryRiskAssessment.mdApprovalDate || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.riskAssessmentForm.valid) {
            var riskAssessment = this.riskAssessmentForm.value;
                
            if (riskAssessment.dateOfAssessment) {
                var dt = new Date(riskAssessment.dateOfAssessment);
                riskAssessment.dateOfAssessment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (riskAssessment.mdApprovalDate) {
                dt = new Date(riskAssessment.mdApprovalDate);
                riskAssessment.mdApprovalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (this.preliminaryRiskAssessment.id === undefined) {
                riskAssessment.loanApplicationId = this.loanApplicationId;
                this._riskAssessmentService.createPreliminaryRiskAssessment(riskAssessment).subscribe(() => {
                    this._riskAssessmentService.getRiskAssessment(this.loanApplicationId).subscribe(data => {
                        this._riskAssessmentService._riskAssessment.next(data);
                        this._matSnackBar.open('Preliminary Risk Assessment details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                Object.keys(riskAssessment).forEach(key => {
                    if (this.preliminaryRiskAssessment.hasOwnProperty(key)) {
                        this.preliminaryRiskAssessment[key] = riskAssessment[key];
                    }
                });
                this._riskAssessmentService.updatePreliminaryRiskAssessment(this.preliminaryRiskAssessment).subscribe(() => {
                    this._matSnackBar.open('Preliminary Risk Assessment details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
