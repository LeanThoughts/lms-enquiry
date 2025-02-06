import { Component } from '@angular/core';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { ActivatedRoute } from '@angular/router';
import { BMCICCReasonForDelayUpdateDialogComponent } from '../bmcIccReasonForDelayUpdate/bmcIccReasonForDelayUpdate.component';

@Component({
    selector: 'fuse-bmc-icc-reason-for-delay',
    templateUrl: './bmcIccReasonForDelay.component.html',
    styleUrls: ['./bmcIccReasonForDelay.component.scss'],
    animations: fuseAnimations
})
export class BMCICCReasonForDelayComponent {
    
    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'reasonForDelay', 'date'
    ];

    loanApplicationId: string;

    selectedReasonForDelay: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _bmcApprovalService: BMCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar, _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._bmcApprovalService.getReasonForDelay(this._bmcApprovalService._iccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.iCCReasonForDelays);
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
        const dialogRef = this._matDialog.open(BMCICCReasonForDelayUpdateDialogComponent, {
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
        const dialogRef = this._matDialog.open(BMCICCReasonForDelayUpdateDialogComponent, {
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
