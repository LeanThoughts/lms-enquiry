import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-icc-approval-update-dialog',
    templateUrl: './iccApprovalUpdate.component.html',
    styleUrls: ['./iccApprovalUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCApprovalUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add ICC Approval Details';

    loanApplicationId = '';
    selectedICCApproval: any;

    iccApprovalForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCApprovalUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedICCApproval = Object.assign({}, _dialogData.selectedICCApproval);
        this.loanApplicationId = _dialogData.loanApplicationId;

        if (this.selectedICCApproval.id !== undefined) {
            this.dialogTitle = 'Modify ICC Approval Details';
        }

        this.iccApprovalForm = this._formBuilder.group({
            meetingNumber: [this.selectedICCApproval.meetingNumber],
            meetingDate: [this.selectedICCApproval.meetingDate || ''],
            remarks: [this.selectedICCApproval.remarks || ''],
            edApprovalDate: [this.selectedICCApproval.edApprovalDate || ''],
            cfoApprovalDate: [this.selectedICCApproval.cfoApprovalDate || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.iccApprovalForm.valid) {
            var iccApproval = this.iccApprovalForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(iccApproval.meetingDate);
            iccApproval.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            // Convert edApprovalDate to UTC
            if (iccApproval.edApprovalDate) {
                dt = new Date(iccApproval.edApprovalDate);
                iccApproval.edApprovalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            // Convert cfoApprovalDate to UTC
            if (iccApproval.cfoApprovalDate) {
                dt = new Date(iccApproval.cfoApprovalDate);
                iccApproval.cfoApprovalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (this.selectedICCApproval.id === undefined) {
                iccApproval.loanApplicationId = this.loanApplicationId;
                this._iccApprovalService.createApprovalByICC(iccApproval).subscribe(() => {
                    this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                        this._iccApprovalService._iccApproval.next(data);
                        this._matSnackBar.open('ICC Approval details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                this.selectedICCApproval.meetingDate = iccApproval.meetingDate;
                this.selectedICCApproval.meetingNumber = iccApproval.meetingNumber;
                this.selectedICCApproval.remarks = iccApproval.remarks;
                this.selectedICCApproval.edApprovalDate = iccApproval.edApprovalDate;
                this.selectedICCApproval.cfoApprovalDate = iccApproval.cfoApprovalDate;
                this._iccApprovalService.updateApprovalByICC(this.selectedICCApproval).subscribe(() => {
                    this._matSnackBar.open('ICC Approval details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
