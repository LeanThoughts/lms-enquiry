import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../enquiryAction.service';
import { EnquiryActionReasonForDelayUpdateComponent } from '../enquiryActionReasonForDelayUpdate/enquiryActionReasonForDelayUpdate.component';

@Component({
  selector: 'fuse-enquiry-action-reason-for-delay',
  templateUrl: './enquiryActionReasonForDelay.component.html',
  styleUrls: ['./enquiryActionReasonForDelay.component.scss']
})
export class EnquiryActionReasonForDelayComponent {

    _loanApplicationId: string;
    _enquiryActionId: string;

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
                _enquiryActionService: EnquiryActionService, 
                public datepipe: DatePipe) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._enquiryActionId = _enquiryActionService._enquiryAction.value.id;

        this._reasonForDelay = _activatedRoute.snapshot.data.routeResolvedData[0];        
        
        this.populateDisplayTable();
    }
 
    /**
     * populateDisplayTable()
     */
    populateDisplayTable(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Reason to hold the Enquiry', description: this._reasonForDelay.reason});
        this.dataSource1.push({particulars: 'Date', description: this.getFormattedDate(this._reasonForDelay.date)});
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
     * openReasonForDelayDialog()
     */
    openReasonForDelayDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'enquiryActionId': this._enquiryActionId,
            'reasonForDelay': this._reasonForDelay
        };
        const dialogRef = this._dialogRef.open(EnquiryActionReasonForDelayUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._reasonForDelay = result.reasonForDelay;
                this.populateDisplayTable();
            }
        });    
    }
}
