import { HttpContext, HttpContextToken } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';

// Requests that finish within this time do not show the busy indicator, which avoids flicker on quick calls
const SHOW_DELAY_MS = 200;

/**
 * Set on requests whose caller shows its own loading state, e.g. the homepage widgets
 */
export const SKIP_BUSY_INDICATOR = new HttpContextToken<boolean>(() => false);

export function skipBusyIndicator(): HttpContext {
    return new HttpContext().set(SKIP_BUSY_INDICATOR, true);
}

@Injectable({
    providedIn: 'root'
})
export class BusyIndicatorService {

    private pendingRequests = 0;
    private showTimer: ReturnType<typeof setTimeout> | null = null;

    readonly busy = signal(false);

    /**
     * Track the start of an http call
     */
    start(): void {
        this.pendingRequests++;
        if (this.pendingRequests === 1 && !this.showTimer) {
            this.showTimer = setTimeout(() => {
                this.showTimer = null;
                this.busy.set(this.pendingRequests > 0);
            }, SHOW_DELAY_MS);
        }
    }

    /**
     * Track the end of an http call, whether it completed, failed or was cancelled
     */
    stop(): void {
        this.pendingRequests = Math.max(0, this.pendingRequests - 1);
        if (this.pendingRequests === 0) {
            if (this.showTimer) {
                clearTimeout(this.showTimer);
                this.showTimer = null;
            }
            this.busy.set(false);
        }
    }
}
