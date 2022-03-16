import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../loanAppraisal.service';
import { ProjectAppraisalCompletionUpdateComponent } from '../projectAppraisalCompletionUpdate/projectAppraisalCompletionUpdate.component';

@Component({
    selector: 'fuse-project-appraisal-completion-details',
    templateUrl: './projectAppraisalCompletionDetails.component.html',
    styleUrls: ['./projectAppraisalCompletionDetails.component.scss'],
    animations: fuseAnimations
})
export class ProjectAppraisalCompletionDetails {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    dataSource = [];
    
    displayedColumns = [
        'particulars', 'description'
    ];

    private _projectAppraisalCompletion: any;
    
    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                _activatedRoute: ActivatedRoute,
                _loanAppraisalService: LoanAppraisalService, public datepipe: DatePipe) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        this._projectAppraisalCompletion = _activatedRoute.snapshot.data.routeResolvedData[7];
        
        this.populateDisplayTables();
    }

    /**
     * populateDisplayTables()
     */
    populateDisplayTables(): void {
        console.log('populating displaying tables now');
        this.dataSource = [];
        this.dataSource.push({particulars: 'Date of Project Appraisal Completion', 
            value: this.getFormattedDate(this._projectAppraisalCompletion.dateOfProjectAppraisalCompletion)});
        this.dataSource.push({particulars: 'Agenda Note Approval by DIR-A', 
            value: this.getFormattedDate(this._projectAppraisalCompletion.agendaNoteApprovalByDirA)});
        this.dataSource.push({particulars: 'Agenda Note Approval by DIR-B', 
            value: this.getFormattedDate(this._projectAppraisalCompletion.agendaNoteApprovalByDirB)});
        this.dataSource.push({particulars: 'Agenda Note Approval by MD & CEO', 
            value: this.getFormattedDate(this._projectAppraisalCompletion.agendaNoteApprovalByMDAndCEO)});
        this.dataSource.push({particulars: 'Agenda Note Submission to Co Secy', 
            value: this.getFormattedDate(this._projectAppraisalCompletion.agendaNoteSubmissionToCoSecy)});
        this.dataSource.push({particulars: 'Remarks', value: this._projectAppraisalCompletion.remarks});
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
     * openProjectAppraisalCompletionUpdateDialog()
     */
    openProjectAppraisalCompletionUpdateDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'loanAppraisalId': this._loanAppraisalId,
            'projectAppraisalCompletion': this._projectAppraisalCompletion
        };
        const dialogRef = this._dialogRef.open(ProjectAppraisalCompletionUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._projectAppraisalCompletion = result.projectAppraisalCompletion;
                this.populateDisplayTables();
            }
        });
    }
}
