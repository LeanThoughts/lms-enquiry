import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { ReportVariant, VariantRequest } from './report-variant.model';

/** REST calls of the report variants: /reports/{report}/variants (own and shared variants of the signed-in user). */
@Injectable({ providedIn: 'root' })
export class ReportVariantService {

    constructor(private http: HttpClient) { }

    private url(report: string): string {
        return `${environment.primaryApiHost}/reports/${report}/variants`;
    }

    list(report: string): Observable<ReportVariant[]> {
        return this.http.get<ReportVariant[]>(this.url(report), { context: skipBusyIndicator() });
    }

    /** Saves the variant; a variant of the user with the same name is overwritten. */
    save(report: string, request: VariantRequest): Observable<ReportVariant> {
        return this.http.post<ReportVariant>(this.url(report), request, { context: skipBusyIndicator() });
    }

    delete(report: string, id: string): Observable<void> {
        return this.http.delete<void>(`${this.url(report)}/${id}`, { context: skipBusyIndicator() });
    }

    errorMessage(error: unknown, fallback: string): string {
        const e = error as { error?: { message?: string }, status?: number };
        if (e?.error && typeof e.error.message === 'string' && e.error.message) {
            return e.error.message;
        }
        return e?.status === 0 ? 'The server cannot be reached. Please try again.' : fallback;
    }
}
