import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { OperatingParameterPLFUpdateDialogComponent } from '../operatingParameterPLFUpdate/operatingParameterPLFUpdate.component';

@Component({
    selector: 'fuse-operating-parameter-plf-list',
    templateUrl: './operatingParameterPLFList.component.html',
    styleUrls: ['./operatingParameterPLFList.component.scss'],
    animations: fuseAnimations
})
export class OperatingParameterPLFListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'year', 'designPlfCuf', 'actualYearlyAveragePlfCuf', 'miUnGenerated', 'remarks'
    ];

    loanApplicationId: string;

    selectedOperatingParameterPLF: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getOperatingParameterPLFs(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort
        });
    }

    /**
     * onSelect()
     * @param selectedOperatingParameterPLF
     */
    onSelect(selectedOperatingParameterPLF: any): void {
        this.selectedOperatingParameterPLF = selectedOperatingParameterPLF;
    }

    /**
     * updateOperatingParameterPLF()
     * @param operation 
     */
    updateOperatingParameterPLF(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedOperatingParameterPLF': undefined
        };
        if (operation === 'updateOperatingParameterPLF') {
            data.selectedOperatingParameterPLF = this.selectedOperatingParameterPLF;
        }
        const dialogRef = this._dialog.open(OperatingParameterPLFUpdateDialogComponent, {
            panelClass: 'fuse-operating-parameter-plf-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getOperatingParameterPLFs(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }
}
