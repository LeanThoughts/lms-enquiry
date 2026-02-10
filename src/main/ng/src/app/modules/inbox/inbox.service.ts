import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { forkJoin, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InboxService implements Resolve<any> {

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {         
    }

    /**
     * Resolve
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            tasks: this.getTasks()
        });
    }

    /**
     * Get tasks
     */
    getTasks(): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/tasklist');
    }

    /**
     * Approve task
     */
    approveTask(workFlowProcessRequestResource: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/approvetask', workFlowProcessRequestResource);
    }
    
    /**
     * Reject task
     */
    rejectTask(workFlowProcessRequestResource: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/rejecttask', workFlowProcessRequestResource);
    }    
}
