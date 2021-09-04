import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { SecurityComplianceModel } from 'app/main/content/model/securityCompliance.model';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-security-compliance-update-dialog',
    templateUrl: './securityComplianceUpdate.component.html',
    styleUrls: ['./securityComplianceUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SecurityComplianceUpdateDialogComponent {

    dialogTitle = 'Add New Security Compliance Maintenance';

    selectedSecurityCompliance: SecurityComplianceModel;

    securityComplianceUpdateForm: FormGroup;

    collateralObjectTypes = LoanMonitoringConstants.collateralObjectTypes;
    collateralAgreementTypes: any;
    applicability = LoanMonitoringConstants.applicability;
    actionPeriods = LoanMonitoringConstants.actionPeriods;
    actionPeriodSuffixes = LoanMonitoringConstants.actionPeriodSuffixes;
    eventTypes = LoanMonitoringConstants.eventTypes;
    unitOfMeasures = LoanMonitoringConstants.unitOfMeasures;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<SecurityComplianceUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedSecurityCompliance !== undefined) {
            this.selectedSecurityCompliance = Object.assign({}, _dialogData.selectedSecurityCompliance);
            this.dialogTitle = 'Modify Security Compliance Maintenance';
            
            // populate collateral agreement types
            this.collateralAgreementTypes = LoanMonitoringConstants.collateralAgreementTypes.filter(obj => {
                return obj.objectType === this.selectedSecurityCompliance.collateralObjectType;
            });
    
        }
        else {
            this.selectedSecurityCompliance = new SecurityComplianceModel({});
        }

        this.securityComplianceUpdateForm = _formBuilder.group({
            collateralObjectType: [this.selectedSecurityCompliance.collateralObjectType],
            collateralAgreementType: [this.selectedSecurityCompliance.collateralAgreementType],
            collateralAgreementTypeDescription: [this.selectedSecurityCompliance.collateralAgreementTypeDescription],
            timelines: [this.selectedSecurityCompliance.timelines],
            applicability: [this.selectedSecurityCompliance.applicability],
            dateOfCreation: [this.selectedSecurityCompliance.dateOfCreation || ''],
            value: [this.selectedSecurityCompliance.value, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            validityDate: [this.selectedSecurityCompliance.validityDate || ''],
            securityPerfectionDate: [this.selectedSecurityCompliance.securityPerfectionDate || ''],
            actionPeriod: [this.selectedSecurityCompliance.actionPeriod],
            actionPeriodSuffix: [this.selectedSecurityCompliance.actionPeriodSuffix],
            numberOfDays: [this.selectedSecurityCompliance.numberOfDays],
            eventType: [this.selectedSecurityCompliance.eventType],
            eventDate: [this.selectedSecurityCompliance.eventDate || ''],
            timelineDate: [this.selectedSecurityCompliance.timelineDate || ''],
            validityPeriod: [this.selectedSecurityCompliance.validityPeriod],
            remarks: [this.selectedSecurityCompliance.remarks],
            location: [this.selectedSecurityCompliance.location],
            additionalText: [this.selectedSecurityCompliance.additionalText],
            realEstateLandArea: [this.selectedSecurityCompliance.realEstateLandArea, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            areaUnitOfMeasure: [this.selectedSecurityCompliance.areaUnitOfMeasure],
            securityNoOfUnits: [this.selectedSecurityCompliance.securityNoOfUnits, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            securityFaceValueAmount: [this.selectedSecurityCompliance.securityFaceValueAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            holdingPercentage: [this.selectedSecurityCompliance.holdingPercentage, [Validators.pattern(MonitoringRegEx.holdingPercentage)]]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.securityComplianceUpdateForm.valid) {
            var securityCompliance: SecurityComplianceModel = new SecurityComplianceModel(this.securityComplianceUpdateForm.value);
            console.log('securityCompliance form', securityCompliance);
            var dt = new Date(securityCompliance.dateOfCreation);
            securityCompliance.dateOfCreation = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityCompliance.validityDate);
            securityCompliance.validityDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityCompliance.securityPerfectionDate);
            securityCompliance.securityPerfectionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityCompliance.eventDate);
            securityCompliance.eventDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityCompliance.timelineDate);
            securityCompliance.timelineDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addSecurityCompliance') {
                this._loanMonitoringService.saveSecurityCompliance(securityCompliance, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Security Compliance added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedSecurityCompliance.collateralObjectType = securityCompliance.collateralObjectType;
                this.selectedSecurityCompliance.collateralAgreementType = securityCompliance.collateralAgreementType;
                this.selectedSecurityCompliance.collateralAgreementTypeDescription  = securityCompliance.collateralAgreementTypeDescription;
                this.selectedSecurityCompliance.timelines  = securityCompliance.timelines;
                this.selectedSecurityCompliance.applicability  = securityCompliance.applicability;
                this.selectedSecurityCompliance.dateOfCreation  = securityCompliance.dateOfCreation;
                this.selectedSecurityCompliance.value  = securityCompliance.value;
                this.selectedSecurityCompliance.validityDate  = securityCompliance.validityDate;
                this.selectedSecurityCompliance.securityPerfectionDate  = securityCompliance.securityPerfectionDate;
                this.selectedSecurityCompliance.actionPeriod  = securityCompliance.actionPeriod;
                this.selectedSecurityCompliance.actionPeriodSuffix  = securityCompliance.actionPeriodSuffix;
                this.selectedSecurityCompliance.numberOfDays  = securityCompliance.numberOfDays;
                this.selectedSecurityCompliance.eventType  = securityCompliance.eventType;
                this.selectedSecurityCompliance.eventDate  = securityCompliance.eventDate;
                this.selectedSecurityCompliance.timelineDate = securityCompliance.timelineDate;
                this.selectedSecurityCompliance.validityPeriod  = securityCompliance.validityPeriod;
                this.selectedSecurityCompliance.remarks  = securityCompliance.remarks;
                this.selectedSecurityCompliance.location  = securityCompliance.location;
                this.selectedSecurityCompliance.additionalText  = securityCompliance.additionalText;
                this.selectedSecurityCompliance.realEstateLandArea  = securityCompliance.realEstateLandArea;
                this.selectedSecurityCompliance.areaUnitOfMeasure  = securityCompliance.areaUnitOfMeasure;
                this.selectedSecurityCompliance.securityNoOfUnits  = securityCompliance.securityNoOfUnits;
                this.selectedSecurityCompliance.securityFaceValueAmount  = securityCompliance.securityFaceValueAmount;
                this.selectedSecurityCompliance.holdingPercentage  = securityCompliance.holdingPercentage;
                this._loanMonitoringService.updateSecurityCompliance(this.selectedSecurityCompliance).subscribe(() => {
                    this._matSnackBar.open('Security Compliance updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * getCollateralAgreementTypes()
     * @param event 
     */
    getCollateralAgreementTypes(event: any) {
        this.collateralAgreementTypes = LoanMonitoringConstants.collateralAgreementTypes.filter(obj => {
            return obj.objectType === event.value;
        });
    }
}
