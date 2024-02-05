import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { DocumentationService } from '../documentation.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LLCFeeUpdateDialogComponent } from '../llcFeeUpdate/llcFeeUpdate.component';
import { LLCLegalCounselFeeUpdateDialogComponent } from '../llcLegalCounselFeeUpdate/llcLegalCounselFeeUpdate.component';

@Component({
    selector: 'fuse-llc-fee',
    templateUrl: './llcFeeAndLegalCounselFee.component.html',
    styleUrls: ['./llcFeeAndLegalCounselFee.component.scss'],
    animations: fuseAnimations
})
export class LLCFeeAndLegalCounselFeeComponent {

    dataSource1: MatTableDataSource<any>;
    @ViewChild(MatSort) sort1: MatSort;

    dataSource2: MatTableDataSource<any>;
    @ViewChild(MatSort) sort2: MatSort;

    loanApplicationId: string;

    displayedColumns1 = [
        'serialNumber', 'invoiceNumber', 'invoiceDate', 'feeName', 'feeAmount', 'statusOfFeeReceipt', 'fileReference', 'remarks'
    ];

    displayedColumns2 = [
        'serialNumber', 'invoiceNumber', 'invoiceDate', 'feeName', 'feeAmount', 'statusOfFeeReceipt', 'fileReference', 'remarks'
    ];

    selectedLLCFee: any;
    selectedLegalCounselFee: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _documentationService: DocumentationService, private _matDialog: MatDialog,
            private _matSnackBar: MatSnackBar) {
                
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;

        _documentationService.getLLCFees().subscribe(data => {
            this.dataSource1 = new MatTableDataSource(data._embedded.lLCFees);
            this.dataSource1.sort = this.sort1;
        });

        _documentationService.getLegalCounselFees().subscribe(data => {
            this.dataSource2 = new MatTableDataSource(data._embedded.legalCounselFees);
            this.dataSource2.sort = this.sort2;
        });
    }

    /**
     * onSelect()
     */
    onSelect(llcFee: any): void {
        this.selectedLLCFee = llcFee;
    }

    onLegalCounselFeeSelect(llcLegalCounselFee: any): void {
        this.selectedLegalCounselFee = llcLegalCounselFee;
    }

    /**
     * addLLCFee()
     */
    addLLCFee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LLCFeeUpdateDialogComponent, {
            panelClass: 'fuse-llc-fee-update-dialog',
            width: '750px',
            data: {
                operation: 'addLLCFee',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._documentationService._documentation.value.id !== '') {
                    this._documentationService.getLLCFees().subscribe(data => {
                        this.dataSource1 = new MatTableDataSource(data._embedded.lLCFees);
                        this.dataSource1.sort = this.sort1;
                    });    
                }
                else {
                    this._documentationService.getDocumentation(this.loanApplicationId).subscribe(data => {
                        this._documentationService._documentation.next(data);
                        this._documentationService.getLLCFees().subscribe(data => {
                            this.dataSource1 = new MatTableDataSource(data._embedded.lLCFees);
                            this.dataSource1.sort = this.sort1;
                        });  
                    });
                }
            }
        });    
    }

    /**
     * updateLLCFee()
     */
    updateLLCFee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LLCFeeUpdateDialogComponent, {
            panelClass: 'fuse-llc-fee-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLLCFee',
                loanApplicationId: this.loanApplicationId,
                selectedLLCFee: this.selectedLLCFee
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getLLCFees().subscribe(data => {
                    this.dataSource1 = new MatTableDataSource(data._embedded.lLCFees);
                    this.dataSource1.sort = this.sort1;
                });    
            }
        });    
    }

    /**
     * deleteLLCFee()
     */
    deleteLLCFee(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._documentationService.deleteLLCFee(this.selectedLLCFee.id).subscribe(() => {
                    this.selectedLLCFee = undefined;
                    this._documentationService.getLLCFees().subscribe(data => {
                        this.dataSource1 = new MatTableDataSource(data._embedded.lLCFees);
                        this.dataSource1.sort = this.sort1;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected llc fee.', 'OK', { duration: 7000 });
                });
            }
        });
    }

    /**
     * addLegalCounselFee()
     */
    addLegalCounselFee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LLCLegalCounselFeeUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-fee-update-dialog',
            width: '750px',
            data: {
                operation: 'addLegalCounselFee',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._documentationService._documentation.value.id !== '') {
                    this._documentationService.getLegalCounselFees().subscribe(data => {
                        this.dataSource2 = new MatTableDataSource(data._embedded.legalCounselFees);
                        this.dataSource2.sort = this.sort2;
                    });    
                }
                else {
                    this._documentationService.getDocumentation(this.loanApplicationId).subscribe(data => {
                        this._documentationService._documentation.next(data);
                        this._documentationService.getLegalCounselFees().subscribe(data => {
                            this.dataSource2 = new MatTableDataSource(data._embedded.legalCounselFees);
                            this.dataSource2.sort = this.sort2;
                        });  
                    });
                }
            }
        });    
    }

    /**
     * updateLegalCounselFee()
     */
    updateLegalCounselFee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LLCLegalCounselFeeUpdateDialogComponent, {
            panelClass: 'fuse-legal-counsel-fee-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLegalCounselFee',
                loanApplicationId: this.loanApplicationId,
                selectedLLCFee: this.selectedLLCFee
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getLegalCounselFees().subscribe(data => {
                    this.dataSource2 = new MatTableDataSource(data._embedded.legalCounselFees);
                    this.dataSource2.sort = this.sort2;
                });    
            }
        });    
    }

    /**
     * deleteLegalCounselFee()
     */
    deleteLegalCounselFee(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._documentationService.deleteLLCFee(this.selectedLLCFee.id).subscribe(() => {
                    this.selectedLLCFee = undefined;
                    this._documentationService.getLegalCounselFees().subscribe(data => {
                        this.dataSource2 = new MatTableDataSource(data._embedded.legalCounselFees);
                        this.dataSource2.sort = this.sort2;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected legal counsel fee.', 'OK', { duration: 7000 });
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
