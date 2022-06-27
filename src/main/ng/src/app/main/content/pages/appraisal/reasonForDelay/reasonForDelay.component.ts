import { DatePipe } from '@angular/common';
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
        'particulars', 'description'
    ];

    private _reasonForDelay: any;
    
    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                private _activatedRoute: ActivatedRoute,
                _loanAppraisalService: LoanAppraisalService, public datepipe: DatePipe) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;

        this._reasonForDelay = _activatedRoute.snapshot.data.routeResolvedData[8];        
        
        this.populateDisplayTables();
    }
 
    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Status of Proposal/ Reason for Delay (if any)', description: this._reasonForDelay.statusOfProposal});
        this.dataSource1.push({particulars: 'Date', description: this.getFormattedDate(this._reasonForDelay.date)});
        this.dataSource1.push({particulars: 'Type', description: this.getHeldBy(this._reasonForDelay.heldBy)});
    }

    /**
     * getFormattedDate()
     */
    getFormattedDate(dt: any): string {
        if (dt) {
            return this.datepipe.transform(dt, 'dd-MM-yyyy')
        }
        else {
            return '';
        }
    }

    /**
     * getHeldBy()
     */
    getHeldBy(heldBy: any): string {
        if (heldBy === '1') {
            return 'With Appraisal';
        }
        else {
            return 'With Risk';
        }
    }

    /**
     * openReasonForDelayDialog()
     */
     openReasonForDelayDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'reasonForDelay': this._reasonForDelay
        };
        const dialogRef = this._dialogRef.open(ReasonForDelayUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._reasonForDelay = result.reasonForDelay;
                this.populateDisplayTables();
            }
        });    
    }
}
