import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCApprovalUpdateDialogComponent } from '../iccApprovalUpdate/iccApprovalUpdate.component';

@Component({
    selector: 'fuse-icc-approval-meeting',
    templateUrl: './iccApproval.component.html',
    styleUrls: ['./iccApproval.component.scss'],
    animations: fuseAnimations
})
export class ICCApprovalMeetingComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'particulars', 'value'
    ];

    loanApplicationId: string;

    selectedICCApproval: any;

    disableAdd = false;

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
        this._iccApprovalService.getApprovalByICC(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.selectedICCApproval = data;
            let tableData = [];
            tableData.push({particulars: 'Meeting Number', value: this.selectedICCApproval.meetingNumber});
            tableData.push({particulars: 'Date', value: this.selectedICCApproval.meetingDate});
            tableData.push({particulars: 'Remarks', value: this.selectedICCApproval.remarks});
            this.dataSource = new MatTableDataSource(tableData);
        });
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCApprovalUpdateDialogComponent, {
            panelClass: 'fuse-icc-approval-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                selectedICCApproval: this.selectedICCApproval
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
