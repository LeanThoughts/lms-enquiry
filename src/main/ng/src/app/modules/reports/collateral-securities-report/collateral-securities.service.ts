import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { toParams } from '../shared/select-option.model';
import { timedGet } from '../shared/report-timing';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { CollateralSecuritiesColumn, CollateralSecuritiesReport, CollateralSecuritiesRow, CollateralSecuritiesSelection } from './collateral-securities.model';

/** REST calls of the Collateral Securities Report: the portal runs the SAP report (ZPFS_LMS_REPORTS_SRV · CollateralSecurityReportSet). */
@Injectable({ providedIn: 'root' })
export class CollateralSecuritiesService {

    private readonly url = environment.primaryApiHost + '/reports/collateral-securities';

    constructor(private http: HttpClient) { }

    run(selection: CollateralSecuritiesSelection): Observable<CollateralSecuritiesReport> {
        const params = toParams(selection);
        return timedGet<CollateralSecuritiesReport>(this.http, this.url, { params, context: skipBusyIndicator() });
    }

    /** Excel of the rows and visible columns as shown (sorted / filtered); no second SAP call. */
    exportExcel(selection: CollateralSecuritiesSelection, rows: CollateralSecuritiesRow[], columns: CollateralSecuritiesColumn[]): Observable<Blob> {
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
