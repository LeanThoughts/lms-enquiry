import { Component, OnInit } from '@angular/core';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';
import { 
    DatePickerComponent, 
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    FileUploaderComponent, 
    FormModule, 
    IconComponent, 
    LayoutGridModule, 
    SelectModule, 
    TitleComponent,
} from '@fundamental-ngx/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from '../../../../message.service';
import { MultiComboboxModule } from '@fundamental-ngx/core';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-identification-details-update',
    templateUrl: './identification-details-update.component.html',
    imports: [
        LayoutGridModule,
        ReactiveFormsModule,
        FormModule,
        SelectModule,
        DatePickerComponent,
        TitleComponent,
        MultiComboboxModule,
        DialogModule,
        DialogCloseButtonComponent,
        FileUploaderComponent,
        IconComponent,
        NgIf,
    ]
})
export class IdentificationDetailsUpdateDialogComponent implements OnInit {

    title: string = '';
    identificationDetailsForm!: FormGroup;
    operation: string = '';
    defaultPartnerRole: string = '';

    selectedIdentification: any;
    selectedBusinessPartnerId: string = '';
    identificationCategories: any;
    countries: any;
    regions: any;
    documentTypes: any;

    formFieldsConfig: any = {};
    filteredFormFieldsConfig: any = {};

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
        this.selectedIdentification = this.dialogRef.data.selectedIdentification;
        if (this.operation !== 'view') {
            this.selectedBusinessPartnerId = this.dialogRef.data.selectedBusinessPartnerId;
            this.identificationCategories = this.dialogRef.data.routeResolvedData.identificationCategories;
            this.countries = this.dialogRef.data.routeResolvedData.countries;
            this.documentTypes = this.dialogRef.data.routeResolvedData.documentTypes;
            this.defaultPartnerRole = this.dialogRef.data.defaultPartnerRole;
            this.formFieldsConfig = this.dialogRef.data.routeResolvedData.formFieldsConfig;
        }

