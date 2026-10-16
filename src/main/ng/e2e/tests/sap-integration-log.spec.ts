import { expect, test } from '@playwright/test';
import { dialog } from './helpers';
import { MockApi } from './mock-api';

/**
 * Administration > SAP Integration Log: every HTTP request to SAP with its payload and outcome. Totals, the list,
 * the failed-only shortcut, the text filter, the detail dialog with request and response, export and purge.
 */
test.describe('SAP Integration Log', () => {

    async function openReport(page: Parameters<typeof dialog>[0]) {
        await page.goto('/sap-integration-log');
        await expect(page.getByRole('heading', { name: 'SAP Integration Log' })).toBeVisible();
        await expect(page.locator('tbody tr').first()).toBeVisible();
    }

    test('lists the calls newest first with totals, status and error', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openReport(page);

        await expect(page.locator('.sl-kpi').first()).toContainText('4');
        await expect(page.locator('.sl-kpi').nth(1)).toContainText('2');           // failed
        await expect(page.locator('.sl-kpi').nth(2)).toContainText('50%');         // success rate
        const rows = page.locator('tbody tr');
        await expect(rows).toHaveCount(4);
        await expect(rows.nth(0)).toContainText('PUT');
        await expect(rows.nth(0)).toContainText("DocumentSet(ZidNo='0000001037'");
        await expect(rows.nth(0)).toContainText('400');
        await expect(rows.nth(0)).toContainText('Invalid key predicate');
        await expect(rows.nth(0)).toHaveClass(/sl-row--failed/);
        await expect(rows.nth(2)).toContainText('201');
        await expect(rows.nth(2)).not.toHaveClass(/sl-row--failed/);
        await expect(rows.nth(3)).toContainText('Connection refused');
        await expect(page.getByText('entries are kept for 90 days')).toBeVisible();
    });

    test('the Failed tile and the search narrow the list', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openReport(page);

        await page.locator('.sl-kpi--button').click();
        await expect.poll(() => api.sent('GET', /\/administration\/sap-http-log(\?|$)/).length).toBeGreaterThan(1);
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('#sl-outcome')).toHaveValue('failed');

        await page.locator('#sl-search').fill('DocumentSet(');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await page.getByRole('button', { name: 'Clear' }).click();
        await expect(page.locator('tbody tr')).toHaveCount(4);
    });

    test('a row opens the call with request headers, payload, response and SAP error', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openReport(page);

        await page.locator('tbody tr').first().click();
        await expect(dialog(page)).toContainText('SAP Call 4 · PUT DocumentSet');
        await expect(dialog(page)).toContainText('Invalid key predicate');
        await expect(dialog(page)).toContainText('6AB73D1A84A1F08AE1000000C0A801CD');
        await expect(dialog(page)).toContainText('Authorization: Basic ***');
        await expect(dialog(page)).toContainText('"DocTitle": "Deed of Pledge"');          // pretty-printed payload
        await expect(dialog(page)).toContainText('"transactionid"');
        await expect(dialog(page)).toContainText('Collateral Management · Collateral Documents');
        await dialog(page).getByRole('button', { name: 'Close' }).click();
        await expect(dialog(page)).toHaveCount(0);
    });

    test('purge asks for confirmation and reloads; display-only roles get no access', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openReport(page);

        await page.locator('#sl-purge-days').fill('30');
        await page.getByRole('button', { name: 'Delete' }).click();
        await expect(dialog(page)).toContainText('more than 30 days ago');
        await dialog(page).getByRole('button', { name: 'Delete' }).click();
        await expect.poll(() => api.sent('DELETE', /\/administration\/sap-http-log/).length).toBe(1);
        expect(api.sent('DELETE', /\/administration\/sap-http-log/)[0].path).toBe('/administration/sap-http-log');

        const reader = new MockApi({ role: 'ZLM014' });
        await reader.install(page);
        await page.goto('/sap-integration-log');
        await expect(page.getByText('No access to this report for the user with the role ZLM014')).toBeVisible();
    });
});
