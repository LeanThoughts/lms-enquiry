import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { RiskAssessmentService } from '../riskAssessment.service';
import { ICCApprovalService } from '../../iccApproval/iccApproval.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

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

    approvalByIcc: any;

    documentTypes = LoanMonitoringConstants.documentTypes;


    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _riskAssessmentService: RiskAssessmentService,
        public _dialogRef: MatDialogRef<PreliminaryRiskAssessmentUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _iccApprovalService: ICCApprovalService) {

        // Fetch selected reason details from the dialog's data attribute.
        this.preliminaryRiskAssessment = Object.assign({}, _dialogData.preliminaryRiskAssessment);
        this.loanApplicationId = _dialogData.loanApplicationId;

        if (this.preliminaryRiskAssessment.id !== undefined) {
            this.dialogTitle = 'Modify Preliminary Risk Assessment Details';
        }

        _iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(response => {
            _iccApprovalService.getApprovalByICC(response.id).subscribe(data => {
                this.approvalByIcc = data;
                console.log('approval by icc is', this.approvalByIcc);
            })
        });

        this.riskAssessmentForm = this._formBuilder.group({
            dateOfAssessment: [this.preliminaryRiskAssessment.dateOfAssessment],
            remarksByRiskDepartment: [this.preliminaryRiskAssessment.remarksByRiskDepartment || ''],
            remarks: [this.preliminaryRiskAssessment.remarks || ''],
            mdApprovalDate: [this.preliminaryRiskAssessment.mdApprovalDate || ''],
            documentTitle: [this.preliminaryRiskAssessment.documentTitle || ''],
            documentType: [this.preliminaryRiskAssessment.documentType || ''],
            document: [this.preliminaryRiskAssessment.document || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.riskAssessmentForm.get('document').setValue(file);
        }
    }
        
    /**
     * submit()
     */
    submit(): void {
        if (this.riskAssessmentForm.valid) {
            if (this.riskAssessmentForm.get('document').value !== '') {
                var formData = new FormData();
                formData.append('file', this.riskAssessmentForm.get('document').value);      
                this._riskAssessmentService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveRiskAssessmentDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                this.saveRiskAssessmentDetails('');
            }
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string) {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * saveRiskAssessmentDetails()
     */
    saveRiskAssessmentDetails(fileReference: string) {
        var riskAssessment = this.riskAssessmentForm.value;
        riskAssessment.fileReference = fileReference;

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
                this.preliminaryRiskAssessment[key] = riskAssessment[key];
            });
            this._riskAssessmentService.updatePreliminaryRiskAssessment(this.preliminaryRiskAssessment).subscribe(() => {
                this._matSnackBar.open('Preliminary Risk Assessment details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });            
        }
    }
}
