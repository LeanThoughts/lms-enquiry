import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../enquiryAction.service';
import { RejectByCustomerUpdateComponent } from '../rejectByCustomerUpdate/rejectByCustomerUpdate.component';

@Component({
  selector: 'fuse-reject-by-customer',
  templateUrl: './rejectByCustomer.component.html',
  styleUrls: ['./rejectByCustomer.component.scss']
})
export class RejectByCustomerComponent {

    _loanApplicationId: string;
    _enquiryActionId: string;

    dataSource1 = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];

    private _rejectBy: any;
    
    /**
     * constructor()
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                private _activatedRoute: ActivatedRoute,
                _enquiryActionService: EnquiryActionService, 
                public datepipe: DatePipe) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._enquiryActionId = _enquiryActionService._enquiryAction.value.id;

        this._rejectBy = _activatedRoute.snapshot.data.routeResolvedData[2];
        
        this.populateDisplayTable();
    }
 
    /**
     * populateDisplayTable()
     */
    populateDisplayTable(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Category', description: this._rejectBy.rejectionCategory});
        this.dataSource1.push({particulars: 'Reason for Customer Rejection', description: this._rejectBy.rejectionReason});
        this.dataSource1.push({particulars: 'Date of Rejection', description: this.getFormattedDate(this._rejectBy.rejectionDate)});
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
     * openRejectionDialog()
     */
     openRejectionDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'enquiryActionId': this._enquiryActionId,
            'rejectBy': this._rejectBy
        };
        const dialogRef = this._dialogRef.open(RejectByCustomerUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._rejectBy = result.rejectBy;
                this.populateDisplayTable();
            }
        });    
    }
}
