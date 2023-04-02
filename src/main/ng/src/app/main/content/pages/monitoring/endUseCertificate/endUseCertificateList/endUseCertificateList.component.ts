import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { EndUseCertificateUpdateDialogComponent } from '../endUseCertificateUpdate/endUseCertificateUpdate.component';

@Component({
    selector: 'fuse-end-use-certificate-list',
    templateUrl: './endUseCertificateList.component.html',
    styleUrls: ['./endUseCertificateList.component.scss'],
    animations: fuseAnimations
})
export class EndUseCertificateListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'eventDate', 'endUseCertificateDate', 'endUseCertificateDueDate', 'documentType','documentTitle', 'download'
    ];

    selectedEndUseCertificate: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getEndUseCertificates(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * addEndUseCertificate()
     */
    addEndUseCertificate(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(EndUseCertificateUpdateDialogComponent, {
            panelClass: 'fuse-end-use-certificate-update-dialog',
            width: '750px',
            data: {
                operation: 'addEndUseCertificate',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getEndUseCertificates(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateEndUseCertificate()
     */
    updateEndUseCertificate(): void {
        //Open the dialog.
        const dialogRef = this._dialog.open(EndUseCertificateUpdateDialogComponent, {
            panelClass: 'fuse-end-use-certificate-update-dialog',
            width: '750px',
            data: {
                operation: 'updateEndUseCertificate',
                loanApplicationId: this.loanApplicationId,
                selectedEndUseCertificate: this.selectedEndUseCertificate
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getEndUseCertificates(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * onSelect()
     */
    onSelect(selectedEndUseCertificate: any): void {
        this.selectedEndUseCertificate = selectedEndUseCertificate;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getDocumentTypeDescription()
     */
    getDocumentTypeDescription(documentType: string): string {
        const obj = LoanMonitoringConstants.documentTypes.filter(f => f.code === documentType)[0];
        if (obj !== undefined)
            return obj.value;
        else
            return '';
    }

    /**
     * deleteEndUseCertificate()
     */
    deleteEndUseCertificate(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteEndUseCertificate(this.selectedEndUseCertificate).subscribe(() => {
                    this.selectedEndUseCertificate = undefined;
                    this._loanMonitoringService.getEndUseCertificates(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }
}