        // Get title
        this.title = this.getTitle();
    }

    /**
     * Get title
     */
    getTitle(): string {
        return this.operation === 'create' ? 'Create Identification Details' : this.operation === 'update' ? 'Update Identification Details' 
            : 'View Identification Details';
    }

    /**
     * On init
     */
    ngOnInit(): void {
        if (this.operation !== 'view') {
            // Initialize partner details form
            this.identificationDetailsForm = new FormGroup({
                identificationCategoryCode: new FormControl(this.selectedIdentification?.identificationCategoryCode || null),
                identificationNumber: new FormControl(this.selectedIdentification?.identificationNumber || null),
                idInstitute: new FormControl(this.selectedIdentification?.idInstitute || null),
                idEntryDate: new FormControl(this.selectedIdentification?.idEntryDate || null),
                idValidFromDate: new FormControl(this.selectedIdentification?.idValidFromDate || null),
                idValidToDate: new FormControl(this.selectedIdentification?.idValidToDate || null),
                country: new FormControl(this.selectedIdentification?.country || 'IN'),
                region: new FormControl(this.selectedIdentification?.region || null),
                documentName: new FormControl(this.selectedIdentification?.documentName || null),
                documentType: new FormControl(this.selectedIdentification?.documentType || null),
                file: new FormControl(null)
            });
            // Invoke on identification category change to set the form validators
            if (this.operation === 'update') {
                this.onIdentificationCategoryChange(this.identificationDetailsForm.get('identificationCategoryCode')?.value);
            }
        }
    }

    /**
     * On country change
     */
    onCountryChange(event: any) {
        this.businessPartnerService.getRegions(event).subscribe(response => {
            this.regions = response;
        });
    }

    /**
     * Submit
     */
    submit() {
        if (this.identificationDetailsForm.invalid) {
            this.identificationDetailsForm.markAllAsTouched();
            return;
        }

        if (this.identificationDetailsForm.get('file')?.value) {
            this.uploadFileAndSave();
        } else {
            this.saveIdentificationDetails('');
        }
    }

    /**
     * Save identification details
     */
    private saveIdentificationDetails(fileReference: string): void {
        const handleError = (operation: string) => (error: any) => {
            this.messageService.showError(
                `${error.message}!! Error ${operation} identification details. Please try again. If the problem persists, please contact `
                + `the administrator.`
            );
        };

        var identificationDetails = this.identificationDetailsForm.value;
        identificationDetails.fileReference = fileReference 
            || (this.operation === 'update' ? this.selectedIdentification.fileReference : '') 
            || '';

        // Fix data related issues
        if (identificationDetails.idEntryDate) {
            const dt = new Date(identificationDetails.idEntryDate);
            identificationDetails.idEntryDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }
        if (identificationDetails.idValidFromDate) {
            const dt = new Date(identificationDetails.idValidFromDate);
            identificationDetails.idValidFromDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }
        if (identificationDetails.idValidToDate) {
            const dt = new Date(identificationDetails.idValidToDate);
            identificationDetails.idValidToDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }

        if (this.operation === 'create') {
            this.businessPartnerService.createBusinessPartnerIdentificationDetails(identificationDetails, this.selectedBusinessPartnerId).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Identification details created successfully');
                    // this.businessPartnerService.selectedEntity$.next('Created');
                    this.dialogRef.close('Created');
                },
                error: handleError('creating')
            });
        }
        else {
            Object.assign(this.selectedIdentification, identificationDetails);
            this.businessPartnerService.updateBusinessPartnerIdentificationDetails(this.selectedIdentification).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Identification details updated successfully');
                    // this.businessPartnerService.selectedEntity$.next('Updated');
                    this.dialogRef.close('Updated');
                },
                error: handleError('updating')
            });
        }
    }

    /**
     * Upload file and then save entity
     */
    private uploadFileAndSave(): void {
        const formData = new FormData();
        const fileValue = this.identificationDetailsForm.get('file')?.value;
        formData.append('file', fileValue[0], fileValue[0].name);
        this.businessPartnerService.uploadVaultDocument(formData).subscribe({
            next: (response: any) => {
                this.saveIdentificationDetails(response.fileReference);
            },
            error: (error: any) => {
                this.messageService.showError('Unable to upload the file. Please try again after sometime or contact your system administrator');
            }
        });
    }

    /**
     * Handle file selection
     */
    handleFileSelection(event: any) {
        this.identificationDetailsForm.get('file')?.setValue(event);
        this.identificationDetailsForm.get('file')?.updateValueAndValidity();
    }

    /**
     * On identification category change
     */
    onIdentificationCategoryChange(event: any) {
        const formFieldsConfig: any = {};
        if (event) {
            const fieldList = this.formFieldsConfig.bupaRoleEntitySetFieldStatusList || [];
            fieldList
            .filter((fieldConfig: any) => fieldConfig.entitySet === 'BusinessPartnerIdentification')
            .filter((fieldConfig: any) => fieldConfig.keyFieldValue === event)
            .forEach((fieldConfig: any) => {
                const fieldKey = fieldConfig.fieldName ?? fieldConfig.field;
                if (fieldKey.trim().split(';').length > 1) {
                    formFieldsConfig[fieldKey.trim().split(';')[0]] = fieldConfig.fieldStatus;
                } 
                else {
                    formFieldsConfig[fieldKey.trim()] = fieldConfig.fieldStatus;
                }
            });    
            this.filteredFormFieldsConfig = { ...formFieldsConfig };
            this.resetFormValidators();
        }
    }

    /**
     * Reset form validators
     */
    resetFormValidators() {
        // More maintainable and scalable way: use field/validator config maps & a loop
        const fieldConfigMap: { [key: string]: { field: string, configKey: string } } = {
            identificationNumber: { field: 'identificationNumber', configKey: 'BusinessPartnerIdentificationNumber' },
            idInstitute: { field: 'idInstitute', configKey: 'idInstitute' },
            idEntryDate: { field: 'idEntryDate', configKey: 'idEntryDate' },
            idValidFromDate: { field: 'idValidFromDate', configKey: 'idValidFromDate' },
            idValidToDate: { field: 'idValidToDate', configKey: 'idValidToDate' },
            country: { field: 'country', configKey: 'country' },
            region: { field: 'region', configKey: 'region' },
            documentName: { field: 'documentName', configKey: 'documentName' },
            documentType: { field: 'documentType', configKey: 'documentType' },
        };
        if (this.operation === 'create') {
            fieldConfigMap['file'] = { field: 'file', configKey: 'fileReference' }
        }

        Object.values(fieldConfigMap).forEach(({ field, configKey }) => {
            const control = this.identificationDetailsForm.get(field);
            if (!control) return;
            
            // Only one validator for all these fields: Validators.required. If there are different validators, expand this logic.
            if (this.filteredFormFieldsConfig[configKey] === 2) {
                control.setValidators([Validators.required]);
            } else {
                control.removeValidators([Validators.required]);
            }
            control.updateValueAndValidity();
        });
    }
}
