import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Reports › Loans Management › Covenants Report (CovenantsReportSet). */
const COLUMNS = [
    { key: 'SerialNo', label: 'Serial no.', type: 'number' },
    { key: 'Ranl', label: 'Contract number', type: 'text' },
    { key: 'ProjectName', label: 'Project name', type: 'text' },
    { key: 'CovTypeDesc', label: 'Covenant type', type: 'text' },
    { key: 'CovKeyFigureDesc', label: 'Key figure', type: 'text' },
    { key: 'CovenantOperator', label: 'Operator', type: 'text' },
    { key: 'CovenantValueFrom', label: 'Covenant value from', type: 'text' },
    { key: 'ValidFrom', label: 'Valid from', type: 'date' },
    { key: 'MaxPenalCharges', label: 'Max penal charge %', type: 'number' },
    { key: 'NextMonitoringDate', label: 'Next invocation date', type: 'date' }
];
const ROWS = [
    { SerialNo: '1', Ranl: '0000010003814', ProjectName: 'Solar Park', CovTypeDesc: 'Financial', CovKeyFigureDesc: 'DSCR',
        CovenantOperator: 'GE', CovenantValueFrom: '1.20', ValidFrom: '2025-01-01', MaxPenalCharges: '2', NextMonitoringDate: '2026-03-31' },
    { SerialNo: '2', Ranl: '0000010003814', ProjectName: 'Solar Park', CovTypeDesc: 'Non-financial', CovKeyFigureDesc: 'Insurance renewal',
        CovenantOperator: '', CovenantValueFrom: null, ValidFrom: '2024-06-15', MaxPenalCharges: '0.5', NextMonitoringDate: null }
];

async function mockReport(page: Page, requests: URLSearchParams[], exports: unknown[]): Promise<void> {
    await page.route(/\/api\/reports\/covenants\/export/, route => {
        exports.push(route.request().postDataJSON());
        return route.fulfill({ status: 200, contentType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', body: 'xlsx' });
    });
    await page.route(/\/api\/reports\/covenants\?/, route => {
        requests.push(new URL(route.request().url()).searchParams);
        return route.fulfill({ json: { columns: COLUMNS, rows: ROWS, total: 2, generatedOn: new Date().toISOString(), query: 'CovenantsReportSet' } });
    });
}

test.describe('Reports: Covenants Report', () => {

    test('listed under Loans Management; runs for company code and loan contracts and shows one row per covenant', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        const exports: unknown[] = [];
        await mockReport(page, requests, exports);

        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        await page.locator('.rp-list__item').filter({ hasText: 'Covenants Report' }).getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/covenants$/);
        await expect(page.locator('.ws-header__title')).toHaveText('Covenants Report');

        await expect(page.locator('#cv-bukrs')).toHaveValue('PFS');
        await page.locator('#cv-ranl-from').fill('0000010003814');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect(page.locator('tbody tr')).toHaveCount(2);
        expect(requests[0].get('companyCode')).toBe('PFS');
        expect(JSON.parse(requests[0].get('contract')!)).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);
        await expect(page.locator('thead th')).toHaveText(COLUMNS.map(c => c.label));
        await expect(page.locator('.ws-result-bar__count')).toHaveText('2 records');
        const first = page.locator('tbody tr').first().locator('td');
        await expect(first.nth(7)).toHaveText('01.01.2025');
        await expect(first.nth(9)).toHaveText('31.03.2026');

        // sort by next invocation date, filter, Excel of the rows as shown
        await page.locator('th', { hasText: 'Next invocation date' }).getByRole('button').click();
        await page.getByLabel('Filter rows').fill('dscr');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => exports.length).toBe(1);
        expect((exports[0] as { rows: unknown[] }).rows).toHaveLength(1);
    });

    test('company code is required', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        await mockReport(page, requests, []);
        await page.goto('/reports/covenants');
        await page.locator('#cv-bukrs').fill('');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.getByText('Enter the Company Code.')).toBeVisible();
        expect(requests).toHaveLength(0);
    });
});
