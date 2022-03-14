import { Component, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
    selector: 'confirmation-dialog-componenet',
    templateUrl: './confirmationDialog.component.html',
    styleUrls: ['./confirmationDialog.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ConfirmationDialogComponent {

    dialogContentMessage = '';
    dialogTitle = '';

    /**
     * constructor()
     */
    constructor(public _dialogRef: MatDialogRef<ConfirmationDialogComponent>, @Inject(MAT_DIALOG_DATA) private _data: any) {
        this.dialogContentMessage = 'Are you sure you want to delete the selected entry?';
        this.dialogTitle = 'Confirmation Dialog';
    }

    /**
     * closeDialog()
     */
    closeConfirmationDialog(response: boolean): void {
        this._dialogRef.close({ 'response': response });
    }
}
