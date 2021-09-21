import { Component, OnInit } from '@angular/core';
import { MatDialog, } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { ProposalDetailsUpdateComponent } from '../proposal-details-update/proposal-details-update.component';

@Component({
    selector: 'fuse-proposal-details',
    templateUrl: './proposal-details.component.html',
    styleUrls: ['./proposal-details.component.scss'],
    animations: fuseAnimations
})
export class ProposalDetailsComponent {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    _proposalDetail: any;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                _activatedRoute: ActivatedRoute,
                _loanAppraisalService: LoanAppraisalService) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        this._proposalDetail = _activatedRoute.snapshot.data.routeResolvedData[4];
    }

    /**
     * openUpdateDialog()
     * @param operation 
     */
    openUpdateDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'proposalDetail': this._proposalDetail
        };
        const dialogRef = this._dialogRef.open(ProposalDetailsUpdateComponent, {
            width: '850px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._proposalDetail = result.proposalDetail;
            }
        });
    }
}
