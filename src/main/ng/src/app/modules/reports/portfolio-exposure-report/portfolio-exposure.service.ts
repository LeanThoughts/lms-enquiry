import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { toParams } from '../shared/select-option.model';
import { timedGet } from '../shared/report-timing';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { PortfolioExposureColumn, PortfolioExposureReport, PortfolioExposureRow, PortfolioExposureSelection } from './portfolio-exposure.model';

/** REST calls of the Portfolio Exposure Report: the portal runs the SAP report (ZPFS_LMS_REPORTS_SRV · PortfolioExposureReportSet). */
@Injectable({ providedIn: 'root' })
export class PortfolioExposureService {

    private readonly url = environment.primaryApiHost + '/reports/portfolio-exposure';

    constructor(private http: HttpClient) { }

    run(selection: PortfolioExposureSelection): Observable<PortfolioExposureReport> {
        const params = toParams(selection);
        return timedGet<PortfolioExposureReport>(this.http, this.url, { params, context: skipBusyIndicator() });
    }

    /** Excel of the rows and visible columns as shown (sorted / filtered); no second SAP call. */
    exportExcel(selection: PortfolioExposureSelection, rows: PortfolioExposureRow[], columns: PortfolioExposureColumn[]): Observable<Blob> {
        return this.http.post(this.url + '/export', { selection, rows, columns }, { responseType: 'blob', context: skipBusyIndicator() });
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
