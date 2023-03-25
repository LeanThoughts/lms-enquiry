import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar, MatTableDataSource } from '@angular/material';

@Component({
    selector: 'fuse-project-monitoring-data-item-history-dialog',
    templateUrl: './projectMonitoringDataItemHistory.component.html',
    styleUrls: ['./projectMonitoringDataItemHistory.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ProjectMonitoringDataItemHistoryComponent {

    dialogTitle = 'Historical Data for - ';

    selectedProjectMonitoringDataItem: any;
    selectedProjectMonitoringDataHistoryItem: any;

    projectMonitoringHistoryDataSource: MatTableDataSource<any>;

    displayedColumns = [
//     'dateOfEntry', 'description', 'originalData','revisedData1', 'revisedData2', 'remarks'
      'dateOfEntry', 'description', 'originalData', 'remarks'

    ];

    /**
     * constructor()
     */
    constructor(public _dialogRef: MatDialogRef<ProjectMonitoringDataItemHistoryComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any) {

        this.selectedProjectMonitoringDataItem = _dialogData.selectedProjectMonitoringDataItem;
        this.dialogTitle = this.dialogTitle + this.selectedProjectMonitoringDataItem.description;
        console.log('history data is', _dialogData.historyData);
        this.projectMonitoringHistoryDataSource = new MatTableDataSource(_dialogData.historyData._embedded.projectMonitoringDataItemHistories);
    }

    /**
     * onHistorySelect()
     */
    onHistorySelect(projectMonitoringDataHistoryItem: any): void {
        this.selectedProjectMonitoringDataHistoryItem = projectMonitoringDataHistoryItem;
    }
}
