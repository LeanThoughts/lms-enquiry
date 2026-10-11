import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { skipBusyIndicator } from '../../busy-indicator.service';
import { ApprovalFilter, ApprovalReportPage } from './outstanding-approvals.model';

/** REST calls of the Outstanding Workflow Approvals report (Administration). */
@Injectable({ providedIn: 'root' })
export class OutstandingApprovalsService {

    private readonly baseUrl = environment.primaryApiHost + '/administration/outstanding-approvals';

    constructor(private http: HttpClient) { }

    private params(filter: ApprovalFilter): HttpParams {
        let params = new HttpParams();
        if (filter.processName) { params = params.set('processName', filter.processName); }
        if (filter.approver.trim()) { params = params.set('approver', filter.approver.trim()); }
        if (filter.requestor.trim()) { params = params.set('requestor', filter.requestor.trim()); }
        if (filter.search.trim()) { params = params.set('search', filter.search.trim()); }
        if (filter.minAgeDays !== null && filter.minAgeDays >= 0) {
            params = params.set('minAgeDays', filter.minAgeDays);
        }
        return params;
    }

    getReport(filter: ApprovalFilter, page: number, size: number): Observable<ApprovalReportPage> {
        const params = this.params(filter).set('page', page).set('size', size);
        return this.http.get<ApprovalReportPage>(this.baseUrl, { params, context: skipBusyIndicator() });
    }

    exportCsv(filter: ApprovalFilter): Observable<Blob> {
        return this.http.get(this.baseUrl + '/export', {
            params: this.params(filter), responseType: 'blob', context: skipBusyIndicator()
        });
    }

    errorMessage(error: unknown, fallback: string): string {
        const e = error as { error?: { message?: string }, status?: number };
        if (e && e.error && typeof e.error.message === 'string' && e.error.message) {
            return e.error.message;
        }
        if (e && e.status === 0) {
            return 'The server cannot be reached. Please try again.';
        }
        return fallback;
    }
}
