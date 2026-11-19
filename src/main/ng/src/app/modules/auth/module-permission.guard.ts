import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';
import { MessageService } from '../../message.service';
import { AuthService } from './auth.service';

/**
 * Runs after routeInterceptor, which has already loaded module permissions from /api/me.
 * The route's data.modulePermission must be one of those permissions.
 */
export const modulePermissionGuard = (route: ActivatedRouteSnapshot): boolean | UrlTree => {
    const authService = inject(AuthService);
    const permission = route.data['modulePermission'] as string | undefined;
    const anyPermission = route.data['modulePermissions'] as string[] | undefined;
    if (permission && authService.hasModulePermission(permission)) {
        return true;
    }
    if (anyPermission?.some(item => authService.hasModulePermission(item))) {
        return true;
    }
    inject(MessageService).showError('You are not authorised to open this page.');
    return inject(Router).createUrlTree(['/homepage']);
};
