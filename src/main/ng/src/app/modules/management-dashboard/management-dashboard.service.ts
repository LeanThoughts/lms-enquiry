import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { skipBusyIndicator } from '../../busy-indicator.service';
import { CardView, ManagementDashboard } from './management-dashboard.model';

const VIEW_STORAGE_KEY = 'management-dashboard.views';

/**
 * Reads the Management Dashboard from the portal API, which fetches it from SAP ECC (ZPFS_LMS_DASHBOARD_SRV) on
 * every call, and remembers each card's graph / table choice in the browser.
 */
@Injectable({
    providedIn: 'root'
})
export class ManagementDashboardService {

    constructor(private http: HttpClient) {
    }

    /**
     * The dashboard of the run date (yyyy-MM-dd) with the amounts in Rs crore or rupees. Errors are passed on with
     * the API's message (404: no snapshot for the date, 502: SAP not reachable) so the screen can show it.
     */
    getDashboard(runDate: string, amountInCrore: boolean): Observable<ManagementDashboard> {
        const params = new HttpParams().set('runDate', runDate).set('amountInCrore', String(amountInCrore));
        return this.http.get<ManagementDashboard>(environment.primaryApiHost + '/management-dashboard',
            { params, context: skipBusyIndicator() }).pipe(
            catchError((error: HttpErrorResponse) => throwError(() => new Error(messageOf(error))))
        );
    }

    /** The remembered view of a card, graph when nothing is remembered. */
    cardView(cardId: string): CardView {
        return this.views()[cardId] === 'table' ? 'table' : 'graph';
    }

    /** Remembers the view of a card for this browser. */
    setCardView(cardId: string, view: CardView): void {
        const views = this.views();
        views[cardId] = view;
        try {
            localStorage.setItem(VIEW_STORAGE_KEY, JSON.stringify(views));
        } catch {
            // private mode or storage disabled: the choice lasts for the page only
        }
    }

    private views(): Record<string, CardView> {
        try {
            return JSON.parse(localStorage.getItem(VIEW_STORAGE_KEY) || '{}') || {};
        } catch {
            return {};
        }
    }
}

/** The message to show for a failed dashboard call: the API's message when it sent one. */
function messageOf(error: HttpErrorResponse): string {
    const apiMessage = error.error?.message || error.error?.error || (typeof error.error === 'string' ? error.error : '');
    if (error.status === 0) {
        return 'The portal server could not be reached.';
    }
    if (apiMessage) {
        return apiMessage;
    }
    return error.status === 404 ? 'No dashboard data was found for the chosen run date.'
        : `The dashboard could not be loaded (HTTP ${error.status}).`;
}
