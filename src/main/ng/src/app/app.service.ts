import {CanActivate, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router/src/router_state';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {UserModel} from './main/content/model/user.model';

@Injectable()
export class AppService implements CanActivate {

    /**
     * Authorization.
     */
    authorization: any;

    /**
     * Currently logged in user.
     */
    currentUser: UserModel;
    
    /**
     * constructor()
     * @param _http
     * @param _router
     */
    constructor(private _http: HttpClient, private _router: Router) {
    }

    /**
     * canActivate()
     * @param route
     * @param state
     */
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> |
        Promise<boolean> {

        return new Observable<boolean>((observer) => {
            this._http.get<UserModel>('enquiry/api/me').subscribe(response => {
                this.currentUser = response;
                if (this.currentUser.role === 'TR0100') {
                    this._router.navigate(['enquiryApplication']);
                    observer.next(false);
                }
                else if (this.currentUser.role === 'ZLM023' || this.currentUser.role === 'ZLM024' || this.currentUser.role === 'ZLM040') {
                    this._router.navigate(['inbox']);
                    observer.next(false);
                }
                else {
                    // this._router.navigate(['enquiryAlerts']);
                    observer.next(true);
                }
            });
        });
    }

    me(): Observable<UserModel> {
        return this._http.get<UserModel>('enquiry/api/me');
    }

    getUserMenu(): Observable<any> {
        return this._http.get<any>('enquiry/api/menu?userRole=' + this.currentUser.role);
    }

    getAuthorization(): Observable<any> {
        return this._http.get<any>('enquiry/api/authorization?userRole=' + this.currentUser.role);
    }
}
