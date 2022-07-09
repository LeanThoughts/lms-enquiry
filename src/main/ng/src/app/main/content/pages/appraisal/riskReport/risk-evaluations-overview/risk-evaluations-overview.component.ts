import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
    selector: 'fuse-risk-evaluations-overview',
    templateUrl: './risk-evaluations-overview.component.html',
    styleUrls: ['./risk-evaluations-overview.component.scss'],
    animations: fuseAnimations
})
export class RiskEvaluationsOverviewComponent {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'riskEvalId', 'wfStatusDesc', 'riskPrjType', 'riskPrjTypeT', 'projectName', 'FinalGrade', 'RiskPrjPhase', 'ratingDate', 'department', 'initiatedBy',
            'firstLvlApprover', 'secondLvlApprover', 'thirdLvlApprover', 'latestReviewer', 'LoanContractId'
    ];

    selectedRow: any;

    /**
     * constructor()
     */
    constructor(private _loanAppraisalService: LoanAppraisalService, private _loanEnquiryService: LoanEnquiryService) {
        console.log(_loanEnquiryService.selectedEnquiry.value);
        var selectedEnquiry = _loanEnquiryService.selectedEnquiry.value;
        this._loanAppraisalService.getRiskModelSummaryForLoanContractId(selectedEnquiry.loanContractId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
        });
    }

    /**
     * displayDetailedView()
     */
    displayDetailedView() {
    }

    /**
     * onRowSelect()
     */
    onRowSelect(obj: any): void {
        this.selectedRow = obj;
    }
}
