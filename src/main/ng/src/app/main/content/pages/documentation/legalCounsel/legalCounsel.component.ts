import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LegalCounselUpdateDialogComponent } from '../legalCounselUpdate/legalCounselUpdate.component';
import { DocumentationService } from '../documentation.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-legal-counsel',
    templateUrl: './legalCounsel.component.html',
    styleUrls: ['./legalCounsel.component.scss'],
    animations: fuseAnimations
})
export class LegalCounselComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'bpCode', 'name', 'startDate', 'endDate', 'documentName', 'documentType', 'fileReference', 'remarks'
    ];

    selectedLegalCounsel: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _documentationService: DocumentationService, private _matDialog: MatDialog,
            private _matSnackBar: MatSnackBar) {
                
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _documentationService.getLegalCounsels().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.legalCounsels);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(counsel: any): void {
        this.selectedLegalCounsel = counsel;
        this._documentationService._selectedLegalCounsel.next(counsel);
    }

    /**
     * addLegalCounsel()
     */
    addLegalCounsel(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LegalCounselUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-update-dialog',
            width: '750px',
            data: {
                operation: 'addLegalCounsel',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._documentationService._documentation.value.id !== '') {
                    this._documentationService.getLegalCounsels().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.legalCounsels);
                        this.dataSource.sort = this.sort;
                    });    
                }
                else {
                    this._documentationService.getDocumentation(this.loanApplicationId).subscribe(data => {
                        this._documentationService._documentation.next(data);
                        this._documentationService.getLegalCounsels().subscribe(data => {
                            this.dataSource = new MatTableDataSource(data._embedded.legalCounsels);
                            this.dataSource.sort = this.sort;
                        });  
                    });
                }
            }
        });    
    }

    /**
     * updateLegalCounsel()
     */
    updateLegalCounsel(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LegalCounselUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLegalCounsel',
                loanApplicationId: this.loanApplicationId,
                selectedLegalCounsel: this.selectedLegalCounsel
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getLegalCounsels().subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.legalCounsels);
                    this.dataSource.sort = this.sort;
                });    
            }
        });    
    }

    delete(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._documentationService.deleteLegalCounsel(this.selectedLegalCounsel.id).subscribe(() => {
                    this.selectedLegalCounsel = undefined;
                    this._documentationService.getLegalCounsels().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.legalCounsels);
                        this.dataSource.sort = this.sort;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected legal counsel.', 'OK', { duration: 7000 });
                });
            }
        });
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
}
