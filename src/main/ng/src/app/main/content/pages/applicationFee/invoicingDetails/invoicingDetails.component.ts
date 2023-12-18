import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ActivatedRoute } from '@angular/router';
import { StateModel } from 'app/main/content/model/state.model';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';

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

    states = StateModel.getStates();
    projectTypes = [];

    meetingNumbers = [];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        private _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, private _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedInvoicingDetail =  _activatedRoute.snapshot.data.routeResolvedData[0];
        let lnAppln = _activatedRoute.snapshot.data.routeResolvedData[1];
        this.projectTypes = _activatedRoute.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
        this.meetingNumbers = _activatedRoute.snapshot.data.routeResolvedData[3];

        this.invoicingDetailForm = this._formBuilder.group({
            iccMeetingNumber: [this.selectedInvoicingDetail.iccMeetingNumber || ''],
            companyName: [this.selectedInvoicingDetail.companyName || lnAppln.loanApplication.projectName],
            cinNumber: [this.selectedInvoicingDetail.cinNumber || lnAppln.partner.CINNumber],
            gstNumber: [this.selectedInvoicingDetail.gstNumber || lnAppln.partner.gstNumber],
            pan: [this.selectedInvoicingDetail.pan || lnAppln.partner.pan, [Validators.pattern(EnquiryApplicationRegEx.pan)]],
            msmeRegistrationNumber: [this.selectedInvoicingDetail.msmeRegistrationNumber || lnAppln.partner.msmeRegistrationNumber],
            doorNumber: [this.selectedInvoicingDetail.doorNumber || lnAppln.partner.addressLine1],
            address: [this.selectedInvoicingDetail.address || lnAppln.partner.addressLine2],
            street: [this.selectedInvoicingDetail.street || lnAppln.partner.street],
            city: [this.selectedInvoicingDetail.city || lnAppln.partner.city],
            state: [this.selectedInvoicingDetail.state || lnAppln.partner.state],
            postalCode: [this.selectedInvoicingDetail.postalCode || lnAppln.partner.postalCode],
            landline: [this.selectedInvoicingDetail.landline || lnAppln.partner.contactNumber],
            mobile: [this.selectedInvoicingDetail.mobile || lnAppln.partner.mobile],
            email: [this.selectedInvoicingDetail.email || lnAppln.partner.email, [Validators.pattern(EnquiryApplicationRegEx.email)]],
            projectType: [this.selectedInvoicingDetail.projectType || lnAppln.loanApplication.projectType],
            pfsDebtAmount: [this.selectedInvoicingDetail.pfsDebtAmount || lnAppln.loanApplication.pfsDebtAmount, 
                [Validators.pattern(EnquiryApplicationRegEx.pfsDebtAmount)]],
            projectCapacity: [this.selectedInvoicingDetail.projectCapacity || lnAppln.loanApplication.projectCapacity, 
                [Validators.pattern(EnquiryApplicationRegEx.projectCapacity), Validators.min(1), Validators.max(999999.99)]],
            projectCapacityUnit: [this.selectedInvoicingDetail.projectCapacityUnit || lnAppln.loanApplication.projectCapacityUnit],
            projectLocationState: [this.selectedInvoicingDetail.projectLocationState || lnAppln.loanApplication.projectLocationState],
            file: ['']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.invoicingDetailForm.get('file').setValue(file);
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.invoicingDetailForm.valid) {
            if (this.invoicingDetailForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.invoicingDetailForm.get('file').value);
                this._applicationFeeService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveInvoicingDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this.selectedInvoicingDetail.id === undefined) {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveInvoicingDetails('');
                }
            }
        }
    }

    /**
     * saveInvoicingDetails()
     */
    saveInvoicingDetails(fileReference: string) {
        if (this.invoicingDetailForm.valid) {
            var invoicingDetail = this.invoicingDetailForm.value;
            invoicingDetail.loanApplicationId = this.loanApplicationId;
            invoicingDetail.fileReference = fileReference;
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
                if (this.selectedInvoicingDetail.fileReference !== '') {
                    this.selectedInvoicingDetail.fileReference = fileReference;
                }
                this._applicationFeeService.updateInvoicingDetail(this.selectedInvoicingDetail).subscribe((data) => {
                    this._matSnackBar.open('Customer/ Invoicing details updated successfully.', 'OK', { duration: 7000 });
                    this.selectedInvoicingDetail = data;
                });
            }
        }
    }
}
