import { Component, ElementRef, HostListener, computed, inject, input, model } from '@angular/core';
import { ButtonComponent, IconComponent } from '@fundamental-ngx/core';
import {
    RANGE_OPTIONS,
    SelectOptionLine,
    SoKind,
    SoOption,
    SoSign,
    cleanLines,
    describeLines,
    fromTo,
    isSimple,
    linesFromTo,
    line
} from './select-option.model';

type TabKey = 'single' | 'range' | 'xsingle' | 'xrange';
interface DraftRow { option: SoOption; low: string; high: string; }

/**
 * A selection parameter that works like an ABAP SELECT-OPTION: plain From / To fields (from = single value, from + to =
 * range, wildcards with *) and a multiple-selection button that opens the SAP-style dialog with the tabs Select Single
 * Values, Select Ranges, Exclude Single Values and Exclude Ranges. The value is the list of lines (sign, option, low,
 * high); it is empty when nothing is selected. Use as {@code <app-select-option label="…" idPrefix="…" [(lines)]="…">}.
 */
@Component({
    selector: 'app-select-option',
    templateUrl: './select-option.component.html',
    styleUrls: ['./select-option.component.scss'],
    imports: [ButtonComponent, IconComponent]
})
export class SelectOptionComponent {

    private readonly host = inject<ElementRef<HTMLElement>>(ElementRef);

    /** Name of the parameter, for the dialog title and the accessible names */
    readonly label = input.required<string>();
    /** Prefix of the element ids: {prefix}-from, {prefix}-to, {prefix}-multi */
    readonly idPrefix = input.required<string>();
    readonly kind = input<SoKind>('text');
    readonly maxLength = input<number>(40);
    readonly lines = model<SelectOptionLine[]>([]);

    readonly simple = computed(() => isSimple(this.lines()));
    readonly fields = computed(() => fromTo(this.lines()));
    readonly count = computed(() => cleanLines(this.lines()).length);
    readonly summary = computed(() => describeLines(this.lines()));

    readonly tabs: { key: TabKey, title: string }[] = [
        { key: 'single', title: 'Select Single Values' },
        { key: 'range', title: 'Select Ranges' },
        { key: 'xsingle', title: 'Exclude Single Values' },
        { key: 'xrange', title: 'Exclude Ranges' }
    ];
    readonly rangeOptions = RANGE_OPTIONS;

    isOpen = false;
    tab: TabKey = 'single';
    draft: Record<TabKey, DraftRow[]> = { single: [], range: [], xsingle: [], xrange: [] };
    error = '';

    // ------------------------------------------------------------------------------------------ from / to fields

    onFrom(value: string): void {
        this.lines.set(linesFromTo(value, this.fields().to, this.kind()));
    }

    onTo(value: string): void {
        this.lines.set(linesFromTo(this.fields().from, value, this.kind()));
    }

    /** The fields only show the first entry when there are several: they are changed in the dialog. */
    onFieldClick(): void {
        if (!this.simple()) {
            this.open();
        }
    }

    // ------------------------------------------------------------------------------------------ dialog

    open(): void {
        this.draft = { single: [], range: [], xsingle: [], xrange: [] };
        for (const l of cleanLines(this.lines())) {
            const excl = l.sign === 'E';
            if (l.option === 'EQ' || l.option === 'CP') {
                this.draft[excl ? 'xsingle' : 'single'].push({ option: 'EQ', low: l.low, high: '' });
            } else {
                this.draft[excl ? 'xrange' : 'range'].push({ option: l.option, low: l.low, high: l.high });
            }
        }
        for (const key of Object.keys(this.draft) as TabKey[]) {
            this.ensureBlankRow(key);
        }
        this.tab = this.firstFilledTab();
        this.error = '';
        this.isOpen = true;
        setTimeout(() => this.host.nativeElement.querySelector<HTMLElement>('.so-dialog input')?.focus());
    }

    @HostListener('document:keydown.escape')
    cancel(): void {
        this.isOpen = false;
    }

    isRangeTab(tab: TabKey = this.tab): boolean {
        return tab === 'range' || tab === 'xrange';
    }

    rows(tab: TabKey = this.tab): DraftRow[] {
        return this.draft[tab];
    }

    filled(tab: TabKey): number {
        return this.draft[tab].filter(r => r.low.trim() || r.high.trim()).length;
    }

    /** SAP keeps a free row at the end of each list. */
    onRowInput(index: number): void {
        const rows = this.draft[this.tab];
        if (index === rows.length - 1 && (rows[index].low.trim() || rows[index].high.trim())) {
            rows.push(this.blank());
        }
        this.error = '';
    }

    onOption(row: DraftRow, option: string): void {
        row.option = option as SoOption;
        if (option !== 'BT' && option !== 'NB') {
            row.high = '';
        }
    }

    twoValues(row: DraftRow): boolean {
        return row.option === 'BT' || row.option === 'NB';
    }

    removeRow(index: number): void {
        this.draft[this.tab].splice(index, 1);
        this.ensureBlankRow(this.tab);
    }

    deleteAll(): void {
        for (const key of Object.keys(this.draft) as TabKey[]) {
            this.draft[key] = [this.blank()];
        }
        this.error = '';
    }

    /** Several values pasted at once (e.g. a column copied from Excel) become one row each. */
    onPaste(event: ClipboardEvent, index: number): void {
        const text = event.clipboardData?.getData('text') ?? '';
        const values = text.split(/[\r\n\t]+/).map(v => v.trim()).filter(v => v);
        if (values.length < 2 || this.isRangeTab()) {
            return;
        }
        event.preventDefault();
        const rows = this.draft[this.tab];
        rows.splice(index, 1, ...values.map(v => ({ option: 'EQ' as SoOption, low: v, high: '' })));
        this.ensureBlankRow(this.tab);
    }

    /** Copy: takes the dialog entries over as the lines of the parameter. */
    apply(): void {
        const result: SelectOptionLine[] = [];
        for (const key of Object.keys(this.draft) as TabKey[]) {
            const sign: SoSign = key === 'xsingle' || key === 'xrange' ? 'E' : 'I';
            for (const row of this.draft[key]) {
                const low = row.low.trim();
                const high = row.high.trim();
                if (!low && !high) {
                    continue;
                }
                if (this.isRangeTab(key)) {
                    if (this.twoValues(row) && (!low || !high)) {
                        this.fail(key, 'Enter both values of the range.');
                        return;
                    }
                    if (this.twoValues(row) && this.kind() === 'date' && high < low) {
                        this.fail(key, 'The upper date is before the lower date.');
                        return;
                    }
                    result.push(line(sign, row.option, low, high));
                } else {
                    result.push(line(sign, this.kind() === 'text' && low.includes('*') ? 'CP' : 'EQ', low));
                }
            }
        }
        this.lines.set(cleanLines(result));
        this.isOpen = false;
    }

    private fail(tab: TabKey, message: string): void {
        this.tab = tab;
        this.error = message;
    }

    private blank(): DraftRow {
        return { option: this.isRangeTab() ? 'BT' : 'EQ', low: '', high: '' };
    }

    private ensureBlankRow(tab: TabKey): void {
        const rows = this.draft[tab];
        const last = rows[rows.length - 1];
        if (!last || last.low.trim() || last.high.trim()) {
            rows.push({ option: this.isRangeTab(tab) ? 'BT' : 'EQ', low: '', high: '' });
        }
    }

    private firstFilledTab(): TabKey {
        return this.tabs.map(t => t.key).find(k => this.filled(k) > 0) ?? 'single';
    }
}
