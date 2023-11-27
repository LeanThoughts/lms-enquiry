import { Component } from '@angular/core';
import { MatTableDataSource, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { RejectedByICCUpdateDialogComponent } from '../rejectedByICCUpdate/rejectedByICCUpdate.component';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-rejected-by-icc',
    templateUrl: './rejectedByICC.component.html',
    styleUrls: ['./rejectedByICC.component.scss'],
    animations: fuseAnimations
})
export class RejectedByICCComponent {
    
    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'particulars', 'value'
    ];

    loanApplicationId: string;

    selectedRejectedByICC: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._iccApprovalService.getRejectedByICC(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.selectedRejectedByICC = data;
            let tableData = [];
            tableData.push({particulars: 'Meeting Number', value: this.selectedRejectedByICC.meetingNumber});
            tableData.push({particulars: 'Date', value: this.selectedRejectedByICC.meetingDate});
            tableData.push({particulars: 'Reason For Rejection', value: this.selectedRejectedByICC.reasonForRejection});
            this.dataSource = new MatTableDataSource(tableData);
        });
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(RejectedByICCUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-icc-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByICC: this.selectedRejectedByICC
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
