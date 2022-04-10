import { DatePipe } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../loanAppraisal.service';
import { ProjectDataUpdateComponent } from '../projectDataUpdate/projectDataUpdate.component';

@Component({
    selector: 'fuse-project-data',
    templateUrl: './projectData.component.html',
    styleUrls: ['./projectData.component.scss'],
    animations: fuseAnimations
})
export class ProjectDataComponent implements OnDestroy {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    dataSource1 = [];
    dataSource2 = [];
    dataSource3 = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];

    private _projectData: any;
    
    subscriptions = new Subscription();
    selectedEnquiry: any;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                private _activatedRoute: ActivatedRoute,
                _loanAppraisalService: LoanAppraisalService, public datepipe: DatePipe) {

        this.subscriptions.add(_loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
        }));

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        this._projectData = _activatedRoute.snapshot.data.routeResolvedData[10];
        
        this.populateDisplayTables();
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }
    
    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        console.log('populating displaying tables now');
        this.dataSource1 = [];
        this.dataSource2 = [];
        this.dataSource3 = [];

        this.dataSource1.push({particulars: 'Project Name', description: this._projectData.projectName});
        this.dataSource1.push({particulars: 'Type of Funding', description: this._projectData.typeOfFunding});
        this.dataSource1.push({particulars: 'Policy Applicable', description: this._projectData.policyApplicable});
        this.dataSource1.push({particulars: 'Technology', description: this._projectData.technology});
        this.dataSource1.push({particulars: 'Unit Size', description: this._projectData.projectCapacityUnitSize});
        this.dataSource1.push({particulars: 'Unit of Measure', description: this._projectData.projectCapacityUnitMeasure});
        this.dataSource1.push({particulars: 'Number of Units', description: this._projectData.numberOfUnits});
        this.dataSource1.push({particulars: 'Design PLF CUF', description: this._projectData.designPlfCuf});
        this.dataSource1.push({particulars: 'Technology Supplier', description: this._projectData.mainContractor});
        this.dataSource1.push({particulars: 'EPC Contractor', description: this._projectData.epcContractor});
        this.dataSource1.push({particulars: 'Resource Assessment Agency', description: this._projectData.resourceAssessmentAgency});
        this.dataSource1.push({particulars: 'O&M Contractor', description: this._projectData.oandmContractor});
        
        this.dataSource2.push({particulars: 'Offtake Volume', description: this._projectData.offtakeVolume});
        this.dataSource2.push({particulars: 'Sale Rate', description: this._projectData.saleRate});
        this.dataSource2.push({particulars: 'Fuel Mix', description: this._projectData.fuelMix});
        this.dataSource2.push({particulars: 'Fuel Cost', description: this._projectData.fuelCost});
        this.dataSource2.push({particulars: 'Station Heat Rate', description: this._projectData.stationHeatRate});
        this.dataSource2.push({particulars: 'O&M Expenses', description: this._projectData.oandmExpenses});
        this.dataSource2.push({particulars: 'Total Land (Acres)', description: this._projectData.totalLand});
        this.dataSource2.push({particulars: 'Project COD', description: this.getFormattedDate(this._projectData.projectCOD)});
        this.dataSource2.push({particulars: 'PPA Rate', description: this._projectData.ppaRate});
        this.dataSource2.push({particulars: 'Off Taker Company', description: this._projectData.offTakerCompany});
        this.dataSource2.push({particulars: 'IPP %', description: this._projectData.ippPercentage});
        this.dataSource2.push({particulars: 'Group Captive %', description: this._projectData.groupCaptivePercentage});
        this.dataSource2.push({particulars: 'Third Party %', description: this._projectData.thirdPartyPercentage});
        this.dataSource2.push({particulars: 'Market %', description: this._projectData.marketPercentage});

        this.dataSource3.push({particulars: 'Plant & Machinery Cost/ EPC Cost', description: this._projectData.epcCost});
        this.dataSource3.push({particulars: 'Overall Project Cost', description: this._projectData.overallProjectCost});
        this.dataSource3.push({particulars: 'Debt Equity Ratio', description: this._projectData.debtEquityRatio});
        this.dataSource3.push({particulars: 'Total Debt', description: this._projectData.totalDebt});
        this.dataSource3.push({particulars: 'Rate of Interest - Pre COD', description: this._projectData.roiPreCod});
        this.dataSource3.push({particulars: 'Rate of Interest - Post COD', description: this._projectData.roiPostCod});
        this.dataSource3.push({particulars: 'Construction Period', description: this._projectData.constructionPeriod});
        this.dataSource3.push({particulars: 'Unit', description: this.getUnitDescription(this._projectData.constructionPeriodUnit)});
        this.dataSource3.push({particulars: 'Moratorium Period', description: this._projectData.moratoriumPeriod});
        this.dataSource3.push({particulars: 'Unit', description: this.getUnitDescription(this._projectData.moratoriumPeriodUnit)});
        this.dataSource3.push({particulars: 'Door to Door - Tenor', description: this._projectData.tenorPeriod});
        this.dataSource3.push({particulars: 'Unit', description: this.getUnitDescription(this._projectData.tenorUnit)});
        this.dataSource3.push({particulars: 'Repayment Schedule', description: this._projectData.repaymentSchedule});
        this.dataSource3.push({particulars: 'DSCR - Minimum', description: this._projectData.dscrMinimum});
        this.dataSource3.push({particulars: 'DSCR - Average', description: this._projectData.dscrAverage});
        this.dataSource3.push({particulars: 'LEV Cost of Supply w/o ROE (Total)', description: this._projectData.levCostTotal});
        this.dataSource3.push({particulars: 'LEV Cost of Supply w/o ROE (Fixed)', description: this._projectData.levCostFixed});
        this.dataSource3.push({particulars: 'LEV Cost of Supply w/o ROE (Variable)', description: this._projectData.levCostVariable});
        this.dataSource3.push({particulars: 'Working Capital Cycle', description: this._projectData.workingCapitalCycle});
        this.dataSource3.push({particulars: 'Unit', description: this.getUnitDescription(this._projectData.workingCapitalUnit)});
    }

    /**
     * getUnitDescription()
     */
    getUnitDescription(unit: string): string {
        if (unit === '1') {
            return 'Days';
        }
        else if (unit === '2') {
            return 'Weeks';
        }
        else if (unit === '3') {
            return 'Months';
        }
        else if (unit === '4') {
            return 'Years';
        }
        else {
            return '';
        }
    }

    /**
     * getFormattedDate()
     */
    getFormattedDate(dt: any): string {
        console.log(dt);
        if (dt) {
            return this.datepipe.transform(dt, 'dd-MM-yyyy')
        }
        else {
            return '';
        }
    }

    /**
     * onRowSelect()
     */
    onRowSelect(obj: any): void {
    }

    /**
     * openProjectDataUpdateDialog()
     */
     openProjectDataUpdateDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'projectData': this._projectData,
            'loanEnquiry': this.selectedEnquiry,
            'unitOfMeasures': this._activatedRoute.snapshot.data.routeResolvedData[11]._embedded.unitOfMeasures,
            'projectTypes': this._activatedRoute.snapshot.data.routeResolvedData[12]._embedded.projectTypes,
            'technologySuppliers': this._activatedRoute.snapshot.data.routeResolvedData[13],
            'epcContractors': this._activatedRoute.snapshot.data.routeResolvedData[14]
        };
        const dialogRef = this._dialogRef.open(ProjectDataUpdateComponent, {
            width: '1200px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._projectData = result.projectData;
                this.populateDisplayTables();
            }
        });   
    }
}
