import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { LIEReportAndFeeUpdateDialogComponent } from '../lieReportAndFeeUpdate/lieReportAndFeeUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-lie-report-fee-list',
    templateUrl: './lieReportAndFeeList.component.html',
    styleUrls: ['./lieReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class LIEReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    selectedLIE: any;
    selectedLIEReportAndFee: any;

    displayedColumns = [
        'serialNumber', 'reportType', 'dateOfReceipt','invoiceDate', 'invoiceNo', 'feeAmount', 'statusOfFeeReceipt', 'statusOfFeePaid', 'documentTitle', 
            'nextReportDate', 'download'
    ];

    subscriptions = new Subscription()
    
    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }

    /**
     * constructor()
     */
    constructor(private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.subscriptions.add(_loanMonitoringService.selectedLIE.subscribe(data => {
            this.selectedLIE = new LIEModel(data);
            if (this.selectedLIE.id !== '') {
                _loanMonitoringService.getLIEReportsAndFees(this.selectedLIE.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }))
    }

    /**
     * onSelect()
     */
    onSelect(lieReportAndFee: any): void {
        this.selectedLIEReportAndFee = lieReportAndFee;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getReportType()
     */
    getReportType(reportType: string): string {
        const filtered = LoanMonitoringConstants.reportTypes.filter(obj => obj.code === reportType);
        return filtered[0].value;
    }

    /**
     * addLIEReportAndFee()
     */
    addLIEReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIEReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-lie-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addLIEReportAndFee',
                selectedLIE: this.selectedLIE,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLIEReportsAndFees(this.selectedLIE.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateLIEReportAndFee()
     */
    updateLIEReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIEReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-lie-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateLIEReportAndFee',
                selectedLIE: this.selectedLIE,
                selectedLIEReportAndFee: this.selectedLIEReportAndFee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLIEReportsAndFees(this.selectedLIE.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }
}
