import { Component, Inject, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatSort, MatTableDataSource } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { NPADetailUpdateDialogComponent } from '../npaDetailsUpdate/npaDetailsUpdate.component';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';

@Component({
    selector: 'fuse-npa-update-component',
    templateUrl: './npaUpdate.component.html',
    styleUrls: ['./npaUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class NPAUpdateComponent implements OnInit {

    assetClasses = LoanMonitoringConstants.assetClasses;
    restructuringTypes = LoanMonitoringConstants.restructuringTypes;
    smaCategories = LoanMonitoringConstants.smaCategories;

    selectedNPA: any = {};
    selectedNPADetail: any;

    npaUpdateForm: FormGroup;

    loanApplicationId = '';

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'lineItemNumber', 'npaAssetClass', 'assetClassificationChangeDate','provisionDate', 'absoluteValue'
    ];

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService, 
        private _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, private _matDialog: MatDialog) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedNPA.id = '';
        _loanMonitoringService.getNPA(this.loanApplicationId).subscribe(data => {

            this.selectedNPA = data;

            this.npaUpdateForm = this._formBuilder.group({
                assetClass: [this.selectedNPA.assetClass || ''],
                npaDeclarationDate: [this.selectedNPA.npaDeclarationDate || ''],
                totalLoanAsset: [this.selectedNPA.totalLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
                securedLoanAsset: [this.selectedNPA.securedLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
                unSecuredLoanAsset: [this.selectedNPA.unSecuredLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
                restructuringType: [this.selectedNPA.restructuringType || ''],
                smaCategory: [this.selectedNPA.smaCategory || ''],
                fraudDate: [this.selectedNPA.fraudDate || ''],
                impairmentReserve: [this.selectedNPA.impairmentReserve || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
                provisionAmount: [this.selectedNPA.provisionAmount || '', [Validators.pattern(MonitoringRegEx.genericAmount)]]
            });

            _loanMonitoringService.getNPADetails(this.selectedNPA.id).subscribe(data => {
                this.dataSource = new MatTableDataSource(data);
                this.dataSource.sort = this.sort;
            });
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.npaUpdateForm = this._formBuilder.group({
            assetClass: [this.selectedNPA.assetClass || ''],
            npaDeclarationDate: [this.selectedNPA.npaDeclarationDate || ''],
            totalLoanAsset: [this.selectedNPA.totalLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
            securedLoanAsset: [this.selectedNPA.securedLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
            unSecuredLoanAsset: [this.selectedNPA.unSecuredLoanAsset || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
            restructuringType: [this.selectedNPA.restructuringType || ''],
            smaCategory: [this.selectedNPA.smaCategory || ''],
            fraudDate: [this.selectedNPA.fraudDate || ''],
            impairmentReserve: [this.selectedNPA.impairmentReserve || '', [Validators.pattern(MonitoringRegEx.genericAmount)]],
            provisionAmount: [this.selectedNPA.provisionAmount || '', [Validators.pattern(MonitoringRegEx.genericAmount)]]
        });
    }

    /**
     * onSelect()
     */
    onSelect(selectedNPADetail: any): void {
        this.selectedNPADetail = selectedNPADetail;
    }

    /**
     * submit()
     */
    submit(): void {
        var npa = this.npaUpdateForm.value;
        var dt = new Date(npa.npaDeclarationDate);
        npa.npaDeclarationDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(npa.fraudDate);
        npa.fraudDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        if (this.selectedNPA !== null) {
            this.selectedNPA.assetClass = npa.assetClass;
            this.selectedNPA.npaDeclarationDate = npa.npaDeclarationDate;
            this.selectedNPA.totalLoanAsset = npa.totalLoanAsset;
            this.selectedNPA.securedLoanAsset = npa.securedLoanAsset;
            this.selectedNPA.unSecuredLoanAsset = npa.unSecuredLoanAsset;
            this.selectedNPA.restructuringType = npa.restructuringType;
            this.selectedNPA.smaCategory = npa.smaCategory;
            this.selectedNPA.fraudDate = npa.fraudDate;
            this.selectedNPA.impairmentReserve = npa.impairmentReserve;
            this.selectedNPA.provisionAmount = npa.provisionAmount;
            this._loanMonitoringService.updateNPA(this.selectedNPA).subscribe((data) => {
                this._matSnackBar.open('NPA details updated successfully.', 'OK', { duration: 7000 });
                this.selectedNPA = data;
            });            
        }
        else {
            this._loanMonitoringService.saveNPA(npa, this.loanApplicationId).subscribe((data) => {
                this._matSnackBar.open('NPA details added successfully.', 'OK', { duration: 7000 });
                this.selectedNPA = data;
            });
        }
    }

    /**
     * updateNPADetails()
     */
    updateNPADetails(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'npaId': this.selectedNPA.id,
            'selectedNPADetail': undefined
        };
        if (operation === 'updateNPADetails') {
            data.selectedNPADetail = this.selectedNPADetail;
        }
        const dialogRef = this._matDialog.open(NPADetailUpdateDialogComponent, {
            panelClass: 'fuse-npa-detail-update-dialog',
            width: '850px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getNPADetails(this.selectedNPA.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });    
            }
        });   
    }

    /**
     * getAssetClassDescription()
     */
    getAssetClassDescription(assetClass: string): string {
        const obj = this.assetClasses.filter(f => f.code === assetClass)[0];
        if (obj !== undefined)
            return obj.value;
        else
            return '';
    }

    /**
     * deleteNPADetails()
     */
    deleteNPADetails(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            this._loanMonitoringService.deleteNPADetails(this.selectedNPADetail.id).subscribe(() => {
                this.selectedNPADetail = undefined;
                this._loanMonitoringService.getNPADetails(this.selectedNPA.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            });
        });
    }
}
