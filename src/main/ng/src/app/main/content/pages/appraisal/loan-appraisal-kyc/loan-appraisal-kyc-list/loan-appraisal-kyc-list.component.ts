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

    dataSource1: MatTableDataSource<any>;
    dataSource2: MatTableDataSource<any>;
    
    displayedColumns1 = [
        'roleType', 'roleDescription', 'businessPartnerName', 'kycStatus'
    ];
    displayedColumns2 = [
        'documentType', 'documentName', 'dateOfCompletion', 'remarks', 'download'
    ];

    selectedPartner: any;
    selectedKYCDocument: any;

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

        if (JSON.stringify(_activatedRoute.snapshot.data.routeResolvedData[1]) !== JSON.stringify({}))
            this.dataSource1 = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[1]._embedded.loanPartners);
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
    }

    /**
     * openUploadDialog()
     */
     openUploadDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'kycDocument': this.selectedKYCDocument,
        };
        const dialogRef = this._dialogRef.open(LoanAppraisalKYCUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.getKYCDocuments();
                this._loanAppraisalService.getLoanOfficersForKyc(this._loanApplicationId).subscribe(data => {
                    this.dataSource1 = new MatTableDataSource(data._embedded.loanPartners);
                });
            }
        });
    }

    /**
     * getKYCDocuments()
     */
    getKYCDocuments(): void {
        this._loanAppraisalService.getKYCDocuments(this.selectedPartner.id).subscribe(data => {
            this.dataSource2 = new MatTableDataSource(data._embedded.knowYourCustomers);
        });
        this.selectedKYCDocument = undefined;
    }

    /**
     * onPartnerSelect()
     */
    onPartnerSelect(loanPartner: any): void {
        this.selectedPartner = loanPartner;
        this.getKYCDocuments();
    }

    /**
     * onKYCDocumentSelect()
     */
    onKYCDocumentSelect(kycDocument: any): void {
        this.selectedKYCDocument = kycDocument;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
