import { Component, ViewChild, OnDestroy, Input } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { LLCReportAndFeeUpdateDialogComponent } from '../llcReportAndFeeUpdate/llcReportAndFeeUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-llc-report-fee-list',
    templateUrl: './llcReportAndFeeList.component.html',
    styleUrls: ['./llcReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class LLCReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    selectedLLC: any;
    selectedLLCReportAndFee: any;

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
        this.subscriptions.add(_loanMonitoringService.selectedLLC.subscribe(data => {
            this.selectedLLC = new LIEModel(data);
            if (this.selectedLLC.id !== '') {
                _loanMonitoringService.getLLCReportsAndFees(this.selectedLLC.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }))
    }

    /**
     * onSelect()
     */
    onSelect(llcReportAndFee: any): void {
        this.selectedLLCReportAndFee = llcReportAndFee;
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
     * addLLCReportAndFee()
     */
    addLLCReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LLCReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-llc-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addLLCReportAndFee',
                selectedLLC: this.selectedLLC,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLLCReportsAndFees(this.selectedLLC.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateLLCReportAndFee()
     */
    updateLLCReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LLCReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-llc-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateLLCReportAndFee',
                selectedLLC: this.selectedLLC,
                selectedLLCReportAndFee: this.selectedLLCReportAndFee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLLCReportsAndFees(this.selectedLLC.id).subscribe(data => {
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
