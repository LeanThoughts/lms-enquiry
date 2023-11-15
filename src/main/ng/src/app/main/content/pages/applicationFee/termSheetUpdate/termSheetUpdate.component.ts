import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../../iccApproval/iccApproval.service';

@Component({
    selector: 'fuse-term-sheet-update-dialog',
    templateUrl: './termSheetUpdate.component.html',
    styleUrls: ['./termSheetUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class TermSheetUpdateDialogComponent implements OnInit {

    dialogTitle = 'New Draft Term Sheet';

    loanApplicationId = '';
    selectedTermSheet: any;

    termSheetForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<TermSheetUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedTermSheet = Object.assign({}, _dialogData.selectedRejectedByICC);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedRejectedByICC !== undefined) {
            if (_dialogData.operation === 'updateICCApproval') {
                this.dialogTitle = 'Modify Draft Term Sheet';
            }
        }
        this.termSheetForm = this._formBuilder.group({
            dateOfIssuance: [this.selectedTermSheet.dateOfICCClearance || ''],
            status: [this.selectedTermSheet.status || ''],
            // acceptanceDate: [this.selectedTermSheet.acceptanceDate || ''],
            document: [this.selectedTermSheet.document || '']
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
        if (this.termSheetForm.valid) {
            var termSheet = this.termSheetForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(termSheet.dateOfIssuance);
            termSheet.dateOfIssuance = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addICCApproval') {
                termSheet.loanApplicationId = this.loanApplicationId;
                // this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc added successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });
            }
            else {
                this.selectedTermSheet.dateOfIssuance = termSheet.dateOfIssuance;
                this.selectedTermSheet.status = termSheet.status;
                this.selectedTermSheet.document = termSheet.document;
                // this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc updated successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });            
            }
        }
    }
}
