import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { 
    ButtonComponent,
    DatePickerModule, 
    FormModule, 
    IconComponent,
    MessageStripAlertService, 
    PaginationModule, 
    PanelModule,
    SelectModule,
    TableModule
} from '@fundamental-ngx/core';
import { ContentDensityDirective } from '@fundamental-ngx/core/content-density';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { SelectionModel } from '@angular/cdk/collections';
import { LoanContractSearchService } from '../loan-contract-search.service';
import { statesOfIndia } from '../../../app.constants';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-loan-contract-list',
    imports: [
        ButtonComponent,
        CommonModule,
        ContentDensityDirective,
        DatePickerModule,
        FormModule,
        IconComponent,
        PaginationModule,
        PanelModule,
        ReactiveFormsModule,
        SelectModule,
        TableModule
    ],
    templateUrl: './loan-contract-list.component.html',
    styleUrl: './loan-contract-list.component.scss'
})
export class LoanContractListComponent implements OnInit, OnDestroy {

    technicalStatuses!: any[];
    states = statesOfIndia.sort((a: any, b: any) => a.value.localeCompare(b.value)); // Sort the states by value
    loanClasses!: any[];
    projectTypes!: any[];
    financingTypes!: any[];
    assistanceTypes!: any[];

    enquiries: any[] = [];
    expanded = true;

    currentPage = 1;
    dataSource: any[] = [];
    itemsPerPage = 10;
    totalItems = 0;

    selectedEnquiry: SelectionModel<any> = new SelectionModel<any>(false, []);

    loanContractSearchForm!: FormGroup;

    functionalStatuses = [
        {code: "1", value:"Enquiry Stage"},
        {code: "2", value:"ICC In-Principle Approval Stage"},
        {code: "10", value:"Prelim Risk Assessment Stage"},
        {code: "11", value:"Application Fee Stage"},
        {code: "3", value:"Appraisal Stage"},
        {code: "12", value:"BMC Approval Stage"},
        {code: "4", value:"Board Approval Stage"},
        {code: "5", value:"Sanction Stage"},
        {code: "6", value:"Documentation Stage"},
        {code: "8", value:"Loan Monitoring Stage"},
        {code: "9", value:"Recovery Stage"}
    ];

    private readonly destroy$ = new Subject<void>();

    constructor(
        private activatedRoute: ActivatedRoute,
        private formBuilder: FormBuilder,
        public loanContractSearchService: LoanContractSearchService,
        private messageStripAlertService: MessageStripAlertService
    ) 
    {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // if no enquiry is selected, then set loanContractSearchService.selectedEnquiry$ to null
        if (this.selectedEnquiry.selected.length === 0) {
            this.loanContractSearchService.selectedEnquiry$.next(null);
        }
        
        // Subscribe to the route resolver to get the data for dropdowns and sort the technical statuses by description as the api does not support sorting
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((data: any) => {
            // this.functionalStatuses = data.routeResolver.functionalStatuses._embedded.functionalStatuses;
            this.loanClasses = data.routeResolver.loanClasses._embedded.loanClasses;
            this.projectTypes = data.routeResolver.projectTypes._embedded.projectTypes;
            this.financingTypes = data.routeResolver.financingTypes._embedded.financingTypes;
            this.assistanceTypes = data.routeResolver.assistanceTypes._embedded.assistanceTypes;
            this.technicalStatuses = data.routeResolver.technicalStatuses.sort((a: any, b: any) => 
                a.description.localeCompare(b.description));
        });

        // Initialize the form
        this.loanContractSearchForm = this.formBuilder.group({
            functionalStatus: [null],
            technicalStatus: [null],
            partyName: [null],
            projectLocationState: [null],
            loanClass: [null],
            projectType: [null],
            financingType: [null],
            assistanceType: [null],
            borrowerCodeFrom: [null],
            borrowerCodeTo: [null],
            loanNumberFrom: [null],
            loanNumberTo: [null],
            enquiryNumber: [null],
            enquiryDate: [null]
        });

        // Restore the search criteria of the last successful search
        if (this.loanContractSearchService.searchFormValue) {
            this.loanContractSearchForm.patchValue(this.loanContractSearchService.searchFormValue);
        }
    }

