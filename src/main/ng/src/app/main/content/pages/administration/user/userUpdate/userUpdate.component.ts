import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserModel } from '../../../../model/user.model';
import { UserService } from '../user.service';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';

@Component({
    selector: 'fuse-user-update-dialog',
    templateUrl: './userUpdate.component.html',
    styleUrls: ['./userUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class UserUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add/ Edit User';

    selectedUser: UserModel;

    userUpdateForm: FormGroup;

    constructor(_formBuilder: FormBuilder, private _userService: UserService,
        public _dialogRef: MatDialogRef<UserUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedUser !== undefined) {
            this.selectedUser = _dialogData.selectedUser;
        }
        else {
            this.selectedUser = new UserModel({});
        }

        this.userUpdateForm = _formBuilder.group({
            firstname: [this.selectedUser.firstName || ''],
            lastname: [this.selectedUser.lastName || ''],
            email: [this.selectedUser.email || ''],
            password: ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]],
            sapBPNumber: [this.selectedUser.sapBPNumber || ''],
            role: [this.selectedUser.role || 'ZLM013'],
            riskDepartment: [this.selectedUser.riskDepartment],
            departmentHead: [this.selectedUser.departmentHead || false],
            riskPortalDisplayOnlyAccess: [this.selectedUser.riskPortalDisplayOnlyAccess || false]
        });
    }

    ngOnInit(): void {
    }

    submit(): void {
        if (this.userUpdateForm.valid) {
            const user: UserModel = new UserModel(this.userUpdateForm.value);

          console.log("user.riskDepartment :" + user.riskDepartment);

          console.log("user.departmentHead :" + user.departmentHead);



          if (user.riskDepartment == null && user.departmentHead == true) {
              this._matSnackBar.open('Error: Select the department.', 'OK', { duration: 7000 });
              return;
            }



            if (this._dialogData.operation === 'addUser') {
                this._userService.createUser(user).subscribe(() => {
                    // Refresh the user list.
                    this._userService.getUsers();
                    // Show a snack.
                    this._matSnackBar.open('New user is added successfully.', 'OK', { duration: 7000 });
                    // Close the add/ update dialog.
                    this._dialogRef.close();
                },
                (error) => {
                    this._matSnackBar.open('User already exists or other errors occured.', 'OK', { duration: 7000 });
                });
            }
            else {
                this._userService.updateUser(user).subscribe(() => {
                    // Refresh the user list.
                    this._userService.getUsers();
                    // Show a snack.
                    this._matSnackBar.open('User details are updated successfully.', 'OK', { duration: 7000 });
                    // Close the add/ update dialog.
                    this._dialogRef.close();
                },
                (error: string) => {
                    // Show a snack.
                    if (error.startsWith('status 412 reading OAuthClient#modifyPassword')) {
                        this._matSnackBar.open('Password cannot be the same as the previous 3 passwords', 'OK', { duration: 7000 });
                    }
                    else {
                        this._matSnackBar.open(error, 'OK', { duration: 7000 });
                    }
                });
            }
        }
    }

  closeClick(): void {
    this._dialogRef.close();
  }
}
