import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { FuseNavigationService } from '@fuse/components/navigation/navigation.service';
import { HttpClient } from '@angular/common/http';
import { UserModel } from './main/content/model/user.model';

@Injectable()
export class EnquiryApplicationRouteGuard implements CanActivate {

    currentUser: UserModel;

    constructor(private _appService: FuseNavigationService, private _httpClient: HttpClient, private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {
        return new Observable<boolean>(observer => {
            this._httpClient.get<UserModel>('enquiry/api/me').subscribe((response) => {
                this.currentUser = response;
                console.log(this.currentUser);
                if (this.currentUser.passwordReset) {
                    this._router.navigate(['forceChangePassword']);
                    observer.next(false);
                }
                else {
                    if (this.currentUser.role === 'TR0100' || this.currentUser.role === 'ZLM023' || this.currentUser.role === 'ZLM013' 
                            || this.currentUser.role === 'ZLM024' || this.currentUser.role === 'ZLM040' || this.currentUser.role === 'ZLM014') {
                        console.log('enquiry route guard - true');
                        observer.next(true);
                    }
                    else {
                        this._router.navigate(['enquiryAlerts']);
                        observer.next(false);
                    }
                }
            });
        });
    }
}
