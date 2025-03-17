import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../loanAppraisal.service';
import { ReasonForDelayUpdateComponent } from '../reasonForDelayUpdate/reasonForDelayUpdate.component';

@Component({
  selector: 'fuse-reason-for-delay',
  templateUrl: './reasonForDelay.component.html',
  styleUrls: ['./reasonForDelay.component.scss']
})
export class ReasonForDelayComponent {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    dataSource1 = [];
    
    displayedColumns = [
        'reasonForDelay'
    ];

    private selectedReasonForDelay: any;
    
    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                private _activatedRoute: ActivatedRoute,
                public _loanAppraisalService: LoanAppraisalService) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        if (Object.keys(_activatedRoute.snapshot.data.routeResolvedData[8]).length > 0) {
            this.dataSource1 = _activatedRoute.snapshot.data.routeResolvedData[8]._embedded.reasonForDelays;
        }
    }
 
    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        this._loanAppraisalService.getReasonForDelay(this._loanAppraisalId).subscribe(response => {
            this.dataSource1 = response._embedded.reasonForDelays;
        });
    }

    /**
     * onSelect()
     */
    onSelect(obj: any): void {
        this.selectedReasonForDelay = obj;
    }

    /**
     * openReasonForDelayDialog()
     */
     openReasonForDelayDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'selectedReasonForDelay': (operation === 'update' || operation === 'view') ? this.selectedReasonForDelay : null,
            'operation': operation
        };
        const dialogRef = this._dialogRef.open(ReasonForDelayUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this.populateDisplayTables();
            }
        });    
    }
}
