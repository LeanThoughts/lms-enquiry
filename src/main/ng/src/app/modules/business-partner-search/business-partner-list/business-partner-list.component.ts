import { Component, Input } from '@angular/core';
import { 
    ButtonComponent, 
    GridListModule, 
    IconModule, 
    LayoutGridModule,
    LinkComponent,
    TableModule} from '@fundamental-ngx/core';
import { BusinessPartnerSearchService } from '../business-partner-search.service';
import { ComponentNgxComponent } from '../../../common/component-ngx/component-ngx.component';
import { RouterLink } from '@angular/router';
import { IconComponent } from '@fundamental-ngx/core/icon';
import { CommonModule } from '@angular/common';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
    selector: 'app-business-partner-list',
    imports: [
        CommonModule,
        LinkComponent,
        TableModule
    ],
    templateUrl: './business-partner-list.component.html'
})
export class BusinessPartnerListComponent {

    @Input() businessPartners!: Array<any>;

    displayedColumns: string[] = ['Bus Partner Number', 'Default Role', 'Name', 'PAN', 'Category', 'Email', 'Contact Person'];
    
    selectedBusinessPartnerId: SelectionModel<any> = new SelectionModel<any>();

    /**
     * Constructor
     */
    constructor(public businessPartnerService: BusinessPartnerSearchService) {
    }

    /**
     * Get status
     */
    getStatus(partnerCategory: string): any {
        return partnerCategory === '1' ? 'information' : partnerCategory === '2' ? 'valid' : partnerCategory === '3' ? 'warning' : 'error';
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
}
