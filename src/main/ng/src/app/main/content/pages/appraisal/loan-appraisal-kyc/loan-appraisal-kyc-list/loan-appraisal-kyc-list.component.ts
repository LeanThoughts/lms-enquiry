import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { LoanAppraisalKYCUpdateComponent } from '../loan-appraisal-kyc-update/loan-appraisal-kyc-update.component';

@Component({
    selector: 'fuse-loan-appraisal-kyc-list',
    templateUrl: './loan-appraisal-kyc-list.component.html',
    styleUrls: ['./loan-appraisal-kyc-list.component.scss'],
    animations: fuseAnimations
})
export class LoanAppraisalKYCListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'kycType', 'dateOfCompletion', 'remarks', 'documentName', 'download'
    ];

    selectedKYC: any;

    _loanApplicationId: string;
    _loanAppraisalId: string;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                private _loanAppraisalService: LoanAppraisalService,
                _activatedRoute: ActivatedRoute) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;

        // Subscribe to route resolved data (data.routeResolvedData[0] = loanPartners) ...
        _activatedRoute.data.subscribe(data => {
            console.log('knowYourCustomers', data.routeResolvedData[1]);
            if (JSON.stringify(data.routeResolvedData[1]) !== JSON.stringify({}))
                this.dataSource = new MatTableDataSource(data.routeResolvedData[1]._embedded.knowYourCustomers);
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
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
            'loanAppraisalKYC': {},
        };
        if (operation === 'modifyAppraisalKYC') {
            data.loanAppraisalKYC = this.selectedKYC;
        }
        const dialogRef = this._dialogRef.open(LoanAppraisalKYCUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.getLoanAppraisalKYCs();
            }
        });
    }

    /**
     * getLoanAppraisalKYCs()
     */
     getLoanAppraisalKYCs(): void {
        this._loanAppraisalService.getLaonAppraisalKYCs(this._loanAppraisalId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.knowYourCustomers);
        });
    }

    /**
     * onRowSelect()
     * @param loanAppraisalKYC 
     */
    onRowSelect(loanAppraisalKYC: any): void {
        this.selectedKYC = loanAppraisalKYC;
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
