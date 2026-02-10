import { Component, OnDestroy, OnInit } from '@angular/core';
import { 
    AvatarComponent,
    ButtonComponent, 
    DialogService, 
    LayoutGridModule, 
    LinkComponent,
    PanelComponent, 
    QuickViewComponent, 
    QuickViewGroupComponent, 
    QuickViewGroupItemComponent, 
    QuickViewGroupItemContentComponent, 
    QuickViewGroupItemLabelComponent, 
    QuickViewGroupTitleComponent, 
    QuickViewSubheaderComponent, 
    QuickViewSubheaderSubtitleComponent, 
    QuickViewSubheaderTitleComponent,
} from '@fundamental-ngx/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { entityComponentConfigs } from '../../../generic/generic-list-component-map.config';
import { GenericUpdateDialogComponent } from '../../../generic/generic-update-dialog/generic-update-dialog.component';
import { Subject, takeUntil } from 'rxjs';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';

@Component({
    selector: 'app-contact-list',
    imports: [
        AvatarComponent,
        ButtonComponent,
        LayoutGridModule,
        PanelComponent,
        QuickViewComponent,
        QuickViewSubheaderComponent,
        QuickViewSubheaderTitleComponent,
        QuickViewSubheaderSubtitleComponent,
        QuickViewGroupComponent,
        QuickViewGroupItemComponent,
        QuickViewGroupItemLabelComponent,
        QuickViewGroupItemContentComponent,
        QuickViewGroupTitleComponent,
        CommonModule,
        LinkComponent,
        PanelComponent
    ],
    templateUrl: './contact-list.component.html'
})
export class ContactListComponent implements OnInit, OnDestroy {

    operation: string = '';
    selectedBusinessPartnerId: any;

    businessPartnerContacts: any[] = [];

    private destroy$ = new Subject<void>();

    /**
     * Constructor
     */
    constructor(private route: ActivatedRoute, private dialog: DialogService, private businessPartnerSearchService: BusinessPartnerSearchService) {
        console.log('route.snapshot.data is', this.route.snapshot.data);
        this.businessPartnerContacts = 
            this.route.snapshot.data['routeResolvedData']['businessPartnerContacts'].sort((a: any, b: any) => a.name.localeCompare(b.name));
        console.log('business partner contacts', this.businessPartnerContacts);

        // Get selectedBusinessPartnerId from route params
        this.selectedBusinessPartnerId = this.route.snapshot.paramMap.get('id');
    }

    /**
     * On init
     */
    ngOnInit(): void {
    }

    /**
     * On zoom glyph click
     */
    onZoomGlyphClick(selectedContact: any): void {
        this.openUpdateDialog(selectedContact);
    }

    /**
     * Open the create dialog
     */
    openCreateDialog() {
        // Determine the config object based on entity
        const config = entityComponentConfigs['businessPartnerLoanContacts'];

        // Open the create dialog and subscribe to the afterClosed event
        const dialogRef = this.dialog.open(GenericUpdateDialogComponent, {
            data: {
                entity: 'businessPartnerLoanContacts',
                operation: 'Create',
                searchString1: this.selectedBusinessPartnerId,
            },
            width: '55rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Created') {
                    this.fetchData();
                }
            }
        });
    }
    
    /**
     * Open the update dialog
     */
    openUpdateDialog(selectedContact: any) {
        // Determine the config object based on entity
        const config = entityComponentConfigs['businessPartnerLoanContacts'];

        // Create a shallow copy of selectedObject and remove properties starting with 'formatted_'
        selectedContact = Object.keys(selectedContact || {}).reduce((acc: any, key: string) => {
            if (!key.startsWith('formatted_')) {
                acc[key] = selectedContact[key];
            }
            return acc;
        }, {});

        // Open the update dialog and subscribe to the afterClosed event
        const dialogRef = this.dialog.open(GenericUpdateDialogComponent, {
            data: {
                entity: 'businessPartnerLoanContacts',
                operation: 'Update',
                searchString1: selectedContact.id,
                selectedObject: selectedContact,
            },
            width: '55rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated') {
                    this.fetchData();
                }
            }
        }); 
    }

    /**
     * Fetch data
     */
    fetchData(): void {
        console.log('fetching data for business partner', this.selectedBusinessPartnerId);
        this.businessPartnerSearchService.getBusinessPartnerContacts(this.selectedBusinessPartnerId).subscribe((contacts: any) => {
            this.businessPartnerContacts = contacts.sort((a: any, b: any) => a.name.localeCompare(b.name));
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
