import { expect, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Configuration tables can be sorted by clicking a column header (asc → desc → original order). */
const f = (name: string, label: string, extra: Record<string, unknown> = {}) => ({
    name, label, type: 'TEXT', idKind: 'NONE', businessKey: false, required: false, maxLength: 255, referenceTable: null,
    referenceEntity: false, options: [], filter: false, idField: false, visible: true, changeable: true, ...extra });

test.describe('Configuration: sortable columns', () => {

    test('a table app sorts on the server: the header click sends sort and dir, a third click removes the sort', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        const table = { key: 'loan-classes', title: 'Loan Classes', configClass: 'Initializer', description: '', valueField: 'code',
            labelField: 'value', fields: [f('id', 'ID', { idKind: 'GENERATED_ID', idField: true, visible: false }),
                f('code', 'Loan Class', { businessKey: true }), f('value', 'Description')] };
        await page.route(/\/configuration\/bp-tables\/loan-classes/, route => {
            const params = new URL(route.request().url()).searchParams;
            requests.push(params);
            const rows = [['1', 'PowerGen-Convl'], ['2', 'Railways'], ['3', 'Airport']]
                .map(([code, value]) => ({ id: code, values: { code, value }, labels: {} }));
            if (params.get('sort') === 'value') {
                rows.sort((a, b) => (params.get('dir') === 'desc' ? -1 : 1) * a.values.value.localeCompare(b.values.value));
            }
            return route.fulfill({ json: { table, rows, page: Number(params.get('page')), size: 25, total: 3, search: '',
                canChange: true, userRole: 'ZLM023', references: {}, filters: {}, sort: params.get('sort'), sortDir: params.get('dir') } });
        });
        await page.goto('/configuration/loan-tables/loan-classes');
        await expect(page.locator('tbody tr').first()).toContainText('PowerGen-Convl');

        const header = page.locator('th', { hasText: 'Description' });
        await header.getByRole('button').click();
        await expect(header).toHaveAttribute('aria-sort', 'ascending');
        await expect(page.locator('tbody tr').first()).toContainText('Airport');
        expect(requests.at(-1)!.get('sort')).toBe('value');
        expect(requests.at(-1)!.get('dir')).toBe('asc');
        expect(requests.at(-1)!.get('page')).toBe('0');

        await header.getByRole('button').click();
        await expect(header).toHaveAttribute('aria-sort', 'descending');
        await expect(page.locator('tbody tr').first()).toContainText('Railways');
        expect(requests.at(-1)!.get('dir')).toBe('desc');

        await header.getByRole('button').click();
        await expect(header).not.toHaveAttribute('aria-sort', /.+/);
        expect(requests.at(-1)!.has('sort')).toBe(false);
        await expect(page.locator('tbody tr').first()).toContainText('PowerGen-Convl');
    });

    test('BP field status sorts the fields of each entity in the browser', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        await page.route(/\/api\/configuration\/bupa-field-status(\?|$)/, route => route.fulfill({ json: {
            roles: [{ code: 'TR0151', description: 'Security Trustee', entityFieldCount: 3, entitySetFieldCount: 0 }],
            canChange: true, userRole: 'ZLM023', entities: ['Partner'], entitySets: [] } }));
        await page.route(/\/api\/configuration\/bupa-field-status\/TR0151/, route => route.fulfill({ json: {
            roleCode: 'TR0151', roleDescription: 'Security Trustee', entitySetFields: [],
            entityFields: [
                { id: 1, entity: 'Partner', fieldName: 'pan', fieldStatus: 2 },
                { id: 2, entity: 'Partner', fieldName: 'cinNumber', fieldStatus: 0 },
                { id: 3, entity: 'Partner', fieldName: 'email', fieldStatus: 1 }
            ] } }));
        await page.goto('/configuration/bupa-field-status?role=TR0151');
        const names = page.locator('td.fs-table__field');
        await expect(names).toHaveText(['pan', 'cinNumber', 'email']);

        const fieldName = page.locator('th', { hasText: 'Field Name' });
        await fieldName.getByRole('button').click();
        await expect(names).toHaveText(['cinNumber', 'email', 'pan']);
        await fieldName.getByRole('button').click();
        await expect(names).toHaveText(['pan', 'email', 'cinNumber']);
        await fieldName.getByRole('button').click();
        await expect(names).toHaveText(['pan', 'cinNumber', 'email']);

        await page.locator('th', { hasText: 'Field Status' }).getByRole('button').click();
        await expect(names).toHaveText(['cinNumber', 'email', 'pan']);
    });
});
