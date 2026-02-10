import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { BehaviorSubject, forkJoin, Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { statesOfIndia } from '../../app.constants';

@Injectable({
  providedIn: 'root'
})
export class LoanContractSearchService implements Resolve<any> {

    selectedEnquiry$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

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
            functionalStatuses: this.getFunctionalStatuses(), // get functional statuses
            technicalStatuses: this.getTechnicalStatuses(), // get technical statuses
            loanClasses: this.getLoanClasses(), // get loan classes
            projectTypes: this.getProjectTypes(), // get project types
            financingTypes: this.getFinancingTypes(), // get financing types
            assistanceTypes: this.getAssistanceTypes() // get assistance types
        });
    }

    /**
     * Get functional statuses
     */
    getFunctionalStatuses(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/functionalStatuses?sort=value').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.functionalStatuses);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/functionalStatuses?sort=value');
    }

    /**
     * Get technical statuses
     */
    getTechnicalStatuses(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/technicalStatuses?sort=value').subscribe({
                    next: (response: any) => {
                        const technicalStatuses = response._embedded.technicalStatuses.sort((a: any, b: any) => a.description.localeCompare(b.description));
                        observer.next(technicalStatuses);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/technicalStatus');
    }

    /**
     * Get loan classes
     */
    getLoanClasses(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/loanClasses?sort=value').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.loanClasses);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/loanClasses?sort=value');
    }

    /**
     * Get project types
     */
    getProjectTypes(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/projectTypes?sort=value').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.projectTypes);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/projectTypes?sort=value');
    }

    /**
     * Get financing types
     */
    getFinancingTypes(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/financingTypes?sort=value').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.financingTypes);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/financingTypes?sort=value');
    }

    /**
     * Get assistance types
     */
    getAssistanceTypes(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/assistanceTypes?sort=value').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.assistanceTypes);
                        observer.complete();
                    },
                    error: (error: any) => {
                        observer.next([]);
                        observer.complete();
                    }
                });
            });    
        }
        return this.http.get(environment.primaryApiHost + '/assistanceTypes?sort=value');
    }

    /**
     * Get states
     */
    getStates(): Observable<any> {
        const states = statesOfIndia.sort((a: any, b: any) => a.value.localeCompare(b.value));
        return of(states);
    }

    /**
     * Get loan types
     */
    public getLoanTypes(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/loanTypes?sort=code&size=500').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.loanTypes);
                        observer.complete();
                    }
                });
            });
        }
        return this.http.get(environment.primaryApiHost + '/loanTypes?sort=code&size=500');
    }

    /**
     * Get project type core sectors
     */
    public getProjectTypeCoreSectors(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/projectTypeCoreSectors?sort=code&size=500').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.projectTypeCoreSectors);
                        observer.complete();
                    }
                });
            });
        }
        return this.http.get(environment.primaryApiHost + '/projectTypeCoreSectors?sort=code&size=500');
    }

    /**
     * Get purpose of loans
     */
    public getPurposeOfLoans(removeEmbedded?: boolean): Observable<any> {
        if (removeEmbedded) {
            return new Observable((observer) => {
                this.http.get(environment.primaryApiHost + '/purposeOfLoans?sort=code&size=500').subscribe({
                    next: (response: any) => {
                        observer.next(response._embedded.purposeOfLoans);
                        observer.complete();
                    }
                });
            });
        }
        return this.http.get(environment.primaryApiHost + '/purposeOfLoans?sort=code&size=500');
    }

    /**
     * Get project capacity units
     */
    public getProjectCapacityUnits(): Observable<any> {
        return new Observable((observer) => {
            this.http.get(environment.primaryApiHost + '/unitOfMeasures').subscribe({
                next: (response: any) => {
                    observer.next(response._embedded.unitOfMeasures);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Get duration units
     */
    public getDurationUnits(): Observable<any> {
        const durationUnits = [
            { code: '1', description: 'Days' },
            { code: '2', description: 'Weeks' },
            { code: '3', description: 'Months' },
            { code: '4', description: 'Years' }
        ];
        return of(durationUnits);
    }

    /**
     * Search loan contracts
     */
    public searchLoanContracts(searchParameters: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/loanApplications/loanContracts/search', searchParameters);
    }
}
