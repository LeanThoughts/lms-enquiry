import { expect, test } from '@playwright/test';
import { choose, input, openCollateral912, openList, openTab } from './helpers';
import { MockApi } from './mock-api';

test.describe('Collateral detail', () => {

    test('shows the header block and the 14 tabs in display mode', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);

        await expect(page.getByText('Collateral 912')).toBeVisible();
        await expect(page.locator('.cm-tabs__tab')).toHaveCount(14);
        for (const tab of ['Applicability', 'Sec. Creation', 'Resp. Parties', 'Post Exec', 'Documents', 'Sec. Specific']) {
            await expect(page.locator('.cm-tabs__tab').filter({ hasText: tab })).toHaveCount(1);
        }
        await expect(page.getByRole('button', { name: 'Change' })).toBeVisible();
    });

    test('Change, edit a field and Save sends the changed collateral', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);

        await page.getByRole('button', { name: 'Change' }).click();
        await input(page, 'Coll. Obj. Descr.').fill('Personal Guarantee 200000 CR');
        await page.getByRole('button', { name: 'Save' }).click();

        await expect.poll(() => api.sent('PUT', /\/collaterals\/items\/I912$/).length).toBe(1);
        expect(api.sent('PUT', /\/collaterals\/items\/I912$/)[0].body.collateralObjectDescription).toBe('Personal Guarantee 200000 CR');
        await expect(page.getByRole('button', { name: 'Change' })).toBeVisible();
    });

    test('a new collateral needs the required fields; the number is assigned on save', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);
        await page.getByRole('button', { name: 'Create' }).click();

        await expect(page.getByText('Assigned on save')).toBeVisible();
        await input(page, 'Condition Description').fill('Pledge of shares');
        await page.getByRole('button', { name: 'Save' }).click();
        await expect(page.getByText('Check the highlighted fields.')).toBeVisible();
        expect(api.sent('POST', /\/items$/)).toHaveLength(0);

        await choose(page, 'Collateral Object', 'Z30001');
        await page.getByRole('button', { name: 'Save' }).click();

        await expect.poll(() => api.sent('POST', /\/collaterals\/loans\/L1\/items$/).length).toBe(1);
        const created = api.sent('POST', /\/collaterals\/loans\/L1\/items$/)[0].body;
        expect(created.conditionGroup).toBe('04');
        expect(created.collateralObjectType).toBe('Z30001');
        await expect(page.getByText('Collateral 914')).toBeVisible();
    });

    test('display-only users get the no-edit-access message and no Change button', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM014' });
        await api.install(page);
        await openCollateral912(page);

        await expect(page.getByText('No edit access for the user with the role ZLM014. Opening collateral details in display mode'))
            .toBeVisible();
        await expect(page.getByRole('button', { name: 'Change' })).toHaveCount(0);
    });

    test('while the checklist is in approval the collateral cannot be changed', async ({ page }) => {
        const api = new MockApi({ workFlowStatusCode: 2 });
        await api.install(page);
        await openCollateral912(page);

        await expect(page.getByText('The checklist is sent for approval')).toBeVisible();
        await expect(page.getByRole('button', { name: 'Change' })).toHaveCount(0);
        await openTab(page, 'Sec. Creation');
        await expect(page.locator('app-collateral-child-grid').getByRole('button', { name: 'Create' })).toHaveCount(0);
    });
});
