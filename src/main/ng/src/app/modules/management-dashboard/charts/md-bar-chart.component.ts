import { DecimalPipe } from '@angular/common';
import { Component, Input } from '@angular/core';

/** One stacked segment of a horizontal bar. */
export interface BarSegment {
    value: number;
    color: string;
    name?: string;
}

/** One horizontal bar: stacked segments, an optional reference (e.g. budget) drawn as a ghost track. */
export interface BarRow {
    label: string;
    segments: BarSegment[];
    /** Reference the segments are measured against (e.g. budget); drawn as a light track behind them. */
    reference?: number | null;
    /** Figure printed at the end of the row; the sum of the segments when omitted. */
    display?: number | null;
    displayFormat?: string;
    /** Secondary text under the figure, e.g. "/ 4,500". */
    note?: string;
    /** Tooltip of the row. */
    tooltip?: string;
    emphasis?: boolean;
    negative?: boolean;
}

export interface ChartLegendItem {
    name: string;
    color: string;
}

/**
 * Horizontal bar chart in plain HTML/CSS (no chart library, like the homepage widgets). Every bar is scaled to the
 * largest row, or to its own reference when references are given (budget vs actual).
 */
@Component({
    selector: 'app-md-bar-chart',
    imports: [DecimalPipe],
    templateUrl: './md-bar-chart.component.html',
    styleUrl: './md-bar-chart.component.scss'
})
export class MdBarChartComponent {

    @Input() legend: ChartLegendItem[] = [];
    @Input() labelWidth = '9.5rem';
    @Input() format = '1.0-2';

    rows: BarRow[] = [];
    private scale = 1;

    @Input('rows') set rowsInput(rows: BarRow[]) {
        this.rows = rows ?? [];
        const totals = this.rows.map(row => Math.max(this.total(row), Math.abs(row.reference ?? 0)));
        this.scale = Math.max(1e-9, ...totals);
    }

    total(row: BarRow): number {
        return row.segments.reduce((sum, segment) => sum + Math.abs(segment.value || 0), 0);
    }

    /** Width of the row's track in % of the chart: its reference, or the full width when none. */
    trackWidth(row: BarRow): number {
        const extent = row.reference != null ? Math.abs(row.reference) : this.total(row);
        return Math.min(100, extent / this.scale * 100);
    }

    /** Width of a segment in % of the row's track. */
    segmentWidth(row: BarRow, segment: BarSegment): number {
        const extent = row.reference != null ? Math.abs(row.reference) : this.total(row);
        return extent > 0 ? Math.min(100, Math.abs(segment.value || 0) / extent * 100) : 0;
    }

    displayValue(row: BarRow): number | null {
        return row.display !== undefined ? row.display : this.total(row);
    }
}
