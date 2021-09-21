import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-project-appraisal-completion-update',
  templateUrl: './projectAppraisalCompletionUpdate.component.html',
  styleUrls: ['./projectAppraisalCompletionUpdate.component.scss']
})
export class ProjectAppraisalCompletionUpdateComponent {

    dialogTitle = "Update Project Appraisal Completion Details";

    _projectAppraisalCompletionForm: FormGroup;
    _projectAppraisalCompletion: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ProjectAppraisalCompletionUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._projectAppraisalCompletion = Object.assign({}, _dialogData.projectAppraisalCompletion);
        console.log('this._projectAppraisalCompletion', this._projectAppraisalCompletion);

        this._projectAppraisalCompletionForm = _formBuilder.group({
            dateOfProjectAppraisalCompletion: [ this._projectAppraisalCompletion.dateOfProjectAppraisalCompletion || undefined ],
            agendaNoteApprovalByDirA: [ this._projectAppraisalCompletion.agendaNoteApprovalByDirA || undefined ],
            agendaNoteApprovalByDirB: [ this._projectAppraisalCompletion.agendaNoteApprovalByDirB || undefined ],
            agendaNoteApprovalByMDAndCEO: [ this._projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO || undefined ],
            agendaNoteSubmissionToCoSecy: [ this._projectAppraisalCompletion.agendaNoteSubmissionToCoSecy || undefined ],
            remarks: [ this._projectAppraisalCompletion.remarks || '' ],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._projectAppraisalCompletionForm.valid) {
            var formValues = this._projectAppraisalCompletionForm.value;

            var dt = new Date(formValues.dateOfProjectAppraisalCompletion);
            formValues.dateOfProjectAppraisalCompletion = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            dt = new Date(formValues.agendaNoteApprovalByDirA);
            formValues.agendaNoteApprovalByDirA = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            dt = new Date(formValues.agendaNoteApprovalByDirB);
            formValues.agendaNoteApprovalByDirB = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            dt = new Date(formValues.agendaNoteApprovalByMDAndCEO);
            formValues.agendaNoteApprovalByMDAndCEO = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            dt = new Date(formValues.agendaNoteSubmissionToCoSecy);
            formValues.agendaNoteSubmissionToCoSecy = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            
            if (JSON.stringify(this._projectAppraisalCompletion) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createProjectAppraisalCompletion(formValues).subscribe(response => {
                    this._matSnackBar.open('Project appraisal completion details created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectAppraisalCompletion': response });
                });
            }
            else {
                console.log('updating');
                this._projectAppraisalCompletion.dateOfProjectAppraisalCompletion = formValues.dateOfProjectAppraisalCompletion;
                this._projectAppraisalCompletion.agendaNoteApprovalByDirA = formValues.agendaNoteApprovalByDirA;
                this._projectAppraisalCompletion.agendaNoteApprovalByDirB = formValues.agendaNoteApprovalByDirB;
                this._projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO = formValues.agendaNoteApprovalByMDAndCEO;
                this._projectAppraisalCompletion.agendaNoteSubmissionToCoSecy = formValues.agendaNoteSubmissionToCoSecy;
                this._projectAppraisalCompletion.remarks = formValues.remarks;
                this._loanAppraisalService.updateProjectAppraisalCompletion(this._projectAppraisalCompletion).subscribe(response => {
                    this._matSnackBar.open('Project appraisal completion details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectAppraisalCompletion': response });
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
