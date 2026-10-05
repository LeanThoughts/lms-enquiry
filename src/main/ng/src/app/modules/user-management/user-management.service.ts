import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    Department,
    User,
    UserManagementAccess,
    UserManagementResolvedData,
    UserRequest,
    UserRole,
    UserSearchCriteria
} from './user-management.model';

/**
 * HTTP access to /api/usermanagement and route resolver for the User Management module.
 */
@Injectable({
    providedIn: 'root'
})
export class UserManagementService implements Resolve<UserManagementResolvedData> {

    private readonly baseUrl = environment.primaryApiHost + '/usermanagement';

    /** Last search criteria, so the list keeps its filters when the user comes back from the detail page. */
    readonly searchCriteria$ = new BehaviorSubject<UserSearchCriteria>({ query: '', role: null, riskDepartment: null, status: 'all' });

    constructor(private http: HttpClient) {}

    /**
     * Resolve the master data (roles and departments) every screen needs
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserManagementResolvedData> {
        return forkJoin({
            roles: this.getRoles(),
            departments: this.getDepartments()
        });
    }

    getAccess(): Observable<UserManagementAccess> {
        return this.http.get<UserManagementAccess>(this.baseUrl + '/access');
    }

    searchUsers(criteria: UserSearchCriteria): Observable<User[]> {
        let params = new HttpParams();
        if (criteria.query?.trim()) {
            params = params.set('query', criteria.query.trim());
        }
        if (criteria.role) {
            params = params.set('role', criteria.role);
        }
        if (criteria.riskDepartment) {
            params = params.set('riskDepartment', criteria.riskDepartment);
        }
        if (criteria.status !== 'all') {
            params = params.set('status', String(criteria.status === 'active'));
        }
        return this.http.get<User[]>(this.baseUrl + '/users', { params });
    }

    getUser(id: string): Observable<User> {
        return this.http.get<User>(`${this.baseUrl}/users/${id}`);
    }

    createUser(request: UserRequest): Observable<User> {
        return this.http.post<User>(this.baseUrl + '/users', request);
    }

    updateUser(id: string, request: UserRequest): Observable<User> {
        return this.http.put<User>(`${this.baseUrl}/users/${id}`, request);
    }

    /** Activate (true) or deactivate (false). Users are never deleted. */
    setUserStatus(id: string, active: boolean): Observable<User> {
        return this.http.patch<User>(`${this.baseUrl}/users/${id}/status`, null, {
            params: new HttpParams().set('active', String(active))
        });
    }

    getRoles(): Observable<UserRole[]> {
        return this.http.get<UserRole[]>(this.baseUrl + '/roles');
    }

    getDepartments(): Observable<Department[]> {
        return this.http.get<Department[]>(this.baseUrl + '/departments');
    }

    /**
     * Readable message from an API error (LmsException body, Bean Validation errors or plain status)
     */
    errorMessage(error: unknown, fallback: string): string {
        if (error instanceof HttpErrorResponse) {
            const body: any = error.error;
            if (body?.message) {
                return body.message;
            }
            if (Array.isArray(body?.errors) && body.errors.length) {
                return body.errors.map((e: any) => e.defaultMessage ?? e.message).filter(Boolean).join(' ');
            }
            if (error.status === 403) {
                return 'You are not authorised to maintain users.';
            }
        }
        return fallback;
    }
}
