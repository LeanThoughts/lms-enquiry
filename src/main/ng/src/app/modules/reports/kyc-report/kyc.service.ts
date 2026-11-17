import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { toParams } from '../shared/select-option.model';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { KycReport, KycRow, KycSelection } from './kyc.model';

/** REST call of the Loans KYC Report: the portal runs the SAP report (ZPFS_LMS_REPORTS_SRV · KycReportSet). */
@Injectable({ providedIn: 'root' })
export class KycService {

    private readonly url = environment.primaryApiHost + '/reports/kyc';

    constructor(private http: HttpClient) { }

    run(selection: KycSelection): Observable<KycReport> {
        const params = toParams(selection, ['companyCode']);
        return this.http.get<KycReport>(this.url, { params, context: skipBusyIndicator() });
    }

    /** Excel of the rows and visible columns as shown (sorted / filtered); no second SAP call. */
    exportExcel(selection: KycSelection, rows: KycRow[], columns: string[] = []): Observable<Blob> {
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
