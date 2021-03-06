import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatSnackBar, MatDialogRef } from '@angular/material';
import { ChangePasswordService } from './changePassword.service';
import { Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import {AppService} from "../../../../app.service";

@Component({
    selector: 'fuse-update-password',
    templateUrl: './updatePassword.component.html',
    styleUrls: ['./updatePassword.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class UpdatePasswordComponent implements OnInit {

    dialogTitle = 'Change Password';

    updatePasswordForm: FormGroup;

    // Private
    private _unsubscribeAll: Subject<any>;

    constructor(_formBuilder: FormBuilder,
                public _dialogRef: MatDialogRef<UpdatePasswordComponent>,
                private _passwordService: ChangePasswordService,
                private _matSnackBar: MatSnackBar,
                private _appService: AppService) {

        this.updatePasswordForm = _formBuilder.group({
          //password        : ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]],
          password        : ['', [Validators.required, Validators.pattern(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/)]],
          passwordConfirm : ['', [Validators.required, confirmPasswordValidator]]
        });

        // Set the private defaults
        this._unsubscribeAll = new Subject();
    }

    ngOnInit(): void {
        // Update the validity of the 'passwordConfirm' field
        // when the 'password' field changes
        this.updatePasswordForm.get('password').valueChanges
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe(() => {
                this.updatePasswordForm.get('passwordConfirm').updateValueAndValidity();
            });
    }

    submit(): void {

      let firstNameLowerCase = this._appService.currentUser.firstName.toLowerCase;
      let lastNameLowerCase = this._appService.currentUser.lastName.toLowerCase();
      let firstName = this._appService.currentUser.firstName;
      let lastName = this._appService.currentUser.lastName;


      let password = this.updatePasswordForm.value.password;

      //Additional Password Validations
      if (password.includes(firstName) ||
        password.includes(firstNameLowerCase)) {
        this._matSnackBar.open('Error: Password should not contain first name of the user.', 'OK', {
          duration: 5000
        });
        return;
      }


      if (password.includes(lastName)||
        password.includes(lastNameLowerCase )) {
        this._matSnackBar.open('Error: Password should not contain last name of the user.', 'OK', {
          duration: 5000
        });
        return;
      }

      if (password.includes('123') ||
        password.includes('1234') ||
        password.includes('12345')) {
        this._matSnackBar.open('Error: Password should not contain running numbers.', 'OK', {
          duration: 5000
        });
        return;
      }



        this._passwordService.changePassword(this.updatePasswordForm.value.password).subscribe(response => {
            this._matSnackBar.open('Password is updated.', 'OK', {
                duration: 7000
            });
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

/**
 * Confirm password validator
 *
 * @param {AbstractControl} control
 * @returns {ValidationErrors | null}
 */
export const confirmPasswordValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {

    if ( !control.parent || !control )
    {
        return null;
    }

    const password = control.parent.get('password');
    const passwordConfirm = control.parent.get('passwordConfirm');

    if ( !password || !passwordConfirm )
    {
        return null;
    }

    if ( passwordConfirm.value === '' )
    {
        return null;
    }

    if ( password.value === passwordConfirm.value )
    {
        return null;
    }

    return { 'passwordsNotMatching': true };
};
