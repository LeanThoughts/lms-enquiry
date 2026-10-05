import { HttpClient, HttpContext } from '@angular/common/http';
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
     * Get loan application by self link
     */
    getLoanApplicationBySelfLink(selfLink: string): Observable<any> {
        return this.http.get(selfLink);
    }

    /**
     * Get loan application by loan contract id (falls back to enquiry number on the backend)
     */
    getLoanApplicationByLoanContractId(loanContractId: string): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/loanApplications/loanContractId/' + loanContractId);
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
    getTasks(context?: HttpContext): Observable<any> {
        return this.http.get(environment.primaryApiHost + '/tasklist', { context });
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
