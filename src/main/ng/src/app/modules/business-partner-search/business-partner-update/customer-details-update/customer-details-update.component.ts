import { Component, OnInit } from '@angular/core';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';
import { 
    CheckboxComponent,
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    FormModule, 
    LayoutGridModule, 
    SelectModule, 
    TitleComponent,
    MultiComboboxComponent
} from '@fundamental-ngx/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from '../../../../message.service';

@Component({
    selector: 'app-customer-details-update',
    templateUrl: './customer-details-update.component.html',
    imports: [
        LayoutGridModule,
        ReactiveFormsModule,
        FormModule,
        SelectModule,
        TitleComponent,
        DialogModule,
        DialogCloseButtonComponent,
        CheckboxComponent,
        MultiComboboxComponent
    ]
})
export class CustomerDetailsUpdateDialogComponent implements OnInit {

    title: string = '';
    customerDetailsForm!: FormGroup;
    
    houseBanks: any[] = [];
    planningGroups: any[] = [];
    sortKeys: any[] = [];
    dunningProcedures: any[] = [];
    paymentTerms: any[] = [];
    paymentMethods: any[] = [];

    operation: string = '';
    defaultPartnerRole: string = '';
    selectedBusinessPartner: any;
    selectedPaymentMethods: any[] = [];

    formFieldsDefaultValues: any = {};
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
        this.operation = this.dialogRef.data.operation;
        this.selectedBusinessPartner = this.dialogRef.data.routeResolvedData.selectedBusinessPartner;
        if (this.operation !== 'view') {
            // Initialize data for dropdowns and other variables
            this.defaultPartnerRole = this.dialogRef.data.defaultPartnerRole;
            this.formFieldsDefaultValues = this.dialogRef.data.routeResolvedData.businessPartnerCustomerFieldValues;
            this.houseBanks = this.dialogRef.data.routeResolvedData.houseBanks;
            this.planningGroups = this.dialogRef.data.routeResolvedData.planningGroups;
            this.sortKeys = this.dialogRef.data.routeResolvedData.sortKeys;
            this.dunningProcedures = this.dialogRef.data.routeResolvedData.dunningProcedures;
            this.paymentTerms = this.dialogRef.data.routeResolvedData.paymentTerms;
            this.paymentMethods = this.dialogRef.data.routeResolvedData.paymentMethods;
            this.formFieldsConfig = this.dialogRef.data.routeResolvedData.formFieldsConfig;
        }
        
