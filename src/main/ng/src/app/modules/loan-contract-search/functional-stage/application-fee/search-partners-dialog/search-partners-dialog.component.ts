import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { 
    ButtonComponent, 
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    FormModule, 
    LayoutGridModule, 
    TableModule,
    TitleComponent
} from '@fundamental-ngx/core';
import { ApplicationFeeService } from '../application-fee.service';
import { MessageService } from '../../../../../message.service';

@Component({
    selector: 'app-search-partners-dialog',
    imports: [
        ButtonComponent,
        DialogModule,
        DialogCloseButtonComponent,
        FormModule,
        LayoutGridModule,
        ReactiveFormsModule,
        TableModule,
        TitleComponent
    ],
    templateUrl: './search-partners-dialog.component.html'
})
export class SearchPartnersDialogComponent {

    partnerForm: FormGroup;
    partners: any[] = [];
    selectedPartner: any;

    /**
     * Constructor
     */
    constructor(
        public dialogRef: DialogRef,
        private formBuilder: FormBuilder,
        private applicationFeeService: ApplicationFeeService,
        private messageService: MessageService
    ) {
        this.partnerForm = this.formBuilder.group({
            partyName1: [''],
            partyNumber: [''],
            searchTerm1: [''],
            searchTerm2: ['']
        });
    }

    /**
     * Search partners. At least one criterion with a minimum of 3 characters is required.
     */
    search(): void {
        const criteria = this.partnerForm.value;
        const values: string[] = Object.values(criteria).map((value: any) => (value || '').trim());
        if (!values.some(value => value.length >= 3)) {
            this.messageService.showError('Please enter valid search criteria (Minimum 3 characters in any field).');
            return;
        }

        this.selectedPartner = null;
        this.applicationFeeService.searchPartners(criteria).subscribe({
            next: (response: any[]) => {
                this.partners = response || [];
                if (this.partners.length === 0) {
                    this.messageService.showInfo('The search criteria did not match any records.');
                }
            },
            error: () => {
                this.messageService.showError('An error occurred while searching partners.');
            }
        });
    }

    /**
     * Close the dialog with the selected partner
     */
    selectPartner(): void {
        if (!this.selectedPartner) {
            this.messageService.showError('Please select a partner from the table.');
            return;
        }
        this.dialogRef.close(this.selectedPartner);
    }
}
