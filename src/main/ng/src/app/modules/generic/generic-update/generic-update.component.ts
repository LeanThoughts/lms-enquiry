import { Component, EventEmitter, Injector, Input, OnInit, Output } from '@angular/core';
import { 
    ComboboxComponent, 
    DatePickerComponent, 
    FileUploaderModule, 
    FormModule, 
    LayoutGridModule, 
    SelectModule,
    ButtonComponent,
    TitleComponent,
    FdDate,
    DatetimeAdapter,
    IconComponent
} from '@fundamental-ngx/core';
import { MessageService } from '../../../message.service';
import { entityServiceMap } from '../service-map.config';
import { entityComponentConfigs } from '../generic-update-component-map.config';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Observable, switchMap, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-generic-update',
    imports: [
        ButtonComponent,
        CommonModule,
        FormsModule,
        DatePipe,
        LayoutGridModule,
        DatePickerComponent,
        ComboboxComponent,
        FormModule,
        ReactiveFormsModule,
        SelectModule,
        FileUploaderModule,
        TitleComponent,
        IconComponent,
    ],
    styleUrls: ['./generic-update.component.scss'],
    templateUrl: './generic-update.component.html',
})
export class GenericUpdateComponent implements OnInit {
    
    config: any;
    // layoutGridColumns: number = 6;

    service$: any;

    // private entity: string = '';

    genericForm!: FormGroup;

    @Input() entity: string = '';
    @Input() loanEnquiryNumber: string = '';
    @Input() operation: string = '';
    @Input() searchString1: string = '';
    @Input() searchString2: string = '';
    @Input() selectedObject: any = {};

    @Output() onCreateSuccess: EventEmitter<any> = new EventEmitter<any>();
    @Output() onUpdateSuccess: EventEmitter<any> = new EventEmitter<any>();

    // @ViewChild('bankKeyTemplate', { static: true }) bankKeyTemplate!: TemplateRef<any>;
    
    today: Date = new Date();

    /**
     * Constructor
     */
    constructor(
        private formBuilder: FormBuilder,
        private messageService: MessageService,
        private injector: Injector,
        private activatedRoute: ActivatedRoute,
        private datetimeAdapter: DatetimeAdapter<FdDate>
    ) {}

