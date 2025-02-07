import { Component } from '@angular/core';
import { MatTableDataSource, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BMCRejectedByICCUpdateDialogComponent } from '../bmcRejectedByIccUpdate/bmcRejectedByIccUpdate.component';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-bmc-rejected-by-icc',
    templateUrl: './bmcRejectedByIcc.component.html',
    styleUrls: ['./bmcRejectedByIcc.component.scss'],
    animations: fuseAnimations
})
export class BMCRejectedByICCComponent {
    
    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'particulars', 'value'
    ];

    loanApplicationId: string;

    selectedRejectedByICC: any;
    enquiryCompletion: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _bmcApprovalService: BMCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar, private _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.enquiryCompletion = this._activatedRoute.snapshot.data.routeResolvedData[3];
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._bmcApprovalService.getBmcRejectedByICC(this._bmcApprovalService._bmcIccApproval.value.id).subscribe(data => {
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
        const dialogRef = this._matDialog.open(BMCRejectedByICCUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-icc-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByICC: this.selectedRejectedByICC,
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
