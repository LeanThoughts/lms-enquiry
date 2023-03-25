import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';

@Component({
    selector: 'fuse-project-monitoring-data-item-update-dialog',
    templateUrl: './projectMonitoringDataItemUpdate.component.html',
    styleUrls: ['./projectMonitoringDataItemUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ProjectMonitoringDataItemUpdateComponent implements OnInit {

    dialogTitle = 'Update Project Monitoring Data';

    selectedProjectMonitoringDataItem: any;

    projectMonitoringDataForm: FormGroup;

    /**
     * constructor()
     * @param _formBuilder
     * @param _loanMonitoringService
     * @param _dialogRef
     * @param _dialogData
     * @param _matSnackBar
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<ProjectMonitoringDataItemUpdateComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        this.selectedProjectMonitoringDataItem = _dialogData.selectedProjectMonitoringDataItem;
        this.selectedProjectMonitoringDataItem.loanApplicationId = _dialogData.loanApplicationId;
        this.selectedProjectMonitoringDataItem.projectMonitoringDataId = _dialogData.projectMonitoringDataId;
        console.log(this.selectedProjectMonitoringDataItem);
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.projectMonitoringDataForm = this._formBuilder.group({
            description: [this.selectedProjectMonitoringDataItem.description],
            dateOfEntry: [this.selectedProjectMonitoringDataItem.dateOfEntry || ''],
            originalData: [this.selectedProjectMonitoringDataItem.originalData || ''],
            // revisedData1: [this.selectedProjectMonitoringDataItem.revisedData1],
            // revisedData2: [this.selectedProjectMonitoringDataItem.revisedData2 || ''],
            remarks: [this.selectedProjectMonitoringDataItem.remarks || ''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.projectMonitoringDataForm.valid) {
            var projectMonitoringDataItem = this.projectMonitoringDataForm.value;
            var dt = new Date(projectMonitoringDataItem.dateOfEntry);
            this.selectedProjectMonitoringDataItem.dateOfEntry = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            this.selectedProjectMonitoringDataItem.originalData = projectMonitoringDataItem.originalData;
            // this.selectedProjectMonitoringDataItem.revisedData1 = projectMonitoringDataItem.revisedData1;
            // this.selectedProjectMonitoringDataItem.revisedData2 = projectMonitoringDataItem.revisedData2;
            this.selectedProjectMonitoringDataItem.remarks = projectMonitoringDataItem.remarks;
            this._loanMonitoringService.updateProjectMonitoringDataItem(this.selectedProjectMonitoringDataItem).subscribe(() => {
                this._matSnackBar.open('Project monitoring data updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }
}
