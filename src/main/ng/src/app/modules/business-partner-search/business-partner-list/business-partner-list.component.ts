import { Component, Input } from '@angular/core';
import { IconComponent, TableModule } from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from '../business-partner-search.service';
import { CommonModule } from '@angular/common';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
    selector: 'app-business-partner-list',
    imports: [
        CommonModule,
        IconComponent,
        TableModule
    ],
    templateUrl: './business-partner-list.component.html',
    styleUrl: './business-partner-list.component.scss'
})
export class BusinessPartnerListComponent {

    @Input() businessPartners!: Array<any>;

    selectedBusinessPartnerId: SelectionModel<any> = new SelectionModel<any>();

    /**
     * Constructor
     */
    constructor(public businessPartnerService: BusinessPartnerSearchService) {
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
