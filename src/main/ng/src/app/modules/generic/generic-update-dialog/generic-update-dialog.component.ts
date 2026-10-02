import { Component, Injector, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { 
    ComboboxComponent, 
    DatePickerComponent, 
    DialogCloseButtonComponent, 
    DialogModule, 
    DialogRef, 
    DialogService,
    FileUploaderModule, 
    FormModule, 
    LayoutGridModule, 
    SelectModule,
    IconModule,
    FdDate,
    DatetimeAdapter
} from '@fundamental-ngx/core';
import { MessageService } from '../../../message.service';
import { entityServiceMap, entityUpdateDialogConfigs } from '../config';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Observable, forkJoin, map, of, switchMap, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';
import { CustomDialogComponent } from '../../../custom-dialog.component';

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
        private injector: Injector,
        private datetimeAdapter: DatetimeAdapter<FdDate>,
        private dialogService: DialogService
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
        if (!entityUpdateDialogConfigs[this.entity]) {
            throw new Error(`No configuration found for entity: ${this.entity}`);
        }
        
        this.config = entityUpdateDialogConfigs[this.entity];
        
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
        this.setupMinDateDependencies();
        this.setupFieldVisibility();
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
                    const value = field.type === 'date'
                        ? this.toFdDate(this.selectedObject[field.name])
                        : this.selectedObject[field.name] || null;
                    const disabled = !!field.readOnlyOnUpdate && this.dialogRef.data.operation === 'Update';
                    formControls[field.name] = [{ value, disabled }, validators];
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
        if (field.type === 'date' && this.isMaxCurrentDate(field)) validators.push(this.noFutureDateValidator);
        if (field.type === 'date' && field.minValue) validators.push(this.minDateValidator(field));
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
                if (field.defaultValue) {
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
        console.log('checking if form is valid');
        if (!this.validateForm()) return;

        if (!this.config.submitConfirmationMessage) {
            this.uploadFilesAndSave();
            return;
        }
        const confirmationDialogRef = this.dialogService.open(CustomDialogComponent, {
            data: {
                title: 'Confirm ' + (this.isCreateMode() ? 'Create' : 'Update'),
                description: this.config.submitConfirmationMessage
            },
            width: '30rem'
        });
        confirmationDialogRef.afterClosed.subscribe({
            next: (result: any) => {
                if (result?.continue) {
                    this.uploadFilesAndSave();
                }
            },
            error: () => {}
        });
    }

    /**
     * Upload the selected files, then save the entity details
     */
    private uploadFilesAndSave(): void {
        forkJoin({
            fileReference: this.uploadFile('file'),
            fileReference1: this.uploadFile('file1'),
            fileReference2: this.uploadFile('file2')
        }).subscribe({
            next: ({ fileReference, fileReference1, fileReference2 }) => {
                this.saveEntityDetails(fileReference, fileReference1, fileReference2);
            },
            error: () => {
                this.messageService.showError('Unable to upload the file. Please try again after sometime or contact your system administrator');
            }
        });
    }

    /**
     * Validate form before submission
     */
    private validateForm(): boolean {
        console.log(this.genericForm.invalid);

        if (this.genericForm.invalid) {
            this.genericForm.markAllAsTouched();
            // this.logInvalidControls();
            return false;
        }
        return true;
    }

    // private logInvalidControls(): void {
    //     const invalidControls = Object.entries(this.genericForm.controls)
    //         .filter(([, control]) => control.invalid)
    //         .map(([name, control]) => ({ name, errors: control.errors, value: control.value }));
    //     console.table(invalidControls);
    //     if (this.genericForm.errors) console.log('Form-level errors:', this.genericForm.errors);
    // }

    /**
     * Upload the file selected in the given control and emit its fileReference, or '' if no file is selected
     */
    private uploadFile(controlName: string): Observable<string> {
        const fileValue = this.genericForm.get(controlName)?.value;
        if (!fileValue?.[0]) return of('');
        return this.service$.uploadVaultDocument(this.createFileFormData(fileValue[0])).pipe(
            map((response: any) => response.fileReference)
        );
    }

    /**
     * Create form data for file upload
     */
    private createFileFormData(file: File): FormData {
        const formData = new FormData();
        formData.append('file', file, file.name);
        return formData;
    }

    /**
     * Save entity details
     */
    private saveEntityDetails(fileReference: string, fileReference1: string, fileReference2: string): void {
        const formData = this.prepareFormData(fileReference, fileReference1, fileReference2);
        const operation = this.dialogRef.data.operation;
        const isCreate = operation === 'Create';
        
        const args = this.buildServiceArgs(formData, fileReference, isCreate);
        const fn = isCreate ? this.config.createFunction : this.config.updateFunction;
        
        console.log('calling service function', isCreate);
        this.callServiceFunction(fn, args, isCreate);
    }

    /**
     * Prepare form data for submission
     */
    private prepareFormData(fileReference: string, fileReference1: string, fileReference2: string): any {
        let formData = { ...this.genericForm.value };
        if (fileReference) {
            formData = { ...formData, fileReference };
        }
        if (fileReference1) {
            formData = { ...formData, fileReference1 };
        }
        if (fileReference2) {
            formData = { ...formData, fileReference2 };
        }
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
                this.messageService.showError(error?.error?.message || errorMsg);
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

    /**
     * Disable dates in the calendar that are after today (maxValue 'currentDate')
     * or before the date referenced by minValue in routeResolvedData
     */
    disableFunction = (field: any, fdDate: FdDate): boolean => {
        if (this.isMaxCurrentDate(field) && this.datetimeAdapter.compareDate(fdDate, FdDate.getToday()) > 0) {
            return true;
        }
        return this.isBeforeMinDate(field, fdDate);
    };

    /**
     * Whether the field is capped at the current date
     */
    private isMaxCurrentDate(field: any): boolean {
        return field.maxValue === 'currentDate';
    }

    /**
     * Minimum allowed date for the field. field.minValue is either the name of another field in this
     * form (e.g. 'meetingDate') or a dotted path into routeResolvedData (e.g. 'enquiryCompletion.date').
     */
    private getMinDate(field: any, form: AbstractControl | null = this.genericForm): Date | null {
        if (!field.minValue) return null;
        const key = String(field.minValue);
        const sourceControl = form?.get(key);
        const value = sourceControl
            ? sourceControl.value
            : key.split('.').reduce((obj: any, k: string) => obj?.[k], this.dialogRef.data.routeResolvedData);
        const minDate = this.toDate(value);
        minDate?.setHours(0, 0, 0, 0);
        return minDate;
    }

    /**
     * Whether a date value falls before the field's minimum date
     */
    private isBeforeMinDate(field: any, value: any, form: AbstractControl | null = this.genericForm): boolean {
        const minDate = this.getMinDate(field, form);
        const date = this.toDate(value);
        if (!minDate || !date) return false;
        date.setHours(0, 0, 0, 0);
        return date.getTime() < minDate.getTime();
    }

    /**
     * Whether a field should be shown. visibleWhen is either a field name (shown when that field has a value)
     * or { field, value } (shown when that field equals the value, or one of the values if value is an array).
     */
    isFieldVisible(field: any): boolean {
        if (!field.visibleWhen) return true;
        const sourceName = this.getVisibleWhenSource(field);
        const value = this.isViewMode()
            ? this.selectedObject[sourceName]
            : this.genericForm?.get(sourceName)?.value;
        if (typeof field.visibleWhen === 'object') {
            const expected = field.visibleWhen.value;
            return Array.isArray(expected) ? expected.includes(value) : value === expected;
        }
        return value != null && value !== '';
    }

    /**
     * Name of the field that controls the visibility of a field with visibleWhen
     */
    private getVisibleWhenSource(field: any): string {
        return typeof field.visibleWhen === 'object' ? field.visibleWhen.field : field.visibleWhen;
    }

    /**
     * Enable fields with visibleWhen only while they are visible. Hidden fields are cleared and disabled
     * so that their validators (e.g. required) do not block submission.
     */
    private setupFieldVisibility(): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (!field.visibleWhen) return;
                const sourceControl = this.genericForm.get(this.getVisibleWhenSource(field));
                const control = this.genericForm.get(field.name);
                if (!sourceControl || !control) return;
                const applyVisibility = () => {
                    if (this.isFieldVisible(field)) {
                        control.enable({ emitEvent: false });
                    } else {
                        control.reset(null, { emitEvent: false });
                        control.disable({ emitEvent: false });
                    }
                };
                sourceControl.valueChanges.subscribe(applyVisibility);
                applyVisibility();
            });
        });
    }

    /**
     * Re-validate date fields whose minValue points at another field whenever that field changes
     */
    private setupMinDateDependencies(): void {
        this.config.rows.forEach((row: any) => {
            row.fields?.forEach((field: any) => {
                if (field.type !== 'date' || !field.minValue) return;
                const sourceControl = this.genericForm.get(String(field.minValue));
                const dependentControl = this.genericForm.get(field.name);
                if (!sourceControl || !dependentControl) return;
                sourceControl.valueChanges.subscribe(() => {
                    dependentControl.updateValueAndValidity();
                });
                dependentControl.updateValueAndValidity();
            });
        });
    }

    /**
     * Reject future dates typed into the date picker input
     */
    private noFutureDateValidator = (control: AbstractControl): ValidationErrors | null =>
        control.value && this.isFutureDate(control.value) ? { futureDate: true } : null;

    /**
     * Reject dates typed into the date picker input that are before the field's minimum date
     */
    private minDateValidator = (field: any) => (control: AbstractControl): ValidationErrors | null =>
        control.value && this.isBeforeMinDate(field, control.value, control.parent)
            ? { minDate: { min: this.getMinDate(field, control.parent) } }
            : null;

    /**
     * Check whether a date value (FdDate, Date or string) falls after today
     */
    private isFutureDate(value: any): boolean {
        const date = this.toDate(value);
        if (!date) return false;
        const endOfToday = new Date();
        endOfToday.setHours(23, 59, 59, 999);
        return date.getTime() > endOfToday.getTime();
    }

    /**
     * Convert an FdDate, Date or date string to a Date, or null if it is empty or invalid
     */
    private toDate(value: any): Date | null {
        if (value == null || value === '') return null;
        const date = value instanceof FdDate ? value.toDate() : new Date(value);
        return isNaN(date.getTime()) ? null : date;
    }

    /**
     * Convert a saved date ('yyyy-MM-dd' string, ISO string or Date) to the FdDate the date picker expects.
     * fd-date-picker flags any non-FdDate value with a dateValidation error.
     */
    private toFdDate(value: any): FdDate | null {
        if (value == null || value === '') return null;
        if (value instanceof FdDate) return value;
        const dateOnly = typeof value === 'string' ? /^(\d{4})-(\d{2})-(\d{2})$/.exec(value) : null;
        if (dateOnly) return new FdDate(+dateOnly[1], +dateOnly[2], +dateOnly[3]);
        const date = new Date(value);
        return isNaN(date.getTime()) ? null : new FdDate(date.getFullYear(), date.getMonth() + 1, date.getDate());
    }
}