        // Get title
        this.title = this.getTitle();
    }

    /**
     * Get title
     */
    getTitle(): string {
        return this.operation === 'create' ? 'Create Customer Details' : this.operation === 'update' ? 'Update Customer Details' : 'View Customer Details';
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // If the operation is view, return
        if (this.operation === 'view') {
            return;
        }

        console.log('formFieldsDefaultValues in customer details update', this.formFieldsDefaultValues);
        let paymentMethods = [];
        console.log('this.selectedBusinessPartner', this.selectedBusinessPartner);
        if (this.selectedBusinessPartner?.paymentMethod) {
            paymentMethods = this.selectedBusinessPartner?.paymentMethod.split('');
        }
        console.log('paymentMethods', paymentMethods);
        paymentMethods.forEach((pmId: any) => {
            this.selectedPaymentMethods.push(this.paymentMethods.find((paymentMethod: any) => paymentMethod.id === pmId));
        });
        console.log('selectedPaymentMethods', this.selectedPaymentMethods);

        // Initialize partner details form
        this.customerDetailsForm = new FormGroup({
            houseBank: new FormControl(
                this.selectedBusinessPartner?.houseBank ?? this.formFieldsDefaultValues?.houseBank ?? null
            ),
            planningGroup: new FormControl(
                this.selectedBusinessPartner?.planningGroup ?? this.formFieldsDefaultValues?.planningGroup ?? null
            ),
            reconAccount: new FormControl(
                this.selectedBusinessPartner?.reconAccount ?? this.formFieldsDefaultValues?.reconAccount ?? null
            ),
            sortKey: new FormControl(
                this.selectedBusinessPartner?.sortKey ?? this.formFieldsDefaultValues?.sortKey ?? null
            ),
            dunningProcedure: new FormControl(
                this.selectedBusinessPartner?.dunningProcedure ?? this.formFieldsDefaultValues?.dunningProcedure ?? null
            ),
            paymentTerms: new FormControl(
                this.selectedBusinessPartner?.paymentTerms ?? this.formFieldsDefaultValues?.paymentTerms ?? null
            ),
            // paymentMethod: new FormControl(
            //     this.selectedBusinessPartner?.paymentMethod ?? this.formFieldsDefaultValues?.paymentMethod ?? null
            // ),
            paymentMethod: new FormControl(null),

            checkDoubleInvoice: new FormControl(
                this.selectedBusinessPartner?.checkDoubleInvoice ?? this.formFieldsDefaultValues?.checkDoubleInvoice ?? null
            ),
        });

        // Invoke configureFormFields to set the form validators
        this.configureFormFields();
    }

    /**
     * Submit
     */
    submit() {
        if (this.customerDetailsForm.invalid) {
            this.customerDetailsForm.markAllAsTouched();
            return;
        }

        this.saveCustomerDetails();
    }

    /**
     * Save identification details
     */
    private saveCustomerDetails(): void {
        const handleError = (operation: string) => (error: any) => {
            this.messageService.showError(
                `${error.message}!! Error ${operation} customer details. Please try again. If the problem persists, please contact `
                + `the administrator.`
            );
        };

        var customerDetails: any = { ...this.customerDetailsForm.value };
        customerDetails.partnerId = this.selectedBusinessPartner.id;

        let paymentMethods = '';
        this.selectedPaymentMethods.forEach((paymentMethod: any) => {
            paymentMethods += paymentMethod.id;
        });
        customerDetails.paymentMethod = paymentMethods;

        if (this.operation === 'update') {
            this.businessPartnerService.updateBusinessPartnerCustomerDetails(customerDetails).subscribe({
                next: (result: any) => {
                    this.messageService.showSuccess('FI Customer/Vendor details updated successfully');
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
        const fieldList = this.formFieldsConfig.bupaRoleEntityFieldStatusList || [];
        console.log('fieldList', fieldList);
        fieldList
        .filter((fieldConfig: any) => fieldConfig.entity === 'Partner')
        .forEach((fieldConfig: any) => {
            const fieldKey = fieldConfig.fieldName ?? fieldConfig.field;
            if (fieldKey.trim()) {
                formFieldsConfig[fieldKey.trim()] = fieldConfig.fieldStatus;
            } 
            else {
                console.warn('Field config missing fieldName/field:', fieldConfig);
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
            houseBank: { field: 'houseBank', configKey: 'houseBank' },
            planningGroup: { field: 'planningGroup', configKey: 'planningGroup' },
            reconAccount: { field: 'reconAccount', configKey: 'reconAccount' },
            sortKey: { field: 'sortKey', configKey: 'sortKey' },
            dunningProcedure: { field: 'dunningProcedure', configKey: 'dunningProcedure' },
            paymentTerms: { field: 'paymentTerms', configKey: 'paymentTerms' },
            // paymentMethod: { field: 'paymentMethod', configKey: 'paymentMethod' },
            checkDoubleInvoice: { field: 'checkDoubleInvoice', configKey: 'checkDoubleInvoice' },
        };

        Object.values(fieldConfigMap).forEach(({ field, configKey }) => {
            const control = this.customerDetailsForm.get(field);
            if (!control) return;
            
            // Only one validator for all these fields: Validators.required. If there are different validators, expand this logic.
            if (this.filteredFormFieldsConfig[configKey] === 2) {
                console.log('configKey', configKey, this.filteredFormFieldsConfig[configKey]);
                control.setValidators([Validators.required]);
            } else {
                control.removeValidators([Validators.required]);
            }
            control.updateValueAndValidity();
        });
    }

    /**
     * On payment method selection change
     */
    onPaymentMethodSelectionChange(event: any) {
        console.log('onPaymentMethodSelectionChange', event);
        this.selectedPaymentMethods = event.selectedItems;
    }
}
