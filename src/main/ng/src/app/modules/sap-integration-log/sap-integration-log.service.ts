import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { skipBusyIndicator } from '../../busy-indicator.service';
import { SapLogDetail, SapLogFilter, SapLogPurgeResult, SapLogReportPage } from './sap-integration-log.model';

/** REST calls of the SAP Integration Log report (Administration). */
@Injectable({ providedIn: 'root' })
export class SapIntegrationLogService {

    private readonly baseUrl = environment.primaryApiHost + '/administration/sap-http-log';

    constructor(private http: HttpClient) { }

    private params(filter: SapLogFilter): HttpParams {
        let params = new HttpParams();
        if (filter.from) { params = params.set('from', filter.from); }
        if (filter.to) { params = params.set('to', filter.to); }
        if (filter.method) { params = params.set('method', filter.method); }
        if (filter.outcome) { params = params.set('outcome', filter.outcome); }
        if (filter.businessProcess) { params = params.set('businessProcess', filter.businessProcess); }
        if (filter.serviceName) { params = params.set('serviceName', filter.serviceName); }
        if (filter.search.trim()) { params = params.set('search', filter.search.trim()); }
        return params;
    }

    getReport(filter: SapLogFilter, page: number, size: number): Observable<SapLogReportPage> {
        const params = this.params(filter).set('page', page).set('size', size);
        return this.http.get<SapLogReportPage>(this.baseUrl, { params, context: skipBusyIndicator() });
    }

    getDetail(id: number): Observable<SapLogDetail> {
        return this.http.get<SapLogDetail>(`${this.baseUrl}/${id}`, { context: skipBusyIndicator() });
    }

    exportCsv(filter: SapLogFilter): Observable<Blob> {
        return this.http.get(this.baseUrl + '/export', {
            params: this.params(filter), responseType: 'blob', context: skipBusyIndicator()
        });
    }

    purge(olderThanDays: number): Observable<SapLogPurgeResult> {
        const params = new HttpParams().set('olderThanDays', olderThanDays);
        return this.http.delete<SapLogPurgeResult>(this.baseUrl, { params, context: skipBusyIndicator() });
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
