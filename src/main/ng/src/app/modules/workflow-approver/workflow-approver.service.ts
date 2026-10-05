import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { forkJoin, map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

// Process names the workflow service starts approvals for
const PROCESSES: { name: string; label: string }[] = [
    { name: 'EnquiryAction', label: 'Process Enquiry' },
    { name: 'ICCApproval', label: 'ICC In-Principle Approval' },
    { name: 'Prelim Risk Assessment', label: 'Prelim Risk Assessment' },
    { name: 'Prelim Risk Notification', label: 'Prelim Risk Notification' },
    { name: 'ApplicationFee', label: 'Application Fee' },
    { name: 'Appraisal', label: 'Appraisal' },
    { name: 'BoardApproval', label: 'Board Approval' },
    { name: 'Sanction', label: 'Sanction' },
    { name: 'Monitoring', label: 'Monitoring' },
    { name: 'BusinessPartner', label: 'Business Partner' },
    { name: 'ReferenceInterestRateValue', label: 'Reference Interest Rate' },
];

@Injectable({
  providedIn: 'root'
})
export class WorkflowApproverService implements Resolve<any> {

    departments: any[] = [];
    processes: { name: string; label: string }[] = [];

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Resolve the departments and processes used by the filters and the update dialog. Process names already maintained
     * in the database are kept selectable even if they are not in the known list.
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return forkJoin({
            departments: this.getDepartments(),
            approvers: this.http.get<any[]>(environment.primaryApiHost + '/workflowApprovers/list')
        }).pipe(
            map(({ departments, approvers }) => {
                this.departments = departments;
                const unknownProcessNames = [...new Set((approvers || []).map((approver) => approver.processName))]
                    .filter((name) => name && !PROCESSES.some((process) => process.name === name));
                this.processes = [...PROCESSES, ...unknownProcessNames.map((name) => ({ name, label: name }))];
                return {
                    departmentCode: this.departments,
                    processName: this.processes
                };
            })
        );
    }

    /**
     * Get departments
     */
    getDepartments(): Observable<any[]> {
        return this.http.get<any[]>(environment.primaryApiHost + '/departments').pipe(
            map((departments) => (departments || [])
                .map((department: any) => ({ ...department, description: `${department.value} (${department.code})` }))
                .sort((a: any, b: any) => (a.value || '').localeCompare(b.value || '')))
        );
    }

    /**
     * Get workflow approvers, filtered by department and/or process. The filter is passed as 'departmentCode|processName',
     * either part may be empty.
     */
    public getWorkflowApprovers(filter: string): Observable<any[]> {
        const [departmentCode = '', processName = ''] = (filter || '').split('|');
        let params = new HttpParams();
        if (departmentCode) params = params.set('departmentCode', departmentCode);
        if (processName) params = params.set('processName', processName);
        return this.http.get<any[]>(environment.primaryApiHost + '/workflowApprovers/list', { params }).pipe(
            map((approvers) => (approvers || []).map((approver) => ({
                ...approver,
                departmentName: this.departments.find((department) => department.code === approver.departmentCode)?.value ?? '',
                processLabel: this.processes.find((process) => process.name === approver.processName)?.label ?? approver.processName
            })))
        );
    }

    /**
     * Create workflow approver
     */
    public createWorkflowApprover(workflowApprover: any): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/workflowApprovers/create', workflowApprover);
    }

    /**
     * Update workflow approver
     */
    public updateWorkflowApprover(workflowApprover: any): Observable<any> {
        return this.http.put(environment.primaryApiHost + '/workflowApprovers/update', workflowApprover);
    }

    /**
     * Delete workflow approver
     */
    public deleteWorkflowApprover(workflowApproverId: number): Observable<any> {
        return this.http.delete(environment.primaryApiHost + '/workflowApprovers/delete/' + workflowApproverId);
    }
}
