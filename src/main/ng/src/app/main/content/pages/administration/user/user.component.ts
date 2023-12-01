import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { UserUpdateDialogComponent } from './userUpdate/userUpdate.component';
import { MatDialog, MatSnackBar } from '@angular/material';
import { UserModel } from '../../../model/user.model';
import { UserService } from './user.service';
import { ForgotPasswordService } from '../../forgotPassword/forgotPassword.service';

@Component({
    selector: 'fuse-user-component',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class UserComponent implements OnInit {

    selectedUser: UserModel;

    constructor(private _userService: UserService, private _dialogRef: MatDialog, private _matSnackBar: MatSnackBar, 
            private _forgotPasswordService: ForgotPasswordService) {
        this._userService.selectedUser.subscribe((data) => {
            this.selectedUser = data;
        });
    }

    ngOnInit(): void {
    }

    displayAddUserDialog(): void {
        // Open the dialog.
        const dialogRef = this._dialogRef.open(UserUpdateDialogComponent, {
            panelClass: 'fuse-user-update-dialog',
            width: '850px',
            data: {
                operation: 'addUser'
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { });
    }

    displayUpdateUserDialog(): void {
        // Open the dialog.
        const dialogRef = this._dialogRef.open(UserUpdateDialogComponent, {
            panelClass: 'fuse-user-update-dialog',
            width: '850px',
            data: {
                operation: 'updateUser',
                selectedUser: this.selectedUser
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { });
    }

    /**
     * resetPassword()
     */
    resetPassword(): void {
        this._matSnackBar.open('Generating a new password. Please wait.', 'OK', {
            duration: 10000
        });
        this._forgotPasswordService.resetPassword(this.selectedUser.email).subscribe(response => {
            this._matSnackBar.open('New password is sent to the email address provided.', 'OK', {
                duration: 7000
            });
        });
    }
}
