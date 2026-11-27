import { HttpClient, HttpContext, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, defer } from 'rxjs';
import { map } from 'rxjs/operators';

/**
 * How long a report run took, in milliseconds: {@code totalMs} as measured in the browser (request to answer), and the
 * server's own figures from the response headers (X-Report-Server-Ms / X-Report-Sap-Ms / X-Report-Sap-Calls), which the
 * backend adds to every report under /reports. The difference total − server is network and transfer time.
 */
export interface ReportTiming {
    totalMs: number;
    serverMs?: number;
    sapMs?: number;
    sapCalls?: number;
}

/** A report answer with its run time attached. */
export type Timed<T> = T & { timing: ReportTiming };

function headerNumber(response: HttpResponse<unknown>, name: string): number | undefined {
    const value = response.headers.get(name);
    if (value === null || value.trim() === '') {
        return undefined;
    }
    const n = Number(value);
    return Number.isFinite(n) ? n : undefined;
}

/** The timing of an answer, from the browser's stopwatch and the server headers. */
export function timingOf(response: HttpResponse<unknown>, startedAt: number, now: number = performance.now()): ReportTiming {
    return {
        totalMs: Math.max(0, Math.round(now - startedAt)),
        serverMs: headerNumber(response, 'X-Report-Server-Ms'),
        sapMs: headerNumber(response, 'X-Report-Sap-Ms'),
        sapCalls: headerNumber(response, 'X-Report-Sap-Calls')
    };
}

/**
 * GET of a report run with its timing: use this in every report service's {@code run()} so the page can show the run
 * time next to the record count. The stopwatch starts on subscribe.
 */
export function timedGet<T extends object>(http: HttpClient, url: string,
                                           options: { params?: HttpParams; context?: HttpContext } = {}): Observable<Timed<T>> {
    return defer(() => {
        const startedAt = performance.now();
        return http.get<T>(url, { ...options, observe: 'response' }).pipe(
            map(response => ({ ...(response.body as T), timing: timingOf(response, startedAt) }))
        );
    });
}

/** "1 record", "1,234 records", "12 of 1,234 records" (filtered). */
export function recordCountText(shown: number, total: number): string {
    const fmt = (n: number) => n.toLocaleString('en-IN');
    if (shown === total) {
        return `${fmt(total)} ${total === 1 ? 'record' : 'records'}`;
    }
    return `${fmt(shown)} of ${fmt(total)} records`;
}

/** 850 ms, 2.41 s, 1 min 05 s */
export function formatDuration(ms: number | undefined): string {
    if (ms === undefined || ms === null || !Number.isFinite(ms)) {
        return '–';
    }
    if (ms < 1000) {
        return `${Math.round(ms)} ms`;
    }
    if (ms < 60000) {
        return `${(ms / 1000).toFixed(ms < 10000 ? 2 : 1)} s`;
    }
    const minutes = Math.floor(ms / 60000);
    const seconds = Math.round((ms % 60000) / 1000);
    return `${minutes} min ${seconds.toString().padStart(2, '0')} s`;
}
