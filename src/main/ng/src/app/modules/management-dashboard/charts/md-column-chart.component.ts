import { DecimalPipe } from '@angular/common';
import { Component, inject, Input, LOCALE_ID } from '@angular/core';
import { ChartLegendItem } from './md-bar-chart.component';

/** A series of the column chart: columns on the left axis or a line on the right axis. */
export interface ColumnSeries {
    name: string;
    values: (number | null)[];
    color: string;
    kind?: 'column' | 'line';
    dashed?: boolean;
    /** Print the value above each column / point. */
    labels?: boolean;
    /** Number format of the labels (Angular DecimalPipe). */
    format?: string;
    /** Suffix of the labels, e.g. '%'. */
    suffix?: string;
}

interface Tick { y: number; text: string; }
interface ColumnShape { x: number; y: number; width: number; height: number; color: string; label: string | null; inside: boolean; }
interface PointShape { x: number; y: number; color: string; label: string | null; }
interface LineShape { points: string; color: string; dashed: boolean; dots: PointShape[]; }
interface Category { x: number; label: string; note: string | null; noteColor: string; tooltip: string; missing: boolean; width: number; }

/**
 * Column chart as inline SVG, no chart library: grouped columns on a left axis and optional lines on a right axis
 * (e.g. loan book amounts with NPA percentages). Categories without any value (e.g. quarters not reached yet) are
 * drawn as dashed placeholders so the axis stays stable through the year.
 */
@Component({
    selector: 'app-md-column-chart',
    imports: [],
    templateUrl: './md-column-chart.component.html',
    styleUrl: './md-column-chart.component.scss'
})
export class MdColumnChartComponent {

    @Input() height = 230;
    @Input() leftFormat = '1.0-0';
    @Input() rightSuffix = '%';
    @Input() ariaLabel = 'Column chart';
    private readonly pipe = new DecimalPipe(inject(LOCALE_ID));

    /** Width of the drawing in viewBox units; narrower cards pass a smaller width so the text keeps its size. */
    widthValue = 600;
    notesValue: (string | null)[] = [];
    tooltipsValue: string[] = [];

    @Input() set width(width: number) {
        this.widthValue = width || 600;
        this.build();
    }

    get width(): number {
        return this.widthValue;
    }

    /** Short text per category drawn above the group, e.g. the net cash flow; a '-' prefix colours it red. */
    @Input() set notes(notes: (string | null)[]) {
        this.notesValue = notes ?? [];
        this.build();
    }

    /** Tooltip per category (hover). */
    @Input() set tooltips(tooltips: string[]) {
        this.tooltipsValue = tooltips ?? [];
        this.build();
    }

    readonly padLeft = 52;
    readonly padRight = 44;
    readonly padTop = 22;
    readonly padBottom = 50;

    private categoryList: string[] = [];
    private seriesList: ColumnSeries[] = [];

    leftTicks: Tick[] = [];
    rightTicks: Tick[] = [];
    columns: ColumnShape[] = [];
    lines: LineShape[] = [];
    cats: Category[] = [];
    legend: (ChartLegendItem & { x: number; y: number })[] = [];
    legendRows = 1;
    hasRightAxis = false;

    @Input() set categories(categories: string[]) {
        this.categoryList = categories ?? [];
        this.build();
    }

    @Input() set series(series: ColumnSeries[]) {
        this.seriesList = series ?? [];
        this.build();
    }

    get plotBottom(): number {
        return this.height - this.padBottom - (this.legendRows - 1) * 14;
    }

