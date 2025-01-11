import { Observable, BehaviorSubject, forkJoin } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserModel } from '../../../model/user.model';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { UserRole } from "../../../model/userRole.model";
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { debug } from 'util';

@Injectable()
export class UserService implements Resolve<any>
{
    public users: BehaviorSubject<UserModel[]> = new BehaviorSubject([new UserModel({})]);

    public usersCast = this.users.asObservable();

    public userRoles: Observable<Array<any>>;

    userId: string;

    selectedUser: BehaviorSubject<UserModel> = new BehaviorSubject(new UserModel({}));

    user: BehaviorSubject<UserModel>;

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient) {
        //this.users= new BehaviorSubject([]) ;
    }

    /**
     * resolve()
     * Router resolveer, fetches data before the ui is created.
     * @param route
     * @param state
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return forkJoin([
            this.getUserRoles() // Get User Roles

        ]);
    }

    /**
     * createUser()
     * Creates a new user.
     * @param user
     */
    public createUser(user: UserModel): Observable<any> {
        return this._http.post('enquiry/api/user', user);
    }

    /**
     * updateUser()
     * Creates a new user.
     * @param user
     */
    public updateUser(user: UserModel): Observable<any> {
        return this._http.put('enquiry/api/user', user).catch(this.errorHandler);
    }

    /**
     * errorHandler()
     * @param error 
     */
    errorHandler(error: HttpErrorResponse): Observable<any> {
        return Observable.throw(error.error.message || 'Service error occured.');
    }

    /**
     * getUsers()
     * Fetches a list of users.
     */
    public getUsers(): void {
        this._http.get<any>('enquiry/api/users?sort=role&sort=firstName').subscribe((response) => {
            const users = new Array<UserModel>();
            response._embedded.users.map((user) => {
                users.push(new UserModel(user));
            });
            if (this.users === undefined) {
                this.users = new BehaviorSubject(users);
            }
            else {
                this.users.next(users);
            }
        });
    }


    /*
        Search Users
     */
    public searchUsers(request: Array<string>): Observable<UserModel[]> {

        let queryParams;

        request.forEach(function (value) {
            if (value != undefined) {
                queryParams = queryParams + value + "&";
            }
        });


        return new Observable(observer => {
            const users = new Array<UserModel>();
            this._http.get<UserModel[]>('enquiry/api/users/queryParams?query=' + request).subscribe(result => {
                result.map(userModel => {
                    users.push(new UserModel(userModel));
                });
                observer.next(users);
                observer.complete();
            });
        });
    }


    /*
      Get user by Email Id
     */

    /**
     * getUserByEmail()
     * @param email
     */
    public getUserByEmail(email: string): Observable<UserModel> {
        return this._http.get<UserModel>('enquiry/api/user?userId=' + email);
    }

    /**
     * getUserRoles()
      */
    public getUserRoles(): Observable<any> {
        return this._http.get<any>('enquiry/api/userRoles?size=200');
    }

    /**
     * getUserDepartments()
     * Fetches a list of departments.
     */
    public getUserDepartments(): Observable<any> {
        return this._http.get<any>('enquiry/api/departments');
    }
}