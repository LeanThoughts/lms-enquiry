import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../enquiryAction.service';
import { OtherDetailsUpdateComponent } from '../otherDetailsUpdate/otherDetailsUpdate.component';

@Component({
  selector: 'fuse-other-details',
  templateUrl: './otherDetails.component.html',
  styleUrls: ['./otherDetails.component.scss']
})
export class OtherDetailsComponent {

    _loanApplicationId: string;
    _enquiryActionId: string;

    dataSource1 = [];
    dataSource2 = [];

    displayedColumns = [
        'particulars', 'description'
    ];

    private _otherDetails: any;
    
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

        this._otherDetails = _activatedRoute.snapshot.data.routeResolvedData[4];
        
        this.populateDisplayTables();
    }
 
    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Name of the Sourcing Company', description: this._otherDetails.nameOfSourcingCompany});
        this.dataSource1.push({particulars: 'Contact Person Name', description: this._otherDetails.contactPersonName});
        this.dataSource1.push({particulars: 'Contact Number', description: this._otherDetails.contactNumber});
        this.dataSource1.push({particulars: 'Email', description: this._otherDetails.email});
        this.dataSource1.push({particulars: 'Enquiry Date', description: this.getFormattedDate(this._otherDetails.enquiryDate)});

        this.dataSource2 = [];
        this.dataSource2.push({particulars: 'Rating', description: this._otherDetails.rating});
        this.dataSource2.push({particulars: 'Credit Standing', description: this._otherDetails.creditStanding});
        this.dataSource2.push({particulars: 'Credit Standing Instruction', description: this._otherDetails.creditStandingInstruction});
        this.dataSource2.push({particulars: 'Credit Standing Text', description: this._otherDetails.creditStandingText});
        this.dataSource2.push({particulars: 'Rating Date', description: this.getFormattedDate(this._otherDetails.ratingDate)});
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
     * openOtherDetailsDialog()
     */
     openOtherDetailsDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'enquiryActionId': this._enquiryActionId,
            'otherDetails': this._otherDetails
        };
        const dialogRef = this._dialogRef.open(OtherDetailsUpdateComponent, {
            width: '800px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._otherDetails = result.otherDetails;
                this.populateDisplayTables();
            }
        });    
    }
}
