import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-invoicing-details',
    templateUrl: './invoicingDetails.component.html',
    styleUrls: ['./invoicingDetails.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class InvoicingDetailsComponent implements OnInit {

    invoicingDetailForm: FormGroup;

    loanApplicationId = '';

    selectedInvoicingDetail: any;

    /**
     * constructor()
     * @param _formBuilder
     * @param _loanMonitoringService
     * @param _dialogRef
     * @param _dialogData
     * @param _matSnackBar
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        private _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, private _matDialog: MatDialog) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedInvoicingDetail = {};
        
        this.invoicingDetailForm = this._formBuilder.group({
            iccMeetingNumber: [this.selectedInvoicingDetail.iccMeetingNumber || ''],
            companyName: [this.selectedInvoicingDetail.companyName || ''],
            cinNumber: [this.selectedInvoicingDetail.cinNumber || ''],
            gstNumber: [this.selectedInvoicingDetail.gstNumber || ''],
            pan: [this.selectedInvoicingDetail.pan || ''],
            msmeRegistrationNumber: [this.selectedInvoicingDetail.msmeRegistrationNumber || ''],
            doorNumber: [this.selectedInvoicingDetail.doorNumber || ''],
            address: [this.selectedInvoicingDetail.address || ''],
            street: [this.selectedInvoicingDetail.street || ''],
            city: [this.selectedInvoicingDetail.city || ''],
            state: [this.selectedInvoicingDetail.state || ''],
            postalCode: [this.selectedInvoicingDetail.postalCode || ''],
            landline: [this.selectedInvoicingDetail.landline || ''],
            mobile: [this.selectedInvoicingDetail.mobile || ''],
            email: [this.selectedInvoicingDetail.email || ''],
            projectType: [this.selectedInvoicingDetail.projectType || ''],
            pfsDebtAmount: [this.selectedInvoicingDetail.pfsDebtAmount || ''],
            projectCapacity: [this.selectedInvoicingDetail.projectCapacity || ''],
            projectCapacityUnit: [this.selectedInvoicingDetail.projectCapacityUnit || ''],
            projectLocationState: [this.selectedInvoicingDetail.projectLocationState || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        var npa = this.invoicingDetailForm.value;

        if (this.selectedInvoicingDetail !== null) {
            // this.selectedInvoicingDetail.assetClass = npa.assetClass;
            // this.selectedInvoicingDetail.npaDeclarationDate = npa.npaDeclarationDate;
            // this.selectedInvoicingDetail.totalLoanAsset = npa.totalLoanAsset;
            // this.selectedInvoicingDetail.securedLoanAsset = npa.securedLoanAsset;
            // this.selectedInvoicingDetail.unSecuredLoanAsset = npa.unSecuredLoanAsset;
            // this.selectedInvoicingDetail.restructuringType = npa.restructuringType;
            // this.selectedInvoicingDetail.smaCategory = npa.smaCategory;
            // this.selectedInvoicingDetail.fraudDate = npa.fraudDate;
            // this.selectedInvoicingDetail.impairmentReserve = npa.impairmentReserve;
            // this.selectedInvoicingDetail.provisionAmount = npa.provisionAmount;
            // this._loanMonitoringService.updateNPA(this.selectedInvoicingDetail).subscribe((data) => {
            //     this._matSnackBar.open('NPA details updated successfully.', 'OK', { duration: 7000 });
            //     this.selectedInvoicingDetail = data;
            // });
        }
        else {
            // this._loanMonitoringService.saveNPA(npa, this.loanApplicationId).subscribe((data) => {
            //     this._matSnackBar.open('NPA details added successfully.', 'OK', { duration: 7000 });
            //     this.selectedInvoicingDetail = data;
            // });
        }
    }

}
