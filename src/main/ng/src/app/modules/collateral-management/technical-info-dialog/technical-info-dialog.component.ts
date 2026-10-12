import { Component } from '@angular/core';
import {
    BarModule,
    DialogModule,
    DialogRef,
    IconComponent,
    TitleComponent
} from '@fundamental-ngx/core';
import { TechnicalInfo, formatDate } from '../collateral.model';

/** Data passed to {@link TechnicalInfoDialogComponent}. */
export interface TechnicalInfoDialogData {
    /** What the record is, e.g. "Collateral 0000000912" or "Coverage". */
    title: string;
    info: TechnicalInfo;
}

/** One row of the technical info display. */
interface TechnicalInfoRow {
    label: string;
    value: string;
    /** Render in a monospace font (the id). */
    code?: boolean;
}

/**
 * "Technical Info" dialog of the collateral screens: the record's id and its audit trail — who created and last
 * changed it, and when. Read-only; closes with the Close button.
 */
@Component({
    selector: 'app-collateral-technical-info-dialog',
    template: `
        <fd-dialog>
            <fd-dialog-header>
                <h1 fd-title class="fd-dialog-header-title">
                    <fd-icon glyph="hint"></fd-icon>
                    Technical Info
                </h1>
            </fd-dialog-header>
            <fd-dialog-body>
                <p class="cm-tech__subtitle">{{ data.title }}</p>
                <dl class="cm-tech__list">
                    @for (row of rows; track row.label) {
                        <dt class="cm-tech__label">{{ row.label }}</dt>
                        <dd class="cm-tech__value" [class.cm-tech__value--code]="row.code"
                            [class.cm-tech__value--empty]="!row.value">{{ row.value || '–' }}</dd>
                    }
                </dl>
            </fd-dialog-body>
            <fd-dialog-footer>
                <fd-button-bar fdType="emphasized" label="Close" (click)="dialogRef.close()"></fd-button-bar>
            </fd-dialog-footer>
        </fd-dialog>
    `,
    styles: [`
        .cm-tech__subtitle { margin: 0 0 1rem; font-weight: 600; }
        .cm-tech__list {
            display: grid;
            grid-template-columns: max-content minmax(14rem, 1fr);
            gap: 0.5rem 1.5rem;
            margin: 0;
            max-width: 36rem;
        }
        .cm-tech__label { font-weight: 600; white-space: nowrap; }
        .cm-tech__value { margin: 0; word-break: break-all; }
        .cm-tech__value--code { font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace; font-size: 0.875rem; }
        .cm-tech__value--empty { opacity: 0.6; }
    `],
    imports: [DialogModule, BarModule, IconComponent, TitleComponent]
})
export class TechnicalInfoDialogComponent {

    readonly data: TechnicalInfoDialogData;
    readonly rows: TechnicalInfoRow[];

    constructor(public dialogRef: DialogRef) {
        this.data = dialogRef.data as TechnicalInfoDialogData;
        const info = this.data.info ?? {};
        this.rows = [
            { label: 'Id', value: info.id ?? '', code: true },
            { label: 'Created on', value: formatDate(info.createdOn) },
            { label: 'Created at', value: formatTime(info.createdAt) },
            { label: 'Created by', value: info.createdByUserName ?? '' },
            { label: 'Changed on', value: formatDate(info.changedOn) },
            { label: 'Changed at', value: formatTime(info.changedAt) },
            { label: 'Changed by', value: info.changedByUserName ?? '' }
        ];
    }
}

/** A backend LocalTime ("09:06:12.123456") as HH:mm:ss. */
function formatTime(value: string | null | undefined): string {
    if (!value) {
        return '';
    }
    const match = /^(\d{2}):(\d{2})(?::(\d{2}))?/.exec(value);
    return match ? `${match[1]}:${match[2]}:${match[3] ?? '00'}` : value;
}
