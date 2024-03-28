import { Component, ViewChild, OnDestroy, Input } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { ConfirmationDialogComponent } from '../confirmationDialog/confirmationDialog.component';
import { SecurityTrusteeReportAndFeeUpdateDialogComponent } from '../securityTrusteeReportAndFeeUpdate/securityTrusteeReportAndFeeUpdate.component';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
    selector: 'fuse-securityTrustee-report-fee-list',
    templateUrl: './securityTrusteeReportAndFeeList.component.html',
    styleUrls: ['./securityTrusteeReportAndFeeList.component.scss'],
    animations: fuseAnimations
})
export class SecurityTrusteeReportAndFeeListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'reportType', 'dateOfReceipt','invoiceDate', 'invoiceNo', 'feeAmount', 'statusOfFeeReceipt', 'statusOfFeePaid', 'documentTitle', 
            'nextReportDate', 'download'
    ];

    selectedSecurityTrustee: any;
    selectedSecurityTrusteeReportAndFee: any;

    subscriptions = new Subscription();

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }
    
    /**
     * constructor()
     */
    constructor(private _dialog: MatDialog, private _loanAppraisalService: LoanAppraisalService) {

        this.subscriptions.add(_loanAppraisalService._selectedSecurityTrustee.subscribe(data => {
            this.selectedSecurityTrustee = data;
            if (this.selectedSecurityTrustee.id !== undefined || this.selectedSecurityTrustee.id !== '') {
                _loanAppraisalService.getSecurityTrusteeReportAndFees(this.selectedSecurityTrustee.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }));
    }

    /**
     *
     * @param enquiry
     */
    onSelect(stReportAndFee: any): void {
        this.selectedSecurityTrusteeReportAndFee = stReportAndFee;
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
    
    /**
     * getReportType()
     * @param reportType 
     */
    getReportType(reportType: string): string {
        const filtered = LoanMonitoringConstants.reportTypes.filter(obj => obj.code === reportType);
        return filtered[0].value;
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    /**
     * addSTReportAndFee()
     */
    addSTReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(SecurityTrusteeReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-securityTrustee-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'addSecurityTrusteeReportAndFee',
                selectedSecurityTrustee: this.selectedSecurityTrustee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSecurityTrusteeReportAndFees(this.selectedSecurityTrustee.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });
    }

    /**
     * updateSTReportAndFee()
     */
    updateSTReportAndFee(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(SecurityTrusteeReportAndFeeUpdateDialogComponent, {
            panelClass: 'fuse-securityTrustee-report-fee-update-dialog',
            width: '1126px',
            data: {
                operation: 'updateSecurityTrusteeReportAndFee',
                selectedSecurityTrustee: this.selectedSecurityTrustee,
                selectedSecurityTrusteeReportAndFee: this.selectedSecurityTrusteeReportAndFee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSecurityTrusteeReportAndFees(this.selectedSecurityTrustee.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * deleteSTReportAndFee()
     */
    deleteSTReportAndFee(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanAppraisalService.deleteSecurityTrusteeReportAndFee(this.selectedSecurityTrusteeReportAndFee, this._module).subscribe(() => {
                    this.selectedSecurityTrusteeReportAndFee = undefined;
                    this._loanAppraisalService.getSecurityTrusteeReportAndFees(this.selectedSecurityTrustee.id).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }
}
