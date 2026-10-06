import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { BpRow, BpTablePage } from './bp-table.model';

/** REST calls of the Configuration apps of the business partner configuration tables. */
@Injectable({ providedIn: 'root' })
export class BpTableService {

    private readonly baseUrl = environment.primaryApiHost + '/configuration/bp-tables';

    constructor(private http: HttpClient) { }

    getPage(table: string, page: number, size: number, search: string): Observable<BpTablePage> {
        let params = new HttpParams().set('page', page).set('size', size);
        if (search.trim()) {
            params = params.set('search', search.trim());
        }
        return this.http.get<BpTablePage>(`${this.baseUrl}/${encodeURIComponent(table)}`,
            { params, context: skipBusyIndicator() });
    }

    create(table: string, values: Record<string, unknown>): Observable<BpRow> {
        return this.http.post<BpRow>(`${this.baseUrl}/${encodeURIComponent(table)}/rows`, { values },
            { context: skipBusyIndicator() });
    }

    update(table: string, id: string, values: Record<string, unknown>): Observable<BpRow> {
        return this.http.put<BpRow>(`${this.baseUrl}/${encodeURIComponent(table)}/rows`, { values },
            { params: new HttpParams().set('id', id), context: skipBusyIndicator() });
    }

    delete(table: string, id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(table)}/rows`,
            { params: new HttpParams().set('id', id), context: skipBusyIndicator() });
    }

    errorMessage(error: unknown, fallback: string): string {
        if (error instanceof HttpErrorResponse) {
            const body = error.error;
            if (body && typeof body === 'object' && typeof body.message === 'string' && body.message) {
                return body.message;
            }
            if (error.status === 0) {
                return 'The server cannot be reached. Please try again.';
            }
        }
        return fallback;
    }
}
