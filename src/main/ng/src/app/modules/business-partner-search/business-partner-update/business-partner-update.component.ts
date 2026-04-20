import { AfterViewInit, ChangeDetectorRef, Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ComponentNgxComponent } from '../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, DialogService, LayoutGridModule, PanelComponent } from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from '../business-partner-search.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent, TabConfig } from '@fundamental-ngx/platform/icon-tab-bar';
import { MessageService } from '../../../message.service';
import { AuthService } from '../../auth/auth.service';
import { GenericUpdateComponent } from '../../generic/generic-update/generic-update.component';
import { statesOfIndia } from '../../../app.constants';
import { ContactListComponent } from './contact-list/contact-list.component';
import { GenericListComponent } from '../../generic/generic-list/generic-list.component';
import { IdentificationDetailsUpdateDialogComponent } from './identification-details-update/identification-details-update.component';
import { BankDetailsUpdateDialogComponent } from './bank-details-update/bank-details-update.component';
import { CustomerDetailsUpdateDialogComponent } from './customer-details-update/customer-details-update.component';
import { KycDetailsUpdateDialogComponent } from './kyc-details-update/kyc-details-update.component';

@Component({
    selector: 'app-business-partner-update',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        LayoutGridModule,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericUpdateComponent,
        GenericListComponent,
        PanelComponent,
        ContactListComponent
    ],
    templateUrl: './business-partner-update.component.html'
})
export class BusinessPartnerUpdateComponent implements OnInit, AfterViewInit, OnDestroy { // implements AfterViewInit {

    disableSendForApproval: boolean = false;
    selectedBusinessPartner: any;
    selectedBusinessPartnerId: string = '';
    operation: string = '';
    title: string = '';

    partnerTitles: any[] = [];
    statesOfIndia: any[] = statesOfIndia;
    legalForms: any[] = [];
    legalEntities: any[] = [];
    houseBanks: any[] = [];
    businessPartnerRoleTypes: any[] = [];
    formFieldsConfig: any = {};
    partnerGroups: any[] = [];

    private destroy$ = new Subject<void>();

    @ViewChild('identificationDetailsList') identificationDetailsList!: GenericListComponent;
    @ViewChild('bankDetailsList') bankDetailsList!: GenericListComponent;
    @ViewChild('customerDetailsList') customerDetailsList!: GenericListComponent;
    @ViewChild('kycDetailsList') kycDetailsList!: GenericListComponent;

    @ViewChild('tab1') tab1!: TemplateRef<any>;
    activeContent!: TemplateRef<any>;

    tabs: TabConfig[] = [
        { icon: 'accelerated', label: 'Partner Details', counter: 1, color: 'critical', active: true }
    ];

