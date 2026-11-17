import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { toParams } from '../shared/select-option.model';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import { WorkflowStatusReport, WorkflowStatusRow, WorkflowStatusSelection } from './workflow-status.model';

/** REST call of the Workflow Status Report: the portal runs the SAP report (ZPFS_LMS_REPORTS_SRV) for the selection. */
@Injectable({ providedIn: 'root' })
export class WorkflowStatusService {

    private readonly url = environment.primaryApiHost + '/reports/workflow-status';

    constructor(private http: HttpClient) { }

    run(selection: WorkflowStatusSelection): Observable<WorkflowStatusReport> {
        const params = toParams(selection);
        return this.http.get<WorkflowStatusReport>(this.url, { params, context: skipBusyIndicator() });
    }

    /** Excel of the rows and visible columns as shown (sorted / filtered); built by the portal from the rows, no second SAP call. */
    exportExcel(selection: WorkflowStatusSelection, rows: WorkflowStatusRow[], columns: string[] = []): Observable<Blob> {
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
