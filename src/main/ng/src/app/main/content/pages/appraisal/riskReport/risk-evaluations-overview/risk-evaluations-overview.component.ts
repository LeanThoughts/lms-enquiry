import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { RiskEvaluationDetails } from '../risk-evaluation-details/risk-evaluation-details.component';

@Component({
    selector: 'fuse-risk-evaluations-overview',
    templateUrl: './risk-evaluations-overview.component.html',
    styleUrls: ['./risk-evaluations-overview.component.scss'],
    animations: fuseAnimations
})
export class RiskEvaluationsOverviewComponent {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'riskEvalId', 'wfStatusDesc', 'riskPrjType', 'riskPrjTypeT', 'projectName', 'FinalGrade', 'RiskPrjPhase', 'ratingDate', 'department', 'initiatedBy'
    ];

    selectedRow: any;

    /**
     * constructor()
     */
    constructor(private _loanAppraisalService: LoanAppraisalService, 
                _loanEnquiryService: LoanEnquiryService, 
                private _dialogRef: MatDialog) {

        var selectedEnquiry = _loanEnquiryService.selectedEnquiry.value;
        this._loanAppraisalService.getRiskModelSummaryForLoanContractId(selectedEnquiry.loanContractId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
        });
    }

    /**
     * displayDetailedView()
     */
    displayDetailedView() {
        // Open the dialog.
        var data = {
            'selectedRisk': this.selectedRow
        };
        const dialogRef = this._dialogRef.open(RiskEvaluationDetails, {
            width: '1200px',
            data: data
        }); 
    }

    /**
     * onRowSelect()
     */
    onRowSelect(obj: any): void {
        this.selectedRow = obj;
    }
}
