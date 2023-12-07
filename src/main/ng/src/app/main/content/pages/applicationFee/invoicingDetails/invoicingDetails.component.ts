import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ActivatedRoute } from '@angular/router';

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
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        private _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, private _matDialog: MatDialog,
        private _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedInvoicingDetail =  _activatedRoute.snapshot.data.routeResolvedData[0];
        
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
        if (this.invoicingDetailForm.valid) {
            var invoicingDetail = this.invoicingDetailForm.value;
            invoicingDetail.loanApplicationId = this.loanApplicationId;
            if (this.selectedInvoicingDetail.id === undefined) {
                    this._applicationFeeService.createInvoicingDetail(invoicingDetail).subscribe((data) => {
                    this._matSnackBar.open('Customer/ Invoicing deails created successfully.', 'OK', { duration: 7000 });
                    this.selectedInvoicingDetail = data;
                });
            }
            else {
                this.selectedInvoicingDetail.iccMeetingNumber = invoicingDetail.iccMeetingNumber;
                this.selectedInvoicingDetail.companyName = invoicingDetail.companyName;
                this.selectedInvoicingDetail.cinNumber = invoicingDetail.cinNumber;
                this.selectedInvoicingDetail.gstNumber = invoicingDetail.gstNumber;
                this.selectedInvoicingDetail.pan = invoicingDetail.pan;
                this.selectedInvoicingDetail.msmeRegistrationNumber = invoicingDetail.msmeRegistrationNumber;
                this.selectedInvoicingDetail.doorNumber = invoicingDetail.doorNumber;
                this.selectedInvoicingDetail.address = invoicingDetail.address;
                this.selectedInvoicingDetail.street = invoicingDetail.street;
                this.selectedInvoicingDetail.city = invoicingDetail.city;
                this.selectedInvoicingDetail.state = invoicingDetail.state;
                this.selectedInvoicingDetail.postalCode = invoicingDetail.postalCode;
                this.selectedInvoicingDetail.landline = invoicingDetail.landline;
                this.selectedInvoicingDetail.mobile = invoicingDetail.mobile;
                this.selectedInvoicingDetail.email = invoicingDetail.email;
                this.selectedInvoicingDetail.projectType = invoicingDetail.projectType;
                this.selectedInvoicingDetail.projectCapacity = invoicingDetail.projectCapacity;
                this.selectedInvoicingDetail.projectCapacityUnit = invoicingDetail.projectCapacityUnit;
                this.selectedInvoicingDetail.projectLocationState = invoicingDetail.projectLocationState;
                this.selectedInvoicingDetail.pfsDebtAmount = invoicingDetail.pfsDebtAmount;
                this._applicationFeeService.updateInvoicingDetail(this.selectedInvoicingDetail).subscribe((data) => {
                    this._matSnackBar.open('Customer/ Invoicing details updated successfully.', 'OK', { duration: 7000 });
                    this.selectedInvoicingDetail = data;
                });
            }
    
        }
    }
}
