import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { TRAModel } from 'app/main/content/model/tra.model';
import { TRAStatementModel } from 'app/main/content/model/traStatement.model';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-tra-statement-update-dialog',
    templateUrl: './traStatementUpdate.component.html',
    styleUrls: ['./traStatementUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class TRAStatementUpdateDialogComponent {

    dialogTitle = 'Add TRA Statement';

    selectedTRA: TRAModel;
    selectedTRAStatement: TRAStatementModel;

    traStatementUpdateForm: FormGroup;

    viewRights = LoanMonitoringConstants.viewRights;
    periodQuarters = LoanMonitoringConstants.periodQuarters;
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<TRAStatementUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedTRA = _dialogData.selectedTRA;
        if (_dialogData.selectedTRAStatement !== undefined) {
            this.selectedTRAStatement = Object.assign({}, _dialogData.selectedTRAStatement);
            this.dialogTitle = 'Modify TRA Statement';
        }
        else {
            this.selectedTRAStatement = new TRAStatementModel({});
        }

        this.traStatementUpdateForm = _formBuilder.group({
            viewRights: [this.selectedTRAStatement.viewRights],
            remarks: [this.selectedTRAStatement.remarks],
            periodQuarter: [this.selectedTRAStatement.periodQuarter],
            periodYear: [this.selectedTRAStatement.periodYear, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            documentType: [this.selectedTRAStatement.documentType],
            file: ['']
        });

        // Sort document types array
        this.documentTypes = this.documentTypes.sort((doc1, doc2) => {
            return doc1.value.localeCompare(doc2.value);
        })
    }

    /**
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.traStatementUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.traStatementUpdateForm.valid) {
            var traStatement: TRAStatementModel = new TRAStatementModel(this.traStatementUpdateForm.value);
            if (this._dialogData.operation === 'addTRAStatement') {
                if (this.traStatementUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.traStatementUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            traStatement.fileReference = response.fileReference;
                            this._loanMonitoringService.saveTRAStatement(traStatement, this.selectedTRA.id).subscribe(() => {
                                this._matSnackBar.open('TRA Statement added successfully.', 'OK', { duration: 5000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this._loanMonitoringService.saveTRAStatement(traStatement, this.selectedTRA.id).subscribe(() => {
                        this._matSnackBar.open('TRA Statement added successfully.', 'OK', { duration: 5000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
            else {
                if (this.traStatementUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.traStatementUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedTRAStatement.viewRights = traStatement.viewRights;
                            this.selectedTRAStatement.remarks = traStatement.remarks;
                            this.selectedTRAStatement.periodQuarter = traStatement.periodQuarter;
                            this.selectedTRAStatement.periodYear = traStatement.periodYear;
                            this.selectedTRAStatement.documentType = traStatement.documentType;
                            this.selectedTRAStatement.fileReference = response.fileReference;
                            this._loanMonitoringService.updateTRAStatement(this.selectedTRAStatement).subscribe(() => {
                                this._matSnackBar.open('TRA Statement updated successfully.', 'OK', { duration: 5000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this.selectedTRAStatement.viewRights = traStatement.viewRights;
                    this.selectedTRAStatement.remarks = traStatement.remarks;
                    this.selectedTRAStatement.periodQuarter = traStatement.periodQuarter;
                    this.selectedTRAStatement.periodYear = traStatement.periodYear;
                    this.selectedTRAStatement.documentType = traStatement.documentType;
                    this._loanMonitoringService.updateTRAStatement(this.selectedTRAStatement).subscribe(() => {
                        this._matSnackBar.open('TRA Statement updated successfully.', 'OK', { duration: 5000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
        }
    }

    /**
     * closeClick()
     */
    closeClick(): void {
        this._dialogRef.close({ 'refresh': false });
    }
    
    /**
     * getFileURL()
     * @param fileReference
     */
     getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }  
}
