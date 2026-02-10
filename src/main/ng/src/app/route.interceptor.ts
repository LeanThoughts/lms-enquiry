import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, UrlTree, Router, RouterStateSnapshot } from "@angular/router";
import { map } from "rxjs/operators";
import { AuthService } from "./modules/auth/auth.service";
import { Observable } from "rxjs";

export const routeInterceptor = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {

    const authService = inject(AuthService);
    const router = inject(Router);

    return authService.isAuthenticated().pipe(
        map(isAuthenticated => {
            console.log('Route Interceptor: isAuthenticated:', isAuthenticated);
            if (!isAuthenticated) {
                router.navigate(['/login']);
                return false;
            }
            return true;
        })
    );
}