    /**
     * Initialize the component
     */
    ngOnInit(): void {

        // Throw an error if the entity is not provided.
        if (!this.entity) {
            throw new Error('entity is mandatory and was not provided.');
        }

        // Inject the service based on the entity. The entity and service map are defined in entityServiceMap in service-map.config.ts.
        const serviceToken = entityServiceMap[this.entity];
        if (!serviceToken) {
            throw new Error(`No service found for entity: ${this.entity}`);
        }
        try {
            this.service$ = this.injector.get(serviceToken);
        } 
        catch (error) {
            throw new Error(`Failed to inject service for entity ${this.entity}: ${error}`);
        }

        // Initialize config. The entity and config map are defined in entityUpdateDialogConfigs in generic-update-dialog-component-map.config.ts.
        this.config = entityComponentConfigs[this.entity];
        // Convert fieldsConfig to rows format if present
        if (this.config.fieldsConfig && !this.config.rows) {
            this.config.rows = this.convertFieldsConfigToRows(this.config.fieldsConfig);
            console.log('this.config.rows', this.config.rows);
        }

        // Initialize layout grid columns
        // this.layoutGridColumns = 12/this.config.rows[0].columns.length;
        // console.log('layoutGridColumns', this.layoutGridColumns);

        // Initialize form only for create and update operations
        console.log('selectedObject in generic update', this.selectedObject);

        if (this.operation === 'Create' || this.operation === 'Update') {
            this.initializeForm();
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
    initializeForm() {
        // Create form controls for all fields
        const formControls: { [key: string]: any } = {};
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                // column.fields.forEach((field: any) => {
                    if (field.name) {
                        // Add validators to the field
                        const validators = [];
                        // if (field.required) validators.push(Validators.required);
                        if (field.pattern) validators.push(Validators.pattern(field.pattern));
                        formControls[field.name] = [this.selectedObject[field.name] || null, validators];
                    }
                // });
            });
        });
        this.genericForm = this.formBuilder.group(formControls);

        // Add required validator to the field if the dependent field is not empty
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                if (field.required && field.required === true) {
                    this.genericForm.get(field.name)?.setValidators([Validators.required]);
                }
                else if (field.required && field.required.dependsOn) {
                    this.genericForm.get(field.required.dependsOn)?.valueChanges.subscribe(value => {
                        if (value) {
                            this.genericForm.get(field.name)?.setValidators([Validators.required]);
                            this.genericForm.get(field.name)?.updateValueAndValidity();
                        }
                        else {
                            const validators = [];
                            if (field.pattern) validators.push(Validators.pattern(field.pattern));
                            this.genericForm.get(field.name)?.setValidators(validators);
                            this.genericForm.get(field.name)?.updateValueAndValidity();
                        }
                    });
                }
            });
        });

        // Check if there are any dependent select fields or file uploaders and initialize callback functions for them
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                // column.fields.forEach((field: any) => {
                    if (field.name) {
                        if (field.type === 'select') {
                            if (field.dependsOn) {
                                this.genericForm.get(field.dependsOn)?.valueChanges.pipe(
                                    tap((value: any) => console.log('value changed', value)),
                                        switchMap(value => this.fetchDropdownOptionsForDependentField(field, value))
                                    ).subscribe(options => {
                                        this.genericForm.get(field.name)?.setValue('');
                                        field.options = options;
                                    }
                                );
                            }
                            else {
                                console.log('hello');
                                this.genericForm.get(field.name)?.valueChanges.pipe().subscribe(value => 
                                    this.handleValueChangeForSelectField(field, value)
                                );
                            }
                        }
                    }
                // });
            });
        });

        // Handle dependent and independent dropdowns
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                const isSelectOrCombobox = field.type === 'combobox' || field.type === 'select';
                if (!isSelectOrCombobox) return;
                if (field.dependsOn) {
                    this.fetchDropdownOptionsForDependentField(field, this.genericForm.get(field.dependsOn)?.value).subscribe((options: any) => {
                        field.options = options;
                    });
                } else {
                    field.options = this.activatedRoute.snapshot.data['routeResolvedData'][field.name];
                }
            });
        });
        
        // Set default values for combobox and select fields, cannot do this before select fields are initialized with options data
        if (this.operation === 'Create') {
            this.setDefaultValues();
        }

        // Call updateValueAndValidity for all fields
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                this.genericForm.get(field.name)?.updateValueAndValidity();
            });
        });
    }

    /**
     * Fetch dropdown options for dependent field
     */
    fetchDropdownOptionsForDependentField(field: any, value: any) {
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
    handleValueChangeForSelectField(field: any, value: any) {
        if (field.name === 'documentType' && value) {
            this.genericForm.get('file')?.setValidators([Validators.required]);
            this.genericForm.get('file')?.updateValueAndValidity();
        }
    }

    /**
     * Set default values for combobox and select fields
     */
    setDefaultValues() {
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                // column.fields.forEach((field: any) => { 
                    if (field.type === 'combobox' || field.type === 'select') {
                        if (field.defaultValue) {
                            this.genericForm.get(field.name)?.setValue(field.defaultValue);
                        }
                    }
                    else if (field.type === 'text') {
                        // Custom logic for certain fields
                        // console.log('loanEnquiryNumber', this.loanEnquiryNumber);
                        // if (field.name === 'loanEnquiryNumber') {
                        //     this.genericForm.get(field.name)?.setValue(this.loanEnquiryNumber);
                        // }
                    }
                // }); 
            });
        });
    }

    /**
     * Display function for combobox
     */
    // comboboxDisplayFunction = (item: any): string => item?.bankName ?? '';

    /**
     * Item clicked event handler for combobox
     */
    comboboxItemClickedEventHandler(event: any, source: string) {
        if (event && source) {
            // if (source === 'bankKey') {
            //     this.genericForm.get('bankName')?.setValue(event.item.bankName);
            //     this.genericForm.get('ifscCode')?.setValue(event.item.bankNumber);
            // }
        }
    }

    /**
     * Handle file selection
     */
    handleFileSelection(event: any) {
        // Make document type field required if file is selected
        this.genericForm.get('documentType')?.setValidators([Validators.required]);
        this.genericForm.get('documentType')?.updateValueAndValidity();
    }

    /**
     * Submit the form
     */
    submit() {
        // Check if the form is valid
        if (this.genericForm.invalid) {
            this.genericForm.markAllAsTouched();
            return;
        }

        // Save the file
        let fileReference = '';
        if (this.genericForm.get('file')?.value) {
            console.log('Saving file');
            var formData = new FormData();
            formData.append('file', this.genericForm.get('file')?.value[0], this.genericForm.get('file')?.value[0].name);
            this.service$.uploadVaultDocument(formData).subscribe({
                next: (response: any) => {
                    fileReference = response.fileReference;
                    this.saveEntityDetails(fileReference);
                },
                error: (error: any) => {
                    this.messageService.showError('Unable to upload the file. Pls try again after sometime or contact your system administrator');
                }
            });
        }
        else {
            this.saveEntityDetails(fileReference);
        }
    }

    /**
     * Save the entity details
     */
    saveEntityDetails(fileReference: string) {
        var formData = this.genericForm.value;
        const operation = this.operation;
        const isCreate = operation === 'Create';

        // Check for date fields and convert to UTC
        this.config.rows.forEach((row: any) => {
            row.fields.forEach((field: any) => {
                // column.fields.forEach((field: any) => {
                    if (field.type === 'date' && formData[field.name]) {
                        const dt = new Date(formData[field.name]);
                        formData[field.name] = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
                    }
                // });
            });
        });

        // Determine the arguments to pass to the create or update function
        let args: any[] = [];
        if (isCreate) {
            // Create list of arguments to pass to the create function including the form data
            args.push(formData);
            // Pass search string 1 and 2 as additional arguments to the create function
            if (this.config.searchString1ForCreate && this.config.passSearchString1Via === 'Query') {
                args.push(this.searchString1);
            }
            else if (this.config.searchString1ForCreate && this.config.passSearchString1Via === 'Object') {
                formData[this.config.searchString1ForCreate] = this.searchString1;
            }
            if (this.config.searchString2ForCreate && this.config.passSearchString2Via === 'Query') {
                args.push(this.searchString2);
            }
            else if (this.config.searchString2ForCreate && this.config.passSearchString2Via === 'Object') {
                formData[this.config.searchString2ForCreate] = this.searchString2;
            }
        }
        else {
            // Update the selected object with the form data and file reference
            Object.assign(this.selectedObject, formData);
            // Pass additional arguments to the update function (if any)
            // if (this.config.searchString1ForUpdate && this.config.passSearchString1As === 'Object') {
            //     this.selectedObject[this.config.searchString1ForUpdate] = this.dialogRef.data.searchString1;
            // }
            if (fileReference) {
                this.selectedObject.fileReference = fileReference;
            }
            args.push(this.selectedObject);
        }

        // Call the create or update function
        const fn = isCreate ? this.config.createFunction : this.config.updateFunction;
        const errorMsg = isCreate ? 'An error occurred while creating.' : 'An error occurred while updating.';
        fn.call(this.service$, ...args).subscribe({
            next: (response: any) => {
                if (this.config.trackObjectAfterCreateAndUpdate) {
                    this.service$.selectedEntity$.next(response[this.config.trackObjectAfterCreateAndUpdate]);
                }
                this.messageService.showSuccess(isCreate ? this.config.createSuccessMessage : this.config.updateSuccessMessage);
                // Emit the success event and let the parent component handle any additional logic
                isCreate ? this.onCreateSuccess.emit(response) : this.onUpdateSuccess.emit(response);
            },
            error: (error: any) => {
                console.log('error', error);
                this.messageService.showError(error?.message || errorMsg);
            }
        });
    }

    /**
     * Disable function for date field
     */
    disableFunction = (field: any, fdDate: FdDate): boolean => {
        // console.log('field', field);
        if (!field.min && field.max === 'currentDate') {
            return this.datetimeAdapter.compareDate(fdDate, FdDate.getToday()) > 0;
        }
        return false;
    }
}
