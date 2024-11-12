import { Component } from '@angular/core';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCReasonForDelayUpdateDialogComponent } from '../iccReasonForDelayUpdate/iccReasonForDelayUpdate.component';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-icc-reason-for-delay',
    templateUrl: './iccReasonForDelay.component.html',
    styleUrls: ['./iccReasonForDelay.component.scss'],
    animations: fuseAnimations
})
export class ICCReasonForDelayComponent {
    
    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'reasonForDelay', 'date'
    ];

    loanApplicationId: string;

    selectedReasonForDelay: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar, _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._iccApprovalService.getReasonForDelay(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
        });
    }

    /**
     * onSelect()
     */
    onSelect(reasonForDelay: any): void {
        this.selectedReasonForDelay = reasonForDelay;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-icc-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
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
     * update()
     */
    update(): void {
        console.log('selected reason for delay is', this.selectedReasonForDelay);
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-icc-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                selectedReasonForDelay: this.selectedReasonForDelay
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.refreshTable();
            }
        });
    }
}
