import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-npa-detail-update-dialog',
    templateUrl: './npaDetailsUpdate.component.html',
    styleUrls: ['./npaDetailsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class NPADetailUpdateDialogComponent {

    dialogTitle = 'Add New NPA Details';

    selectedNPADetail: any;
    npaDetailUpdateForm: FormGroup;

    assetClasses = LoanMonitoringConstants.assetClasses;

    subscriptions = new Subscription();
    loanContractId = '';

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService, private _loanEnquiryService: LoanEnquiryService,
        public _dialogRef: MatDialogRef<NPADetailUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any, 
        private _matSnackBar: MatSnackBar) {

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.loanContractId = data.loanContractId;
        }));          
    
        // Fetch selected user details from the dialog's data attribute.
        this.selectedNPADetail = Object.assign({}, _dialogData.selectedNPADetail);

        if (this._dialogData.operation === 'updateNPADetails') {
            this.dialogTitle = 'Modify NPA Details';
        }

        this.npaDetailUpdateForm = _formBuilder.group({
            lineItemNumber: [this.selectedNPADetail.lineItemNumber],
            npaAssetClass: [ this.selectedNPADetail.npaAssetClass || '' ],
            assetClassificationChangeDate: [ this.selectedNPADetail.assetClassificationChangeDate || '' ],
            provisionDate: [ this.selectedNPADetail.provisionDate || ''],
            percentageSecured: [ this.selectedNPADetail.percentageSecured || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            percentageUnsecured: [ this.selectedNPADetail.percentageUnsecured || '' , [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            absoluteValue: [ this.selectedNPADetail.absoluteValue || '', [Validators.pattern(MonitoringRegEx.twelveCommaTwo)]],
            loanAssetValue: [ this.selectedNPADetail.loanAssetValue || '', [Validators.pattern(MonitoringRegEx.twelveCommaTwo)]],
            securedLoanAsset: [ this.selectedNPADetail.securedLoanAsset || '', [Validators.pattern(MonitoringRegEx.twelveCommaTwo)] ],
            unsecuredLoanAsset: [ this.selectedNPADetail.unsecuredLoanAsset || '', [Validators.pattern(MonitoringRegEx.twelveCommaTwo)]],
            npaProvisionValue: [ this.selectedNPADetail.npaProvisionValue || '', [Validators.pattern(MonitoringRegEx.twelveCommaTwo)]],
            netAssetValue: [ this.selectedNPADetail.netAssetValue || '' ],
            remarks: [ this.selectedNPADetail.remarks || '']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.npaDetailUpdateForm.valid) {
            this.saveNPADetails();
        }
    }

    /**
     * saveNPADetails()
     */
    saveNPADetails(): void {
        var npaDetail = this.npaDetailUpdateForm.value;

        var dt = new Date(npaDetail.assetClassificationChangeDate);
        npaDetail.assetClassificationChangeDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(npaDetail.provisionDate);
        npaDetail.provisionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        npaDetail.npaId = this._dialogData.npaId;
        if (this._dialogData.operation === 'addNPADetails') {
            this._loanMonitoringService.saveNPADetails(this._dialogData.npaId, npaDetail).subscribe(() => {
                this._matSnackBar.open('NPA details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedNPADetail.loanNumber = this.loanContractId;
            this.selectedNPADetail.lineItemNumber = npaDetail.lineItemNumber;
            this.selectedNPADetail.npaAssetClass = npaDetail.npaAssetClass;
            this.selectedNPADetail.assetClassificationChangeDate = npaDetail.assetClassificationChangeDate;
            this.selectedNPADetail.provisionDate = npaDetail.provisionDate;
            this.selectedNPADetail.percentageSecured = npaDetail.percentageSecured;
            this.selectedNPADetail.percentageUnsecured = npaDetail.percentageUnsecured;
            this.selectedNPADetail.absoluteValue = npaDetail.absoluteValue;
            this.selectedNPADetail.loanAssetValue = npaDetail.loanAssetValue;
            this.selectedNPADetail.securedLoanAsset = npaDetail.securedLoanAsset;
            this.selectedNPADetail.unsecuredLoanAsset = npaDetail.unsecuredLoanAsset;
            this.selectedNPADetail.npaProvisionValue = npaDetail.npaProvisionValue;
            this.selectedNPADetail.netAssetValue = npaDetail.netAssetValue;
            this.selectedNPADetail.remarks = npaDetail.remarks;
            this._loanMonitoringService.updateNPADetails(this._dialogData.npaId, this.selectedNPADetail).subscribe(() => {
                this._matSnackBar.open('NPA details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });            
        }
    }
}
