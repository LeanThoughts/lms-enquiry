import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { RiskAssessmentService } from '../riskAssessment.service';
import { PreliminaryRiskAssessmentUpdateDialogComponent } from '../riskAssessmentInfoUpdate/riskAssessmentInfoUpdate.component';

@Component({
    selector: 'fuse-preliminary-risk-assessment',
    templateUrl: './riskAssessmentInfo.component.html',
    styleUrls: ['./riskAssessmentInfo.component.scss'],
    animations: fuseAnimations
})
export class PreliminaryRiskAssessmentComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'particulars', 'value'
    ];

    loanApplicationId: string;

    preliminaryRiskAssessment: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _riskAssessmentService: RiskAssessmentService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._riskAssessmentService.getPreliminaryRiskAssessment(this._riskAssessmentService._riskAssessment.value.id).subscribe(data => {
            this.preliminaryRiskAssessment = data;
            let tableData = [];
            tableData.push({particulars: 'Date of Assessment', value: this.preliminaryRiskAssessment.dateOfAssessment});
            tableData.push({particulars: 'Remarks By Risk Department', value: this.preliminaryRiskAssessment.remarksByRiskDepartment});
            tableData.push({particulars: 'MD Approval Date', value: this.preliminaryRiskAssessment.mdApprovalDate});
            tableData.push({particulars: 'Remarks', value: this.preliminaryRiskAssessment.remarks});
            this.dataSource = new MatTableDataSource(tableData);
        });
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(PreliminaryRiskAssessmentUpdateDialogComponent, {
            panelClass: 'fuse-preliminary-risk-assessment-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                preliminaryRiskAssessment: this.preliminaryRiskAssessment
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