    private build(): void {
        const n = this.categoryList.length;
        const columnSeries = this.seriesList.filter(s => (s.kind ?? 'column') === 'column');
        const lineSeries = this.seriesList.filter(s => s.kind === 'line');
        this.hasRightAxis = lineSeries.length > 0;
        // legend: items flow left to right (~6.2 units per character plus the swatch) and wrap to a second row
        let lx = this.padLeft, ly = 0;
        this.legend = this.seriesList.map(s => {
            const itemWidth = s.name.length * 6.2 + 26;
            if (lx > this.padLeft && lx + itemWidth > this.widthValue - 8) {
                lx = this.padLeft;
                ly += 14;
            }
            const item = { name: s.name, color: s.color, x: lx, y: ly };
            lx += itemWidth;
            return item;
        });
        this.legendRows = ly / 14 + 1;
        this.columns = [];
        this.lines = [];
        this.cats = [];
        if (!n) {
            this.leftTicks = [];
            this.rightTicks = [];
            return;
        }

        const plotTop = this.padTop;
        const plotBottom = this.plotBottom;
        const plotHeight = plotBottom - plotTop;
        const plotWidth = this.width - this.padLeft - this.padRight;
        const slot = plotWidth / n;

        const leftMax = niceMax(Math.max(0, ...columnSeries.flatMap(s => s.values.map(v => Math.abs(v ?? 0)))));
        const rightValues = lineSeries.flatMap(s => s.values.filter((v): v is number => v != null));
        const rightMax = niceMax(Math.max(0, ...rightValues));
        const yLeft = (v: number) => plotBottom - (leftMax > 0 ? Math.abs(v) / leftMax : 0) * plotHeight;
        const yRight = (v: number) => plotBottom - (rightMax > 0 ? v / rightMax : 0) * plotHeight;

        this.leftTicks = [0, 1, 2, 3, 4].map(i => ({
            y: plotBottom - i / 4 * plotHeight,
            text: this.tick(leftMax * i / 4)
        }));
        this.rightTicks = this.hasRightAxis ? [0, 1, 2, 3, 4].map(i => ({
            y: plotBottom - i / 4 * plotHeight,
            text: this.tick(rightMax * i / 4) + this.rightSuffix
        })) : [];

        const gap = Math.min(6, slot * 0.06);
        const groupWidth = slot * 0.62;
        const columnWidth = columnSeries.length ? (groupWidth - gap * (columnSeries.length - 1)) / columnSeries.length : groupWidth;

        this.categoryList.forEach((label, i) => {
            const centre = this.padLeft + slot * (i + 0.5);
            const missing = this.seriesList.every(s => s.values[i] == null);
            const note = this.notesValue[i] ?? null;
            this.cats.push({
                x: centre, label, note, width: groupWidth,
                noteColor: note && /-\s*\d/.test(note) ? '#bb0000' : '#107e3e',
                tooltip: this.tooltipsValue[i] ?? label, missing
            });
            columnSeries.forEach((s, k) => {
                const v = s.values[i];
                if (v == null) {
                    return;
                }
                const y = yLeft(v);
                this.columns.push({
                    x: centre - groupWidth / 2 + k * (columnWidth + gap), y, width: columnWidth,
                    height: Math.max(0, plotBottom - y), color: s.color,
                    label: s.labels ? this.label(v, s) : null,
                    inside: plotBottom - y > 22 && lineSeries.length > 0
                });
            });
        });

        lineSeries.forEach(s => {
            const dots: PointShape[] = [];
            s.values.forEach((v, i) => {
                if (v == null) {
                    return;
                }
                dots.push({ x: this.padLeft + slot * (i + 0.5), y: yRight(v), color: s.color, label: s.labels ? this.label(v, s) : null });
            });
            this.lines.push({
                points: dots.map(d => `${d.x},${d.y}`).join(' '), color: s.color, dashed: !!s.dashed, dots
            });
        });
    }

    private tick(value: number): string {
        return this.pipe.transform(value, value >= 1000 ? '1.0-0' : '1.0-2') ?? String(value);
    }

    private label(v: number, s: ColumnSeries): string {
        return (this.pipe.transform(v, s.format ?? '1.0-2') ?? '') + (s.suffix ?? '');
    }
}

/** A round axis maximum at or above the value: 1, 2, 2.5, 5 × 10^k. */
function niceMax(value: number): number {
    if (!(value > 0)) {
        return 1;
    }
    const exponent = Math.floor(Math.log10(value));
    const base = Math.pow(10, exponent);
    const fraction = value / base;
    const step = fraction <= 1 ? 1 : fraction <= 2 ? 2 : fraction <= 2.5 ? 2.5 : fraction <= 5 ? 5 : 10;
    return step * base;
}
