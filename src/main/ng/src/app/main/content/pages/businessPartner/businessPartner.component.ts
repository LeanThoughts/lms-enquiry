import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from './businessPartner.service';

@Component({
    selector: 'fuse-business-partner',
    templateUrl: './businessPartner.component.html',
    styleUrls: ['./businessPartner.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerComponent implements OnInit, OnDestroy {

    expandPanel1: boolean = true;

    businessPartnerRole: FormControl = new FormControl('');
    businessPartnerRoles: any[] = [];
    selectedRoles: any[] = [];

    title: string = 'Create Business Partner';
    workflowStatus: string = '';

    /**
     * constructor()
     */
    constructor(private _activatedRoute: ActivatedRoute, private _businessPartnerService: BusinessPartnerService) {
        console.log(this._activatedRoute.routeConfig.path);
        this.businessPartnerRoles = this._activatedRoute.snapshot.data['routeResolvedData'][0]._embedded.businessPartnerRoleTypes;
        if (this._activatedRoute.routeConfig.path === 'createBusinessPartner') {
            this.selectedRoles.push(this._businessPartnerService.businessPartnerCategoryAndRole.value.defaultPartnerRole);
        }
        else {
            // this.selectedRoles = this._businessPartnerService.selectedPartner.value.roles;
        }
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
     * sendAppraisalForApproval()
     */
    sendForApproval(): void {
    }

    /**
     * addRoleToBusinessPartner()
     */
    addRoleToBusinessPartner(): void {
        this.selectedRoles.push(this.businessPartnerRole.value);
    }

    /**
     * getSelectedRoles()
     */
    getSelectedRoles(): string {
        if (this.selectedRoles.length > 0) {
            return this.selectedRoles.map(role => role.value).join(', ');
        }
        return '';
    }
}