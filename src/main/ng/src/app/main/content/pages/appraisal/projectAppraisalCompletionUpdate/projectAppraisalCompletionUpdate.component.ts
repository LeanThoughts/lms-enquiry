import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
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
    documentTypes = LoanMonitoringConstants.documentTypes;

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
        // this._projectAppraisalCompletion.fileReference = '';
        console.log('this._projectAppraisalCompletion', this._projectAppraisalCompletion);

        this._projectAppraisalCompletionForm = _formBuilder.group({
            dateOfProjectAppraisalCompletion: [ this._projectAppraisalCompletion.dateOfProjectAppraisalCompletion || undefined ],
            agendaNoteApprovalByDirA: [ this._projectAppraisalCompletion.agendaNoteApprovalByDirA || undefined ],
            agendaNoteApprovalByDirB: [ this._projectAppraisalCompletion.agendaNoteApprovalByDirB || undefined ],
            agendaNoteApprovalByMDAndCEO: [ this._projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO || undefined ],
            agendaNoteSubmissionToCoSecy: [ this._projectAppraisalCompletion.agendaNoteSubmissionToCoSecy || undefined ],
            remarks: [ this._projectAppraisalCompletion.remarks || '' ],
            documentTitle: [this._projectAppraisalCompletion.documentTitle],
            documentType: [this._projectAppraisalCompletion.documentType || ''],
            file: ['']
        });
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this._projectAppraisalCompletionForm.get('file').setValue(file);
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * submit()
     */
    submit(): void {

        if (this._projectAppraisalCompletionForm.valid) {
            if (this._projectAppraisalCompletionForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this._projectAppraisalCompletionForm.get('file').value);      
                this._loanAppraisalService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveProjectAppraisalCompletionDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                this.saveProjectAppraisalCompletionDetails('');
            }
        }
    }

    /**
     * saveProjectAppraisalCompletionDetails()
     */
    saveProjectAppraisalCompletionDetails(fileReference: string) {
        if (this._projectAppraisalCompletionForm.valid) {
            var formValues = this._projectAppraisalCompletionForm.value;

            if (formValues.dateOfProjectAppraisalCompletion) {
                var dt = new Date(formValues.dateOfProjectAppraisalCompletion);
                formValues.dateOfProjectAppraisalCompletion = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (formValues.agendaNoteApprovalByDirA) {
                dt = new Date(formValues.agendaNoteApprovalByDirA);
                formValues.agendaNoteApprovalByDirA = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (formValues.agendaNoteApprovalByDirB) {
                dt = new Date(formValues.agendaNoteApprovalByDirB);
                formValues.agendaNoteApprovalByDirB = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (formValues.agendaNoteApprovalByMDAndCEO) {
                dt = new Date(formValues.agendaNoteApprovalByMDAndCEO);
                formValues.agendaNoteApprovalByMDAndCEO = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (formValues.agendaNoteSubmissionToCoSecy) {
                dt = new Date(formValues.agendaNoteSubmissionToCoSecy);
                formValues.agendaNoteSubmissionToCoSecy = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (JSON.stringify(this._projectAppraisalCompletion) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                formValues.fileReference = fileReference;
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
                this._projectAppraisalCompletion.fileReference = formValues.fileReference;
                this._projectAppraisalCompletion.documentTitle = formValues.documentTitle;
                this._projectAppraisalCompletion.documentType = formValues.documentType;
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
