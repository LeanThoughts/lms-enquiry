import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { ProjectMonitoringDataItemHistoryComponent } from '../projectMonitoringDataItemHistory/projectMonitoringDataItemHistory.component';
import { ProjectMonitoringDataItemUpdateComponent } from '../projectMonitoringDataItemUpdate/projectMonitoringDataItemUpdate.component';

@Component({
    selector: 'fuse-project-monitoring-data-item-list',
    templateUrl: './projectMonitoringDataItemList.component.html',
    styleUrls: ['./projectMonitoringDataItemList.component.scss'],
    animations: fuseAnimations
})
export class ProjectMonitoringDataItemListComponent implements OnInit {

    loanApplicationId: string;

    projectMonitoringData: any;

    projectMonitoringDataSource: MatTableDataSource<any>;

    selectedProjectMonitoringDataItem: any;

    displayedColumns = [
        //'dateOfEntry', 'description', 'originalData','revisedData1', 'revisedData2', 'remarks'
        'dateOfEntry', 'description', 'originalData',  'remarks'

    ];

    /**
     * constructor()
     */
    constructor(private _enquiryService: LoanEnquiryService, private _monitoringService: LoanMonitoringService, private _dialog: MatDialog,
                private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;
        _monitoringService.getProjectMonitoringData(this.loanApplicationId).subscribe(data => {
            if (data === null) {
                _monitoringService.saveProjectMonitoringData(this.loanApplicationId).subscribe(response => {
                    this.projectMonitoringData = response;
                    this.projectMonitoringDataSource = new MatTableDataSource(this.projectMonitoringData.projectMonitoringDataItems);
                    console.log(this.projectMonitoringData, 'this.projectMonitoringData');
                });
            }
            else {
                this.projectMonitoringData = data;
                this.projectMonitoringDataSource = new MatTableDataSource(this.projectMonitoringData.projectMonitoringDataItems);
            }
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        /**
         * this.sort will not be initialized in the constructor phase. It will be undefined and hence sorting
         * will not work. The below line has to be in ngOnInit() which is executed after all initializations.
         */
        //this.dataSource.sort = this.sort;
    }

    /**
     * onSelect()
     * @param onSelect
     */
    onSelect(projectMonitoringDataItem: any): void {
        this.selectedProjectMonitoringDataItem = projectMonitoringDataItem;
    }

    /**
     * updateProjectMonitoringData()
     */
    updateProjectMonitoringData(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ProjectMonitoringDataItemUpdateComponent, {
            panelClass: 'fuse-project-monitoring-data-item-update-dialog',
            width: '850px',
            data: {
                loanApplicationId: this.loanApplicationId,
                projectMonitoringDataId: this.projectMonitoringData.id,
                selectedProjectMonitoringDataItem: this.selectedProjectMonitoringDataItem
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result.refresh) {
                this._monitoringService.getProjectMonitoringData(this.loanApplicationId).subscribe(response => {
                    this.projectMonitoringData = response;
                    this.projectMonitoringDataSource = new MatTableDataSource(this.projectMonitoringData.projectMonitoringDataItems);
                });
            }
        });
    }

    displayProjectMonitoringDataHistory(history: any): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ProjectMonitoringDataItemHistoryComponent, {
            panelClass: 'fuse-project-monitoring-data-item-history-dialog',
            width: '900px',
            data: {
                selectedProjectMonitoringDataItem: this.selectedProjectMonitoringDataItem,
                historyData: history
            }
        });
    }

    /**
     * getHistory()
     */
    getHistory(): void {
        this._monitoringService.getProjectMonitoringDataItemHistory(this.projectMonitoringData.id, this.selectedProjectMonitoringDataItem.particulars)
                .subscribe(response => {
                    if (response._embedded.projectMonitoringDataItemHistories.length > 0) {
                        this.displayProjectMonitoringDataHistory(response);
                    }
                    else {
                        this._matSnackBar.open('No history data found.', 'OK', { duration: 7000 });
                    }
                });
    }

  downloadHistory(): void {
    this._monitoringService.getProjectMonitoringDataItemHistoryDownload(this.projectMonitoringData.id )
      .subscribe(response => {
        this._matSnackBar.open('Project monitoring history download', 'OK', { duration: 7000 });

      });
  }
}
