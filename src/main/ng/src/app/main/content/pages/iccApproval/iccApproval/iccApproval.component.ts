import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCApprovalUpdateDialogComponent } from '../iccApprovalUpdate/iccApprovalUpdate.component';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

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

    enquiryCompletion: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar, private _datePipe: DatePipe, private _activatedRoute: ActivatedRoute) {

        console.log('activated route', _activatedRoute);
        this.enquiryCompletion = _activatedRoute.snapshot.data.routeResolvedData[3];
        console.log('enquiry completion', this.enquiryCompletion);

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
            tableData.push({particulars: 'Remarks', value: this.selectedICCApproval.remarks});
            tableData.push({particulars: 'Meeting Date', value: 
                this._datePipe.transform(this.selectedICCApproval.meetingDate, 'dd/MM/yyyy')});
            tableData.push({particulars: 'ED Approval Date', value: 
                this._datePipe.transform(this.selectedICCApproval.edApprovalDate, 'dd/MM/yyyy')});
            tableData.push({particulars: 'CFO Approval Date', value: 
                this._datePipe.transform(this.selectedICCApproval.cfoApprovalDate, 'dd/MM/yyyy')});
            if (this.selectedICCApproval.fileReference1 !== '') {
                tableData.push({particulars: 'Minutes Document', value: this.selectedICCApproval.fileReference1});
            }
            if (this.selectedICCApproval.fileReference2 !== '') {
                tableData.push({particulars: 'Mail from Company Secretary/Internal Note Sheet', value: this.selectedICCApproval.fileReference2});
            }
            this.dataSource = new MatTableDataSource(tableData);
        });
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
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
                selectedICCApproval: this.selectedICCApproval,
                enquiryCompletion: this.enquiryCompletion
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
