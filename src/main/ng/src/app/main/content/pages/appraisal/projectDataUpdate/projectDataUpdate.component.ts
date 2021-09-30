import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanAppraisalRegEx } from '../loanAppraisal.regEx';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-project-data-update',
  templateUrl: './projectDataUpdate.component.html',
  styleUrls: ['./projectDataUpdate.component.scss']
})
export class ProjectDataUpdateComponent {

    dialogTitle = "Update Project Data";

    _projectDataStep1Form: FormGroup;
    _projectData: any;
    _loanEnquiry: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ProjectDataUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._projectData = Object.assign({}, _dialogData.projectData);
        this._loanEnquiry = Object.assign({}, _dialogData.loanEnquiry);

        this._projectDataStep1Form = _formBuilder.group({
            projectName: [this._projectData.projectName || this._loanEnquiry.projectName],
            typeOfFunding: [this._projectData.typeOfFunding || ''],
            policyApplicable: [this._projectData.policyApplicable || ''],
            projectCapacity: [this._projectData.projectCapacity || this._loanEnquiry.projectCapacity, 
                [Validators.pattern(LoanAppraisalRegEx.projectCapacity)]],
            projectCapacityUnit: [this._projectData.projectCapacityUnit || this._loanEnquiry.projectCapacityUnit],
            numberOfUnits: [this._projectData.numberOfUnits || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            projectType: [this._projectData.projectType || this._loanEnquiry.projectType],
            mainContractor: [this._projectData.mainContractor || ''],
            epcContractor: [this._projectData.epcContractor || ''],
            resourceAssemblyAgency: [this._projectData.resourceAssemblyAgency || ''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._projectDataStep1Form.valid) {
            var formValues = this._projectDataStep1Form.value;
            if (JSON.stringify(this._projectData) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new project data');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createProjectData(formValues).subscribe(response => {
                    this._matSnackBar.open('Project data created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectData': response });
                });
            }
            else {
                console.log('updating project data');
                this._projectData.projectName = formValues.projectName;
                this._projectData.typeOfFunding = formValues.typeOfFunding;
                this._projectData.policyApplicable = formValues.policyApplicable;
                this._projectData.projectCapacity = formValues.projectCapacity;
                this._projectData.projectCapacityUnit = formValues.projectCapacityUnit;
                this._projectData.numberOfUnits = formValues.numberOfUnits;
                this._projectData.projectType = formValues.projectType;
                this._projectData.mainContractor = formValues.mainContractor;
                this._projectData.epcContractor = formValues.epcContractor;
                this._projectData.resourceAssemblyAgency = formValues.resourceAssemblyAgency;
                this._loanAppraisalService.updateProjectData(this._projectData).subscribe(response => {
                    this._matSnackBar.open('Project data updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectData': response });
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
