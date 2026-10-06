import { expect, test } from '@playwright/test';
import { choose, dialog, input, openCollateral912, openTab } from './helpers';
import { MockApi } from './mock-api';

test.describe('Collateral child tables', () => {

    test('Coverage: create a row in the dialog; it gets the next serial number', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);
        await openTab(page, 'Sec. Creation');
        await page.getByRole('button', { name: 'Change' }).click();

        const grid = page.locator('app-collateral-child-grid');
        await grid.getByRole('button', { name: 'Create' }).click();
        await input(dialog(page), 'Effective from date').fill('1/1/2024');
        await choose(dialog(page), 'Coverage Basis', 'Sanction Amount');
        await dialog(page).getByRole('button', { name: 'Save' }).click();

        await expect(grid.locator('tbody tr')).toHaveCount(2);
        const sent = api.sent('POST', /\/items\/I912\/coverages$/)[0].body;
        expect(sent.effectiveFromDate).toBe('2024-01-01');
        expect(sent.coverageBasis).toBe('4');
        await expect(grid.locator('tbody tr').nth(1)).toContainText('2');
    });

    test('Documents: stage and type are required; an uploaded file is linked in the grid', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);
        await openTab(page, 'Documents');
        const grid = page.locator('app-collateral-child-grid');
        await expect(grid.getByText('In SAP')).toBeVisible();
        await page.getByRole('button', { name: 'Change' }).click();

        await grid.getByRole('button', { name: 'Create' }).click();
        await dialog(page).getByRole('button', { name: 'Save' }).click();
        await expect(dialog(page)).toContainText('Check the highlighted fields.');

        await choose(dialog(page), 'Document Stage', 'Creation');
        await choose(dialog(page), 'Document Type', 'Legal Counsel Report');
        await input(dialog(page), 'Document Title').fill('Deed of pledge');
        await dialog(page).locator('input[type=file]').setInputFiles({
            name: 'Deed_of_Pledge_912.pdf', mimeType: 'application/pdf', buffer: Buffer.from('%PDF-1.4 test')
        });
        await expect(dialog(page).getByText('Deed_of_Pledge_912.pdf')).toBeVisible();
        await dialog(page).getByRole('button', { name: 'Save' }).click();

        const sent = api.sent('POST', /\/items\/I912\/documents$/)[0].body;
        expect(sent).toMatchObject({ documentStage: '1', documentType: 'ZPFSLM101', documentTitle: 'Deed of pledge',
            fileReference: '3f2b8c1e-9a4d-4e6f-8b1a-2c3d4e5f6a7b', fileName: 'Deed_of_Pledge_912.pdf' });
        await expect(grid.locator('a.cm-grid-table__file')).toContainText('Deed_of_Pledge_912.pdf');
    });

    test('Sec. Specific: the currency is proposed as INR', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);
        await openTab(page, 'Sec. Specific');
        await page.getByRole('button', { name: 'Change' }).click();

        await page.locator('app-collateral-child-grid').getByRole('button', { name: 'Create' }).click();
        await choose(dialog(page), 'Securities Type', 'Uncalled Share Capital');
        await input(dialog(page), 'Holding %').fill('12.5');
        await dialog(page).getByRole('button', { name: 'Save' }).click();

        const sent = api.sent('POST', /\/items\/I912\/securities$/)[0].body;
        expect(sent).toMatchObject({ securitiesType: 'UNCALLED_SHARE_CAP', nominalValueCurrency: 'INR', holdingPercentage: 12.5 });
    });
});
