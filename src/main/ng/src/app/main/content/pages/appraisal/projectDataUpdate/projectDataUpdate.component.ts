import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-project-data-update',
  templateUrl: './projectDataUpdate.component.html',
  styleUrls: ['./projectDataUpdate.component.scss']
})
export class ProjectDataUpdateComponent {

    dialogTitle = "Update Project Data";

    _projectDataStep1Form: FormGroup;
    _projectDataStep2Form: FormGroup;
    _projectDataStep3Form: FormGroup;

    _projectData: any;
    _loanEnquiry: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ProjectDataUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._projectData = Object.assign({}, _dialogData.projectData);
        this._loanEnquiry = Object.assign({}, _dialogData.loanEnquiry);

        this._projectDataStep1Form = _formBuilder.group({
            projectName: [this._projectData.projectName || this._loanEnquiry.projectName],
            typeOfFunding: [this._projectData.typeOfFunding || ''],
            policyApplicable: [this._projectData.policyApplicable || ''],
            technology: [this._projectData.technology || ''],
            projectCapacityUnitMeasure: [this._projectData.projectCapacityUnitMeasure || this._loanEnquiry.projectCapacityUnitMeasure],
            projectCapacityUnitSize: [this._projectData.projectCapacityUnitSize || this._loanEnquiry.projectCapacityUnitSize],
            numberOfUnits: [this._projectData.numberOfUnits || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            designPlfCuf: [this._projectData.designPlfCuf || ''],
            mainContractor: [this._projectData.mainContractor || ''],
            epcContractor: [this._projectData.epcContractor || ''],
            resourceAssessmentAgency: [this._projectData.resourceAssessmentAgency || ''],
            oandmContractor: [this._projectData.epcContractor || '']
        });

        this._projectDataStep2Form = _formBuilder.group({
            offtakeVolume: [this._projectData.offtakeVolume || '', [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]],
            saleRate: [this._projectData.saleRate || '', [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]],
            fuelMix: [this._projectData.fuelMix || ''],
            fuelCost: [this._projectData.fuelCost || '', [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]],
            stationHeatRate: [this._projectData.stationHeatRate || '', [Validators.pattern(MonitoringRegEx.tenCommaTwo)]],
            oandmExpenses: [this._projectData.oandmExpenses || '', [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]],
            totalLand: [this._projectData.totalLand || '', [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]],
            projectCOD: [this._projectData.projectCOD || ''],
            ppaRate: [this._projectData.ppaRate || '', [Validators.pattern(MonitoringRegEx.twoCommaTwo)]],
            offTakerCompany: [this._projectData.offTakerCompany || ''],
            ippPercentage: [this._projectData.ippPercentage || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            groupCaptivePercentage: [this._projectData.groupCaptivePercentage || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            thirdPartyPercentage: [this._projectData.thirdPartyPercentage || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            marketPercentage: [this._projectData.marketPercentage || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]]
        });

        this._projectDataStep3Form = _formBuilder.group({
            epcCost: [this._projectData.epcCost || '', [Validators.pattern(MonitoringRegEx.thirteenCommaTwo)]],
            overallProjectCost: [this._projectData.overallProjectCost || '',  [Validators.pattern(MonitoringRegEx.sixteenCommaTwo)]],
            debtEquityRatio: [this._projectData.twoCommaTwo || ''],
            totalDebt: [this._projectData.totalDebt || '', [Validators.pattern(MonitoringRegEx.sixteenCommaTwo)]],
            roiPreCod: [this._projectData.roiPreCod || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            roiPostCod: [this._projectData.roiPostCod || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            constructionPeriod: [this._projectData.constructionPeriod || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            constructionPeriodUnit: [this._projectData.constructionPeriodUnit || ''],
            moratoriumPeriod: [this._projectData.moratoriumPeriod || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            moratoriumPeriodUnit: [this._projectData.moratoriumPeriodUnit || ''],
            tenorPeriod: [this._projectData.tenorPeriod || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            tenorUnit: [this._projectData.tenorUnit || ''],
            repaymentSchedule: [this._projectData.repaymentSchedule || ''],
            dscrMinimum: [this._projectData.dscrMinimum || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            dscrAverage: [this._projectData.dscrAverage || '', [Validators.pattern(MonitoringRegEx.threeCommaTwo)]],
            levCostTotal: [this._projectData.levCostTotal || '', [Validators.pattern(MonitoringRegEx.thirteenCommaTwo)]],
            levCostFixed: [this._projectData.levCostFixed || '', [Validators.pattern(MonitoringRegEx.thirteenCommaTwo)]],
            levCostVariable: [this._projectData.levCostVariable || '', [Validators.pattern(MonitoringRegEx.thirteenCommaTwo)]],
            workingCapitalCycle: [this._projectData.workingCapitalCycle || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            workingCapitalUnit: [this._projectData.workingCapitalUnit || '']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._projectDataStep1Form.valid) {
            var formValues = this._projectDataStep1Form.value;
            Object.assign(formValues, this._projectDataStep2Form.value, this._projectDataStep3Form.value);

            var dt = new Date(formValues.projectCOD);
            formValues.projectCOD = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._projectData) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new project data');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createProjectData(formValues).subscribe(response => {
                    this._matSnackBar.open('Project data created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectData': response });
                });
            }
            else {
                console.log('updating project data');

                this._projectData.projectName = formValues.projectName;
                this._projectData.typeOfFunding = formValues.typeOfFunding;
                this._projectData.policyApplicable = formValues.policyApplicable;
                this._projectData.technology = formValues.technology;
                this._projectData.projectCapacityUnitMeasure = formValues.projectCapacityUnitMeasure;
                this._projectData.projectCapacityUnitSize = formValues.projectCapacityUnitSize;
                this._projectData.numberOfUnits = formValues.numberOfUnits;
                this._projectData.designPlfCuf = formValues.designPlfCuf;
                this._projectData.mainContractor = formValues.mainContractor;
                this._projectData.epcContractor = formValues.epcContractor;
                this._projectData.resourceAssessmentAgency = formValues.resourceAssessmentAgency;
                this._projectData.oandmContractor = formValues.oandmContractor;

                this._projectData.offtakeVolume = formValues.offtakeVolume;
                this._projectData.saleRate = formValues.saleRate;
                this._projectData.fuelMix = formValues.fuelMix;
                this._projectData.fuelCost = formValues.fuelCost;
                this._projectData.stationHeatRate = formValues.stationHeatRate;
                this._projectData.oandmExpenses = formValues.oandmExpenses;
                this._projectData.totalLand = formValues.totalLand;
                this._projectData.projectCOD = formValues.projectCOD;
                this._projectData.ppaRate = formValues.ppaRate;
                this._projectData.offTakerCompany = formValues.offTakerCompany;
                this._projectData.ippPercentage = formValues.ippPercentage;
                this._projectData.groupCaptivePercentage = formValues.groupCaptivePercentage;
                this._projectData.thirdPartyPercentage = formValues.thirdPartyPercentage;
                this._projectData.marketPercentage = formValues.marketPercentage;

                this._projectData.epcCost = formValues.epcCost;
                this._projectData.overallProjectCost = formValues.overallProjectCost;
                this._projectData.debtEquityRatio = formValues.debtEquityRatio;
                this._projectData.totalDebt = formValues.totalDebt;
                this._projectData.roiPreCod = formValues.roiPreCod;
                this._projectData.roiPostCod = formValues.roiPostCod;
                this._projectData.constructionPeriod = formValues.constructionPeriod;
                this._projectData.constructionPeriodUnit = formValues.constructionPeriodUnit;
                this._projectData.moratoriumPeriod = formValues.moratoriumPeriod;
                this._projectData.moratoriumPeriodUnit = formValues.moratoriumPeriodUnit;
                this._projectData.tenorPeriod = formValues.tenorPeriod;
                this._projectData.tenorUnit = formValues.tenorUnit;
                this._projectData.repaymentSchedule = formValues.repaymentSchedule;
                this._projectData.dscrMinimum = formValues.dscrMinimum;
                this._projectData.dscrAverage = formValues.dscrAverage;
                this._projectData.levCostTotal = formValues.levCostTotal;
                this._projectData.levCostFixed = formValues.levCostFixed;
                this._projectData.levCostVariable = formValues.levCostVariable;
                this._projectData.workingCapitalCycle = formValues.workingCapitalCycle;
                this._projectData.workingCapitalUnit = formValues.workingCapitalUnit;

                this._loanAppraisalService.updateProjectData(this._projectData).subscribe(response => {
                    this._matSnackBar.open('Project data updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'projectData': response });
                });
            }
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }
}
