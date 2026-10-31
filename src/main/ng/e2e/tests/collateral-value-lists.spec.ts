import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Configuration > Collaterals > Collateral Value Lists: the collateral_value table filtered by value list. */
const field = (name: string, label: string, extra: Record<string, unknown> = {}) => ({
    name, label, type: 'TEXT', idKind: 'NONE', businessKey: false, required: false, maxLength: 255, referenceTable: null,
    referenceEntity: false, options: [], filter: false, idField: false, visible: true, changeable: true, ...extra });

const TABLE = {
    key: 'collateral-values', title: 'Collateral Value Lists', module: 'collaterals', configClass: 'CollateralValueInitializer',
    description: 'Dropdown values of Collateral Management', valueField: 'code', labelField: 'description',
    fields: [
        field('id', 'ID', { type: 'NUMBER', idKind: 'GENERATED_ID', idField: true, visible: false, changeable: false }),
        field('listName', 'Value List', { businessKey: true, required: true, maxLength: 40, filter: true, changeable: false,
            options: [{ value: 'CONDITION_GROUP', label: 'Condition Group (ZCONDITION_GRP), default 04' },
                { value: 'DOCUMENT_STAGE', label: 'Document Stage (DOC_STAGE)' }] }),
        field('code', 'Code', { businessKey: true, required: true, maxLength: 20, changeable: false }),
        field('description', 'Description', { required: true, maxLength: 100 }),
        field('sortOrder', 'Sort Order', { type: 'NUMBER', maxLength: 18 })
    ]
};

const ROWS = [
    ['CONDITION_GROUP', '04', 'Collateral Conditions', 4],
    ['DOCUMENT_STAGE', '1', 'Creation', 1],
    ['DOCUMENT_STAGE', '2', 'Perfection', 2]
].map(([listName, code, description, sortOrder], i) => ({ id: String(i + 1), values: { listName, code, description, sortOrder }, labels: {} }));

async function mockValues(page: Page, requests: URLSearchParams[], posted: unknown[]): Promise<void> {
    await page.route(/\/configuration\/bp-tables\/collateral-values/, route => {
        const request = route.request();
        if (request.method() === 'POST') {
            posted.push(request.postDataJSON());
            return route.fulfill({ status: 201, json: { id: '9', values: request.postDataJSON().values, labels: {} } });
        }
        const params = new URL(request.url()).searchParams;
        requests.push(params);
        const list = params.get('listName');
        const rows = ROWS.filter(row => !list || row.values.listName === list);
        return route.fulfill({ json: { table: TABLE, rows, page: 0, size: 25, total: rows.length, search: '',
            canChange: true, userRole: 'ZLM023', references: {}, filters: list ? { listName: list } : {} } });
    });
}

test.describe('Configuration: Collateral Value Lists', () => {

    test('the value list filter loads one list; a new entry is added to the filtered list', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        const posted: unknown[] = [];
        await mockValues(page, requests, posted);
        await page.goto('/configuration/collateral-tables/collateral-values');

        await expect(page.getByRole('tab', { name: 'Collaterals' })).toHaveClass(/is-selected/);
        await expect(page.getByText('Configuration: Collateral Value Lists')).toBeVisible();
        await expect(page.locator('tbody tr')).toHaveCount(3);

        const filter = page.getByLabel('Filter by Value List');
        await expect(filter.locator('option')).toHaveText(['All', 'Condition Group (ZCONDITION_GRP), default 04', 'Document Stage (DOC_STAGE)']);
        await filter.selectOption('DOCUMENT_STAGE');
        await expect(page.locator('tbody tr')).toHaveCount(2);
        expect(requests.at(-1)!.get('listName')).toBe('DOCUMENT_STAGE');
        expect(requests.at(-1)!.get('page')).toBe('0');
        await expect(page).toHaveURL(/listName=DOCUMENT_STAGE/);

        await page.getByRole('button', { name: 'Change' }).click();
        await page.getByRole('button', { name: 'Add Entry' }).click();
        await expect(page.locator('#bt-listName')).toHaveValue('DOCUMENT_STAGE');
        await page.locator('#bt-code').fill('6');
        await page.locator('#bt-description').fill('Archived');
        await page.getByRole('button', { name: 'Add', exact: true }).click();
        await expect.poll(() => posted.length).toBe(1);
        expect(posted[0]).toEqual({ values: { listName: 'DOCUMENT_STAGE', code: '6', description: 'Archived', sortOrder: '' } });
    });

    test('a link with the value list opens the list filtered', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        await mockValues(page, requests, []);
        await page.goto('/configuration/collateral-tables/collateral-values?listName=CONDITION_GROUP');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        expect(requests[0].get('listName')).toBe('CONDITION_GROUP');
        await expect(page.getByLabel('Filter by Value List')).toHaveValue('CONDITION_GROUP');
    });
});
