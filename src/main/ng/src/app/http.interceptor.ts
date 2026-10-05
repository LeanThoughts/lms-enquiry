import {
    HttpRequest,
    HttpEvent,
    HttpHandlerFn,
    HttpErrorResponse,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, finalize, Observable, throwError } from 'rxjs';
import { MessageService } from './message.service';
import { BusyIndicatorService, SKIP_BUSY_INDICATOR } from './busy-indicator.service';

export function httpInterceptor(req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> {

    const messageService = inject(MessageService);
    const busyIndicatorService = inject(BusyIndicatorService);

    // Using Router.navigate() is preferred in Angular as it preserves app state and triggers proper lifecycle hooks
    const router = inject(Router);

    // Clone the request and add withCredentials to include the session cookie
    const modifiedReq = req.clone({
        withCredentials: true
    });

    const showBusyIndicator = !req.context.get(SKIP_BUSY_INDICATOR);
    if (showBusyIndicator) {
        busyIndicatorService.start();
    }

    // Handle the request and check for 401 responses
    return next(modifiedReq).pipe(
        finalize(() => {
            if (showBusyIndicator) {
                busyIndicatorService.stop();
            }
        }),
        catchError((error: HttpErrorResponse) => {
            // If the BFF returns 401 (Unauthorized) or 403 (Forbidden),
            // it means the session is no longer valid or access is denied.
            if (error.status === 401 || error.status === 403) {
                console.warn('HTTP Interceptor: Unauthorized/Forbidden response. Redirecting to login.', error);
                // Redirect to the Angular login route. The AuthGuard will then
                // trigger the full login flow via the BFF.
                router.navigate(['/login']);
            }
            // Re-throw the error for other error handlers to catch
            return throwError(() => error);
        })
    )
}
