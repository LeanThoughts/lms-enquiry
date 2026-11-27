import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Reports › Collaterals › Collateral Securities Report (CollateralSecurityReportSet). */
const COLUMNS = [
    { key: 'ZslNo', label: 'Serial no.', type: 'number' },
    { key: 'Ranl', label: 'Contract number', type: 'text' },
    { key: 'ZloanAcName', label: 'Project name', type: 'text' },
    { key: 'ZconditionDesc', label: 'Condition description', type: 'text' },
    { key: 'ZcomplianceText', label: 'Compliance status', type: 'text' },
    { key: 'ZcomplianceDate', label: 'Compliance date', type: 'date' },
    { key: 'PenalChgAppl', label: 'Penal charges applicable', type: 'text' },
    { key: 'ZsecValue', label: 'Nominal value', type: 'amount' }
];
const ROWS = [
    { ZslNo: '1', Ranl: '0000010003814', ZloanAcName: 'Solar Park', ZconditionDesc: 'Mortgage of project land',
        ZcomplianceText: 'Complied', ZcomplianceDate: '2025-01-01', PenalChgAppl: 'No', ZsecValue: '125000000.00' },
    { ZslNo: '2', Ranl: '0000010003814', ZloanAcName: 'Solar Park', ZconditionDesc: 'Pledge of 51% shares',
        ZcomplianceText: 'Not Complied', ZcomplianceDate: null, PenalChgAppl: 'Yes', ZsecValue: '5000000.00' }
];

async function mockReport(page: Page, requests: URLSearchParams[], exports: unknown[]): Promise<void> {
    await page.route(/\/api\/reports\/collateral-securities\/export/, route => {
        exports.push(route.request().postDataJSON());
        return route.fulfill({ status: 200, contentType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', body: 'xlsx' });
    });
    await page.route(/\/api\/reports\/collateral-securities\?/, route => {
        requests.push(new URL(route.request().url()).searchParams);
        return route.fulfill({ json: { columns: COLUMNS, rows: ROWS, total: 2, generatedOn: new Date().toISOString(),
            query: 'CollateralSecurityReportSet' } });
    });
}

test.describe('Reports: Collateral Securities Report', () => {

    test('listed under Collaterals; runs with the SAP selection screen and shows one row per collateral condition', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        const exports: unknown[] = [];
        await mockReport(page, requests, exports);

        await page.goto('/reports');
        await page.locator('.rp-module').nth(1).getByRole('button', { name: 'Show reports' }).click();
        const item = page.locator('.rp-list__item').filter({ hasText: 'Collateral Securities Report' });
        await item.getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/collateral-securities$/);
        await expect(page.locator('.ws-header__title')).toHaveText('Collateral Securities Report');
        await expect(page.locator('.ws-crumbs')).toContainText('Collaterals');

        // the SAP screen: company code PFS, loan number and loan partner ranges, five filters all checked
        await expect(page.locator('#cs-bukrs')).toHaveValue('PFS');
        for (const id of ['complied', 'notComplied', 'inProcess', 'partiallyComplied', 'timelineAvailable']) {
            await expect(page.locator(`#cs-${id}`)).toBeChecked();
        }
        await page.locator('#cs-ranl-from').fill('0000010003814');
        await page.locator('#cs-gpart-from').fill('1000000123');
        await page.locator('#cs-gpart-to').fill('1000000199');
        await page.getByLabel('In Process').uncheck();
        await expect(page.locator('.ws-filter__summary')).toContainText('Only Complied, Not Complied, Partially Complied, Timeline Available');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect(page.locator('tbody tr')).toHaveCount(2);
        const q = requests[0];
        expect(q.get('companyCode')).toBe('PFS');
        expect(JSON.parse(q.get('contract')!)).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);
        expect(JSON.parse(q.get('partner')!)).toEqual([{ sign: 'I', option: 'BT', low: '1000000123', high: '1000000199' }]);
        expect([q.get('complied'), q.get('notComplied'), q.get('inProcess'), q.get('partiallyComplied'), q.get('timelineAvailable')])
            .toEqual(['true', 'true', 'false', 'true', 'true']);

        await expect(page.locator('thead th')).toHaveText(COLUMNS.map(c => c.label));
        await expect(page.locator('.ws-result-bar__count')).toHaveText('2 records');
        const first = page.locator('tbody tr').first().locator('td');
        await expect(first.nth(5)).toHaveText('01.01.2025');
        await expect(first.nth(7)).toHaveText('12,50,00,000.00');

        // quick filter and Excel of the rows as shown
        await page.getByLabel('Filter rows').fill('pledge');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => exports.length).toBe(1);
        const body = exports[0] as { rows: unknown[], selection: { inProcess: boolean } };
        expect(body.rows).toHaveLength(1);
        expect(body.selection.inProcess).toBe(false);
    });

    test('company code and at least one filter are required', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        await mockReport(page, requests, []);
        await page.goto('/reports/collateral-securities');
        for (const label of ['Complied', 'Not Complied', 'In Process', 'Partially Complied', 'Timeline Available']) {
            await page.getByLabel(label, { exact: true }).uncheck();
        }
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.getByText('Select at least one filter')).toBeVisible();
        await page.getByLabel('Complied', { exact: true }).check();
        await page.locator('#cs-bukrs').fill('');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.getByText('Enter the Company Code.')).toBeVisible();
        expect(requests).toHaveLength(0);
    });
});
