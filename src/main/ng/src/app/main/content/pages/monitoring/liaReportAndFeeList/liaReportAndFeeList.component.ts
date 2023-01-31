import { Component, ViewChild, OnDestroy } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { LIAReportAndFeeUpdateDialogComponent } from '../liaReportAndFeeUpdate/liaReportAndFeeUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-lia-report-fee-list',
    templateUrl: './liaReportAndFeeList.component.html',
    styleUrls: ['./liaReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class LIAReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    selectedLIA: any;
    selectedLIAReportAndFee: any;

    displayedColumns = [
        'serialNumber', 'reportType', 'dateOfReceipt','invoiceDate', 'invoiceNo', 'feeAmount', 'statusOfFeeReceipt', 'statusOfFeePaid', 'documentTitle', 
            'nextReportDate', 'download'
    ];

    subscriptions = new Subscription()
    
    /**
     * constructor()
     */
    constructor(private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.subscriptions.add(_loanMonitoringService.selectedLIA.subscribe(data => {
            this.selectedLIA = new LIEModel(data);
            if (this.selectedLIA.id !== '') {
                _loanMonitoringService.getLIAReportsAndFees(this.selectedLIA.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }))
    }

    /**
     * onSelect()
     */
    onSelect(liaReportAndFee: any): void {
        this.selectedLIAReportAndFee = liaReportAndFee;
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
     * addLIAReportAndFee()
     */
    addLIAReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIAReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-lia-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addLIAReportAndFee',
                selectedLIA: this.selectedLIA
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLIAReportsAndFees(this.selectedLIA.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateLIAReportAndFee()
     */
    updateLIAReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIAReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-lia-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateLIAReportAndFee',
                selectedLIA: this.selectedLIA,
                selectedLIAReportAndFee: this.selectedLIAReportAndFee
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLIAReportsAndFees(this.selectedLIA.id).subscribe(data => {
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