    /**
     * On select enquiry
     */
    onSelectEnquiry(enquiry: any): void {
        this.selectedEnquiry.select(enquiry.loanApplication.enquiryNo.id);
        this.loanContractSearchService.selectedEnquiry$.next(enquiry);
        console.log('selected enquiry is', enquiry);
    }
    
    /**
     * Search for loan contracts
     */
    searchLoanContracts(): void {
        if (Object.values(this.loanContractSearchForm.value).every(value => value === null))
            this.displayAlertMessage('Provide at least one search parameter');
        else {
            const formValue = this.loanContractSearchForm.value;
            const searchParameters = { ...formValue };
            if (formValue.enquiryDate) {
                searchParameters.enquiryDate = new Date(Date.UTC(formValue.enquiryDate.year, formValue.enquiryDate.month - 1, 
                    formValue.enquiryDate.day));
            }
            this.loanContractSearchService.searchLoanContracts(searchParameters).subscribe((result) => {
                if (result.length === 0) {
                    this.displayAlertMessage('No records found for search criteria');
                }
                else {
                    this.loanContractSearchService.searchFormValue = formValue;
                    result.sort((a: any, b: any) => b.loanApplication.enquiryNo.id - a.loanApplication.enquiryNo.id);
                    this.enquiries = result;
                    this.totalItems = this.enquiries.length;
                    // Reset to first page when getting new results
                    this.currentPage = 1;
                    this.expanded = false;
                    this.updateDataSource();
                }
            });
        }
    }
    
    /**
     * Clear the search form, the stored search criteria and the results
     */
    clearSearch(): void {
        this.loanContractSearchForm.reset();
        this.loanContractSearchService.searchFormValue = null;
        this.enquiries = [];
        this.totalItems = 0;
        this.currentPage = 1;
        this.updateDataSource();
    }

    /**
     * On items per page change
     */
    onItemsPerPageChange(pageSize: number): void {
        this.itemsPerPage = pageSize;
        // Stay on the current page if it still exists for the new page size, otherwise move to the last page
        const maxPage = Math.max(1, Math.ceil(this.totalItems / this.itemsPerPage));
        this.currentPage = Math.min(this.currentPage, maxPage);
        this.updateDataSource();
    }

    /**
     * On page change
     */
    onPageChange(page: number): void {
        this.currentPage = page;
        this.updateDataSource();
    }

    /**
     * Update data source based on current page and items per page
     */
    updateDataSource(): void {
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        // Ensure endIndex doesn't exceed total items
        const endIndex = Math.min(startIndex + this.itemsPerPage, this.totalItems);
        this.dataSource = this.enquiries?.slice(startIndex, endIndex) || [];
    }
        
    /**
     * Get project type
     */
    getProjectTypeDescription(projectType: string): string {
        return this.projectTypes.find((type: any) => type.code === projectType)?.value || projectType;
    }

    /**
     * Get loan class description
     */
    getLoanClassDescription(loanClass: string): string {
        return this.loanClasses.find((type: any) => type.code === loanClass)?.value || loanClass;
    }

    /**
     * Get assistance type description
     */
    getAssistanceTypeDescription(assistanceType: string): string {
        return this.assistanceTypes.find((type: any) => type.code === assistanceType)?.value || assistanceType;
    }

    /**
     * Get project capacity
     */
    getProjectCapacity(enquiry: any): string {
        if (enquiry.loanApplication.projectCapacity)
            return enquiry.loanApplication.projectCapacity + " " + enquiry.loanApplication.projectCapacityUnit;
        else
            return '';
    }

    /**
     * Get the initials of a name (first letters of the first two words)
     */
    getInitials(name: string): string {
        return (name || '')
            .trim()
            .split(/\s+/)
            .slice(0, 2)
            .map((word) => word.charAt(0).toUpperCase())
            .join('') || '?';
    }

    /**
     * Display alert message
     */
    private displayAlertMessage(message: string): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true,
            messageStrip: {
                duration: 7000,
                mousePersist: true,
                type: 'error',
                dismissible: true
            }
        });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }    
}
