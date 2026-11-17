/** A saved selection screen (SAP variant) of a report, as returned by the portal. */
export interface ReportVariant {
    id: string;
    name: string;
    description: string | null;
    owner: string;
    /** Saved by the signed-in user (else a shared variant of another user) */
    mine: boolean;
    shared: boolean;
    /** The user's own default variant: loaded when the report is opened */
    defaultVariant: boolean;
    changedOn: string | null;
    parameters: Record<string, unknown>;
}

export interface VariantRequest {
    name: string;
    description: string;
    shared: boolean;
    defaultVariant: boolean;
    parameters: Record<string, unknown>;
}

/** What a loaded variant tells the report: the parameters, and whether to run the report right away. */
export interface VariantLoad {
    name: string;
    parameters: Record<string, unknown>;
    run: boolean;
}

/** The parameters of a column layout (saved as a variant of the report key <report>_layout): the hidden columns. */
export function layoutParameters(hidden: string[]): Record<string, unknown> {
    return { hidden };
}

/**
 * The hidden columns of a saved layout, limited to the columns that exist now (all of them when none are known yet,
 * as in a report whose columns come with the result). Null when the layout would hide every column.
 */
export function layoutHidden(saved: Record<string, unknown>, known: string[]): string[] | null {
    const raw = saved?.['hidden'];
    const list = Array.isArray(raw) ? raw.filter((k): k is string => typeof k === 'string') : [];
    if (!known.length) {
        return list;
    }
    const knownSet = new Set(known);
    const hidden = list.filter(k => knownSet.has(k));
    return hidden.length >= known.length ? null : hidden;
}

/** The selection without the keys that are not part of a variant (fixed values, today's date). */
export function variantParameters(selection: object, skip: string[] = []): Record<string, unknown> {
    const out: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(selection)) {
        if (!skip.includes(key)) {
            out[key] = JSON.parse(JSON.stringify(value));
        }
    }
    return out;
}

/**
 * A selection from a variant on top of a base (the defaults): only keys the base has, of the same kind (text, flag or
 * select-option lines), are taken over, so a variant saved by an older version of the screen still loads.
 */
export function applyParameters<T extends object>(base: T, saved: Record<string, unknown>, skip: string[] = []): T {
    const out = { ...base } as Record<string, unknown>;
    for (const [key, value] of Object.entries(saved ?? {})) {
        if (skip.includes(key) || !(key in out) || value === null || value === undefined) {
            continue;
        }
        const current = out[key];
        if (Array.isArray(current) ? Array.isArray(value) : typeof current === typeof value) {
            out[key] = JSON.parse(JSON.stringify(value));
        }
    }
    return out as T;
}
