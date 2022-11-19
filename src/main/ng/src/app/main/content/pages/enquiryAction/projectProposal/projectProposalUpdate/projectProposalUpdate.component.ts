import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../../enquiryAction.service';

@Component({
  selector: 'fuse-project-proposal-update',
  templateUrl: './projectProposalUpdate.component.html',
  styleUrls: ['./projectProposalUpdate.component.scss']
})
export class ProjectProposalUpdateComponent {

    dialogTitle = "Update Project Proposal";
    expandPanel1 = true;

    displayTabs = false;

    projectProposal: any = {};
    projectProposalForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                public _dialogRef: MatDialogRef<ProjectProposalUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar,
                public _loanEnquiryService: LoanEnquiryService,
                private _enquiryActionService: EnquiryActionService) { 

        // Fetch selected project proposal details from the dialog's data attribute
        this.projectProposal = Object.assign({}, _dialogData.projectProposal);
        if (JSON.stringify(this.projectProposal) !== JSON.stringify({})) {
            this.displayTabs = true;
        }

        this.projectProposalForm = this._formBuilder.group({
            loanEnquiryNumber: [ this.projectProposal.loanEnquiryNumber || '' ],
            proposalFormSharingDate: [ this.projectProposal.proposalFormSharingDate || undefined ],
            proposalStatus: [ this.projectProposal.proposalStatus || '' ],
            documentName: [ this.projectProposal.documentName || '' ],
            documentType: [ this.projectProposal.documentType || '' ],
            documentVersion: [ this.projectProposal.documentVersion || '' ],
            additionalDetails: [ this.projectProposal.additionalDetails || '' ]
        });
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close();
    }

    /**
     * submit()
     */
    submit(): void {
        console.log(this.projectProposalForm.value);
        if (this.projectProposalForm.valid) {
            var formValues = this.projectProposalForm.value;

            var dt = new Date(formValues.proposalFormSharingDate);
            formValues.proposalFormSharingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this.projectProposal) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._enquiryActionService.createProjectProposal(formValues).subscribe(response => {
                    this.projectProposal = response;
                    this._matSnackBar.open('Project Proposal created successfully.', 'OK', { duration: 7000 });
                    this.displayTabs = true;
                });
            }
            else {
                console.log('updating');
                this.projectProposal.proposalFormSharingDate = formValues.proposalFormSharingDate;
                this.projectProposal.proposalStatus = formValues.proposalStatus;
                this.projectProposal.documentName = formValues.documentName;
                this.projectProposal.documentType = formValues.documentType;
                this.projectProposal.documentVersion = formValues.documentVersion;
                this.projectProposal.additionalDetails = formValues.additionalDetails;
                this._enquiryActionService.updateProjectProposal(this.projectProposal).subscribe(response => {
                    this.projectProposal = response;
                    this._matSnackBar.open('Project Proposal updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    onFileSelect(event: any): void {
        
    }
}
