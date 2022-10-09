import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../enquiryAction.service';
import { EnquiryCompletionUpdateComponent } from '../enquiryCompletionUpdate/enquiryCompletionUpdate.component';

@Component({
  selector: 'fuse-enquiry-completion',
  templateUrl: './enquiryCompletion.component.html',
  styleUrls: ['./enquiryCompletion.component.scss']
})
export class EnquiryCompletionComponent {

    _loanApplicationId: string;
    _enquiryActionId: string;

    dataSource1 = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];

    private _enquiryCompletion: any;
    
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

        this._enquiryCompletion = _activatedRoute.snapshot.data.routeResolvedData[3];
        
        this.populateDisplayTable();
    }
 
    /**
     * populateDisplayTable()
     */
    populateDisplayTable(): void {
        this.dataSource1 = [];
        this.dataSource1.push({particulars: 'Product Type', description: this._enquiryCompletion.productType    });
        this.dataSource1.push({particulars: 'Term', description: this._enquiryCompletion.term});
        this.dataSource1.push({particulars: 'Date of Completion', description: this.getFormattedDate(this._enquiryCompletion.date)});
        this.dataSource1.push({particulars: 'Remarks', description: this._enquiryCompletion.remarks});
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
     * openEnquiryCompletionDialog()
     */
     openEnquiryCompletionDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'enquiryActionId': this._enquiryActionId,
            'enquiryCompletion': this._enquiryCompletion
        };
        const dialogRef = this._dialogRef.open(EnquiryCompletionUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._enquiryCompletion = result.enquiryCompletion;
                this.populateDisplayTable();
            }
        });    
    }
}
