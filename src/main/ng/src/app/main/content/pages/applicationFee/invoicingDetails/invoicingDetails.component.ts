import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ActivatedRoute } from '@angular/router';
import { StateModel } from 'app/main/content/model/state.model';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { Observable } from 'rxjs';
import { PartnerModel } from 'app/main/content/model/partner.model';
import { map, startWith } from 'rxjs/operators';
import { PartnerService } from '../../administration/partner/partner.service';

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

    partnerNameFilteredOptions: Observable<PartnerModel[]>;
    partnerIdFilteredOptions: Observable<PartnerModel[]>;
  
    partners: Array<PartnerModel>;
    selectedPartnerId = '';

    partnerNameFormControl = new FormControl();
    partnerIdFormControl = new FormControl();

    readonlyFields = true;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, _activatedRoute: ActivatedRoute) {

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

        this.partners = _activatedRoute.snapshot.data.routeResolvedData[4];
        // Check for nulls and convert partynumber to string
        this.partners.map(x => {
            if (!x.partyNumber)
                x.partyNumber = '';
            else
                x.partyNumber = x.partyNumber.toString();
            if (!x.addressLine1) x.addressLine1 = '';
            if (!x.addressLine2) x.addressLine2 = '';
            if (!x.street) x.street = '';
            if (!x.city) x.city = '';
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.partnerNameFilteredOptions = this.partnerNameFormControl.valueChanges
            .pipe(
                startWith(''),
                map(name => name ? this._filterPartnersByName(name) : this.partners.slice())
            );

        this.partnerIdFilteredOptions = this.partnerIdFormControl.valueChanges
            .pipe(
                startWith(''),
                map(name => name ? this._filterPartnersById(name) : this.partners.slice())
            );
    }

    /**
     * validatePartnerByName()
     */
    validatePartnerByName($event) {
        let partner: PartnerModel;
        if ($event.target.value.trim() !== '') {
            const filteredPartners = this.partners.filter(partner => partner.partyName1.toLowerCase().localeCompare(
                $event.target.value.toLowerCase()) === 0);
            if (filteredPartners.length > 0) {
                partner = filteredPartners[0];
                this.partnerIdFormControl.setValue(partner.partyNumber);
                this.selectedPartnerId = partner.id;
                this.loadPartnerForm(partner);
            }
        }
    }

    /**
     * validatePartnerById()
     */
    validatePartnerById($event) {
        let partner: PartnerModel;
        if ($event.target.value.trim() !== '') {
            const filteredPartners = this.partners.filter(partner => partner.partyNumber.localeCompare($event.target.value) === 0);
            if (filteredPartners.length > 0) {
                partner = filteredPartners[0];
                this.partnerNameFormControl.setValue(partner.partyName1);
                this.selectedPartnerId = partner.id;
                this.loadPartnerForm(partner);
            }
        }
    }

    /**
     * _filterPartnersByName()
     */
    private _filterPartnersByName(value: string): PartnerModel[] {
        const filterValue = value.toLowerCase();
        return this.partners.filter(partner => partner.partyName1.toLowerCase().indexOf(filterValue) === 0);
    }

    /**
     * _filterPartnersById()
     */
    private _filterPartnersById(value: string): PartnerModel[] {
        const filterValue = value.toLowerCase();
        return this.partners.filter(partner => {
            if (partner.partyNumber.trim() !== '') {
                return partner.partyNumber.toLowerCase().indexOf(filterValue) === 0
            }
            else
                return false;
        });
    }

    /**
     * getPartyAddress()
     */
    getPartyAddress(partner: PartnerModel): string {
        let str = partner.addressLine1.trim();
        str = str + ' ' + partner.addressLine2;
        str = str.trim() + ' ' + partner.street;
        str = str.trim() + ' ' + partner.city;
        return str.trim();;
    }

    /**
     * loadPartnerForm()
     */
    loadPartnerForm(partner: any): void {
        this.invoicingDetailForm.patchValue({
            companyName: partner.partyName1,
            cinNumber: partner.CINNumber,
            gstNumber: partner.gstNumber,
            pan: partner.pan,
            msmeRegistrationNumber: partner.msmeRegistrationNumber,
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
}
