import { expect, test } from '@playwright/test';
import { dialog, openCollateral912, openList, openTab } from './helpers';
import { MockApi } from './mock-api';

/**
 * "Technical Info" on every collateral screen: the checklist (list), the collateral (detail) and each child record
 * (dialog) show the record's id and audit trail — created on / at / by, changed on / at / by.
 */
test.describe('Technical Info', () => {

    /** The value shown next to a label in the Technical Info dialog. */
    function value(page: Parameters<typeof dialog>[0], label: string) {
        return dialog(page).locator('dt', { hasText: new RegExp(`^${label}$`) }).locator('xpath=following-sibling::dd[1]');
    }

    test('checklist: the list shows the checklist\'s id and audit trail', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openList(page);

        await page.getByRole('button', { name: 'Technical Info' }).click();
        await expect(dialog(page)).toContainText('Technical Info');
        await expect(dialog(page)).toContainText('Collateral checklist of 0000010003200');
        await expect(value(page, 'Id')).toHaveText('C1');
        await expect(value(page, 'Created on')).toHaveText('01.10.2026');
        await expect(value(page, 'Created at')).toHaveText('08:15:30');          // seconds kept, fraction dropped
        await expect(value(page, 'Created by')).toHaveText('writer@pfs.local');
        await expect(value(page, 'Changed on')).toHaveText('09.10.2026');
        await expect(value(page, 'Changed at')).toHaveText('17:45:05');
        await expect(value(page, 'Changed by')).toHaveText('legal@pfs.local');

        await dialog(page).getByRole('button', { name: 'Close' }).click();
        await expect(page.locator('fd-dialog')).toHaveCount(0);
    });

    test('collateral: the detail screen shows the collateral\'s id and audit trail', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);

        await page.getByRole('button', { name: 'Technical Info' }).click();
        await expect(dialog(page)).toContainText('Collateral 912');
        await expect(value(page, 'Id')).toHaveText('I912');
        await expect(value(page, 'Created on')).toHaveText('05.10.2026');
        await expect(value(page, 'Created at')).toHaveText('09:06:12');
        await expect(value(page, 'Created by')).toHaveText('test@pfs.local');
        await expect(value(page, 'Changed on')).toHaveText('08.10.2026');
        await expect(value(page, 'Changed at')).toHaveText('14:30:00');
        await expect(value(page, 'Changed by')).toHaveText('writer@pfs.local');
        await dialog(page).getByRole('button', { name: 'Close' }).click();
    });

    test('child record: the coverage dialog shows the row\'s id; never-changed fields show a dash', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);
        await openTab(page, 'Sec. Creation');

        // open the existing coverage row (display mode) and ask for its technical info
        const grid = page.locator('app-collateral-child-grid');
        await grid.locator('tbody tr').first().dblclick();
        const child = dialog(page);
        await expect(child).toContainText('Display');
        await child.getByRole('button', { name: 'Technical Info' }).click();

        // the technical info dialog opens on top of the child dialog
        const info = page.locator('fd-dialog').last();
        await expect(info).toContainText('Technical Info');
        await expect(info.locator('dt', { hasText: /^Id$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('V1');
        await expect(info.locator('dt', { hasText: /^Created on$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('03.10.2026');
        await expect(info.locator('dt', { hasText: /^Created at$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('11:02:44');
        await expect(info.locator('dt', { hasText: /^Created by$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('writer@pfs.local');
        await expect(info.locator('dt', { hasText: /^Changed on$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('–');
        await expect(info.locator('dt', { hasText: /^Changed by$/ }).locator('xpath=following-sibling::dd[1]')).toHaveText('–');
        await info.getByRole('button', { name: 'Close' }).click();
        await expect(page.locator('fd-dialog')).toHaveCount(1);   // back to the child dialog
    });

    test('child record: a new (unsaved) row has no technical info yet', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await openCollateral912(page);
        await openTab(page, 'Sec. Creation');
        await page.getByRole('button', { name: 'Change' }).click();
        await page.locator('app-collateral-child-grid').getByRole('button', { name: 'Create' }).click();

        await expect(dialog(page)).toContainText('New');
        await expect(dialog(page).getByRole('button', { name: 'Technical Info' })).toHaveCount(0);
    });
});
