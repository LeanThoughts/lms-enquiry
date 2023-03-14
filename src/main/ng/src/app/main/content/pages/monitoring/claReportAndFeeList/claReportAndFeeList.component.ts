import { Component, ViewChild, OnDestroy, Input } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { CLAReportAndFeeUpdateDialogComponent } from '../claReportAndFeeUpdate/claReportAndFeeUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-cla-report-fee-list',
    templateUrl: './claReportAndFeeList.component.html',
    styleUrls: ['./claReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class CLAReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    selectedCLA: any;
    selectedCLAReportAndFee: any;

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
        this.subscriptions.add(_loanMonitoringService.selectedCLA.subscribe(data => {
            this.selectedCLA = new LIEModel(data);
            if (this.selectedCLA.id !== '') {
                _loanMonitoringService.getCLAReportsAndFees(this.selectedCLA.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }))
    }

    /**
     * onSelect()
     */
    onSelect(claReportAndFee: any): void {
        this.selectedCLAReportAndFee = claReportAndFee;
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
     * addCLAReportAndFee()
     */
    addCLAReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(CLAReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-cla-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addCLAReportAndFee',
                selectedCLA: this.selectedCLA,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getCLAReportsAndFees(this.selectedCLA.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateCLAReportAndFee()
     */
    updateCLAReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(CLAReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-cla-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateCLAReportAndFee',
                selectedCLA: this.selectedCLA,
                selectedCLAReportAndFee: this.selectedCLAReportAndFee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getCLAReportsAndFees(this.selectedCLA.id).subscribe(data => {
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
