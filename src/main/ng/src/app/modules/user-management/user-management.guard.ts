import { inject } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { MessageService } from '../../message.service';
import { UserManagementService } from './user-management.service';

/**
 * Lets only users with the "Maintain Users" authorization into the module.
 * Used after routeInterceptor (which checks authentication).
 */
export const userManagementAccessGuard = (): Observable<boolean | UrlTree> => {
    const userManagementService = inject(UserManagementService);
    const messageService = inject(MessageService);
    const router = inject(Router);

    return userManagementService.getAccess().pipe(
        map(access => {
            if (access?.allowed) {
                return true;
            }
            messageService.showError('You are not authorised to maintain users.');
            return router.createUrlTree(['/homepage']);
        }),
        catchError(() => {
            messageService.showError('Could not check your User Management access.');
            return of(router.createUrlTree(['/homepage']));
        })
    );
};
