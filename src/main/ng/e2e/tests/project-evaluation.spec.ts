import { expect, test } from '@playwright/test';
import { choose, dialog, input, openList } from './helpers';
import { MockApi } from './mock-api';

/** Maintain Project Valuation (SAP tab Project(Coll.) Valuation, table ZLMS_COLL_EVAL). */
test.describe('Project valuation', () => {

    test('opens from the collaterals of the loan and shows the Major Collateral Type and the valuations', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);

        await page.getByRole('button', { name: 'Maintain Project Valuation' }).click();
        await expect(page).toHaveURL(/\/collateral-management\/loan\/L1\/project-evaluation$/);
        await expect(page.getByText('Project Valuation of Loan 0000010003200')).toBeVisible();
        await expect(page.locator('app-collateral-field').filter({ hasText: 'Major Collateral Type' })).toContainText('Others');

        const grid = page.locator('app-collateral-child-grid');
        await expect(grid.locator('thead th')).toContainText(['Line Item No.', 'Valuation By', 'Valuation Date', 'Total Loan Asset',
            'Total Current Liabilities', 'Net (Valuation Amount)', 'Currency', 'Remarks']);
        await expect(grid.locator('tbody tr')).toHaveCount(2);
        await expect(grid.locator('tbody tr').first()).toContainText('Credit Team');
        await expect(grid.locator('tbody tr').first()).toContainText('1,21,212.00');
        await expect(page.getByTestId('pv-latest')).toContainText('INR 1,21,212.00');
        await expect(page.getByTestId('pv-latest')).toContainText('01.01.2005 · Credit Team');

        // back to the collaterals of the loan
        await page.getByRole('button', { name: 'Back to the collaterals of the loan' }).click();
        await expect(page).toHaveURL(/\/collateral-management\/loan\/L1$/);
    });

    test('New Valuation posts to the loan and gets the next line item number', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/collateral-management/loan/L1/project-evaluation');
        const grid = page.locator('app-collateral-child-grid');
        await expect(grid.locator('tbody tr')).toHaveCount(2);

        await grid.getByRole('button', { name: 'New Valuation' }).click();
        await expect(dialog(page)).toContainText('Valuation – New');
        await dialog(page).getByRole('button', { name: 'Save' }).click();
        await expect(dialog(page)).toContainText('Check the highlighted fields.');

        await choose(dialog(page), 'Valuation By', 'Valuer');
        await input(dialog(page), 'Valuation Date').fill('3/31/2026');
        await input(dialog(page), 'Total Loan Asset').fill('250000000');
        await input(dialog(page), 'Total Current Liabilities').fill('40000000');
        await input(dialog(page), 'Net (Valuation Amount)').fill('210000000.50');
        await input(dialog(page), 'Remarks').fill('Independent valuer report');
        await dialog(page).getByRole('button', { name: 'Save' }).click();

        await expect(grid.locator('tbody tr')).toHaveCount(3);
        const sent = api.sent('POST', /^\/collaterals\/loans\/L1\/project-evaluations$/)[0].body;
        expect(sent.valuationBy).toBe('2');
        expect(sent.valuationDate).toBe('2026-03-31');
        expect(sent.currency).toBe('INR');
        expect(Number(sent.netValuationAmount)).toBe(210000000.5);
        await expect(grid.locator('tbody tr').nth(2)).toContainText('3');
        await expect(grid.locator('tbody tr').nth(2)).toContainText('21,00,00,000.50');
        await expect(page.getByTestId('pv-latest')).toContainText('31.03.2026');
    });

    test('Change Valuation and Delete Valuation act on the selected row', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/collateral-management/loan/L1/project-evaluation');
        const grid = page.locator('app-collateral-child-grid');

        await expect(grid.getByRole('button', { name: 'Change Valuation' })).toBeDisabled();
        await grid.locator('tbody tr').nth(1).click();
        await grid.getByRole('button', { name: 'Change Valuation' }).click();
        await input(dialog(page), 'Remarks').fill('Revalued');
        await dialog(page).getByRole('button', { name: 'Save' }).click();
        await expect(grid.locator('tbody tr').nth(1)).toContainText('Revalued');
        expect(api.sent('PUT', /^\/collaterals\/project-evaluations\/PV2$/)[0].body.remarks).toBe('Revalued');

        await grid.getByRole('button', { name: 'Delete Valuation' }).click();
        await dialog(page).getByRole('button', { name: 'Delete' }).click();
        await expect(grid.locator('tbody tr')).toHaveCount(1);
        expect(api.sent('DELETE', /^\/collaterals\/project-evaluations\/PV2$/)).toHaveLength(1);
    });

    test('the Major Collateral Type is saved when it is chosen', async ({ page }) => {
        const api = new MockApi();
        api.majorCollateralType = null;
        await api.install(page);
        await page.goto('/collateral-management/loan/L1/project-evaluation');
        await expect(page.locator('app-collateral-child-grid tbody tr')).toHaveCount(2);

        await choose(page, 'Major Collateral Type', 'Others');
        await expect(page.getByText('Major Collateral Type saved.')).toBeVisible();
        expect(api.sent('PUT', /\/project-evaluations\/major-collateral-type$/)[0].body).toEqual({ majorCollateralType: '1' });
    });

    test('Send for Approval starts the checklist workflow and locks the valuations', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/collateral-management/loan/L1/project-evaluation');
        await expect(page.locator('app-collateral-child-grid tbody tr')).toHaveCount(2);

        await page.getByRole('button', { name: 'Send for Approval' }).click();
        await expect(dialog(page)).toContainText('with its 2 valuations for approval');
        await dialog(page).getByRole('button', { name: 'Send for Approval' }).click();

        await expect(page.getByText('sent for approval with the collateral checklist')).toBeVisible();
        expect(api.sent('PUT', /\/collaterals\/workflow\/startprocess$/)[0].body).toMatchObject({ businessProcessId: 'C1' });
        await expect(page.getByText('Sent for Approval', { exact: true })).toBeVisible();
        await expect(page.locator('app-collateral-child-grid').getByRole('button', { name: 'New Valuation' })).toHaveCount(0);
        await expect(page.getByRole('button', { name: 'Send for Approval' })).toBeDisabled();
    });

    test('while the checklist waits for approval nothing can be changed', async ({ page }) => {
        const api = new MockApi({ workFlowStatusCode: 2 });
        await api.install(page);
        await page.goto('/collateral-management/loan/L1/project-evaluation');

        const grid = page.locator('app-collateral-child-grid');
        await expect(grid.locator('tbody tr')).toHaveCount(2);
        await expect(page.getByText('waiting for approval. Valuations and the Major Collateral Type')).toBeVisible();
        await expect(page.getByText('Sent for Approval')).toBeVisible();
        await expect(grid.getByRole('button', { name: 'New Valuation' })).toHaveCount(0);
        await expect(page.locator('app-collateral-field').filter({ hasText: 'Major Collateral Type' }).locator('fd-select')).toHaveCount(0);
    });

    test('display-only users see the valuations without New, Change or Delete', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM014' });
        await api.install(page);
        await openList(page);
        await page.getByRole('button', { name: 'Maintain Project Valuation' }).click();

        const grid = page.locator('app-collateral-child-grid');
        await expect(grid.locator('tbody tr')).toHaveCount(2);
        await expect(page.getByText('Display only')).toBeVisible();
        await expect(grid.getByRole('button', { name: 'New Valuation' })).toHaveCount(0);
        await expect(grid.getByRole('button', { name: 'Delete Valuation' })).toHaveCount(0);
        await expect(page.locator('app-collateral-field').filter({ hasText: 'Major Collateral Type' }).locator('fd-select')).toHaveCount(0);

        await grid.locator('tbody tr').first().dblclick();
        await expect(dialog(page)).toContainText('Credit Team');
    });
});
