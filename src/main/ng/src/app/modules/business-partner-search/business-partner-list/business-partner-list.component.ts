import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IconComponent, PaginationModule, TableModule } from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from '../business-partner-search.service';
import { CommonModule } from '@angular/common';
import { SelectionModel } from '@angular/cdk/collections';
import { BP_SEARCH_PAGE_SIZES } from '../business-partner-search.model';

@Component({
    selector: 'app-business-partner-list',
    imports: [
        CommonModule,
        IconComponent,
        PaginationModule,
        TableModule
    ],
    templateUrl: './business-partner-list.component.html',
    styleUrl: './business-partner-list.component.scss'
})
export class BusinessPartnerListComponent {

    @Input() businessPartners!: Array<any>;
    /** All matches of the search (the list shows one page of them) */
    @Input() total = 0;
    /** Current page, 0 based */
    @Input() page = 0;
    @Input() size = BP_SEARCH_PAGE_SIZES[0];
    /** A search was made (else the list invites to search) */
    @Input() searched = false;
    @Output() pageChange = new EventEmitter<number>();
    @Output() pageSizeChange = new EventEmitter<number>();

    readonly pageSizes = BP_SEARCH_PAGE_SIZES;

    selectedBusinessPartnerId: SelectionModel<any> = new SelectionModel<any>();

    /**
     * Constructor
     */
    constructor(public businessPartnerService: BusinessPartnerSearchService) {
    }

    /** "26–50 of 719" */
    get rangeText(): string {
        if (!this.total) {
            return '';
        }
        const from = this.page * this.size + 1;
        const to = Math.min(this.total, from + (this.businessPartners?.length ?? 0) - 1);
        return `${from}–${to} of ${this.total}`;
    }

    /** fd-pagination counts pages from 1 */
    onPage(page: number): void {
        const index = Math.max(0, page - 1);
        if (index !== this.page) {
            this.selectedBusinessPartnerId.clear();
            this.businessPartnerService.selectedEntity$.next(null);
            this.pageChange.emit(index);
        }
    }

    onPageSize(size: number): void {
        if (size && size !== this.size) {
            this.selectedBusinessPartnerId.clear();
            this.businessPartnerService.selectedEntity$.next(null);
            this.pageSizeChange.emit(size);
        }
    }

    /**
     * Get partner category description
     */
    getPartnerCategoryDescription(partnerCategory: string): string {
        const categoryMap: { [key: string]: string } = {
            '1': 'Person',
            '2': 'Organization',
            '3': 'Group'
        };
        return categoryMap[partnerCategory] || '--';
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
     * Format address, omitting missing / blank parts
     */
    formatAddress(partner: any): string {
        return [partner?.addressLine1, partner?.addressLine2, partner?.city]
            .map((part) => (part == null ? '' : String(part).trim().replace(/^,+|,+$/g, '').trim()))
            .filter((part) => part !== '')
            .join(', ');
    }
}
