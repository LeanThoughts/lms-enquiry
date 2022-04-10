import { Component, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource, } from '@angular/material';
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

    dataSource1 = [];
    dataSource2 = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];
    
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

        this.populateDisplayTables();
    }

    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        console.log('populating displaying tables now');
        this.dataSource1 = [];
        this.dataSource2 = [];
        this.dataSource1.push({particulars: 'Rate of Interest (Pre COD) %', description: this._proposalDetail.rateOfInterestPreCod});
        this.dataSource1.push({particulars: 'Rate of Interest (Post COD) %', description: this._proposalDetail.rateOfInterestPostCod});
        this.dataSource1.push({particulars: 'Spread Reset (From COD)', description: this._proposalDetail.spreadReset});
        this.dataSource1.push({particulars: 'Spread Reset Unit', description: this.getUnitDescription(this._proposalDetail.spreadResetUnit)});
        this.dataSource1.push({particulars: 'Effective Rate of Interest %', description: this._proposalDetail.effectiveRateOfInterest});
        this.dataSource1.push({particulars: 'Construction Period', description: this._proposalDetail.constructionPeriod});
        this.dataSource1.push({particulars: 'Construction Period Unit', description: this.getUnitDescription(this._proposalDetail.constructionPeriodUnit)});
        this.dataSource1.push({particulars: 'Moratorium Period', description: this._proposalDetail.moratoriumPeriod});
        this.dataSource1.push({particulars: 'Moratorium Period Unit', description: this.getUnitDescription(this._proposalDetail.moratoriumPeriodUnit)});
        
        this.dataSource2.push({particulars: 'Repayment Period', description: this._proposalDetail.repaymentPeriod});
        this.dataSource2.push({particulars: 'Repayment Period Unit', description: this.getUnitDescription(this._proposalDetail.repaymentPeriodUnit)});
        this.dataSource2.push({particulars: 'Tenor', description: this._proposalDetail.tenor});
        this.dataSource2.push({particulars: 'Tenor Unit', description: this.getUnitDescription(this._proposalDetail.tenorUnit)});
        this.dataSource2.push({particulars: 'Availability Period', description: this._proposalDetail.availabilityPeriod});
        this.dataSource2.push({particulars: 'Availability Period Unit', description: this.getUnitDescription(this._proposalDetail.availabilityPeriodUnit)});
        this.dataSource2.push({particulars: 'Pre Payment Charges', description: this._proposalDetail.prePaymentCharges});
        this.dataSource2.push({particulars: 'Fee Details Schedule', description: this._proposalDetail.feeDetailsSchedule});    
    }

    /**
     * getUnitDescription()
     */
    getUnitDescription(unit: string): string {
        if (unit === '1') {
            return 'Days';
        }
        else if (unit === '2') {
            return 'Weeks';
        }
        else if (unit === '3') {
            return 'Months';
        }
        else if (unit === '4') {
            return 'Years';
        }
        else {
            return '';
        }
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
            width: '1000px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._proposalDetail = result.proposalDetail;
                this.populateDisplayTables();
            }
        });
    }

    /**
     * onRowSelect()
     */
    onRowSelect(obj: any): void {

    }
}
