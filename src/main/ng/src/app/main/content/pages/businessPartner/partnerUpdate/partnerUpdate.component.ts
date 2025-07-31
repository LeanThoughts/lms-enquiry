import { Component, OnDestroy, OnInit } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { PartnerModel } from 'app/main/content/model/partner.model';
import { PartnerService } from '../../administration/partner/partner.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { BusinessPartnerService } from '../businessPartner.service';
import { MatSnackBar } from '@angular/material';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';

@Component({
    selector: 'fuse-partner-update',
    templateUrl: './partnerUpdate.component.html',
    styleUrls: ['./partnerUpdate.component.scss'],
    animations: fuseAnimations
})
export class PartnerUpdateComponent implements OnInit, OnDestroy {

    title: string = 'Create Business Partner';

    partnerDetailsForm: FormGroup;

    selectedPartner: PartnerModel;
    states: any;

    partnerTitles: any;
    businessPartnerRoles: any;
    businessPartnerCategoryAndRole: any;

    legalForms: any;
    legalEntities: any;
    houseBanks: any;

    /**
     * constructor()
     */
    constructor(public _partnerService: PartnerService, 
                private _formBuilder: FormBuilder, 
                private _activatedRoute: ActivatedRoute,
                private _businessPartnerService: BusinessPartnerService, 
                private _matSnackBar: MatSnackBar) {

        this.businessPartnerRoles = this._activatedRoute.snapshot.data.routeResolvedData[0]._embedded.businessPartnerRoleTypes;
            
        this.selectedPartner = this._partnerService.selectedPartner.value;
        console.log('selected partner ######', this.selectedPartner);
        this.states = this._activatedRoute.snapshot.data['routeResolvedData'][1];
        this.businessPartnerCategoryAndRole = _businessPartnerService.businessPartnerCategoryAndRole.value;
        console.log('businessPartnerCategoryAndRole', this.businessPartnerCategoryAndRole);

        this._businessPartnerService.getLegalForms().subscribe(response => {
            this.legalForms = response._embedded.legalForms;
        });

        this._businessPartnerService.getLegalEntities().subscribe(response => {
            this.legalEntities = response._embedded.legalEntities;
        });

        this._businessPartnerService.getHouseBanks().subscribe(response => {
            this.houseBanks = response._embedded.houseBanks;
        });

        if (this._activatedRoute.routeConfig.path === 'updateBusinessPartner') {
            this._businessPartnerService.getTitles(this.selectedPartner.partnerCategory).subscribe(response => {
                this.partnerTitles = response._embedded.titles;
            });
        }
        else {
            this.partnerTitles = this._activatedRoute.snapshot.data['routeResolvedData'][6]._embedded.titles;
        }
    }

    /**
     * partnerCategoryChange()
     */
    partnerCategoryChange(event: any): void {
        this._businessPartnerService.getTitles(event.value).subscribe(response => {
            this.partnerTitles = response._embedded.titles;
        });
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.partnerDetailsForm = this._formBuilder.group({
            title: [this.selectedPartner.title || null],
            partyName1: [this.selectedPartner.partyName1 || null, Validators.required],
            partyName2: [this.selectedPartner.partyName2 || null],
            searchTerm1: [this.selectedPartner.searchTerm1 || null],
            searchTerm2: [this.selectedPartner.searchTerm2 || null],
            addressLine1: [this.selectedPartner.addressLine1 || null],
            addressLine2: [this.selectedPartner.addressLine2 || null],
            addressLine3: [this.selectedPartner.addressLine3 || null],
            postalCode: [this.selectedPartner.postalCode || null, [Validators.pattern(EnquiryApplicationRegEx.numbersOnly)]],
            state: [this.selectedPartner.state || null],
            country: [this.selectedPartner.country || 'India'],
            city: [this.selectedPartner.city || null],
            contactNumber: [this.selectedPartner.contactNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
            email: [this.selectedPartner.email || null, [Validators.pattern(EnquiryApplicationRegEx.email)]],
            mobileNumber: [this.selectedPartner.mobileNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
            faxNumber: [this.selectedPartner.faxNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
            partnerCategory: [this.selectedPartner.partnerCategory || this.businessPartnerCategoryAndRole.partnerCategory],
            defaultPartnerRole: [this.selectedPartner.defaultPartnerRole || 
                (this.businessPartnerCategoryAndRole.defaultPartnerRole ? this.businessPartnerCategoryAndRole.defaultPartnerRole.code : null)],
            addressValidFromDate: [this.selectedPartner.addressValidFromDate || null],
            externalBPNumber: [this.selectedPartner.externalBPNumber || null],
            legalForm: [this.selectedPartner.legalForm || null],
            legalEntity: [this.selectedPartner.legalEntity || null],
            houseBank: [this.selectedPartner.houseBank || null]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.partnerDetailsForm.valid) {

            var partnerDetails = this.partnerDetailsForm.value;
            if (partnerDetails.addressValidFromDate) {
                const dt = new Date(partnerDetails.addressValidFromDate);
                partnerDetails.addressValidFromDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            this.partnerDetailsForm.value.country = '';
            if (this.selectedPartner.id === '') {     
                this._businessPartnerService.createPartner(partnerDetails).subscribe((response: any) => {
                    this.selectedPartner = response;
                    this._partnerService.selectedPartner.next(response);
                    this._matSnackBar.open('Partner created successfully', 'Close', {duration: 7000});
                }, error => {
                    this._matSnackBar.open(error.error.message, 'Close', {duration: 7000});
                }); 
            }
            else {
                Object.keys(this.partnerDetailsForm.value).forEach(key => {
                    this.selectedPartner[key] = this.partnerDetailsForm.value[key];
                });
                this.selectedPartner.addressValidFromDate = partnerDetails.addressValidFromDate;
                this._businessPartnerService.updatePartner(this.selectedPartner).subscribe((response: any) => {
                    this.selectedPartner = response;
                    this._partnerService.selectedPartner.next(response);
                    this._matSnackBar.open('Partner updated successfully', 'Close', {duration: 7000});
                }, error => {
                    this._matSnackBar.open(error.error.message, 'Close', {duration: 7000});
                });
            }
        }
        else {
            this._matSnackBar.open('Please fill in all the required fields', 'Close', {duration: 7000});
        }
    }
}
