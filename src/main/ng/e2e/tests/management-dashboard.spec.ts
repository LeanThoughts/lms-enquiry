import { expect, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Management Dashboard: SAP ZPFS_LMS_DASHBOARD_SRV figures, one card per navigation, graph ⇄ table per card. */
test.describe('Management dashboard', () => {

    test('is the second entry of the navigation and loads the SAP snapshot of today', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/homepage');

        // entries of the "Home" group of the main navigation, in order
        const group = page.getByRole('navigation', { name: 'Main navigation' }).getByRole('group', { name: 'Home' });
        const items = group.getByRole('treeitem');
        await expect(items).toHaveText([/Home/, /Management Dashboard/, /Inbox/]);
        await items.nth(1).getByRole('link').click();
        await expect(page).toHaveURL(/\/management-dashboard$/);

        // one call to the API with today's date and amounts in crore
        await expect(page.getByTestId('md-kpis')).toBeVisible();
        const calls = api.sent('GET', /^\/management-dashboard$/);
        expect(calls.length).toBeGreaterThanOrEqual(1);
        expect(calls[0].query['runDate']).toMatch(/^\d{4}-\d{2}-\d{2}$/);
        expect(calls[0].query['amountInCrore']).toBe('true');

        // header from the SAP snapshot
        await expect(page.getByTestId('md-subtitle')).toContainText('FY 2026-27');
        await expect(page.getByTestId('md-subtitle')).toContainText('Snapshot DB-20261128');
        await expect(page.getByTestId('md-subtitle')).toContainText('Updated in SAP 28.11.2026 09:12');

        // KPI strip from the latest quarter with figures (Q2)
        const kpis = page.getByTestId('md-kpis');
        await expect(kpis).toContainText('18,642');
        await expect(kpis).toContainText('3.28%');
        await expect(kpis).toContainText('65.0%');
        await expect(kpis).toContainText('7.82%');
        await expect(kpis).toContainText('▲ 3.4% vs Q1');

        // every section card is there
        for (const id of ['loan-book', 'budget-actual', 'liquidity', 'cash-flow', 'collection-efficiency', 'sma-overdue', 'ecl-overdue',
            'interest-rates', 'ratings', 'proposals']) {
            await expect(page.getByTestId('md-card-' + id)).toBeVisible();
        }
    });

    test('shows graphs by default and switches a card to its table, remembered for the next visit', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/management-dashboard');

        const loanBook = page.getByTestId('md-card-loan-book');
        await expect(loanBook.locator('svg')).toBeVisible();
        await expect(loanBook.locator('table')).toHaveCount(0);
        await expect(loanBook.getByRole('button', { name: 'Graph view' })).toHaveAttribute('aria-pressed', 'true');
        // quarters without figures are placeholders
        await expect(loanBook.locator('svg')).toContainText('n/a');

        await loanBook.getByRole('button', { name: 'Table view' }).click();
        await expect(loanBook.locator('table thead th')).toContainText(['Quarter', 'Gross', 'Net of Prov.', 'Gross NPA', 'Gross NPA %']);
        await expect(loanBook.locator('table tbody tr')).toHaveCount(4);
        await expect(loanBook.locator('table tbody tr').nth(1)).toContainText('18,642.00');
        await expect(loanBook.locator('svg')).toHaveCount(0);

        // the choice is kept per card: other cards stay graphs
        const sma = page.getByTestId('md-card-sma-overdue');
        await expect(sma.locator('app-md-bar-chart')).toBeVisible();
        await expect(sma).toContainText('SMA-0 (1-30 days)');

        // and survives a reload of the page
        await page.reload();
        await expect(page.getByTestId('md-card-loan-book').locator('table')).toBeVisible();
        await expect(page.getByTestId('md-card-sma-overdue').locator('app-md-bar-chart')).toBeVisible();

        // Interest rates and ratings are tables without a switch
        const rates = page.getByTestId('md-card-interest-rates');
        await expect(rates.locator('table')).toBeVisible();
        await expect(rates.getByRole('button', { name: 'Table view' })).toHaveCount(0);
        await expect(rates.locator('tbody tr').first()).toContainText('01.10.2026');
    });

    test('re-reads SAP for another run date and amount unit, and shows SAP\'s message when there is no snapshot', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/management-dashboard');
        await expect(page.getByTestId('md-kpis')).toBeVisible();

        await page.getByRole('radio', { name: '₹', exact: true }).click();
        await expect.poll(() => api.sent('GET', /^\/management-dashboard$/).at(-1)?.query['amountInCrore']).toBe('false');
        await expect(page.getByTestId('md-card-liquidity')).toContainText('₹');

        await page.getByTestId('md-run-date').fill('2025-03-31');
        await page.getByRole('button', { name: 'Refresh' }).click();
        await expect(page.getByTestId('md-error')).toContainText('No dashboard data found for 2025-03-31.');
        await expect(page.getByTestId('md-kpis')).toHaveCount(0);
        expect(api.sent('GET', /^\/management-dashboard$/).at(-1)?.query['runDate']).toBe('2025-03-31');
    });
});
