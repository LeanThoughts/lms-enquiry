import { Component } from '@angular/core';
import {
    BarModule,
    ButtonComponent,
    DialogModule,
    DialogRef,
    IconComponent,
    TitleComponent
} from '@fundamental-ngx/core';
import { SapLogDetail, formatSize, prettyBody, urlPath } from '../sap-integration-log.model';

/** Data passed to {@link SapLogDetailDialogComponent}. */
export interface SapLogDetailDialogData {
    detail: SapLogDetail;
}

interface DetailRow {
    label: string;
    value: string;
    code?: boolean;
}

/**
 * One SAP call of the integration log: what was sent (method, URL, headers, body) and what came back (status,
 * body, error, SAP transaction id), with the business context. Read-only; bodies can be copied.
 */
@Component({
    selector: 'app-sap-log-detail-dialog',
    template: `
        <fd-dialog>
            <fd-dialog-header>
                <h1 fd-title class="fd-dialog-header-title">
                    <fd-icon [glyph]="detail.success ? 'accept' : 'error'"
                        [class.sl-ok]="detail.success" [class.sl-bad]="!detail.success"></fd-icon>
                    SAP Call {{ detail.id }} · {{ detail.httpMethod }} {{ detail.entitySet || detail.serviceName || '' }}
                </h1>
            </fd-dialog-header>
            <fd-dialog-body>
                <dl class="sl-list">
                    @for (row of rows; track row.label) {
                        <dt class="sl-label">{{ row.label }}</dt>
                        <dd class="sl-value" [class.sl-value--code]="row.code" [class.sl-value--empty]="!row.value">{{ row.value || '–' }}</dd>
                    }
                </dl>

                @if (detail.errorMessage) {
                    <div class="sl-error" role="alert">
                        <fd-icon glyph="message-error"></fd-icon>
                        <span>{{ detail.errorMessage }}</span>
                    </div>
                }

                <section class="sl-section">
                    <div class="sl-section__head">
                        <h3 class="sl-section__title">Request</h3>
                        <span class="sl-section__meta">{{ detail.requestContentType || '' }} {{ formatSize(detail.requestSize) }}</span>
                        <button fd-button fdType="transparent" glyph="copy" label="Copy" [disabled]="!detail.requestBody"
                            (click)="copy(detail.requestBody)"></button>
                    </div>
                    <pre class="sl-pre sl-pre--headers">{{ detail.requestHeaders || '(no headers recorded)' }}</pre>
                    <pre class="sl-pre">{{ requestBody || '(no body)' }}</pre>
                </section>

                <section class="sl-section">
                    <div class="sl-section__head">
                        <h3 class="sl-section__title">Response</h3>
                        <span class="sl-section__meta">
                            HTTP {{ detail.responseStatus || '—' }} {{ detail.responseStatusText || '' }} ·
                            {{ detail.responseContentType || '' }} {{ formatSize(detail.responseSize) }}
                        </span>
                        <button fd-button fdType="transparent" glyph="copy" label="Copy" [disabled]="!detail.responseBody"
                            (click)="copy(detail.responseBody)"></button>
                    </div>
                    <pre class="sl-pre">{{ responseBody || (detail.responseStatus ? '(empty body)' : '(no response: ' + (detail.errorMessage || 'transport failure') + ')') }}</pre>
                </section>
            </fd-dialog-body>
            <fd-dialog-footer>
                <fd-button-bar fdType="emphasized" label="Close" (click)="dialogRef.close()"></fd-button-bar>
            </fd-dialog-footer>
        </fd-dialog>
    `,
    styles: [`
        :host { display: block; }
        .sl-ok { color: var(--sapPositiveColor, #107e3e); }
        .sl-bad { color: var(--sapNegativeColor, #bb0000); }
        .sl-list {
            display: grid;
            grid-template-columns: max-content minmax(16rem, 1fr);
            gap: 0.375rem 1.5rem;
            margin: 0 0 0.75rem;
        }
        .sl-label { font-weight: 600; white-space: nowrap; }
        .sl-value { margin: 0; word-break: break-all; }
        .sl-value--code { font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace; font-size: 0.8125rem; }
        .sl-value--empty { opacity: 0.6; }
        .sl-error {
            display: flex; gap: 0.5rem; align-items: flex-start;
            margin: 0 0 0.75rem; padding: 0.5rem 0.75rem;
            border: 1px solid var(--sapNegativeColor, #bb0000); border-radius: 0.25rem;
            color: var(--sapNegativeTextColor, #bb0000); background: var(--sapErrorBackground, #ffebeb);
            word-break: break-word;
        }
        .sl-section { margin-top: 0.75rem; }
        .sl-section__head { display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.25rem; }
        .sl-section__title { margin: 0; font-size: 1rem; }
        .sl-section__meta { flex: 1; font-size: 0.75rem; color: var(--sapContent_LabelColor); }
        .sl-pre {
            margin: 0 0 0.5rem; padding: 0.5rem 0.75rem; max-height: 22rem; overflow: auto;
            font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace; font-size: 0.75rem; line-height: 1.4;
            white-space: pre-wrap; word-break: break-all;
            border: 1px solid var(--sapList_BorderColor); border-radius: 0.25rem; background: var(--sapList_Background);
        }
        .sl-pre--headers { max-height: 8rem; color: var(--sapContent_LabelColor); }
    `],
    imports: [DialogModule, BarModule, IconComponent, TitleComponent, ButtonComponent]
})
export class SapLogDetailDialogComponent {

    readonly detail: SapLogDetail;
    readonly rows: DetailRow[];
    readonly requestBody: string;
    readonly responseBody: string;
    readonly formatSize = formatSize;

    constructor(public dialogRef: DialogRef) {
        this.detail = (dialogRef.data as SapLogDetailDialogData).detail;
        const d = this.detail;
        this.requestBody = prettyBody(d.requestBody);
        this.responseBody = prettyBody(d.responseBody);
        this.rows = [
            { label: 'Requested at', value: d.requestedAt },
            { label: 'Duration', value: d.durationMs === null ? '' : `${d.durationMs} ms` },
            { label: 'URL', value: d.url, code: true },
            { label: 'Host', value: d.host ?? '' },
            { label: 'Service / entity set', value: [d.serviceName, d.entitySet].filter(Boolean).join(' / ') },
            { label: 'Outcome', value: d.success ? 'Success' : 'Failed' },
            { label: 'SAP transaction id', value: d.sapTransactionId ?? '', code: true },
            { label: 'Business process', value: [d.businessProcess, d.subProcess].filter(Boolean).join(' · ') },
            { label: 'Business object id', value: d.businessObjectId ?? '', code: true },
            { label: 'Integration pointer', value: d.pointerId === null ? '' : String(d.pointerId) },
            { label: 'Caller', value: d.caller ?? '', code: true },
            { label: 'User / thread', value: [d.userName, d.threadName].filter(Boolean).join(' · ') }
        ];
    }

    copy(text: string | null): void {
        if (text && navigator.clipboard) {
            navigator.clipboard.writeText(text).catch(() => undefined);
        }
    }

    /** Exposed for the template. */
    urlPath(url: string): string {
        return urlPath(url);
    }
}
