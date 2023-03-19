import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-npa-update-component',
    templateUrl: './npaUpdate.component.html',
    styleUrls: ['./npaUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class NPAUpdateComponent implements OnInit {

    documentTypes = LoanMonitoringConstants.siteVisitDocumentTypes;
    selectedNPA: any = {};
    npaUpdateForm: FormGroup;

    loanApplicationId = '';

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService, 
        private _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.selectedNPA.id = '';
        _loanMonitoringService.getNPADetails(this.loanApplicationId).subscribe(data => {
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
}
