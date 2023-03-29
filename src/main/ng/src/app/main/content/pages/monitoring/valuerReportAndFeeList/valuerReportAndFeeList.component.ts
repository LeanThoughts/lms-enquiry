import { Component, ViewChild, OnDestroy, Input } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { ValuerReportAndFeeUpdateDialogComponent } from '../valuerReportAndFeeUpdate/valuerReportAndFeeUpdate.component';

@Component({
    selector: 'fuse-valuer-report-fee-list',
    templateUrl: './valuerReportAndFeeList.component.html',
    styleUrls: ['./valuerReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class ValuerReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    selectedValuer: any;
    selectedValuerReportAndFee: any;

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
        this.subscriptions.add(_loanMonitoringService.selectedValuer.subscribe(data => {
            this.selectedValuer = new LIEModel(data);
            if (this.selectedValuer.id !== '') {
                _loanMonitoringService.getValuerReportsAndFees(this.selectedValuer.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }))
    }

    /**
     * onSelect()
     */
    onSelect(valuerReportAndFee: any): void {
        this.selectedValuerReportAndFee = valuerReportAndFee;
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
     * addValuerReportAndFee()
     */
    addValuerReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ValuerReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-valuer-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addValuerReportAndFee',
                selectedValuer: this.selectedValuer,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getValuerReportsAndFees(this.selectedValuer.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateValuerReportAndFee()
     */
    updateValuerReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ValuerReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-valuer-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateValuerReportAndFee',
                selectedValuer: this.selectedValuer,
                selectedValuerReportAndFee: this.selectedValuerReportAndFee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getValuerReportsAndFees(this.selectedValuer.id).subscribe(data => {
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

    /**
     * deleteValuerReportAndFee()
     */
    deleteValuerReportAndFee(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteValuerReportAndFee(this.selectedValuerReportAndFee, this._module).subscribe(() => {
                    this.selectedValuerReportAndFee = undefined;
                    this._loanMonitoringService.getValuerReportsAndFees(this.selectedValuer.id).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
