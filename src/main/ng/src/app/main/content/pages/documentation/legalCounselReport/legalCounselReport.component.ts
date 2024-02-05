import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { DocumentationService } from '../documentation.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { Subscription } from 'rxjs';
import { LegalCounselReportUpdateDialogComponent } from '../legalCounselReportUpdate/legalCounselReportUpdate.component';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-legal-counsel-report',
    templateUrl: './legalCounselReport.component.html',
    styleUrls: ['./legalCounselReport.component.scss'],
    animations: fuseAnimations
})
export class LegalCounselReportComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'submissionDate', 'fiscalYear', 'period', 'documentName', 'documentType', 'fileReference', 'remarks'
    ];

    selectedLegalCounselReport: any;

    selectedLegalCounselId: string;

    subscriptions = new Subscription()

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _documentationService: DocumentationService, private _matDialog: MatDialog,
            private _matSnackBar: MatSnackBar) {
                
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.subscriptions.add(_documentationService._selectedLegalCounsel.subscribe(legalCounsel => {
            this.selectedLegalCounselId = legalCounsel.id;
            console.log('legal counsel is', this.selectedLegalCounselId);

            _documentationService.getLegalCounselReports(this.selectedLegalCounselId).subscribe(data => {
                this.dataSource = new MatTableDataSource(data._embedded.legalCounselReports);
                this.dataSource.sort = this.sort;
            });    
        }));
    }

    /**
     * onSelect()
     */
    onSelect(counselReport: any): void {
        this.selectedLegalCounselReport = counselReport;
    }

    /**
     * addLegalCounselReport()
     */
    addLegalCounselReport(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LegalCounselReportUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-report-update-dialog',
            width: '750px',
            data: {
                operation: 'addLegalCounselReport',
                selectedLegalCounselId: this._documentationService._selectedLegalCounsel.value.id
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getLegalCounselReports(this.selectedLegalCounselId).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.legalCounselReports);
                    this.dataSource.sort = this.sort;
                });
            }
        });    
    }

    /**
     * updateLegalCounselReport()
     */
    updateLegalCounselReport(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LegalCounselReportUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-report-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLegalCounselReport',
                selectedLegalCounselReport: this.selectedLegalCounselReport
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getLegalCounselReports(this.selectedLegalCounselId).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.legalCounselReports);
                    this.dataSource.sort = this.sort;
                });    
            }
        });    
    }

    /**
     * delete()
     */
    delete(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._documentationService.deleteLegalCounselReport(this.selectedLegalCounselReport.id).subscribe(() => {
                    this.selectedLegalCounselReport = undefined;
                    this._documentationService.getLegalCounselReports(this.selectedLegalCounselId).subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.legalCounselReports);
                        this.dataSource.sort = this.sort;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected legal counsel report.', 'OK', { duration: 7000 });
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
     * getPeriod()
     */
    getPeriod(period: string): string {
        if (period === '')
            return '';
        else if (period ==='1')
            return 'First Quarter';
        else if (period ==='2')
            return 'Second Quarter';
        else if (period ==='4')
            return 'Third Quarter';
        else
            return 'Fourth Quarter';
    }
}
