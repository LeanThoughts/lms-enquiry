import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { BusinessPartnerService } from './businessPartner.service';
import { MatDialog, MatSnackBar, MatTabChangeEvent } from '@angular/material';
import { ConfirmationDialogComponent } from '../appraisal/confirmationDialog/confirmationDialog.component';
import { Subscription } from 'rxjs';
import { PartnerService } from '../administration/partner/partner.service';
import { AppService } from 'app/app.service';
import { PartnerUpdateComponent } from './partnerUpdate/partnerUpdate.component';
import { BusinessPartnerContactDetailsUpdateDialogComponent } from './contactDetailsUpdate/contactDetailsUpdate.component';

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

    subscriptions: Subscription = new Subscription();

    disableSendForApproval: boolean;
    
    @ViewChild(PartnerUpdateComponent) partnerUpdateComponent: PartnerUpdateComponent;
    
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

        this.subscriptions.add(this._partnerService.selectedPartner.subscribe(partner => {
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
        }));
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.workflowStatus = this.businessPartner ? this.businessPartner.workFlowStatusDescription : '';
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
                if (result && result.response) {
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
        if (!this.businessPartner) {
            this._matSnackBar.open('Please save the business partner details before sending for approval', 'OK', { duration: 7000 });
            return;
        }

        this.disableSendForApproval = true;
        // Check bank details
        this._businessPartnerService.getBusinessPartnerBankDetails(this.businessPartner.id).subscribe({
            next: response => {
                if (response._embedded.businessPartnerBankDetails.length === 0) {
                    this._matSnackBar.open('Please add at least one bank details before sending for approval', 'OK', { duration: 7000 });
                    this.disableSendForApproval = false;
                    return;
                }

                // Check identification details
                this._businessPartnerService.getBusinessPartnerIdentificationDetails(this.businessPartner.id).subscribe({
                    next: bpIds => {
                        const identificationCategory = bpIds._embedded.businessPartnerIdentifications.find(category => 
                            category.identificationCategoryCode === 'Z00002');
                        if (!identificationCategory) {
                            this._matSnackBar.open('Please add PAN in identification details before sending for approval', 'OK', 
                                { duration: 7000 });
                            this.disableSendForApproval = false;
                            return;
                        }

                        const currentUser = this._appService.currentUser;
                        const name = `${currentUser.firstName} ${currentUser.lastName}`;
                        const { email } = currentUser;

                        // Send for approval
                        this._businessPartnerService.sendBusinessPartnerForWorkflowApproval(this.businessPartnerId, name, email).subscribe({
                            next: response => {
                                this._partnerService.selectedPartner.next(response);
                                this._matSnackBar.open('Business partner is sent for approval.', 'OK', { duration: 7000 });
                                this.disableSendForApproval = true;
                            },
                            error: () => {
                                this.disableSendForApproval = false;
                                this._matSnackBar.open('Error occurred. Please try again later or contact your system administrator',
                                    'OK', { duration: 7000 });
                            }
                        });
                    },
                    error: () => {
                        this._matSnackBar.open('Error validating identification details. Please try again.', 'OK', { duration: 7000 });
                        this.disableSendForApproval = false;
                    }
                });
            },
            error: () => {
                this._matSnackBar.open('Error validating bank details. Please try again.', 'OK', { duration: 7000 });
                this.disableSendForApproval = false;
            }
        });
    }

    /**
     * onTabChange()
     */
    onTabChange(event: MatTabChangeEvent): void {
        // Check if the partner detail form has changes
        if (this.partnerUpdateComponent.partnerDetailsForm.dirty && this.partnerUpdateComponent.partnerDetailsForm.touched) {

            const currentFormValue = this.partnerUpdateComponent.partnerDetailsForm.value;
            const initialFormValue = this.partnerUpdateComponent.selectedPartner;
            
            const hasChanges = Object.keys(currentFormValue).some(key => 
                JSON.stringify(currentFormValue[key]) !== JSON.stringify(initialFormValue[key])
            );

            if (hasChanges) {
                console.warn('Partner detail form has changes, submitting');
                this.partnerUpdateComponent.submit();
            } else {
                console.log('No actual changes in the form');
            }
        }
    }
}
