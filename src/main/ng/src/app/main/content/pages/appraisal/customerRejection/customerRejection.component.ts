import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { CustomerRejectionUpdateComponent } from '../customerRejectionUpdate/customerRejectionUpdate.component';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-customer-rejection',
  templateUrl: './customerRejection.component.html',
  styleUrls: ['./customerRejection.component.scss']
})
export class CustomerRejectionComponent {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    dataSource1 = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];

    private _customerRejection: any;
    
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

        this._customerRejection = _activatedRoute.snapshot.data.routeResolvedData[9];     
        
        this.populateDisplayTables();
    }
 
    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Reason for Customer Rejection', description: this._customerRejection.reasonForRejection});
        this.dataSource1.push({particulars: 'Date', description: this.getFormattedDate(this._customerRejection.date)});
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
     * openCustomerRejectionUpdateDialog()
     */
    openCustomerRejectionUpdateDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'customerRejection': this._customerRejection
        };
        const dialogRef = this._dialogRef.open(CustomerRejectionUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._customerRejection = result.customerRejection;
                this.populateDisplayTables();
            }
        });   
    }
}
