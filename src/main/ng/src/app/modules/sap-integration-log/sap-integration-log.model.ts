/** SAP Integration Log report (Administration): every HTTP request the portal sent to SAP. */

export interface SapLogRow {
    id: number;
    requestedAt: string;
    durationMs: number | null;
    httpMethod: string;
    url: string;
    serviceName: string | null;
    entitySet: string | null;
    responseStatus: number | null;
    responseStatusText: string | null;
    success: boolean;
    errorMessage: string | null;
    sapTransactionId: string | null;
    businessProcess: string | null;
    subProcess: string | null;
    businessObjectId: string | null;
    pointerId: number | null;
    caller: string | null;
    userName: string | null;
    requestSize: number | null;
    responseSize: number | null;
}

export interface SapLogDetail extends SapLogRow {
    host: string | null;
    requestHeaders: string | null;
    requestContentType: string | null;
    requestBody: string | null;
    responseContentType: string | null;
    responseBody: string | null;
    threadName: string | null;
}

export interface SapLogCount {
    key: string;
    count: number;
}

export interface SapLogSummary {
    total: number;
    failed: number;
    succeeded: number;
    averageDurationMs: number;
    maxDurationMs: number;
    lastCallAt: string | null;
    lastFailureAt: string | null;
    byMethod: SapLogCount[];
    byStatus: SapLogCount[];
    byEntitySet: SapLogCount[];
}

export interface SapLogReportPage {
    rows: SapLogRow[];
    summary: SapLogSummary;
    page: number;
    size: number;
    total: number;
    businessProcesses: string[];
    serviceNames: string[];
    generatedOn: string;
    canView: boolean;
    canPurge: boolean;
    loggingEnabled: boolean;
    retentionDays: number;
    userRole: string | null;
}

export interface SapLogFilter {
    from: string;
    to: string;
    method: string;
    outcome: '' | 'success' | 'failed';
    businessProcess: string;
    serviceName: string;
    search: string;
}

export interface SapLogPurgeResult {
    deleted: number;
    olderThanDays: number;
}

export const SAP_LOG_PAGE_SIZES = [25, 50, 100, 200];

export const EMPTY_SAP_LOG_FILTER: SapLogFilter = {
    from: '', to: '', method: '', outcome: '', businessProcess: '', serviceName: '', search: ''
};

/** The path of an SAP URL without host and query string, e.g. /sap/opu/odata/sap/…/DocumentSet(ZidNo='…'). */
export function urlPath(url: string | null | undefined): string {
    if (!url) {
        return '';
    }
    const noQuery = url.split('?')[0];
    const match = /^[a-z]+:\/\/[^/]+(\/.*)$/i.exec(noQuery);
    return match ? match[1] : noQuery;
}

/** Pretty-prints JSON text; anything else is returned as it is. */
export function prettyBody(text: string | null | undefined): string {
    if (!text) {
        return '';
    }
    const trimmed = text.trim();
    if (!(trimmed.startsWith('{') || trimmed.startsWith('['))) {
        return text;
    }
    try {
        return JSON.stringify(JSON.parse(trimmed), null, 2);
    } catch {
        return text;
    }
}

/** Bytes as a short human size. */
export function formatSize(bytes: number | null | undefined): string {
    if (bytes === null || bytes === undefined) {
        return '';
    }
    if (bytes < 1024) {
        return `${bytes} B`;
    }
    if (bytes < 1024 * 1024) {
        return `${(bytes / 1024).toFixed(1)} KB`;
    }
    return `${(bytes / 1024 / 1024).toFixed(2)} MB`;
}