    selectedTab: string = 'Partner Details';

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        private authService: AuthService,
        private businessPartnerService: BusinessPartnerSearchService,
        // private cdr: ChangeDetectorRef,
        public router: Router,
        private messageService: MessageService,
        private dialog: DialogService,
        private cdr: ChangeDetectorRef) 
    {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Subscribe to route parameters
        this.route.paramMap.subscribe(params => {
            console.log('params', params);
            this.selectedBusinessPartnerId = params.get('id') || '';
            this.operation = params.get('operation') || '';
        });
        
        // Subscribe to selected business partner and save the changes if any in selectedBusinessPartner
        this.businessPartnerService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe(businessPartner => {
            this.selectedBusinessPartner = businessPartner;
            
            if (this.selectedBusinessPartner.partnerCategory) {
                this.businessPartnerService.getTitles(this.selectedBusinessPartner.partnerCategory).subscribe(response => {
                    this.partnerTitles = response._embedded.titles;
                    this.updateSelectedBusinessPartnerForSelectFields();
                });
            }
            else {
                this.updateSelectedBusinessPartnerForSelectFields();
            }

            this.businessPartnerService.getBusinessPartnerRoleFieldConfig(this.selectedBusinessPartner.defaultPartnerRole).subscribe({
                next: (response) => {
                    this.formFieldsConfig = response;
                }
            });
        });

        // Get countries from route resolved data
        const resolvedData = this.route.snapshot.data['routeResolvedData'];
        if (resolvedData) {
            this.legalForms = resolvedData['legalForms'] ? resolvedData['legalForms']._embedded.legalForms : [];
            this.legalEntities = resolvedData['legalEntities'] ? resolvedData['legalEntities']._embedded.legalEntities : [];
            this.houseBanks = resolvedData['houseBanks'] ? resolvedData['houseBanks']._embedded.houseBanks : [];
            this.businessPartnerRoleTypes = resolvedData['businessPartnerRoleTypes'];
            this.partnerGroups = resolvedData['partnerGroups'] ? resolvedData['partnerGroups']._embedded.partnerGroups : [];
        }

        // Set title based on operation
        this.title = this.getTitle();
    }

    /**
     * After view init
     */
    ngAfterViewInit(): void {
        this.tabs[0].renderer = this.tab1;
        this.activeContent = this.tab1;
        this.cdr.detectChanges();
    }

    /**
     * Get the title for the page
     */
    private getTitle(): string {
        var title = '';
        if (this.operation === 'create') {
            title = 'Create Business Partner';
        }
        else {
            title = this.operation === 'update' ? 'Update Business Partner (' : 'View Business Partner (';
            if (this.selectedBusinessPartner.partyNumber)
                title += 'BP Number: ' + this.selectedBusinessPartner.partyNumber;
            if (title.includes('BP Number:'))
                title += ' / ';
            if (this.selectedBusinessPartner.partyName.trim())
                title += 'Name: ' + this.selectedBusinessPartner.partyName;
            title += ')';
        }
        console.log('title', title);
        return title;
    }

    /**
     * Send for approval
     */
    sendForApproval() {
        this.disableSendForApproval = false;
        
        const handleError = (message: string) => (error: any) => {
            this.disableSendForApproval = false;
            this.messageService.showError(message);
        };

        // Check if at least one bank account is present
        this.businessPartnerService.getBusinessPartnerBankDetails(this.selectedBusinessPartnerId).subscribe({
            next: (bankDetails) => {
                if (bankDetails.length === 0) {
                    this.messageService.showError('At least one bank account is required to send for approval');
                    return;
                }
                
                // Check if identification details exist
                this.businessPartnerService.getBusinessPartnerIdentificationDetails(this.selectedBusinessPartnerId).subscribe({
                    next: (identificationDetails) => {
                        const pan = identificationDetails.find((identification: any) => identification.identificationCategoryCode === 'Z00002');
                        if (!pan) {
                            this.messageService.showError('Please add PAN in identification details before sending for approval');
                            return;
                        }
                        
                        const currentUser = this.authService.currentUser;
                        console.log('currentUser', currentUser);
                        const name = `${currentUser.firstName} ${currentUser.lastName}`;
                        const email = currentUser.email;
                        this.disableSendForApproval = true;
                        this.businessPartnerService.sendBusinessPartnerForWorkflowApproval(this.selectedBusinessPartnerId, name, email).subscribe({
                            next: () => {
                                this.messageService.showSuccess('Business partner sent for approval successfully');
                            },
                            error: handleError('Error sending business partner for approval')
                        });
                    },
                    error: handleError('Error retrieving identification details')
                });
            },
            error: handleError('Error retrieving bank details')
        });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }


    /**
     * Update the selected business partner for select fields
     */
    private updateSelectedBusinessPartnerForSelectFields() {
        // Add partner category description to the selected business partner
        const categoryMap: { [key: string]: string } = {
            '1': 'Person',
            '2': 'Organization',
            '3': 'Group'
        };
        this.selectedBusinessPartner['formatted_partnerCategory'] = categoryMap[this.selectedBusinessPartner.partnerCategory] || '--';

        this.selectedBusinessPartner['formatted_title'] = this.partnerTitles.find(t => t.code === this.selectedBusinessPartner.title)?.value || '--';

        this.selectedBusinessPartner['formatted_state'] = this.statesOfIndia.find(s => s.code === this.selectedBusinessPartner.state)?.value || '--';
        this.selectedBusinessPartner['formatted_country'] = 'India';

        this.selectedBusinessPartner['formatted_legalForm'] = this.legalForms.find(f => f.code === this.selectedBusinessPartner.legalForm)?.value || '--';
        this.selectedBusinessPartner['formatted_legalEntity'] = this.legalEntities.find(e => e.code === this.selectedBusinessPartner.legalEntity)?.value || '--';
        this.selectedBusinessPartner['formatted_houseBank'] = this.houseBanks.find(b => b.bankKey === this.selectedBusinessPartner.houseBank)?.description || '--';
        this.selectedBusinessPartner['formatted_defaultPartnerRole'] = 
            this.businessPartnerRoleTypes.find(r => r.code === this.selectedBusinessPartner.defaultPartnerRole)?.value || '--';
        this.selectedBusinessPartner['formatted_partnerGroup'] = 
            this.partnerGroups.find(g => g.code === this.selectedBusinessPartner.partnerGroup)?.value || '--';
    }

    /**
     * Edit basic information
     */
    editBasicInformation() {
        this.router.navigate(['/business-partners/profile/update', this.selectedBusinessPartner.id]);
    }
    
    /**
     * Open the identification details update dialog
     */
    openIdentificationDetailsUpdateDialog(selectedIdentification?: any) {
        const routeResolvedData = this.route.snapshot.data['routeResolvedData'];
        const dialogRef = this.dialog.open(IdentificationDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                routeResolvedData: {
                    identificationCategories: routeResolvedData.identificationCategoryCode,
                    countries: routeResolvedData.country,
                    documentTypes: routeResolvedData.documentType,
                    formFieldsConfig: this.formFieldsConfig
                },
                operation: selectedIdentification ? 'update' : 'create',
                selectedIdentification: selectedIdentification,
                defaultPartnerRole: this.selectedBusinessPartner.defaultPartnerRole
            },
            width: '55rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated' || result === 'Created') {
                    this.identificationDetailsList.fetchData();
                }
            }
        });
    }

    /**
     * Open the identification details view dialog
     */
    openIdentificationDetailsViewDialog(selectedIdentification?: any) {
        console.log('selectedIdentification', selectedIdentification);
        this.dialog.open(IdentificationDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                operation: 'view',
                selectedIdentification: selectedIdentification,
            },
            width: '45rem'
        });
    }

    /**
     * Open the bank details update dialog
     */
    openBankDetailsUpdateDialog(selectedBankDetails?: any) {
        const routeResolvedData = this.route.snapshot.data['routeResolvedData'];
        const dialogRef = this.dialog.open(BankDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                routeResolvedData: {
                    bankKey: routeResolvedData.bankKey,
                    bankCountry: routeResolvedData.bankCountry,
                    formFieldsConfig: this.formFieldsConfig
                },
                operation: selectedBankDetails ? 'update' : 'create',
                selectedBankDetails: selectedBankDetails,
                defaultPartnerRole: this.selectedBusinessPartner.defaultPartnerRole
            },
            width: '60rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated' || result === 'Created') {
                    this.bankDetailsList.fetchData();
                }
            }
        });
    }

    /**
     * Open the bank details view dialog
     */
    openBankDetailsViewDialog(selectedBankDetails?: any) {
        this.dialog.open(BankDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                operation: 'view',
                selectedBankDetails: selectedBankDetails,
            },
            width: '45rem'
        });
    }

    /**
     * Open the customer details update dialog
     */
    openCustomerDetailsUpdateDialog(selectedCustomerDetails?: any) {
        this.businessPartnerService.getBusinessPartnerCustomerDetailsFieldConfig(this.selectedBusinessPartner.defaultPartnerRole, 
            this.selectedBusinessPartner.partnerGroup).subscribe({
            
            next: (response) => {
                const routeResolvedData = this.route.snapshot.data['routeResolvedData'];
                console.log('routeResolvedData', routeResolvedData);
                const dialogRef = this.dialog.open(CustomerDetailsUpdateDialogComponent, {
                    data: {
                        selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                        routeResolvedData: {
                            houseBanks: routeResolvedData.houseBanks._embedded.houseBanks,
                            planningGroups: routeResolvedData.planningGroups._embedded.planningGroups,
                            sortKeys: routeResolvedData.sortKeys._embedded.sortKeys,
                            dunningProcedures: routeResolvedData.dunningProcedures._embedded.dunningProcedures,
                            paymentTerms: routeResolvedData.paymentTerms._embedded.paymentTermses,
                            paymentMethods: routeResolvedData.paymentMethods._embedded.paymentMethods,
                            businessPartnerCustomerFieldValues: response,
                            selectedBusinessPartner: selectedCustomerDetails,
                            formFieldsConfig: this.formFieldsConfig,
                        },
                        operation: 'update',
                        defaultPartnerRole: this.selectedBusinessPartner.defaultPartnerRole
                    },
                    width: '50rem'
                });
                dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
                    next: (result: any) => {
                        if (result === 'Updated' || result === 'Created') {
                            this.customerDetailsList.fetchData();
                        }
                    }
                });
            },
            error: (error: any) => {
                this.messageService.showError('Error retrieving customer details field configuration: Default partner role not assigned to partner !!');
            }
        });
    }

    /**
     * Open the customer details view dialog
     */
    openCustomerDetailsViewDialog(selectedCustomerDetails?: any) {
        this.dialog.open(CustomerDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                operation: 'view',
                selectedCustomerDetails: selectedCustomerDetails,
            },
            width: '40rem'
        });
    }

    /**
     * Open the KYC details update dialog
     */
    openKYCDetailsUpdateDialog(selectedKYCDetails?: any) {
        const dialogRef = this.dialog.open(KycDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                operation: selectedKYCDetails ? 'update' : 'create',
                selectedKYCDetails: selectedKYCDetails,
            },
            width: '45rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated' || result === 'Created') {
                    this.kycDetailsList.fetchData();
                }
            }
        });
    }

    /**
     * Open the KYC details view dialog
     */
    openKYCDetailsViewDialog(selectedKYCDetails?: any) {
        this.dialog.open(KycDetailsUpdateDialogComponent, {
            data: {
                selectedBusinessPartnerId: this.selectedBusinessPartnerId,
                operation: 'view',
                selectedKYCDetails: selectedKYCDetails,
            },
            width: '40rem'
        });
    }

    /**
     * On icon tab selected
     */
    onIconTabSelected(event: any) {
        console.log('event', event);
        this.activeContent = event.renderer;
    }
}
