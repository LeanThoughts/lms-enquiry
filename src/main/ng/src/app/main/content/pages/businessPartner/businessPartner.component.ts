import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from './businessPartner.service';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ConfirmationDialogComponent } from '../appraisal/confirmationDialog/confirmationDialog.component';
import { Subscription } from 'rxjs';
import { PartnerService } from '../administration/partner/partner.service';
import { AppService } from 'app/app.service';

@Component({
    selector: 'fuse-business-partner',
    templateUrl: './businessPartner.component.html',
    styleUrls: ['./businessPartner.component.scss'],
    animations: fuseAnimations
})
export class BusinessPartnerComponent implements OnInit, OnDestroy {

    expandPanel1: boolean = true;

    businessPartnerId: string;
    businessPartner: any;

    businessPartnerRoleType: FormControl = new FormControl('');
    businessPartnerRoleTypes: any[] = [];
    selectedRoleTypes: any[] = [];

    title: string = 'Create Business Partner';
    workflowStatus: string = '';

    subscription: Subscription;

    disableSendForApproval: boolean;
    
    /**
     * constructor()
     */
    constructor(private _activatedRoute: ActivatedRoute, 
        private _businessPartnerService: BusinessPartnerService, 
        private _matDialog: MatDialog,
        private _partnerService: PartnerService, 
        private _matSnackBar: MatSnackBar, 
        private _appService: AppService) {

        this.businessPartnerRoleTypes = this._activatedRoute.snapshot.data['routeResolvedData'][0]._embedded.businessPartnerRoleTypes;
        
        if (this._activatedRoute.routeConfig.path === 'createBusinessPartner') {
            this.selectedRoleTypes.push(this._businessPartnerService.businessPartnerCategoryAndRole.value.defaultPartnerRole);
        }
        else {
            if (this._activatedRoute.snapshot.data['routeResolvedData'][9]) {
                this._activatedRoute.snapshot.data['routeResolvedData'][9]._embedded.businessPartnerRoles.forEach(role => {
                    console.log('adding role', role.roleType);
                    this.selectedRoleTypes.push(role.roleType);
                });
            }
        }

        this.subscription = this._partnerService.selectedPartner.subscribe(partner => {
            this.businessPartner = partner;
            this.businessPartnerId = partner ? partner.id : null;
            if (partner && this._activatedRoute.routeConfig.path === 'updateBusinessPartner') {
                this.title = 'Update Business Partner :' + partner.partyName;
            }
            if (this.businessPartnerId && this._activatedRoute.routeConfig.path === 'createBusinessPartner') {
                this._businessPartnerService.createBusinessPartnerRole(this.businessPartnerId, 
                    this.selectedRoleTypes[0].id, true).subscribe(response => {
                });
            }
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
     * addRoleToBusinessPartner()
     */
    addRoleToBusinessPartner(): void {
        if (this._activatedRoute.routeConfig.path === 'updateBusinessPartner') {
            const dialogRef = this._matDialog.open(ConfirmationDialogComponent, {
                width: '400px',
                data: {
                    message: 'Are you sure you want to add this role to the business partner?'
                }
            });
            dialogRef.afterClosed().subscribe((result) => {
                if (result.response) {
                    if (this.businessPartnerId) {
                        this._businessPartnerService.createBusinessPartnerRole(this.businessPartnerId, this.businessPartnerRoleType.value.id, false).
                            subscribe(response => {
                                this._matSnackBar.open('Role added to business partner', 'Close', { duration: 7000 });
                                this.selectedRoleTypes.push(this.businessPartnerRoleType.value);
                                this.businessPartnerRoleType.reset();
                            });
                    }
                }
            });
        }
        else {
            this._matSnackBar.open('Please save the business partner details before adding a role', 'Close', { duration: 7000 });
        }
    }

    /**
     * getSelectedRoles()
     */
    getSelectedRoles(): string {
        if (this.selectedRoleTypes.length > 0) {
            return this.selectedRoleTypes.map(role => role.value).join(',  ');
        }
        return '';
    }

    /**
     * sendForApproval()
     */
    sendForApproval(): void {
        if (this.businessPartner) {
            let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
            let email = this._appService.currentUser.email;
            this._matSnackBar.open('Please wait while attempting to send the business partner for approval.', 'OK', { duration: 25000 });
            this._businessPartnerService.sendBusinessPartnerForWorkflowApproval(this.businessPartnerId, name, email).subscribe(
            response => {
                this._partnerService.selectedPartner.next(response);
                // this._matSnackBar.dismiss();
                this._matSnackBar.open('Business partner is sent for approval.', 'OK', { duration: 7000 });
            },
            error => {
                this.disableSendForApproval = false;
                this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator',
                    'OK', { duration: 7000 });
            });
            this.disableSendForApproval = true;
            // this._location.back();
        }
        else {
            this._matSnackBar.open('Please save the business partner details before sending for approval', 'OK', { duration: 7000 });
        }
    }
}
