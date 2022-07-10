import { Component, Inject } from '@angular/core';
import { Form, FormControl } from '@angular/forms';
import { MatDialogRef, MatTableDataSource, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'fuse-risk-evaluation-details',
  templateUrl: './risk-evaluation-details.component.html',
  styleUrls: ['./risk-evaluation-details.component.scss']
})
export class RiskEvaluationDetails {

    dialogTitle = "Risk Evaluation Details";

    selectedRisk: any;

    riskModelId: FormControl;
    loanContractNumber: FormControl;
    projectTypeDescription: FormControl;
    ratingDate: FormControl;
    projectPhase: FormControl;
    projectName: FormControl;
    workflowStatus: FormControl;
    initDepartment: FormControl;
    finalProjectGrade: FormControl;
    firstApprover: FormControl;
    secondApprover: FormControl;
    thirdApprover: FormControl;
    latestReviewer: FormControl;

    dataSource1: MatTableDataSource<any>;
    displayedColumns1 = [
        'ratingType', 'score', 'grade'
    ];

    dataSource2: MatTableDataSource<any>;
    displayedColumns2 = [
        'projectPhase', 'componentName', 'score'
    ];

    /**
     * constructor()
     */
    constructor(public _dialogRef: MatDialogRef<RiskEvaluationDetails>, @Inject(MAT_DIALOG_DATA) private _dialogData: any) { 

        this.selectedRisk = Object.assign({}, _dialogData.selectedRisk);
        console.log(this.selectedRisk);
        this.displayData();
    }

    /**
     * displayData()
     */
    displayData(): void {
        this.riskModelId = new FormControl(this.selectedRisk.riskEvalId);
        this.loanContractNumber = new FormControl(this.selectedRisk.loanContractId);
        this.projectTypeDescription = new FormControl(this.selectedRisk.riskPrjTypeT);
        this.ratingDate = new FormControl(this.selectedRisk.ratingDate);
        this.projectPhase = new FormControl(this.selectedRisk.RiskPrjPhase);
        this.projectName = new FormControl(this.selectedRisk.projectName);
        this.workflowStatus = new FormControl(this.selectedRisk.wfStatusDesc);
        this.initDepartment = new FormControl(this.selectedRisk.department);
        this.finalProjectGrade = new FormControl(this.selectedRisk.FinalGrade);
        this.firstApprover = new FormControl(this.selectedRisk.firstLvlApprover);
        this.secondApprover = new FormControl(this.selectedRisk.secondLvlApprover);
        this.thirdApprover = new FormControl(this.selectedRisk.thirdLvlApprover);
        this.latestReviewer = new FormControl(this.selectedRisk.latestReviewer);

        this.dataSource1 = new MatTableDataSource(this.selectedRisk.riskEvaluation_OverallScore_Nav);

        this.dataSource2 = new MatTableDataSource(this.selectedRisk.riskEvaluation_ComponentScore_Nav);
    }
}
