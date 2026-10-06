import { expect, test } from '@playwright/test';
import { MockApi } from './mock-api';

test.describe('Configuration: Checklist ID Number Range', () => {

    test('ZLM023 sets the highest SAP ZID_NO; the next number follows it', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        await page.goto('/configuration/checklist-id-number-range');
        await expect(page.locator('.cfg-next__value')).toHaveText('914');

        await page.getByRole('button', { name: 'Change' }).click();
        await page.locator('#cfg-sap-highest').fill('500');
        await page.getByRole('button', { name: 'Save' }).click();
        await expect(page.getByText('cannot be below 913')).toBeVisible();

        await page.locator('#cfg-sap-highest').fill('912000');
        await expect(page.getByText('The next collateral gets Checklist ID No.')).toContainText('912001');
        await page.getByRole('button', { name: 'Save' }).click();

        await expect(page.locator('.cfg-next__value')).toHaveText('912001');
        expect(api.sent('PUT', /\/collaterals\/configuration\/checklist-id$/)[0].body).toEqual({ sapHighestNumber: 912000 });
        await expect(page.getByText('Last changed by test@pfs.local')).toBeVisible();
    });

    test('other roles see the number range in display mode', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM035' });
        await api.install(page);
        await page.goto('/configuration/checklist-id-number-range');

        await expect(page.getByText('No edit access for the user with the role ZLM035')).toBeVisible();
        await expect(page.getByRole('button', { name: 'Change' })).toHaveCount(0);
    });
});
