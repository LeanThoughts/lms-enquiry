import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ApplicationFeeService } from '../applicationFee.service';
import { FormalRequestUpdateDialogComponent } from '../formalRequestUpdate/formalRequestUpdate.component';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-formal-request',
    templateUrl: './formalRequest.component.html',
    styleUrls: ['./formalRequest.component.scss'],
    animations: fuseAnimations
})
export class FormalRequestComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'documentName', 'uploadDate', 'documentLetterDate', 'documentReceivedDate', 'download'
    ];

    loanApplicationId: string;

    selectedFormalRequest: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, 
                _activatedRoute: ActivatedRoute,
                private _applicationFeeService: ApplicationFeeService, 
                private _matDialog: MatDialog,
                private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._applicationFeeService.getFormalRequests(this._applicationFeeService._applicationFee.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.formalRequests);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedFormalRequest = row;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(FormalRequestUpdateDialogComponent, {
            panelClass: 'fuse-formal-request-update-dialog',
            width: '850px',
            data: {
                operation: 'addFormalRequest',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._applicationFeeService._applicationFee.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                        this._applicationFeeService._applicationFee.next(data);
                        this.refreshTable(); 
                    });
                }
            }
        });    
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(FormalRequestUpdateDialogComponent, {
            panelClass: 'fuse-formal-request-update-dialog',
            width: '750px',
            data: {
                operation: 'updateFormalRequest',
                loanApplicationId: this.loanApplicationId,
                selectedFormalRequest: this.selectedFormalRequest
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.refreshTable();
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
                this._applicationFeeService.deleteFormalRequest(this.selectedFormalRequest.id).subscribe(() => {
                    this.selectedFormalRequest = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected formal request details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
