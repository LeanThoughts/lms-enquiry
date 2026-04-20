import { Component, OnInit } from '@angular/core';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';
import { 
    DatePickerComponent, 
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    FormModule, 
    LayoutGridModule, 
    SelectModule, 
    TitleComponent,
} from '@fundamental-ngx/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from '../../../../message.service';
import { MultiComboboxModule } from '@fundamental-ngx/core';

@Component({
    selector: 'app-kyc-details-update',
    templateUrl: './kyc-details-update.component.html',
    imports: [
        LayoutGridModule,
        ReactiveFormsModule,
        FormModule,
        SelectModule,
        DatePickerComponent,
        TitleComponent,
        DialogModule,
        DialogCloseButtonComponent,
    ]
})
export class KycDetailsUpdateDialogComponent implements OnInit {

    title: string = '';
    kycDetailsForm!: FormGroup;
    operation: string = '';
    defaultPartnerRole: string = '';
    selectedKYCDetails: any;
    selectedBusinessPartnerId: string = '';

    /**
     * Constructor
     */
    constructor(
        public dialogRef: DialogRef, 
        private businessPartnerService: BusinessPartnerSearchService, 
        private messageService: MessageService) 
    {
        // Initialize data for dropdowns and other variables
        this.operation = this.dialogRef.data.operation;
        this.selectedKYCDetails = this.dialogRef.data.selectedKYCDetails;
        if (this.operation !== 'view') {
            this.selectedBusinessPartnerId = this.dialogRef.data.selectedBusinessPartnerId;
            this.defaultPartnerRole = this.dialogRef.data.defaultPartnerRole;
        }

        // Get title
        this.title = this.getTitle();
    }

    /**
     * Get title
     */
    getTitle(): string {
        return this.operation === 'create' ? 'Create KYC Details' : this.operation === 'update' ? 'Update KYC Details' : 'View KYC Details';
    }

    /**
     * On init
     */
    ngOnInit(): void {
        if (this.operation !== 'view') {
            // Initialize kyc details form
            this.kycDetailsForm = new FormGroup({
                kycDate: new FormControl(this.selectedKYCDetails?.kycDate || null, [Validators.required]),
                kycRiskCategory: new FormControl(this.selectedKYCDetails?.kycRiskCategory || null, [Validators.required]),
                reKYCDate: new FormControl(this.selectedKYCDetails?.reKYCDate || null),
                reKYCRiskCategory: new FormControl(this.selectedKYCDetails?.reKYCRiskCategory || null),
            });
        }
    }

    /**
     * Submit
     */
    submit() {
        if (this.kycDetailsForm.invalid) {
            this.kycDetailsForm.markAllAsTouched();
            return;
        }

        this.saveKycDetails();
    }

    /**
     * Save identification details
     */
    private saveKycDetails(): void {
        const handleError = (operation: string) => (error: any) => {
            this.messageService.showError(
                `${error.message}!! Error ${operation} KYC details. Please try again. If the problem persists, please contact `
                + `the administrator.`
            );
        };

        var kycDetails: any = {};
        Object.assign(kycDetails, this.kycDetailsForm.value);

        // Fix data related issues
        if (kycDetails.kycDate) {
            const dt = new Date(kycDetails.kycDate);
            kycDetails.kycDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }
        if (kycDetails.reKYCDate) {
            const dt = new Date(kycDetails.reKYCDate);
            kycDetails.reKYCDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }

        if (this.operation === 'create') {
            this.businessPartnerService.createBusinessPartnerKYCDetails(kycDetails, this.selectedBusinessPartnerId).subscribe({
                next: (result: any) => {
                    this.messageService.showSuccess('KYC details created successfully');
                    this.dialogRef.close('Created');
                },
                error: handleError('creating')
            });
        }
        else {
            Object.assign(this.selectedKYCDetails, kycDetails);
            this.businessPartnerService.updateBusinessPartnerKYCDetails(this.selectedKYCDetails).subscribe({
                next: (result: any) => {
                    this.messageService.showSuccess('KYC details updated successfully');
                    this.dialogRef.close('Updated');
                },
                error: handleError('updating')
            });
        }
    }
}
