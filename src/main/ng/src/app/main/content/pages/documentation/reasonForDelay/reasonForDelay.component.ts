import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { DocumentationService } from '../documentation.service';
import { DocumentationReasonForDelayUpdateDialogComponent } from '../reasonForDelayUpdate/reasonForDelayUpdate.component';

@Component({
    selector: 'fuse-documentation-reason-for-delay',
    templateUrl: './reasonForDelay.component.html',
    styleUrls: ['./reasonForDelay.component.scss'],
    animations: fuseAnimations
})
export class DocumentationReasonForDelayComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'date', 'reason'
    ];

    loanApplicationId: string;

    selectedReason: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _documentationService: DocumentationService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _documentationService.getReasonForDelays().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.documentationReasonForDelays);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(lfa: any): void {
        this.selectedReason = lfa;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(DocumentationReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-documentation-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'addReason',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._documentationService._documentation.value.id !== '') {
                    this._documentationService.getReasonForDelays().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.documentationReasonForDelays);
                        this.dataSource.sort = this.sort;
                    });    
                }
                else {
                    this._documentationService.getDocumentation(this.loanApplicationId).subscribe(data => {
                        this._documentationService._documentation.next(data);
                        this._documentationService.getReasonForDelays().subscribe(data => {
                            this.dataSource = new MatTableDataSource(data._embedded.documentationReasonForDelays);
                            this.dataSource.sort = this.sort;
                        });  
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
        const dialogRef = this._matDialog.open(DocumentationReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-sanction-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'updateReason',
                loanApplicationId: this.loanApplicationId,
                selectedReason: this.selectedReason
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getReasonForDelays().subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.documentationReasonForDelays);
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
                this._documentationService.deleteDocumentationReasonForDelay(this.selectedReason.id).subscribe(() => {
                    this.selectedReason = undefined;
                    this._documentationService.getReasonForDelays().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.documentationReasonForDelays);
                        this.dataSource.sort = this.sort;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected reason for delay.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
