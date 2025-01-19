import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import {ActivatedRoute} from '@angular/router';
import {MatDialog, MatPaginator, MatSnackBar} from "@angular/material";
import { ReferenceInterestValueService } from './referenceInterestValue.service';
import { ReferenceInterestValueUpdateComponent } from './referenceInterestValueUpdate/referenceInterestValueUpdate.component';
import { AppService } from 'app/app.service';
import { ConfirmationDialogComponent } from '../appraisal/confirmationDialog/confirmationDialog.component';
        
@Component({
    selector: 'fuse-reference-interest-value',
    templateUrl: './referenceInterestValue.component.html',
    styleUrls: ['./referenceInterestValue.component.scss'],
    animations: fuseAnimations
})
export class ReferenceInterestValueComponent implements OnInit, OnDestroy {

    @ViewChild(MatPaginator ) paginator: MatPaginator;

    displayedColumns: string[] = ['referenceInterestRateTypeCode', 'referenceInterestRateTypeDescription', 'validFromDate', 'interestRate', 
        'workFlowStatusDescription'
    ];
    dataSource: any[];
    expandPanel = true;
    referenceInterestRateSearchForm: FormGroup;
    referenceInterestRateTypes: any[];
    selectedRow: any;

    /**
     * Constructor
     */
    constructor(_route: ActivatedRoute, 
                _formBuilder: FormBuilder,
                private _dialog: MatDialog,
                public referenceInterestValueService: ReferenceInterestValueService,
                private _appService: AppService,
                private _matSnackBar: MatSnackBar) 
    {
                
        this.referenceInterestRateSearchForm = _formBuilder.group({
            referenceRateType: ['']
        });

        console.log('_route.snapshot.data.routeResolvedData', _route.snapshot.data.routeResolvedData);
        this.referenceInterestRateTypes = _route.snapshot.data.routeResolvedData;
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * searchReferenceInterestValues()
     */
    searchReferenceInterestValues(): void {
        if (this.referenceInterestRateSearchForm.value.referenceRateType !== '') {
            this.referenceInterestValueService.getReferenceInterestRateValues(this.referenceInterestRateSearchForm.value.referenceRateType).
                subscribe((response: any) => {
                    this.dataSource = response;
                });
        }
        this.selectedRow = undefined;
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedRow = row;
    }

    /**
     * sendForApproval()
     */
    sendForApproval(): void {
        if (this.selectedRow.modificationStatus !== 1) {
            this._matSnackBar.open('Reference Interest Value is already sent for approval. Only modified values can be sent for approval.', 
                'OK', { duration: 7000 });
            return;
        }
        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        let email = this._appService.currentUser.email;
        this._matSnackBar.open('Please wait while attempting to send monitoring for approval.', 'OK', { duration: 25000 });
        this.referenceInterestValueService.sendReferenceInterestValueForApproval(this.selectedRow.id, name, email).subscribe(
            response => {
                this._matSnackBar.dismiss();
                this.searchReferenceInterestValues();
                this._matSnackBar.open('Reference Interest Value is sent for approval.', 'OK', { duration: 7000 });
            },
            error => {
                this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator', 
                    'OK', { duration: 7000 });
            });
    }

    /**
     * addReferenceInterestRateValue()
     */
    addReferenceInterestRateValue(): void {
        // Check if a reference rate type is selected.
        if (this.referenceInterestRateSearchForm.value.referenceRateType === '') {
            this._matSnackBar.open('Please select a reference rate type to add a reference interest value.', 'OK', { duration: 7000 });
            return;
        }
        // Open the dialog.
        const selectedReferenceInterestType = 
            this.referenceInterestRateTypes.find(rate => rate.id === this.referenceInterestRateSearchForm.value.referenceRateType);
        const dialogRef = this._dialog.open(ReferenceInterestValueUpdateComponent, {
            panelClass: 'fuse-reference-interest-value-update-dialog',
            width: '800px',
            data: {
                operation: 'add',
                selectedReferenceInterestType: selectedReferenceInterestType,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            this.selectedRow = undefined;
            if (result.refresh) {
                this.searchReferenceInterestValues();
            }
        });        
    }

    /**
     * updateReferenceInterestRateValue()
     */
    updateReferenceInterestRateValue(): void {
        // Check if a reference interest value is selected.
        if (this.selectedRow === null) {
            this._matSnackBar.open('Please select a reference interest value to update.', 'OK', { duration: 7000 });
            return;
        }
        // Open the dialog.
        const selectedReferenceInterestType = 
            this.referenceInterestRateTypes.find(rate => rate.id === this.referenceInterestRateSearchForm.value.referenceRateType);
        const dialogRef = this._dialog.open(ReferenceInterestValueUpdateComponent, {
            panelClass: 'fuse-reference-interest-value-update-dialog',
            width: '800px',
            data: {
                operation: 'update',
                selectedReferenceInterestType: selectedReferenceInterestType,
                selectedReferenceInterestValue: this.selectedRow,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            this.selectedRow = undefined;
            if (result.refresh) {
                this.searchReferenceInterestValues();
            }
        });
    }

    /**
     * deleteReferenceInterestRateValue()
     */
    deleteReferenceInterestRateValue(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent, {
            data: {
                message: 'Are you sure? This will impact cash flows of existing loans in the system.'
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result.response) {
                this.referenceInterestValueService.deleteReferenceInterestValue(this.selectedRow.id).subscribe(() => {
                    this.searchReferenceInterestValues();
                });
            }
        });
    }
}
