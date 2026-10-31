import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { 
    BreadcrumbModule, 
    ButtonComponent, 
    DeprecatedToolbarSizeDirective, 
    DynamicPageModule, 
    FormModule, 
    IconModule, 
    LayoutGridModule, 
    ToolbarComponent, 
    ToolbarSeparatorComponent 
} from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from './business-partner-search.service';
import { MessageService } from '../../message.service';
import { SelectionModel } from '@angular/cdk/collections';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { BusinessPartnerListComponent } from './business-partner-list/business-partner-list.component';
import {
    BP_SEARCH_PAGE_SIZES,
    BusinessPartnerRow,
    BusinessPartnerSearchCriteria,
    BusinessPartnerSearchResult
} from './business-partner-search.model';

/** A business partner role for the Partner Role dropdown (businessPartnerRoleTypes) */
interface PartnerRoleOption {
    code: string;
    value: string;
}

@Component({
    selector: 'app-business-partner-search',
    imports: [
        ButtonComponent,
        FormModule,
        IconModule,
        LayoutGridModule,
        ReactiveFormsModule,
        BusinessPartnerListComponent,
        BreadcrumbModule,
        DynamicPageModule,
        ToolbarComponent,
        DeprecatedToolbarSizeDirective,
        ToolbarSeparatorComponent
    ],
    templateUrl: './business-partner-search.component.html',
    styleUrl: './business-partner-search.component.scss'
})
export class BusinessPartnerSearchComponent implements OnInit, OnDestroy {

    destroy$: Subject<void> = new Subject<void>();

    /** The current page of the result (null before the first search) */
    result: BusinessPartnerSearchResult | null = null;
    page = 0;
    size = BP_SEARCH_PAGE_SIZES[0];
    searching = false;

    /** Values of the Partner Role dropdown */
    partnerRoles: PartnerRoleOption[] = [];

    businessPartnerSearchForm = new FormGroup({
        partnerName: new FormControl(''),
        email: new FormControl(''),
        partnerNumberFrom: new FormControl(''),
        partnerNumberTo: new FormControl(''),
        panNumber: new FormControl(''),
        searchTerm: new FormControl(''),
        partnerRole: new FormControl('')
    });

    selectedBusinessPartner: any;
    selectedBusinessPartnerId: SelectionModel<any> = new SelectionModel<any>();

    /**
     * Constructor
     */
    constructor(
        private businessPartnerService: BusinessPartnerSearchService,
        private messageService: MessageService,
        private route: ActivatedRoute,
        public router: Router
    ) {}

    get businessPartners(): BusinessPartnerRow[] {
        return this.result?.rows ?? [];
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Partner roles for the dropdown, from the route resolver (businessPartnerRoleTypes)
        const roleTypes = this.route.snapshot.data?.['routeResolvedData']?.businessPartnerRoleTypes;
        this.partnerRoles = (Array.isArray(roleTypes) ? roleTypes : [])
            .filter((role: any) => role && role.code)
            .map((role: any) => ({ code: String(role.code), value: role.value ? String(role.value) : '' }))
            .sort((a: PartnerRoleOption, b: PartnerRoleOption) => (a.value || a.code).localeCompare(b.value || b.code));

        // Restore the last search (e.g. after returning from Update / View)
        const state = this.businessPartnerService.businessPartnerSearchParameters$.value;
        if (state?.criteria) {
            this.businessPartnerSearchForm.patchValue(this.toForm(state.criteria));
            this.size = state.size || this.size;
            this.load(state.page || 0);
        }

        // Subscribe to the selected entity from the service
        this.businessPartnerService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe({
            next: (value: any) => {
                this.selectedBusinessPartner = value;
            }
        });
    }

    /**
     * Search business partners (first page)
     */
    searchBusinessPartners(): void {
        // Check if at least one search parameter is provided
        const criteria = this.criteria();
        const hasValue = Object.values(criteria).some(value => value !== null && value !== undefined && String(value).trim() !== '');
        if (!hasValue) {
            this.messageService.showError('Please provide a value for at least one search parameter.');
            return;
        }

        // Check if Partner number to is lesser than partner number from
        const from = Number(criteria.partnerNumberFrom);
        const to = Number(criteria.partnerNumberTo);
        if (criteria.partnerNumberTo && criteria.partnerNumberFrom && !isNaN(from) && !isNaN(to) && to < from) {
            this.messageService.showError('Partner Number To should be greater than Partner Number From');
            return;
        }
        this.load(0);
    }

    /** Another page of the result */
    goToPage(page: number): void {
        this.load(page);
    }

    /** Another page size: back to the first page */
    setPageSize(size: number): void {
        if (size && size !== this.size) {
            this.size = size;
            this.load(0);
        }
    }

    private load(page: number): void {
        const criteria = this.criteria();
        this.searching = true;
        this.businessPartnerService.searchBusinessPartnersPaged(criteria, page, this.size).pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: BusinessPartnerSearchResult) => {
                this.searching = false;
                this.result = result;
                this.page = result.page;
                this.size = result.size;
                this.businessPartnerService.businessPartnerSearchParameters$.next({ criteria, page: result.page, size: result.size });
            },
            error: (error: any) => {
                this.searching = false;
                this.messageService.showError(error?.error?.message || error?.message || 'The business partners could not be searched.');
            }
        });
    }

    /** The form as search criteria */
    private criteria(): BusinessPartnerSearchCriteria {
        const form = this.businessPartnerSearchForm.value;
        return {
            name: form.partnerName || '',
            email: form.email || '',
            pan: form.panNumber || '',
            searchTerm: form.searchTerm || '',
            partnerRole: form.partnerRole || '',
            partnerNumberFrom: form.partnerNumberFrom || '',
            partnerNumberTo: form.partnerNumberTo || ''
        };
    }

    private toForm(criteria: BusinessPartnerSearchCriteria): any {
        return {
            partnerName: criteria.name ?? '',
            email: criteria.email ?? '',
            panNumber: criteria.pan ?? '',
            searchTerm: criteria.searchTerm ?? '',
            partnerRole: criteria.partnerRole ?? '',
            partnerNumberFrom: criteria.partnerNumberFrom ?? '',
            partnerNumberTo: criteria.partnerNumberTo ?? ''
        };
    }

    /**
     * Clear search
     */
    clearSearch(): void {
        this.businessPartnerSearchForm.reset({ partnerName: '', email: '', partnerNumberFrom: '', partnerNumberTo: '',
            panNumber: '', searchTerm: '', partnerRole: '' });
        this.result = null;
        this.page = 0;
        this.businessPartnerService.businessPartnerSearchParameters$.next(null);
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
        this.router.navigate(['/business-partners/update', this.selectedBusinessPartner.id]);
    }

    /**
     * View business partner
     */
    viewBusinessPartner(): void {
        this.businessPartnerService.selectedEntity$.next(this.selectedBusinessPartner);
        this.router.navigate(['/business-partners/view', this.selectedBusinessPartner.id]);
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
