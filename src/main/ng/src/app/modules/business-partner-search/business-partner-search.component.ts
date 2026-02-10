import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent, FormModule, IconModule, LayoutGridModule, TableModule } from '@fundamental-ngx/core';
import { SelectModule } from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from './business-partner-search.service';
import { ComponentNgxComponent } from '../../common/component-ngx/component-ngx.component';
import { MessageService } from '../../message.service';
import { SelectionModel } from '@angular/cdk/collections';
import { Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { BusinessPartnerListComponent } from './business-partner-list/business-partner-list.component';

@Component({
    selector: 'app-business-partner-search',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        FormModule,
        IconModule,
        LayoutGridModule,
        ReactiveFormsModule,
        BusinessPartnerListComponent
    ],
    templateUrl: './business-partner-search.component.html'
})
export class BusinessPartnerSearchComponent implements OnInit, OnDestroy {

    destroy$: Subject<void> = new Subject<void>();

    businessPartners: Array<any> = [];

    businessPartnerSearchForm = new FormGroup({
        partnerName: new FormControl(''),
        email: new FormControl(''),
        partnerNumberFrom: new FormControl(''),
        partnerNumberTo: new FormControl(''),
        panNumber: new FormControl(''),
    });

    selectedBusinessPartner: any;
    selectedBusinessPartnerId: SelectionModel<any> = new SelectionModel<any>();

    /**
     * Constructor
     */
    constructor(
        private businessPartnerService: BusinessPartnerSearchService,
        private messageService: MessageService,
        public router: Router
    ) {}

    /**
     * On init
     */
    ngOnInit(): void {
        // Initialize the search parameters from the service
        const formValues = this.businessPartnerService.businessPartnerSearchParameters$.value;
        if (formValues) {
            this.businessPartnerSearchForm.patchValue(formValues);
            this.searchBusinessPartners();
        }

        // Subscribe to the selected entity from the service
        this.businessPartnerService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe({
            next: (value: any) => {
                this.selectedBusinessPartner = value;
            }
        });
    }

    /**
     * Search business partners
     */
    searchBusinessPartners(): void {
        // Check if at least one search parameter is provided
        const formValues = this.businessPartnerSearchForm.value;
        const hasValue = Object.values(formValues).some(value => value !== null && value !== undefined && value.toString().trim() !== '');
        if (!hasValue) {
            this.messageService.showError('Please provide a value for at least one search parameter.');
            return;
        }

        // Check if Partner number to is lesser than partner number from
        if (formValues.partnerNumberTo && formValues.partnerNumberFrom && formValues.partnerNumberTo < formValues.partnerNumberFrom) {
            this.messageService.showError('Partner Number To should be greater than Partner Number From');
            return;
        }
        
        // Prepare request parameters and call the service method
        let requestParameters: Array<string> = [formValues.partnerName || '',
            formValues.email || '',
            formValues.partnerNumberFrom || '',
            formValues.partnerNumberTo || '',
            formValues.panNumber || ''];
        this.businessPartnerService.searchBusinessPartners(requestParameters).subscribe({
            next: (partners: any[]) => {
                this.businessPartnerService.businessPartnerSearchParameters$.next(formValues);
                this.businessPartners = partners.sort((a: any, b: any) => a.partyName.localeCompare(b.partyName));
            },
            error: (error: any) => {
                this.messageService.showError(error.message);
            }
        });
    }

    /**
     * Clear search
     */
    clearSearch(): void {
        this.businessPartnerSearchForm.reset();
        this.businessPartners = [];
        this.selectedBusinessPartnerId.clear();
        this.businessPartnerService.selectedEntity$.next(null);
    }

    /**
     * Create business partner
     */
    createBusinessPartner(): void {
        this.businessPartnerService.selectedEntity$.next(null);
        this.router.navigate(['/business-partners/profile/create']);
    }

    /**
     * Update business partner
     */
    updateBusinessPartner(): void {
        this.businessPartnerService.selectedEntity$.next(this.selectedBusinessPartner);
        this.router.navigate(['/business-partners/update', this.selectedBusinessPartner.id, this.selectedBusinessPartner.defaultPartnerRole]);
    }

    /**
     * View business partner
     */
    viewBusinessPartner(): void {
        this.businessPartnerService.selectedEntity$.next(this.selectedBusinessPartner);
        this.router.navigate(['/business-partners/view', this.selectedBusinessPartner.id, this.selectedBusinessPartner.defaultPartnerRole]);
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}