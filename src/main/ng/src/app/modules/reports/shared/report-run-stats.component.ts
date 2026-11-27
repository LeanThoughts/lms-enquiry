import { Component, OnDestroy, OnInit, computed, input, signal } from '@angular/core';
import { IconComponent } from '@fundamental-ngx/core';
import { ReportTiming, formatDuration } from './report-timing';

/**
 * Run time of a report, shown next to its record count: the total time as the user waited for it, and how it splits
 * into SAP, portal and network. Used as {@code <app-report-run-stats [timing]="report.timing" />}.
 */
@Component({
    selector: 'app-report-run-stats',
    imports: [IconComponent],
    template: `
        @if (timing(); as t) {
            <span class="rs" [attr.title]="tooltip()" data-testid="report-run-time">
                <span class="rs__total"><fd-icon glyph="history" aria-hidden="true"></fd-icon>{{ total() }}</span>
                @if (parts().length) {
                    <span class="rs__parts">({{ parts().join(' · ') }})</span>
                }
            </span>
        }
    `,
    styles: [`
        :host { display: inline-flex; }
        .rs { display: inline-flex; align-items: center; gap: 0.375rem; font-size: var(--sapFontSmallSize, 0.75rem);
              color: var(--sapContent_LabelColor, #6a6d70); white-space: nowrap; }
        .rs__total { display: inline-flex; align-items: center; gap: 0.25rem; padding: 0.125rem 0.5rem; border-radius: 0.75rem;
                     font-family: var(--sapFontBoldFamily, var(--sapFontFamily)); color: var(--sapInformativeTextColor, var(--sapTextColor));
                     background: var(--sapInformationBackground, #e5f0fa); }
        .rs__parts { font-variant-numeric: tabular-nums; }
        @media (max-width: 40rem) { .rs__parts { display: none; } }
    `]
})
export class ReportRunStatsComponent {

    readonly timing = input<ReportTiming | undefined>();

    readonly total = computed(() => formatDuration(this.timing()?.totalMs));

    readonly parts = computed(() => {
        const t = this.timing();
        if (!t) {
            return [];
        }
        const parts: string[] = [];
        if (t.sapMs !== undefined) {
            parts.push(`SAP ${formatDuration(t.sapMs)}`);
        }
        if (t.serverMs !== undefined) {
            const portal = Math.max(0, t.serverMs - (t.sapMs ?? 0));
            parts.push(`portal ${formatDuration(portal)}`);
            parts.push(`network ${formatDuration(Math.max(0, t.totalMs - t.serverMs))}`);
        }
        return parts;
    });

    readonly tooltip = computed(() => {
        const t = this.timing();
        if (!t) {
            return '';
        }
        const lines = [`Total run time: ${formatDuration(t.totalMs)}`];
        if (t.sapMs !== undefined) {
            lines.push(`SAP gateway: ${formatDuration(t.sapMs)}${t.sapCalls && t.sapCalls > 1 ? ` (${t.sapCalls} calls)` : ''}`);
        }
        if (t.serverMs !== undefined) {
            lines.push(`Portal processing: ${formatDuration(Math.max(0, t.serverMs - (t.sapMs ?? 0)))}`);
            lines.push(`Network and transfer: ${formatDuration(Math.max(0, t.totalMs - t.serverMs))}`);
        }
        return lines.join('\n');
    });
}

/** Seconds elapsed since it appeared (for the "Running the report in SAP…" state). */
@Component({
    selector: 'app-report-elapsed',
    template: `<span class="re" data-testid="report-elapsed">{{ seconds() }} s</span>`,
    styles: [`:host { display: inline; } .re { font-variant-numeric: tabular-nums; }`]
})
export class ReportElapsedComponent implements OnInit, OnDestroy {

    readonly seconds = signal(0);
    private startedAt = 0;
    private timer: ReturnType<typeof setInterval> | undefined;

    ngOnInit(): void {
        this.startedAt = Date.now();
        this.timer = setInterval(() => this.seconds.set(Math.floor((Date.now() - this.startedAt) / 1000)), 1000);
    }

    ngOnDestroy(): void {
        if (this.timer) {
            clearInterval(this.timer);
        }
    }
}
