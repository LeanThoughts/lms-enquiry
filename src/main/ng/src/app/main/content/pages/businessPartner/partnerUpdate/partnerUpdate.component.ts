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

    /**
     * constructor()
     */
    constructor(public _partnerService: PartnerService, 
                private _formBuilder: FormBuilder, 
                private _activatedRoute: ActivatedRoute,
                private _businessPartnerService: BusinessPartnerService, 
                private _matSnackBar: MatSnackBar) {

        if (this._activatedRoute.routeConfig.path === 'updateBusinessPartner') {
            this.partnerTitles = this._activatedRoute.snapshot.data['routeResolvedData'][11]._embedded.titles;
        }
        else {
            this.partnerTitles = this._activatedRoute.snapshot.data['routeResolvedData'][6]._embedded.titles;
        }
            
        this.selectedPartner = this._partnerService.selectedPartner.value;
        console.log('selected partner ####', this.selectedPartner);
        this.states = this._activatedRoute.snapshot.data['routeResolvedData'][1];

        this.partnerDetailsForm = this._formBuilder.group({
            title: [this.selectedPartner.title || null],
            partyName1: [this.selectedPartner.partyName1 || null, Validators.required],
            partyName2: [this.selectedPartner.partyName2 || null, Validators.required],
            searchTerm1: [this.selectedPartner.searchTerm1 || null],
            searchTerm2: [this.selectedPartner.searchTerm2 || null],
            addressLine1: [this.selectedPartner.addressLine1 || null],
            street: [this.selectedPartner.street || null],
            postalCode: [this.selectedPartner.postalCode || null, [Validators.pattern(EnquiryApplicationRegEx.numbersOnly)]],
            state: [this.selectedPartner.state || null],
            country: [this.selectedPartner.country || 'India'],
            city: [this.selectedPartner.city || null],
            contactNumber: [this.selectedPartner.contactNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
            email: [this.selectedPartner.email || null, [Validators.pattern(EnquiryApplicationRegEx.email)]],
            mobileNumber: [this.selectedPartner.mobileNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
            faxNumber: [this.selectedPartner.faxNumber || null, [Validators.pattern(EnquiryApplicationRegEx.telephoneNumber)]],
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
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.partnerDetailsForm.valid) {
            this.partnerDetailsForm.value.country = '';
            if (this.selectedPartner.id === '') {     
                this._businessPartnerService.createPartner(this.partnerDetailsForm.value).subscribe((response: any) => {
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
                this._businessPartnerService.updatePartner(this.selectedPartner).subscribe((response: any) => {
                    this.selectedPartner = response;
                    this._partnerService.selectedPartner.next(response);
                    this._matSnackBar.open('Partner updated successfully', 'Close', {duration: 7000});
                }, error => {
                    this._matSnackBar.open(error.error.message, 'Close', {duration: 7000});
                });
            }
        }
    }
}
