import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { Observable, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    public currentUser: any = null;

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Initiates the login process by redirecting the user to the Spring Boot BFF's OAuth2 authorization endpoint. Spring Security will 
     * then handle the redirect to Microsoft Entra ID's managed login page.
     */
    public redirectToBFFLogin(): void {
        window.location.href = `${environment.primaryHost}/oauth2/authorization/azure`;
    }

    /**
     * Checks if the user is currently authenticated by making a request to a protected endpoint.
     * The BFF will use its session cookie to determine authentication status.
     * Returns an Observable<boolean> True if authenticated, false otherwise.
     */
    public isAuthenticated(): Observable<boolean> {
        console.log('*** Entering isAuthenticated() method ***'); // <-- Add this line
        console.log('Checking if user is authenticated');
        const meUrl = environment.primaryApiHost + '/me'; // Define URL for clearer logging
        // Make a lightweight request to a protected endpoint on the BFF. The browser will automatically send the session cookie.
        // The BFF will respond with 200 OK if authenticated, or 401 Unauthorized if not.
        return this.http.get<any>(meUrl, { observe: 'response' }).pipe(
            map(response => {
                console.log('BFF response:', response);
                this.currentUser = response.body;
                // If the BFF returns a 200 OK, the user is authenticated.
                return response.status === 200;
            }),
            catchError((error: HttpErrorResponse) => {
                // If the BFF returns 401 Unauthorized, or any other error, the user is not authenticated.
                if (error.status === 401) {
                    console.warn('Authentication check failed on BFF:', error.status, error.message);
                    return of(false);
                }
                else if (error.status === 0  && error.statusText === 'Unknown Error') {
                    return of(false);
                }
                else {
                    // For other errors, propagate them up
                    return throwError(() => error);
                }
            })
        );
    }

    /**
     * Calls the BFF's logout endpoint by performing a direct browser redirect.
     * This bypasses HttpClient's handling, allowing the browser to manage the full redirect chain from the server.
     */
    public logout(): void {
        // Perform a direct browser redirect to the BFF's logout endpoint.
        // Spring Security will handle the session invalidation and then redirect the browser to the logoutSuccessUrl configured in 
        // SecurityConfig.
        console.log('Initiating direct browser redirect for logout.');
        window.location.href = `${environment.primaryHost}/logout`;
    }    
}
