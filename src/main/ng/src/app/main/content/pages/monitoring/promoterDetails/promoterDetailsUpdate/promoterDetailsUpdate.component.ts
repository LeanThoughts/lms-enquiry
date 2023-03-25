import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { PromoterDetailsItemModel } from 'app/main/content/model/promoterDetailsItem.model';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-promoter-details-update-dialog',
    templateUrl: './promoterDetailsUpdate.component.html',
    styleUrls: ['./promoterDetailsUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class PromoterDetailsUpdateDialogComponent {

    dialogTitle = 'Add New Promoter Details';

    selectedPromoterDetailItem: PromoterDetailsItemModel;

    promoterDetailUpdateForm: FormGroup;
  
    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<PromoterDetailsUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected promoter details from the dialog's data attribute.
        if (_dialogData.selectedPromoterDetailItem !== undefined) {
            this.selectedPromoterDetailItem = Object.assign({}, _dialogData.selectedPromoterDetailItem);
            this.dialogTitle = 'Update Promoter Details';
        }
        else {
            this.selectedPromoterDetailItem = new PromoterDetailsItemModel({});
        }

        this.promoterDetailUpdateForm = _formBuilder.group({
            shareHoldingCompany: [this.selectedPromoterDetailItem.shareHoldingCompany],
            paidupCapitalEquitySanction: [this.selectedPromoterDetailItem.paidupCapitalEquitySanction, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            paidupCapitalEquityCurrent: [this.selectedPromoterDetailItem.paidupCapitalEquityCurrent, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            equityLinkInstrumentSanction: [this.selectedPromoterDetailItem.equityLinkInstrumentSanction, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            equityLinkInstrumentCurrent: [this.selectedPromoterDetailItem.equityLinkInstrumentCurrent, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            groupExposure: [this.selectedPromoterDetailItem.groupExposure, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            dateOfChange: [this.selectedPromoterDetailItem.dateOfChange || '']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.promoterDetailUpdateForm.valid) {
            var promoterDetailItem: PromoterDetailsItemModel = new PromoterDetailsItemModel(this.promoterDetailUpdateForm.value);

            var dt = new Date(promoterDetailItem.dateOfChange);
            promoterDetailItem.dateOfChange = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addPromoterDetailItem') {
                // Convert text data to numeric  values by using the unary operator ...
                promoterDetailItem.paidupCapitalEquitySanction = +promoterDetailItem.paidupCapitalEquitySanction;
                promoterDetailItem.paidupCapitalEquityCurrent = +promoterDetailItem.paidupCapitalEquityCurrent;
                promoterDetailItem.equityLinkInstrumentSanction = +promoterDetailItem.equityLinkInstrumentSanction;
                promoterDetailItem.equityLinkInstrumentCurrent = +promoterDetailItem.equityLinkInstrumentCurrent;
                
                this._loanMonitoringService.savePromoterDetailItem(promoterDetailItem, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Promoter details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedPromoterDetailItem.shareHoldingCompany = promoterDetailItem.shareHoldingCompany;
                this.selectedPromoterDetailItem.paidupCapitalEquitySanction = +promoterDetailItem.paidupCapitalEquitySanction;
                this.selectedPromoterDetailItem.paidupCapitalEquityCurrent  = +promoterDetailItem.paidupCapitalEquityCurrent;
                this.selectedPromoterDetailItem.equityLinkInstrumentSanction  = +promoterDetailItem.equityLinkInstrumentSanction;
                this.selectedPromoterDetailItem.equityLinkInstrumentCurrent  = +promoterDetailItem.equityLinkInstrumentCurrent;
                this.selectedPromoterDetailItem.groupExposure = promoterDetailItem.groupExposure;
                this.selectedPromoterDetailItem.dateOfChange = promoterDetailItem.dateOfChange;
                this._loanMonitoringService.updatePromoterDetailItem(this.selectedPromoterDetailItem).subscribe(() => {
                    this._matSnackBar.open('Promoter details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });       
            }
        }
    }
}
