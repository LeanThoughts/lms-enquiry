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
        'particulars', 'value'
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
            console.log('reason for delay in refresh table is', data);
            this.selectedReasonForDelay = data;
            let tableData = [];
            tableData.push({particulars: 'Reason For Delay', value: this.selectedReasonForDelay.reasonForDelay});
            tableData.push({particulars: 'Date', value: this.selectedReasonForDelay.date});
            console.log(tableData);
            this.dataSource = new MatTableDataSource(tableData);
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
