import { Component, Injector, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { 
    ComboboxComponent, 
    DatePickerComponent, 
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    FileUploaderModule, 
    FormModule, 
    LayoutGridModule, 
    SelectModule,
    IconModule
} from '@fundamental-ngx/core';
import { MessageService } from '../../../message.service';
import { entityServiceMap } from '../service-map.config';
import { entityComponentConfigs } from '../generic-update-dialog-component-map.config';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Observable, switchMap, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';

@Component({
    selector: 'app-generic-update-dialog',
    imports: [
        CommonModule,
        FormsModule,
        DatePipe,
        LayoutGridModule,
        DatePickerComponent,
        ComboboxComponent,
        DialogModule,
        DialogCloseButtonComponent,
        FormModule,
        ReactiveFormsModule,
        SelectModule,
        FileUploaderModule,
        IconModule,
    ],
    styleUrls: ['./generic-update-dialog.component.scss'],
    templateUrl: './generic-update-dialog.component.html',
})
export class GenericUpdateDialogComponent implements OnInit {
    
    config: any;
    service$: any;
    private entity: string = '';
    genericForm!: FormGroup;
    selectedObject: any = {};
    title: string = '';

    @ViewChild('bankKeyTemplate', { static: true }) bankKeyTemplate!: TemplateRef<any>;
    
    apiUrl: string = environment.primaryApiHost;

    /**
     * Constructor
     */
    constructor(
        public dialogRef: DialogRef, 
        private formBuilder: FormBuilder,
        private messageService: MessageService,
        private injector: Injector
    ) {}

    /**
     * Initialize the component
     */
    ngOnInit(): void {
        this.validateAndSetEntity();
        this.injectEntityService();
        this.loadEntityConfig();
        this.initializeForm();
    }

    /**
     * Validate and set entity from dialog data
     */
    private validateAndSetEntity(): void {
        if (!this.dialogRef.data?.entity) {
            throw new Error('entity is mandatory and was not provided.');
        }
        this.entity = this.dialogRef.data.entity;
    }

    /**
     * Inject service based on entity mapping
     */
    private injectEntityService(): void {
        const serviceToken = entityServiceMap[this.entity];
        if (!serviceToken) {
            throw new Error(`No service found for entity: ${this.entity}`);
        }
        
        try {
            this.service$ = this.injector.get(serviceToken);
        } catch (error) {
            throw new Error(`Failed to inject service for entity ${this.entity}: ${error}`);
        }
    }

    /**
     * Load configuration for the entity
     */
    private loadEntityConfig(): void {
        if (!entityComponentConfigs[this.entity]) {
            throw new Error(`No configuration found for entity: ${this.entity}`);
        }
        
        this.config = entityComponentConfigs[this.entity];
        
        // Convert fieldsConfig to rows format if present
        if (this.config.fieldsConfig && !this.config.rows) {
            this.config.rows = this.convertFieldsConfigToRows(this.config.fieldsConfig);
            console.log('this.config.rows', this.config.rows);
        }
    }

    /**
     * Convert fieldsConfig to rows format
     */
    private convertFieldsConfigToRows(fieldsConfig: any[]): any[] {
        const rowsMap = new Map<number, any[]>();
        
        fieldsConfig.forEach((field) => {
            const rowNumber = field.row || 1;
            if (!rowsMap.has(rowNumber)) {
                rowsMap.set(rowNumber, []);
            }
            rowsMap.get(rowNumber)!.push(field);
        });
        
        return Array.from(rowsMap.entries())
            .sort(([a], [b]) => a - b)
            .map(([, fields]) => ({ fields }));
    }

    /**
     * Initialize the form
     */
    private initializeForm(): void {
        this.setTitleAndSelectedObject();
        
        if (this.isViewMode()) {
            return; // Do not initialize form for view mode
        }

        this.createFormControls();
        this.setupFieldDependencies();
        this.setDataForDropdowns();
        
        if (this.isCreateMode()) {
            this.setDefaultValues();
        }
    }

    /**
     * Set title and selected object based on operation
     */
    private setTitleAndSelectedObject(): void {
        const operation = this.dialogRef.data.operation;
        
        if (operation === 'Create') {
            this.title = this.config.createDialogTitle;
        } else {
            Object.assign(this.selectedObject, this.dialogRef.data.selectedObject);
            console.log('this.selectedObject', this.selectedObject);
            if (operation === 'Update') {
                this.title = this.config.updateDialogTitle;
            } else if (operation === 'View') {
                this.title = this.config.viewDialogTitle;
            }
        }
    }

    /**
     * Check if current operation is view mode
     */
    private isViewMode(): boolean {
        return this.dialogRef.data.operation === 'View';
    }

    /**
     * Check if current operation is create mode
     */
    private isCreateMode(): boolean {
        return this.dialogRef.data.operation === 'Create';
    }

    /**
     * Create form controls for all fields
     */
    private createFormControls(): void {
        const formControls: { [key: string]: any } = {};
        
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (field.name) {
                    const validators = this.buildValidators(field);
                    formControls[field.name] = [this.selectedObject[field.name] || null, validators];
                }
            });
        });
        
        this.genericForm = this.formBuilder.group(formControls);
    }

    /**
     * Build validators for a field
     */
    private buildValidators(field: any): any[] {
        const validators = [];
        if (field.required) validators.push(Validators.required);
        if (field.pattern) validators.push(Validators.pattern(field.pattern));
        return validators;
    }

    /**
     * Setup field dependencies and event handlers
     */
    private setupFieldDependencies(): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (field.name && field.type === 'select') {
                    if (field.dependsOn) {
                        this.setupDependentField(field);
                    } else {
                        this.setupIndependentSelectField(field);
                    }
                }
            });
        });
    }

    /**
     * Setup dependent select field
     */
    private setupDependentField(field: any): void {
        this.genericForm.get(field.dependsOn)?.valueChanges.pipe(
            tap((value: any) => console.log('value changed', value)),
            switchMap(value => this.fetchDropdownOptionsForDependentField(field, value))
        ).subscribe(options => {
            this.genericForm.get(field.name)?.setValue('');
            field.options = options;
        });
    }

    /**
     * Setup independent select field
     */
    private setupIndependentSelectField(field: any): void {
        this.genericForm.get(field.name)?.valueChanges.pipe().subscribe(value => 
            this.handleValueChangeForSelectField(field, value)
        );
    }

    /**
     * Fetch dropdown options for dependent field
     */
    private fetchDropdownOptionsForDependentField(field: any, value: any): Observable<any> {
        return new Observable(observer => {
            field.displayFunction.call(this.service$, value).subscribe((options: any) => {
                observer.next(options);
                observer.complete();
            });
        });
    }

    /**
     * Handle value change for select field
     */
    private handleValueChangeForSelectField(field: any, value: any): void {
        if (field.name === 'documentType' && value) {
            this.setFileFieldRequired();
        }
    }

    /**
     * Set file field as required
     */
    private setFileFieldRequired(): void {
        this.genericForm.get('file')?.setValidators([Validators.required]);
        this.genericForm.get('file')?.updateValueAndValidity();
    }

    /**
     * Set data for dropdowns and combobox fields
     */
    private setDataForDropdowns(): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (this.isSelectOrComboboxField(field)) {
                    this.setFieldOptions(field);
                }
            });
        });
    }

    /**
     * Check if field is select or combobox
     */
    private isSelectOrComboboxField(field: any): boolean {
        return field.type === 'combobox' || field.type === 'select';
    }

    /**
     * Set options for a field
     */
    private setFieldOptions(field: any): void {
        if (field.dependsOn) {
            this.setDependentFieldOptions(field);
        } else {
            this.setIndependentFieldOptions(field);
        }
    }

    /**
     * Set options for dependent field
     */
    private setDependentFieldOptions(field: any): void {
        const dependentValue = this.genericForm.get(field.dependsOn)?.value;
        this.fetchDropdownOptionsForDependentField(field, dependentValue).subscribe((options: any) => {
            field.options = options;
        });
    }

    /**
     * Set options for independent field
     */
    private setIndependentFieldOptions(field: any): void {
        if (field.name === 'bankKey') {
            this.setBankKeyFieldOptions(field);
        } else if (field.name === 'rejectionCategory') {
            this.setRejectionCategoryOptions(field);
        } else {
            field.options = this.dialogRef.data.routeResolvedData?.[field.name];
        }
    }

    /**
     * Set bank key field specific options
     */
    private setBankKeyFieldOptions(field: any): void {
        field.itemTemplate = this.bankKeyTemplate;
        field.options = this.dialogRef.data.routeResolvedData?.[field.name];
    }

    /**
     * Set rejection category options based on entity
     */
    private setRejectionCategoryOptions(field: any): void {
        let options = this.dialogRef.data.routeResolvedData?.[field.name];
        
        if (this.entity === 'processEnquiryRejectedByPFS') {
            options = options?.filter((option: any) => option.code !== '1');
        } else if (this.entity === 'processEnquiryRejectedByCustomer') {
            options = options?.filter((option: any) => option.code === '1');
        }
        
        field.options = options;
    }

    /**
     * Set default values for fields
     */
    private setDefaultValues(): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (this.isSelectOrComboboxField(field) && field.defaultValue) {
                    this.genericForm.get(field.name)?.setValue(field.defaultValue);
                }
            });
        });
    }

    /**
     * Handle combobox item clicked event
     */
    comboboxItemClickedEventHandler(event: any, source: string): void {
        if (!event || !source) return;
        
        if (source === 'bankKey') {
            this.handleBankKeySelection(event);
        }
    }

    /**
     * Handle bank key selection
     */
    private handleBankKeySelection(event: any): void {
        this.genericForm.get('bankName')?.setValue(event.item.bankName);
        this.genericForm.get('ifscCode')?.setValue(event.item.bankNumber);
    }

    /**
     * Handle file selection
     */
    handleFileSelection(event: any): void {
        this.genericForm.get('documentType')?.setValidators([Validators.required]);
        this.genericForm.get('documentType')?.updateValueAndValidity();
    }

    /**
     * Submit the form
     */
    submit(): void {
        if (!this.validateForm()) return;
        
        const fileValue = this.genericForm.get('file')?.value;
        if (fileValue) {
            this.uploadFileAndSave();
        } else {
            this.saveEntityDetails('');
        }
    }

    /**
     * Validate form before submission
     */
    private validateForm(): boolean {
        if (this.genericForm.invalid) {
            this.genericForm.markAllAsTouched();
            return false;
        }
        return true;
    }

    /**
     * Upload file and then save entity
     */
    private uploadFileAndSave(): void {
        const formData = this.createFileFormData();
        
        this.service$.uploadVaultDocument(formData).subscribe({
            next: (response: any) => {
                this.saveEntityDetails(response.fileReference);
            },
            error: (error: any) => {
                this.messageService.showError('Unable to upload the file. Please try again after sometime or contact your system administrator');
            }
        });
    }

    /**
     * Create form data for file upload
     */
    private createFileFormData(): FormData {
        const formData = new FormData();
        const fileValue = this.genericForm.get('file')?.value;
        formData.append('file', fileValue[0], fileValue[0].name);
        return formData;
    }

    /**
     * Save entity details
     */
    private saveEntityDetails(fileReference: string): void {
        const formData = this.prepareFormData(fileReference);
        const operation = this.dialogRef.data.operation;
        const isCreate = operation === 'Create';
        
        const args = this.buildServiceArgs(formData, fileReference, isCreate);
        const fn = isCreate ? this.config.createFunction : this.config.updateFunction;
        
        this.callServiceFunction(fn, args, isCreate);
    }

    /**
     * Prepare form data for submission
     */
    private prepareFormData(fileReference: string): any {
        const formData = { ...this.genericForm.value };
        
        this.convertDateFields(formData);
        this.addSearchStrings(formData);
        
        return formData;
    }

    /**
     * Convert date fields to UTC
     */
    private convertDateFields(formData: any): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (field.type === 'date' && formData[field.name]) {
                    const dt = new Date(formData[field.name]);
                    formData[field.name] = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
                }
            });
        });
    }

    /**
     * Add search strings to form data if needed
     */
    private addSearchStrings(formData: any): void {
        if (this.config.searchString1ForCreate && this.config.passSearchString1Via === 'Object') {
            formData[this.config.searchString1ForCreate] = this.dialogRef.data.searchString1;
        }
        if (this.config.searchString2ForCreate && this.config.passSearchString2Via === 'Object') {
            formData[this.config.searchString2ForCreate] = this.dialogRef.data.searchString2;
        }
    }

    /**
     * Build arguments for service function call
     */
    private buildServiceArgs(formData: any, fileReference: string, isCreate: boolean): any[] {
        if (isCreate) {
            return this.buildCreateArgs(formData);
        } else {
            return this.buildUpdateArgs(formData, fileReference);
        }
    }

    /**
     * Build arguments for create operation
     */
    private buildCreateArgs(formData: any): any[] {
        const args = [formData];
        
        if (this.config.searchString1ForCreate && this.config.passSearchString1Via === 'Query') {
            args.push(this.dialogRef.data.searchString1);
        }
        
        return args;
    }

    /**
     * Build arguments for update operation
     */
    private buildUpdateArgs(formData: any, fileReference: string): any[] {
        const updatedObject = { ...this.selectedObject, ...formData };
        
        if (fileReference) {
            updatedObject.fileReference = fileReference;
        }
        
        return [updatedObject];
    }

    /**
     * Call service function and handle response
     */
    private callServiceFunction(fn: any, args: any[], isCreate: boolean): void {
        const errorMsg = isCreate ? 'An error occurred while creating.' : 'An error occurred while updating.';
        
        fn.call(this.service$, ...args).subscribe({
            next: (response: any) => {
                this.handleSuccessResponse(response, isCreate);
            },
            error: (error: any) => {
                console.log('error', error);
                this.messageService.showError(error?.message || errorMsg);
            }
        });
    }

    /**
     * Handle successful service response
     */
    private handleSuccessResponse(response: any, isCreate: boolean): void {
        if (this.config.trackObjectAfterCreateAndUpdate) {
            this.service$.selectedEntity$.next(response[this.config.trackObjectAfterCreateAndUpdate]);
        }
        
        const successMessage = isCreate ? this.config.createSuccessMessage : this.config.updateSuccessMessage;
        this.messageService.showSuccess(successMessage);
        
        const closeMessage = isCreate ? 'Created' : 'Updated';
        this.dialogRef.close(closeMessage);
    }
}
