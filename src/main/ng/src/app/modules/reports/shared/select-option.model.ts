import { HttpParams } from '@angular/common/http';

/**
 * SAP SELECT-OPTIONS for the report selection screens: a selection parameter holds any number of lines
 * (sign I include / E exclude, option, low, high) like an ABAP range table. They are entered as from / to or in the
 * multiple-selection dialog; the portal sends them as a JSON array and the backend turns them into the OData $filter.
 */
export type SoSign = 'I' | 'E';
export type SoOption = 'EQ' | 'NE' | 'GT' | 'GE' | 'LT' | 'LE' | 'BT' | 'NB' | 'CP' | 'NP';
export type SoKind = 'text' | 'date';

export interface SelectOptionLine {
    sign: SoSign;
    option: SoOption;
    low: string;
    high: string;
}

/** The options offered for ranges in the dialog, with their SAP symbols. */
export const RANGE_OPTIONS: { code: SoOption, text: string, symbol: string }[] = [
    { code: 'BT', text: 'Between', symbol: 'from – to' },
    { code: 'NB', text: 'Not between', symbol: 'not from – to' },
    { code: 'GE', text: 'Greater or equal', symbol: '≥' },
    { code: 'GT', text: 'Greater than', symbol: '>' },
    { code: 'LE', text: 'Less or equal', symbol: '≤' },
    { code: 'LT', text: 'Less than', symbol: '<' },
    { code: 'NE', text: 'Not equal', symbol: '≠' }
];

export function line(sign: SoSign, option: SoOption, low: string, high = ''): SelectOptionLine {
    return { sign, option, low, high };
}

/** The lines without empty ones, trimmed, BT without upper limit as EQ — what is sent to the backend. */
export function cleanLines(lines: SelectOptionLine[] | null | undefined): SelectOptionLine[] {
    const out: SelectOptionLine[] = [];
    for (const l of lines ?? []) {
        let low = (l.low ?? '').trim();
        let high = (l.high ?? '').trim();
        let option = l.option;
        if (!low && !high) {
            continue;
        }
        if (!low) {
            low = high;
            high = '';
            option = 'LE';
        }
        if ((option === 'BT' || option === 'NB') && !high) {
            option = option === 'BT' ? 'EQ' : 'NE';
        }
        if (option !== 'BT' && option !== 'NB') {
            high = '';
        }
        out.push({ sign: l.sign === 'E' ? 'E' : 'I', option, low, high });
    }
    return out;
}

/** Whether nothing is selected. */
export function isEmpty(lines: SelectOptionLine[] | null | undefined): boolean {
    return cleanLines(lines).length === 0;
}

/** The query parameter value: the cleaned lines as JSON, or undefined when nothing is selected. */
export function serialize(lines: SelectOptionLine[] | null | undefined): string | undefined {
    const cleaned = cleanLines(lines);
    return cleaned.length ? JSON.stringify(cleaned) : undefined;
}

/** From / to fields to lines: from = EQ (CP with *), from + to = BT, only to = up to. */
export function linesFromTo(from: string, to: string, kind: SoKind): SelectOptionLine[] {
    const f = (from ?? '').trim();
    const t = (to ?? '').trim();
    if (!f && !t) {
        return [];
    }
    if (!f) {
        return [line('I', 'LE', t)];
    }
    if (!t) {
        return [line('I', kind === 'text' && f.includes('*') ? 'CP' : 'EQ', f)];
    }
    return [line('I', 'BT', f, t)];
}

/** Whether the lines fit the plain from / to fields (at most one include line of EQ, CP, BT or "up to"). */
export function isSimple(lines: SelectOptionLine[] | null | undefined): boolean {
    const l = cleanLines(lines);
    if (l.length === 0) {
        return true;
    }
    return l.length === 1 && l[0].sign === 'I' && ['EQ', 'CP', 'BT', 'LE'].includes(l[0].option);
}

/** The text of the from / to fields for the lines: of the one simple line, or of the first include line. */
export function fromTo(lines: SelectOptionLine[] | null | undefined): { from: string, to: string } {
    const first = cleanLines(lines).find(l => l.sign === 'I');
    if (!first) {
        return { from: '', to: '' };
    }
    if (first.option === 'LE' && isSimple(lines)) {
        return { from: '', to: first.low };
    }
    return { from: first.low, to: first.high };
}

/** A readable text of one line, e.g. "100 – 200", "≥ 5", "excl. 150". */
export function describeLine(l: SelectOptionLine, format: (v: string) => string = v => v): string {
    const low = format(l.low);
    const high = format(l.high);
    let text: string;
    switch (l.option) {
        case 'BT': text = `${low} – ${high}`; break;
        case 'NB': text = `not ${low} – ${high}`; break;
        case 'NE': text = `≠ ${low}`; break;
        case 'GT': text = `> ${low}`; break;
        case 'GE': text = `≥ ${low}`; break;
        case 'LT': text = `< ${low}`; break;
        case 'LE': text = `≤ ${low}`; break;
        case 'NP': text = `not ${low}`; break;
        default: text = low;
    }
    return l.sign === 'E' ? `excl. ${text}` : text;
}

/** All lines as one text, e.g. "100 – 200, 300, excl. 150"; empty when nothing is selected. */
export function describeLines(lines: SelectOptionLine[] | null | undefined, format?: (v: string) => string): string {
    return cleanLines(lines).map(l => describeLine(l, format)).join(', ');
}

/**
 * The query parameters of a selection: select-options as JSON arrays, booleans always, text when filled.
 * Keys in {@code skip} are not sent.
 */
export function toParams(selection: object, skip: string[] = []): HttpParams {
    let params = new HttpParams();
    for (const [key, value] of Object.entries(selection)) {
        if (skip.includes(key)) {
            continue;
        }
        if (Array.isArray(value)) {
            const json = serialize(value as SelectOptionLine[]);
            if (json) {
                params = params.set(key, json);
            }
        } else if (typeof value === 'boolean') {
            params = params.set(key, String(value));
        } else if (typeof value === 'string' && value.trim()) {
            params = params.set(key, value.trim());
        }
    }
    return params;
}

/** A message when a date range of the lines ends before it starts (e.g. typed in the From / To fields); else null. */
export function dateRangeError(label: string, lines: SelectOptionLine[] | null | undefined): string | null {
    const bad = cleanLines(lines).find(l => (l.option === 'BT' || l.option === 'NB') && l.high < l.low);
    return bad ? `${label}: the upper date is before the lower date.` : null;
}
