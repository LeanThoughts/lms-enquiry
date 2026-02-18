import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';

@Component({
    selector: 'app-homepage',
    standalone: true,
    templateUrl: './homepage.component.html',
    imports: [
        CommonModule,
        // ButtonComponent
    ]
})
export class HomepageComponent {
    
    /**
     * Constructor
     */
    constructor(private authService: AuthService) {
        authService.isAuthenticated().subscribe(isAuthenticated => {
        });
    }
}