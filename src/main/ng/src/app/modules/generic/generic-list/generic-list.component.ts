import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Injector, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ButtonComponent, DialogService, IconComponent, LayoutGridModule, TableModule, TitleComponent, ToolbarComponent } from '@fundamental-ngx/core';
import { Subject, takeUntil } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { MessageService } from '../../../message.service';
import { entityServiceMap } from '../service-map.config';
import { GenericUpdateDialogComponent } from '../generic-update-dialog/generic-update-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { entityComponentConfigs } from '../generic-list-component-map.config';
import { environment } from '../../../../environments/environment';

@Component({
    selector: 'app-generic-list',
    imports: [
        TableModule,
        LayoutGridModule,
        ButtonComponent,
        IconComponent,
        TitleComponent,
        ToolbarComponent
    ],
    providers: [
        DatePipe
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './generic-list.component.html',
})
export class GenericListComponent implements OnInit, OnDestroy {
    
    config: any;

    destroy$ = new Subject<void>();

    data: any[] = [];
    formattedData: any[] = []; // Store preformatted data
    selectedObject: any;
    selectedObjectId: SelectionModel<string> = new SelectionModel<string>();
    service$: any;
    disableCreateButton: boolean = false;

    private _searchString1: string = '';
    private _searchString2: string = '';
    private _entity: string = '';

    apiUrl: string = environment.primaryApiHost;

    // Search String 1
    @Input()
    set searchString1(value: string) {
        // if (value === undefined || value === null || value === '') {
        //     throw new Error('searchString1 input is mandatory and was not provided.');
        // }
        this._searchString1 = value;
    }
    get searchString1(): string {
        return this._searchString1;
    }
    
    // Search String 2
    @Input()
    set searchString2(value: string) {
        this._searchString2 = value;
    }
    get searchString2(): string {
        return this._searchString2;
    }

    // Entity
    @Input()
    set entity(value: string) {
        if (value === undefined || value === null || value === '') {
            throw new Error('entity input is mandatory and was not provided.');
        }
        this._entity = value;
    }
    get entity(): string {
        return this._entity;
    }


    @Output() onCreateClick: EventEmitter<void> = new EventEmitter<void>();
    @Output() onUpdateClick: EventEmitter<any> = new EventEmitter<string>();
    @Output() onViewClick: EventEmitter<any> = new EventEmitter<any>();
    @Output() onCreateSuccess: EventEmitter<any> = new EventEmitter<any>();
    @Output() onUpdateSuccess: EventEmitter<any> = new EventEmitter<any>();

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        private dialog: DialogService, 
        private datePipe: DatePipe,
        private injector: Injector,
        private messageService: MessageService,
        private cdr: ChangeDetectorRef, // Inject ChangeDetectorRef
        private router: Router
    ) {}

    /**
     * Initialize the component
     */
    ngOnInit(): void {

        // Throw an error if the searchString1 input is not provided.
        // if (!this.searchString1) {
        //     throw new Error('searchString1 input is mandatory and was not provided.');
        // }        

        // Throw an error if the entity input is not provided.
        if (!this.entity) {
            throw new Error('entity input is mandatory and was not provided.');
        }

        // Inject the service based on the entity. The entity and service map are defined in entityServiceMap.
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

        // Initialize config. The entity and config map are defined in entityConfigs in generic-list-component-map.config.ts.
        this.config = entityComponentConfigs[this.entity];
        if (!this.config) {
            throw new Error(`No configuration found for entity: ${this.entity}`);
        }

        // Fetch data from the service
        this.fetchData();
    }

    /**
     * Fetch data from the service and format the data
     */
    public fetchData() {
        console.log('fetchData is called for entity', this.entity);
        // If the searchString1 is not provided, set the data to an empty array and return
        if (!this.searchString1) {
            this.data = [];
            this.cdr.markForCheck();
            return;
        }
        // Fetch data to display in the table from the service
        this.config.fetchFunction.call(this.service$, this.searchString1).subscribe({
            next: (response: any) => {
                this.data = (response || []).map((item: any) => {
                    const formattedItem: any = { ...item };
                    this.config.displayedColumns.forEach((col: any) => {
                        formattedItem[`formatted_${col.name}`] = this.getFormattedValue(item, col.name);
                        // console.log('formattedItem[`formatted_${col.name}`]', formattedItem[`formatted_${col.name}`]);
                    });
                    return formattedItem;
                });
                // console.log('formattedData', this.data);
                // Disable the create button if the disableCreateButtonAfterCreate flag is true and the response is not empty
                if (this.config.disableCreateButtonAfterCreate && response && response.length > 0) {
                    this.disableCreateButton = true;
                }
                this.cdr.markForCheck();
            },
            error: (error: any) => {
                this.messageService.showError(error.message);
                this.cdr.markForCheck();
            },
        });
    }

    /**
     * Return the formatted value for a column
     */
    getFormattedValue(item: any, col: any): string {
        // Handle file reference column
        if (col === 'fileReference') {
            return item[col] ? item[col].toString() : '';
        }

        // Find the field configuration for the column
        item = Object.assign({}, item);
        const field = this.config.displayedColumns.find((field: any) => field.name === col);

        if (!field) {
            console.warn(`No field config found for column: ${col}`);
            return item[col] ? item[col].toString() : '';
        }

        if (field.type === 'text') {
            // console.log('returning text or number for', col, item[col]);
            return item[col] ? item[col].toString() : '';
        }
        else if (field.type === 'number') {
            // console.log('transforming number for', col, item[col]);
            return (item[col] !== null && item[col] !== undefined && item[col] !== '') 
                ? Number(item[col]).toFixed(2) 
                : '';
        }
        else if (field.type === 'date') {
            // console.log('transforming date for', col, item[col]);
            const dateValue = item[col] ? this.datePipe.transform(item[col], 'dd/MM/yyyy') : '';
            // console.log(`Formatting date for ${col}:`, { input: item[col], output: dateValue });
            return dateValue || '';
        }
        else if (field.type === 'select') {
            // console.log('transforming select for', col, item[col]);
            return item[col] ? item[col].toString() : '';
            // if (field.viewOperationKey && item[field.viewOperationKey]) {
            //     return item[field.viewOperationKey] ? item[field.viewOperationKey].toString() : '';
            // }
            // else {
            //     return item[col] ? item[col].toString() : '';
            // }
        }
        // else if (field.type === 'combobox' || field.type === 'select' && !field.dependsOn) {
        //     const resolvedData = this.route.snapshot.data['routeResolvedData']?.[field.name] || [];
        //     const matchingRow = resolvedData.find((row: any) => row[field.valueKey] === item[col]);
        //     return matchingRow ? matchingRow[field.displayKey] || '' : '';
        // }
        // else if (field.type === 'combobox' || field.type === 'select' && field.dependsOn) {
        //     // Implement the dependent select data
        // }
        else {
            // console.log('in getformattedvalue last else part', item[col]);
            return item[col] ? item[col].toString() : '';
        }
    }

    /**
     * Open the create dialog
     */
    openCreateDialog() {
        if (this.config.onlyEmitCreateEvent) {
            this.onCreateClick.emit();
            return;
        }

        // TODO: remove this after implementing events for create and update
        // If the create dialog is opened from the process-proposal component, redirect to the create-project-proposal route
        if (this.entity === 'processEnquiryProjectProposal') {
            this.router.navigate(['/process-enquiry', this.searchString1, 'loanApplication', this.searchString2, 'create-project-proposal']);
            return;
        }

        // Pass any route resolved data to the create dialog
        const routeResolvedData: any = {};
        console.log('this.config.routeResolvedData is', this.config.routeResolvedData);
        if (this.config.routeResolvedData != null) {
            this.config.routeResolvedData.forEach((key: string) => {
                // console.log('key is', key);
                // console.log('this.route.snapshot.data is', this.route.snapshot.data);
                if (this.route.snapshot.data['routeResolvedData']) {
                    routeResolvedData[key] = this.route.snapshot.data['routeResolvedData'][key];
                }
                else {
                    routeResolvedData[key] = undefined;
                }
                // console.log('routeResolvedData of key is', routeResolvedData[key]);
            });
        }
        // Open the create dialog and subscribe to the afterClosed event
        const dialogRef = this.dialog.open(GenericUpdateDialogComponent, {
            data: {
                entity: this.entity,
                operation: 'Create',
                searchString1: this.searchString1,
                searchString2: this.searchString2,
                ...(routeResolvedData != null ? { routeResolvedData: routeResolvedData } : {})
            },
            width: this.config.updateDialogWidth
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                // Update searchString1 if it is null, undefined or empty string
                if (!this.searchString1) {
                    this.searchString1 = this.service$.selectedEntity$.value.id;
                }
                // Fetch data to update the table
                if (result === 'Updated' || result === 'Created') {
                    this.config.emitOnCreateSuccess && this.onCreateSuccess.emit();
                    this.fetchData();
                }
            }
        });
    }

    /**
     * Open the update dialog
     */
    openUpdateDialog() {
        // If the onlyEmitUpdateEvent flag is true, emit the onUpdateClick event
        if (this.config.onlyEmitUpdateEvent) {
            this.onUpdateClick.emit(this.selectedObject);
            return;
        }

        // Pass any route resolved data to the update dialog
        const routeResolvedData: any = {};
        if (this.config.routeResolvedData != null) {
            this.config.routeResolvedData.forEach((key: string) => {
                routeResolvedData[key] = this.route.snapshot.data['routeResolvedData'][key];
            });
        }

        // Create a shallow copy of selectedObject and remove properties starting with 'formatted_'
        const selectedObject = Object.keys(this.selectedObject || {}).reduce((acc: any, key: string) => {
            if (!key.startsWith('formatted_')) {
                acc[key] = this.selectedObject[key];
            }
            return acc;
        }, {});

        // Open the update dialog and subscribe to the afterClosed event
        const dialogRef = this.dialog.open(GenericUpdateDialogComponent, {
            data: {
                entity: this.entity,
                operation: 'Update',
                searchString1: this.searchString1,
                selectedObject: this.selectedObject,
                ...(routeResolvedData != null ? { routeResolvedData: routeResolvedData } : {})
            },
            width: this.config.updateDialogWidth
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated' || result === 'Created') {
                    this.selectedObject = null;
                    this.selectedObjectId.clear();
                    this.config.emitOnUpdateSuccess && this.onUpdateSuccess.emit();
                    this.fetchData();
                }
            }
        }); 
    }

    /**
     * Open the view dialog
     */
    openViewDialog() {
        if (this.config.onlyEmitViewEvent) {
            this.onViewClick.emit(this.selectedObject);
            return;
        }
        this.dialog.open(GenericUpdateDialogComponent, {
            data: {
                entity: this.entity,
                operation: 'View',
                selectedObject: this.selectedObject,
                // ...(routeResolvedData != null ? { routeResolvedData: routeResolvedData } : {})
            },
            width: this.config.viewDialogWidth
        });
    }

    /**
     * Destroy the component
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}