import { expect, test } from '@playwright/test';
import { dialog, openList } from './helpers';
import { MockApi } from './mock-api';

test.describe('Collateral list', () => {

    test('lists the collaterals of the loan with loan facts and compliance status', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);

        await expect(page.getByText('Collaterals of Loan 0000010003200')).toBeVisible();
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('tbody tr').first()).toContainText('912');
        await expect(page.locator('tbody tr').first()).toContainText('Complied');
        await expect(page.getByText('Not Sent for Approval')).toBeVisible();
        await expect(page.getByRole('button', { name: 'Create' })).toBeVisible();
    });

    test('display-only users see no Create, Delete or Send for Approval', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM014' });
        await api.install(page);
        await openList(page);

        await expect(page.getByText('Display only')).toBeVisible();
        await expect(page.getByRole('button', { name: 'Create' })).toHaveCount(0);
        await expect(page.getByRole('button', { name: 'Delete' })).toHaveCount(0);
        await expect(page.getByRole('button', { name: 'Send for Approval' })).toHaveCount(0);
    });

    test('Send for Approval asks for confirmation, sends the checklist id and locks the list', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);

        await page.getByRole('button', { name: 'Send for Approval' }).click();
        await expect(dialog(page)).toContainText('Send the collateral checklist of loan 0000010003200');
        await dialog(page).getByRole('button', { name: 'Send for Approval' }).click();

        await expect(page.getByText('The collateral checklist was sent for approval')).toBeVisible();
        expect(api.sent('PUT', /\/collaterals\/workflow\/startprocess$/)[0].body).toEqual({ businessProcessId: 'C1' });
        await expect(page.getByText('waiting for approval').first()).toBeVisible();
        await expect(page.getByRole('button', { name: 'Send for Approval' })).toBeDisabled();
        await expect(page.getByRole('button', { name: 'Create' })).toHaveCount(0);
    });

    test('a rejected checklist shows the rejection reason and can be sent again', async ({ page }) => {
        const api = new MockApi({ workFlowStatusCode: 4, rejectionReason: 'Security trustee missing' });
        await api.install(page);
        await openList(page);

        await expect(page.getByText('Rejected by the approver: Security trustee missing')).toBeVisible();
        await expect(page.getByRole('button', { name: 'Send for Approval' })).toBeEnabled();
    });

    test('opening the list from a workflow task loads the checklist by its id', async ({ page }) => {
        const api = new MockApi({ workFlowStatusCode: 2 });
        await api.install(page);
        await page.goto('/collateral-management/checklist/C1');

        await expect(page.locator('tbody tr')).toHaveCount(2);
        expect(api.sent('GET', /\/collaterals\/checklists\/C1$/)).toHaveLength(1);
    });

    test('deleting a collateral asks for confirmation and removes the row', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);

        await page.locator('tbody tr').filter({ hasText: '913' }).click();
        await page.getByRole('button', { name: 'Delete' }).click();
        await dialog(page).getByRole('button', { name: 'Delete' }).click();

        await expect(page.locator('tbody tr')).toHaveCount(1);
        expect(api.sent('DELETE', /\/collaterals\/items\/I913$/)).toHaveLength(1);
    });
});
