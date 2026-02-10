import { Component, OnInit } from '@angular/core';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';
import { 
    ButtonComponent, 
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
    selector: 'app-bank-details-update',
    templateUrl: './bank-details-update.component.html',
    imports: [
        ButtonComponent,
        LayoutGridModule,
        ReactiveFormsModule,
        FormModule,
        SelectModule,
        DatePickerComponent,
        TitleComponent,
        MultiComboboxModule,
        DialogModule,
        DialogCloseButtonComponent,
    ]
})
export class BankDetailsUpdateDialogComponent implements OnInit {

    title: string = '';
    bankDetailsForm!: FormGroup;
    operation: string = '';
    defaultPartnerRole: string = '';
    selectedBankDetails: any;
    selectedBusinessPartnerId: string = '';
    bankKeys: any[] = [];
    filteredBankKeys: any[] = [];
    bankCountries: any[] = [];

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
        if (this.operation !== 'view') {
            this.selectedBusinessPartnerId = this.dialogRef.data.selectedBusinessPartnerId;
            this.defaultPartnerRole = this.dialogRef.data.defaultPartnerRole;
            this.formFieldsConfig = this.dialogRef.data.routeResolvedData.formFieldsConfig;
            this.bankKeys = this.dialogRef.data.routeResolvedData.bankKey;
            this.bankCountries = this.dialogRef.data.routeResolvedData.bankCountry;
        }
        this.selectedBankDetails = this.dialogRef.data.selectedBankDetails;

        // Get title
        this.title = this.getTitle();
    }

    /**
     * Get title
     */
    getTitle(): string {
        return this.operation === 'create' ? 'Create Bank Details' : this.operation === 'update' ? 'Update Bank Details' : 'View Bank Details';
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Initialize partner details form
        if (this.operation !== 'view') {
            this.bankDetailsForm = new FormGroup({
                serialNumber: new FormControl(this.selectedBankDetails?.serialNumber || null),
                bankKeySearchText: new FormControl(this.selectedBankDetails?.bankKeySearchText || null),
                bankKey: new FormControl(this.selectedBankDetails?.bankKey || null),
                bankName: new FormControl(this.selectedBankDetails?.bankName || null),
                ifscCode: new FormControl(this.selectedBankDetails?.ifscCode || null),
                accountNumber: new FormControl(this.selectedBankDetails?.accountNumber || null),
                entryDate: new FormControl(this.selectedBankDetails?.entryDate || null),
                validFromDate: new FormControl(this.selectedBankDetails?.validFromDate || null),
                validToDate: new FormControl(this.selectedBankDetails?.validToDate || null),
                bankCountry: new FormControl(this.selectedBankDetails?.bankCountry || null),
                referenceNumber: new FormControl(this.selectedBankDetails?.referenceNumber || null),
                accountHolderName: new FormControl(this.selectedBankDetails?.accountHolderName || null),
                bankAccountName: new FormControl(this.selectedBankDetails?.bankAccountName || null),
            });
            // Invoke configureFormFields to set the form validators
            this.configureFormFields();
        }
    }


    /**
     * Search bank keys
     */
    searchBankKeys() {
        const searchText = this.bankDetailsForm.value.bankKeySearchText;
        this.filteredBankKeys = this.bankKeys.filter((bankKey: any) =>
            (bankKey.bankKey && bankKey.bankKey.toLowerCase().includes(searchText?.toLowerCase())) ||
            (bankKey.bankName && bankKey.bankName.toLowerCase().includes(searchText?.toLowerCase()))
        );
    }

    /**
     * On bank key selection change
     */
    onBankKeySelectionChange(event: any) {
        console.log('event', event);
        const bank: any = this.bankKeys.filter((bankKey: any) => bankKey.bankKey === event)[0];
        console.log('bank', bank);
        if (bank) {
            this.bankDetailsForm.get('bankName')?.setValue(bank.bankName);
            this.bankDetailsForm.get('ifscCode')?.setValue(bank.bankNumber);
        }
    }

    /**
     * Submit
     */
    submit() {
        if (this.bankDetailsForm.invalid) {
            this.bankDetailsForm.markAllAsTouched();
            return;
        }

        this.saveBankDetails();
    }

    /**
     * Save identification details
     */
    private saveBankDetails(): void {
        const handleError = (operation: string) => (error: any) => {
            this.messageService.showError(
                `${error.message}!! Error ${operation} bank details. Please try again. If the problem persists, please contact `
                + `the administrator.`
            );
        };

        console.log('this.bankDetailsForm.value', this.bankDetailsForm.value);
        var bankDetails: any = {};
        Object.assign(bankDetails, this.bankDetailsForm.value);
        bankDetails.bankKey = bankDetails.bankKey.bankKey;
        console.log('bankDetails', bankDetails);

        // Fix data related issues
        if (bankDetails.entryDate) {
            const dt = new Date(bankDetails.entryDate);
            bankDetails.entryDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }
        if (bankDetails.validFromDate) {
            const dt = new Date(bankDetails.validFromDate);
            bankDetails.validFromDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }
        if (bankDetails.validToDate) {
            const dt = new Date(bankDetails.validToDate);
            bankDetails.validToDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        }

        if (this.operation === 'create') {
            this.businessPartnerService.createBusinessPartnerBankDetails(bankDetails, this.selectedBusinessPartnerId).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Bank details created successfully');
                    this.dialogRef.close('Created');
                },
                error: handleError('creating')
            });
        }
        else {
            Object.assign(this.selectedBankDetails, bankDetails);
            this.businessPartnerService.updateBusinessPartnerBankDetails(this.selectedBankDetails).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Bank details updated successfully');
                    this.dialogRef.close('Updated');
                },
                error: handleError('updating')
            });
        }
    }

    /**
     * Configure form fields and filter it based on the default partner role
     */
    configureFormFields() {
        const formFieldsConfig: any = {};
        const fieldList = this.formFieldsConfig.bupaRoleEntitySetFieldStatusList || [];
        fieldList
        .filter((fieldConfig: any) => fieldConfig.entitySet === 'BusinesPartnerBankDetail')
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
        console.log('this.filteredFormFieldsConfig', this.filteredFormFieldsConfig);
        this.resetFormValidators();
    }

    /**
     * Reset form validators
     */
    resetFormValidators() {
        // More maintainable and scalable way: use field/validator config maps & a loop
        const fieldConfigMap: { [key: string]: { field: string, configKey: string } } = {
            bankKey: { field: 'bankKey', configKey: 'bankKey' },
            bankName: { field: 'bankName', configKey: 'bankName' },
            ifscCode: { field: 'ifscCode', configKey: 'ifscCode' },
            accountNumber: { field: 'accountNumber', configKey: 'accountNumber' },
            entryDate: { field: 'entryDate', configKey: 'entryDate' },
            validFromDate: { field: 'validFromDate', configKey: 'validFromDate' },
            validToDate: { field: 'validToDate', configKey: 'validToDate' },
            bankCountry: { field: 'bankCountry', configKey: 'bankCountry' },
            referenceNumber: { field: 'referenceNumber', configKey: 'referenceNumber' },
            accountHolderName: { field: 'accountHolderName', configKey: 'accountHolderName' },
            bankAccountName: { field: 'bankAccountName', configKey: 'bankAccountName' },
        };

        Object.values(fieldConfigMap).forEach(({ field, configKey }) => {
            const control = this.bankDetailsForm.get(field);
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

    /**
     * Display function for combobox
     */
    bankKeyDisplayFunction(bankKey: any): string {
        if (!bankKey) return '';
        return bankKey?.bankKey + ' | ' + bankKey?.bankName;
    }
}
