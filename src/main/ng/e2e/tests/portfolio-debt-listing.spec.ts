import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Reports › Loans Management › Portfolio Debt Listing (PortfolioDebtListingReportSet). */
const COLUMNS = [
    { key: 'SerialNo', label: 'Serial no.', type: 'number' },
    { key: 'Ranl', label: 'Contract number', type: 'text' },
    { key: 'ProjectName', label: 'Project name', type: 'text' },
    { key: 'SanctionDate', label: 'Sanction date', type: 'date' },
    { key: 'GrossSanction', label: 'Gross sanction amount', type: 'amount' },
    { key: 'AmountOsRptDate', label: 'Outstanding on run date', type: 'amount' },
    { key: 'RevisedCurrentRiskGrade', label: 'Current risk grade', type: 'text' }
];
const ROWS = [
    { SerialNo: '1', Ranl: '0000010003814', ProjectName: 'Solar Park', SanctionDate: '2025-01-01', GrossSanction: '1250000000.00',
        AmountOsRptDate: '900000000.00', RevisedCurrentRiskGrade: 'PFS 3' },
    { SerialNo: '2', Ranl: '0000010003900', ProjectName: 'Run-of-river 60MW', SanctionDate: '2024-06-15', GrossSanction: '450000000.00',
        AmountOsRptDate: null, RevisedCurrentRiskGrade: 'PFS 5' }
];

async function mockReport(page: Page, requests: URLSearchParams[], exports: unknown[]): Promise<void> {
    await page.route(/\/api\/reports\/portfolio-debt-listing\/export/, route => {
        exports.push(route.request().postDataJSON());
        return route.fulfill({ status: 200, contentType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', body: 'xlsx' });
    });
    await page.route(/\/api\/reports\/portfolio-debt-listing\?/, route => {
        const params = new URL(route.request().url()).searchParams;
        requests.push(params);
        // the backend's timing headers (ReportTimingAdvice); cross-origin in the tests, so CORS must expose them
        const origin = route.request().headers()['origin'] ?? '*';
        return route.fulfill({
            headers: { 'Access-Control-Allow-Origin': origin, 'Access-Control-Allow-Credentials': 'true',
                'Access-Control-Expose-Headers': 'X-Report-Server-Ms, X-Report-Sap-Ms, X-Report-Sap-Calls, Server-Timing',
                'X-Report-Server-Ms': '1350', 'X-Report-Sap-Ms': '1200', 'X-Report-Sap-Calls': '1' },
            json: { columns: COLUMNS, rows: ROWS, total: 2, runDate: params.get('runDate'),
                generatedOn: new Date().toISOString(), query: 'PortfolioDebtListingReportSet' } });
    });
}

test.describe('Reports: Portfolio Debt Listing', () => {

    test('the catalogue lists it; it runs for the loan contracts and run date and shows one row per loan', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        const exports: unknown[] = [];
        await mockReport(page, requests, exports);

        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        const item = page.locator('.rp-list__item').filter({ hasText: 'Portfolio Debt Listing' });
        await expect(item).toHaveCount(1);
        await item.getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/portfolio-debt-listing$/);
        await expect(page.locator('.ws-header__title')).toHaveText('Portfolio Debt Listing');

        // the SAP screen: company code PFS (fixed), loan contract range, run date (today)
        await expect(page.locator('#pd-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#pd-bukrs')).toBeDisabled();
        await expect(page.locator('#pd-run-date')).not.toHaveValue('');
        await page.locator('#pd-ranl-from').fill('0000010003814');
        await page.locator('#pd-run-date').fill('2026-10-31');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect(page.locator('tbody tr')).toHaveCount(2);
        // record count and run time: SAP 1.20 s, portal 150 ms, the rest network
        await expect(page.locator('.ws-result-bar__count')).toHaveText('2 records');
        const runTime = page.getByTestId('report-run-time');
        await expect(runTime).toContainText(/\d+(\.\d+)? (ms|s)/);
        await expect(runTime).toContainText('SAP 1.20 s');
        await expect(runTime).toContainText('portal 150 ms');
        await expect(runTime).toContainText('network');
        expect(await runTime.getAttribute('title')).toContain('SAP gateway: 1.20 s');
        expect(requests[0].get('runDate')).toBe('2026-10-31');
        expect(JSON.parse(requests[0].get('contract')!)).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);
        await expect(page.locator('thead th')).toHaveText(COLUMNS.map(c => c.label));
        const first = page.locator('tbody tr').first().locator('td');
        await expect(first.nth(3)).toHaveText('01.01.2025');                 // dates as dd.MM.yyyy
        await expect(first.nth(4)).toHaveText('1,25,00,00,000.00');         // amounts with Indian digit grouping

        // sort by gross sanction (numerically), then filter rows
        await page.locator('th', { hasText: 'Gross sanction amount' }).getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Run-of-river 60MW');
        await page.getByLabel('Filter rows').fill('solar');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await expect(page.locator('.ws-result-bar__count')).toHaveText('1 of 2 records');

        // hide a column; the Excel carries the visible columns and rows as shown
        await page.locator('.ws-cols > button').click();
        await page.locator('.ws-cols__panel').getByLabel('Current risk grade', { exact: true }).uncheck();
        await expect(page.locator('thead th')).toHaveCount(COLUMNS.length - 1);
        await page.locator('.ws-header__title').click();
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => exports.length).toBe(1);
        const body = exports[0] as { rows: unknown[], columns: { key: string }[], selection: { runDate: string } };
        expect(body.rows).toHaveLength(1);
        expect(body.columns.map(c => c.key)).not.toContain('RevisedCurrentRiskGrade');
        expect(body.selection.runDate).toBe('2026-10-31');
    });

    test('a run date is required', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const requests: URLSearchParams[] = [];
        await mockReport(page, requests, []);
        await page.goto('/reports/portfolio-debt-listing');
        await page.locator('#pd-run-date').fill('');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.getByText('Enter the Run Date.')).toBeVisible();
        expect(requests).toHaveLength(0);
    });
});
