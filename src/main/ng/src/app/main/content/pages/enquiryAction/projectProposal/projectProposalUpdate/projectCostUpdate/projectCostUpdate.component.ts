import { Component, Input, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { ShareHolderUpdateComponent } from '../shareHolderUpdate/shareHolderUpdate.component';
import { ConfirmationDialogComponent } from 'app/main/content/pages/appraisal/confirmationDialog/confirmationDialog.component';

@Component({
    selector: 'fuse-project-cost-update',
    templateUrl: './projectCostUpdate.component.html',
    styleUrls: ['./projectCostUpdate.component.scss'],
    animations: fuseAnimations
})
export class ProjectCostUpdateComponent implements OnDestroy {

    _projectCostForm: FormGroup;
    _projectCost: any = {};

    _projectProposal: any;

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getProjectCost(this._projectProposal.id).subscribe(response => {
                this._projectCost = response;
                this.initializeFormValues();
            });
            this._enquiryActionService.getShareHolders(this._projectProposal.id).subscribe(response => {
                this.dataSource = new MatTableDataSource(response._embedded.shareHolders);
            });
        }
    }

    _selectedShareHolder: any;

    dataSource: MatTableDataSource<any>;
       
    displayedColumns = [
        'companyName', 'equityCapital', 'percentageHolding'
    ];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                private _dialogRef: MatDialog, 
                private _matSnackBar: MatSnackBar,
                public _enquiryActionService: EnquiryActionService) { 

        this._projectCostForm = this._formBuilder.group({
            projectCost: new FormControl('', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]),
            debt: new FormControl('', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]),
            equity: new FormControl('', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]),
            pfsDebtAmount: new FormControl('', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]),
            debtEquityRatio: new FormControl('', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)])
        });
        
        this.initializeFormValues();
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
    }

    /**
     * submit()
     */
    submit(): void {        
        console.log(this._projectCostForm.value);
        if (this._projectCostForm.valid) {
            var formValues = this._projectCostForm.value;
            if (JSON.stringify(this._projectCost) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._projectProposal.id;
                this._enquiryActionService.createProjectCost(formValues).subscribe(response => {
                    this._projectCost = response;
                    this._matSnackBar.open('Project cost created successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                console.log('updating existing record');
                this._projectCost.projectCost = formValues.projectCost;
                this._projectCost.debt = formValues.debt;
                this._projectCost.equity = formValues.equity;
                this._projectCost.pfsDebtAmount = formValues.pfsDebtAmount;
                this._projectCost.debtEquityRatio = formValues.debtEquityRatio;
                this._enquiryActionService.updateProjectCost(this._projectCost).subscribe(response => {
                    this._projectCost = response;
                    this._matSnackBar.open('Project cost updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * initializeFormValues()
     */
    initializeFormValues(): void {
        this._projectCostForm.setValue({
            'projectCost': this._projectCost.projectCost || '',
            'debt': this._projectCost.debt || '',
            'equity': this._projectCost.equity || '',
            'pfsDebtAmount': this._projectCost.pfsDebtAmount || '',
            'debtEquityRatio': this._projectCost.debtEquityRatio || ''
        });
    }

    /**
     * onRowSelect()
     */
    onRowSelect(shareHolder: any): void {
        this._selectedShareHolder = shareHolder;
    }

    /**
     * openUpdateDialog()
     */
    openUpdateDialog(operation: string) {
        // Open the dialog.
        var data = {
            'projectProposalId': this._projectProposal.id,
            'shareHolder': {}
        };
        if (operation === 'modifyShareHolder') {
            data.shareHolder = this._selectedShareHolder;
        }
        const dialogRef = this._dialogRef.open(ShareHolderUpdateComponent, {
            data: data,
            width: '750px'
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((data) => {
            if (data.refresh === true) {
                this._enquiryActionService.getShareHolders(this._projectProposal.id).subscribe(response => {
                    this.dataSource.data = response._embedded.shareHolders;
                });
            }
        });    
    }

    /**
     * delete()
     */
    delete(): void {
        const dialogRef = this._dialogRef.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((data) => {
            if (data.response) {
                this._enquiryActionService.deleteShareHolder(this._selectedShareHolder).subscribe(() => {
                    this._enquiryActionService.getShareHolders(this._projectProposal.id).subscribe(response => {
                        this.dataSource.data = response._embedded.shareHolders;
                    });
                    this._selectedShareHolder = undefined;
                });
            }
        });        
    }
}
