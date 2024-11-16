import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ActivatedRoute } from '@angular/router';
import { StateModel } from 'app/main/content/model/state.model';
import { SearchPartnersDialogComponent } from '../searchPartnersDialog/searchPartnersDialog.component';
import { BusinessPartnerService } from '../../businessPartner/businessPartner.service';
import { LoanAppraisalService } from '../../appraisal/loanAppraisal.service';

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

    // partnerNameFilteredOptions: Observable<PartnerModel[]>;
    // partnerIdFilteredOptions: Observable<PartnerModel[]>;
  
    // partners: Array<PartnerModel>;
    selectedPartnerId = '';
    selectedPartner: any;

    partnerNameFormControl = new FormControl();
    partnerIdFormControl = new FormControl();

    readonlyFields = true;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _applicationFeeService: ApplicationFeeService,
                _enquiryService: LoanEnquiryService, 
                private _matSnackBar: MatSnackBar, 
                _activatedRoute: ActivatedRoute, 
                private _matDialog: MatDialog, 
                private _businessPartnerService: BusinessPartnerService,
                private _loanAppraisalService: LoanAppraisalService) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedInvoicingDetail =  _activatedRoute.snapshot.data.routeResolvedData[0];
        // let lnAppln = _activatedRoute.snapshot.data.routeResolvedData[1];
        this.projectTypes = _activatedRoute.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
        // this.meetingNumbers = _activatedRoute.snapshot.data.routeResolvedData[3];

        this.invoicingDetailForm = this._formBuilder.group({
            companyName: [''],
            cinNumber: [''],
            gstNumber: [''],
            pan: [''],
            msmeRegistrationNumber: [''],
            doorNumber: [''],
            address: [''],
            street: [''],
            city: [''],
            state: [''],
            postalCode: [''],
            landline: [''],
            mobile: [''],
            email: ['']
        });

        if (this.selectedInvoicingDetail.id !== undefined) {
            console.log(this.selectedInvoicingDetail._links.partner.href);
            _applicationFeeService.getPartner(this.selectedInvoicingDetail._links.partner.href).subscribe(data => {
                this.partnerNameFormControl.setValue(data.partyName1);
                this.partnerIdFormControl.setValue(data.partyNumber);
                this.selectedPartnerId = data.id;
                this.loadPartnerForm(data);
            });
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * loadPartnerForm()
     */
    loadPartnerForm(partner: any): void {
        this.invoicingDetailForm.patchValue({
            companyName: partner.partyName1,
            // cinNumber: partner.CINNumber,
            // gstNumber: partner.gstNumber,
            // pan: partner.pan,
            // msmeRegistrationNumber: partner.msmeRegistrationNumber,
            doorNumber: partner.addressLine1,
            address: partner.addressLine2,
            street: partner.street,
            city: partner.city,
            state: partner.state,
            postalCode: partner.postalCode,
            landline: partner.contactNumber,
            mobile: partner.mobile,
            email: partner.email
        });
    }

    /**
     * loadOtherDetails()
     */
    loadOtherDetails(identificationDetails: any[]): void {
        const identificationMap = {
            '1258': 'cinNumber',
            '1263': 'gstNumber', 
            '1257': 'pan',
            '1261': 'msmeRegistrationNumber'
        };

        const formValues = {};
        
        Object.entries(identificationMap).forEach(([code, formField]) => {
            const identification = identificationDetails.find(id =>
                id.identificationCategoryId.toString() === code
            );
            console.log('identification', identification);
            if (identification) {
                formValues[formField] = identification.identificationNumber;
            }
        });
        console.log('formValues', formValues);
        this.invoicingDetailForm.patchValue(formValues);
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.invoicingDetailForm.valid) {
            this.saveInvoicingDetails();
        }
    }

    /**
     * saveInvoicingDetails()
     */
    saveInvoicingDetails() {
        if (this.selectedPartnerId === '') {
            this._matSnackBar.open('Please select a partner.', 'OK', { duration: 7000 });
        }
        else {
            var invoicingDetail = this.invoicingDetailForm.value;
            invoicingDetail.loanApplicationId = this.loanApplicationId;
            invoicingDetail.partnerId = this.selectedPartnerId;
            if (this.selectedInvoicingDetail.id === undefined) {
                    this._applicationFeeService.createInvoicingDetail(invoicingDetail).subscribe((data) => {
                    this._matSnackBar.open('Customer/ Invoicing deails saved successfully.', 'OK', { duration: 7000 });
                    this.selectedInvoicingDetail = data;
                    this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                        this._applicationFeeService._applicationFee.next(data);
                    });

                    const loanPartner = {
                        businessPartnerId: this.selectedPartner.partyNumber,
                        businessPartnerName: this.selectedPartner.partyName1 + ' ' + this.selectedPartner.partyName2,
                        roleType: 'TR0100',
                        roleDescription: 'Main Loan Partner',
                        kycStatus: 'Not Started',
                        loanApplicationId: this.loanApplicationId
                    }
                    this._loanAppraisalService.createLoanOfficer(loanPartner).subscribe(data => {
                        // this._matSnackBar.open('Loan partner added successfully.', 'OK', { duration: 7000 });
                    });
                });
            }
            else {
                this.selectedInvoicingDetail.partnerId = this.selectedPartnerId;
                this._applicationFeeService.updateInvoicingDetail(this.selectedInvoicingDetail).subscribe((data) => {
                    this._matSnackBar.open('Customer/ Invoicing details updated successfully.', 'OK', { duration: 7000 });
                    this.selectedInvoicingDetail = data;
                });
            }
        }
    }

    searchPartners() {
        // Open the search dialog.
        const dialogRef = this._matDialog.open(SearchPartnersDialogComponent, {
            panelClass: 'fuse-search-partners-dialog',
            width: '900px',
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result.selectedPartner) {
                if (result.selectedPartner.id) {
                    this.loadPartnerForm(result.selectedPartner);
                    this.selectedPartnerId = result.selectedPartner.id;
                    this.selectedPartner = result.selectedPartner;
                    this._businessPartnerService.getBusinessPartnerIdentificationDetails(this.selectedPartnerId).subscribe(data => {
                        this.loadOtherDetails(data._embedded.businessPartnerIdentifications);
                    });
                }
                else {
                    this._matSnackBar.open('Errors occured while selection a partner.', 'OK', { duration: 7000 });
                }
            }
        });        
    }
}
