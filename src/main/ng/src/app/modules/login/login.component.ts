import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '@fundamental-ngx/core';
import { AuthService } from '../auth/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    templateUrl: './login.component.html',
    imports: [
        CommonModule,
        ButtonComponent
    ]
})
export class LoginComponent {
    
    /**
     * Constructor
     */
    constructor(private authService: AuthService) {
    }
  
    /**
     * Login button click handler
     */
    login() {
        this.authService.redirectToBFFLogin();
    }
}
