import { expect, test } from '@playwright/test';
import { MockApi } from './mock-api';

/**
 * Reports app: the four modules on the catalogue page, the report list of a module, launching the Workflow Status
 * Report, its SAP-like selection screen, the result with sortable columns and the quick filter.
 */
test.describe('Reports', () => {

    test('the catalogue shows the four modules; a module button opens its report list', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');

        await expect(page.getByRole('heading', { name: 'Reports', level: 1 })).toBeVisible();
        const modules = page.locator('.rp-module');
        await expect(modules).toHaveCount(4);
        await expect(modules.nth(0)).toContainText('Loans Management');
        await expect(modules.nth(1)).toContainText('Collaterals');
        await expect(modules.nth(2)).toContainText('Business Partner');
        await expect(modules.nth(3)).toContainText('Miscellaneous');
        await expect(modules.nth(3)).toContainText('1 report');
        await expect(page.locator('.rp-list')).toHaveCount(0);

        await expect(modules.nth(0)).toContainText('2 reports');
        await modules.nth(1).getByRole('button', { name: 'Show reports' }).click();
        await expect(page.locator('.rp-list')).toContainText('No reports in this module yet.');

        await modules.nth(3).getByRole('button', { name: 'Show reports' }).click();
        await expect(page.locator('.rp-list')).toHaveCount(1);
        await expect(page.locator('.rp-list__item')).toContainText('Workflow Status Report');
        await page.locator('.rp-list__item').getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/workflow-status$/);
        await expect(page.getByRole('heading', { name: 'Loans: Workflow Status Report' })).toBeVisible();
    });

    test('Workflow Status Report: selection defaults, Execute calls SAP with the selection, all columns sortable', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/workflow-status');

        await expect(page.locator('#ws-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#ws-relobj')).toHaveValue('VDARL');
        await expect(page.getByText('No result yet')).toBeVisible();

        await page.locator('#ws-ranl-from').fill('0000010003814');
        await page.locator('#ws-date-from').fill('2026-09-01');
        await page.locator('#ws-date-to').fill('2026-10-12');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect.poll(() => api.sent('GET', /\/reports\/workflow-status/).length).toBe(1);
        const query = api.sent('GET', /\/reports\/workflow-status/)[0].query!;
        expect(JSON.parse(query.contract)).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);   // from only = single value
        expect(JSON.parse(query.date)).toEqual([{ sign: 'I', option: 'BT', low: '2026-09-01', high: '2026-10-12' }]);   // from + to = range

        // the mock filters by contract number: two of the three rows
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('.ws-result-bar__count')).toHaveText('2 rows');
        await expect(page.locator('thead th')).toHaveCount(25);                 // every property of the entity
        await expect(page.locator('thead th').nth(15)).toContainText('Work Item');

        // sort by Date: ascending, then descending, then off
        const dateHeader = page.locator('thead th').nth(4);
        await dateHeader.getByRole('button').click();
        await expect(dateHeader).toHaveAttribute('aria-sort', 'ascending');
        await expect(page.locator('tbody tr').first().locator('td').nth(4)).toHaveText('15.09.2026');
        await dateHeader.getByRole('button').click();
        await expect(dateHeader).toHaveAttribute('aria-sort', 'descending');
        await expect(page.locator('tbody tr').first().locator('td').nth(4)).toHaveText('01.10.2026');
        await dateHeader.getByRole('button').click();
        await expect(dateHeader).toHaveAttribute('aria-sort', 'none');

        // sort by Approver Name (text) works on any column
        const approver = page.locator('thead th').nth(18);
        await approver.getByRole('button').click();
        await expect(page.locator('tbody tr').first().locator('td').nth(18)).toHaveText('Anita Rao');

        // quick filter
        await page.getByLabel('Filter rows').fill('interest');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await expect(page.locator('.ws-result-bar__count')).toHaveText('1 of 2 rows');
        await expect(page.locator('tbody tr').first()).toContainText('Rejected');

        // Excel: the rows as shown (the one filtered row) are posted to the export
        await page.getByRole('button', { name: 'Download Excel' }).first().click();
        await expect.poll(() => api.sent('POST', /\/reports\/workflow-status\/export$/).length).toBe(1);
        const exported = api.sent('POST', /\/reports\/workflow-status\/export$/)[0].body;
        expect(exported.rows).toHaveLength(1);
        expect(exported.rows[0].taskText).toBe('Change interest conditions');
        expect(exported.selection.companyCode).toBe('PFS');
    });

    test('an SAP error is shown on the screen', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.route(/\/enquiry\/api\/reports\/workflow-status/, route => route.fulfill({
            status: 502, contentType: 'application/json',
            body: JSON.stringify({ message: "SAP answered HTTP 400 Bad Request: Property 'Datum' is not filterable" })
        }));
        await page.goto('/reports/workflow-status');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('.ws-strip')).toContainText("Property 'Datum' is not filterable");
    });

    test('ICC Approval Status Report (Loans Management): launch, selection, Execute, sortable columns', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');

        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        const icc = page.locator('.rp-list__item').filter({ hasText: 'ICC Approval Status Report' });
        await expect(icc).toHaveCount(1);
        await icc.getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/icc-approval$/);
        await expect(page.getByRole('heading', { name: 'Loans : ICC Approval Status Report' })).toBeVisible();

        await expect(page.locator('#icc-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#icc-bukrs')).toBeDisabled();
        await expect(page.getByText('No result yet')).toBeVisible();

        await page.locator('#icc-fyear').fill('2026');
        await page.locator('#icc-quarter').selectOption('3');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect.poll(() => api.sent('GET', /\/reports\/icc-approval/).length).toBe(1);
        await expect(page.locator('tbody tr')).toHaveCount(1);                    // the mock filters by quarter
        await expect(page.locator('thead th')).toHaveCount(22);                   // every property of the entity
        await expect(page.locator('tbody tr').first()).toContainText('Himalaya Hydro Pvt Ltd');

        // the selection collapsed after the run: expand it again to change the quarter
        await expect(page.locator('#icc-quarter')).toBeHidden();
        await page.locator('.ws-panel__toggle').click();
        // clear the quarter: both rows; sort by Sanctioned Amount (numeric): ascending, then descending
        await page.locator('#icc-quarter').selectOption('');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('tbody tr').first()).toContainText('1,25,00,00,000.00');   // Indian digit grouping
        const amount = page.locator('thead th').nth(8);
        await amount.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Himalaya Hydro');
        await amount.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power');

        // Excel download posts the rows as shown
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => api.sent('POST', /\/reports\/icc-approval\/export/).length).toBe(1);
    });

    test('column picker: hide / show several columns, remembered, and the Excel follows the visible columns', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/icc-approval');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('thead th')).toHaveCount(22);

        // open the picker, hide three columns
        await page.locator('.ws-cols > button').click();
        const panel = page.locator('.ws-cols__panel');
        await expect(panel).toContainText('22 of 22 shown');
        for (const label of ['Serial Number', 'Project Sector', 'Board App Remarks']) {
            await panel.getByLabel(label, { exact: true }).uncheck();
        }
        await expect(panel).toContainText('19 of 22 shown');
        await expect(page.locator('thead th')).toHaveCount(19);
        await expect(page.locator('thead')).not.toContainText('Serial Number');
        await expect(page.locator('tbody tr').first().locator('td')).toHaveCount(19);

        // click outside closes the picker; the button shows how many columns are visible
        await page.locator('.ws-header__title').click();
        await expect(panel).toHaveCount(0);
        await expect(page.locator('.ws-cols > button')).toContainText('Columns (19/22)');

        // sorting still works on the remaining columns
        await page.locator('thead th').filter({ hasText: 'Borrower Name' }).getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Himalaya Hydro');

        // the Excel download carries the visible columns only
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => api.sent('POST', /\/reports\/icc-approval\/export/).length).toBe(1);
        const exported = api.sent('POST', /\/reports\/icc-approval\/export/)[0].body;
        expect(exported.columns).toHaveLength(19);
        expect(exported.columns).not.toContain('Serial Number');

        // remembered after a reload; Show all brings everything back
        await page.reload();
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('thead th')).toHaveCount(19);
        await page.locator('.ws-cols > button').click();
        await page.locator('.ws-cols__all').click();
        await expect(page.locator('thead th')).toHaveCount(22);
    });

    test('column picker: the last visible column cannot be hidden (Workflow Status Report)', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/workflow-status');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('thead th')).toHaveCount(25);

        await page.locator('.ws-cols > button').click();
        const boxes = page.locator('.ws-cols__list input[type=checkbox]');
        const n = await boxes.count();
        expect(n).toBe(25);
        for (let i = 0; i < n - 1; i++) {
            await boxes.nth(i).uncheck();
        }
        await expect(page.locator('thead th')).toHaveCount(1);
        await expect(boxes.nth(n - 1)).toBeDisabled();                       // the last one stays
    });

    test('Loans KYC Report: SAP-like selection screen with defaults, Execute sends every parameter, sortable result', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/kyc');
        await expect(page.getByRole('heading', { name: 'Loans KYC Report' })).toBeVisible();

        // the defaults of the SAP screen
        await expect(page.locator('#kyc-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#kyc-bukrs')).toBeDisabled();
        await expect(page.locator('#kyc-status-from')).toHaveValue('60');
        await expect(page.locator('#kyc-auth')).not.toBeChecked();
        await expect(page.locator('#kyc-effcap')).toBeChecked();
        await expect(page.locator('#kyc-run-date')).not.toHaveValue('');
        await expect(page.getByText('No result yet')).toBeVisible();

        await page.locator('#kyc-date-from').fill('2026-04-01');
        await page.locator('#kyc-date-to').fill('2026-10-15');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect.poll(() => api.sent('GET', /\/reports\/kyc/).length).toBe(1);
        const sent = api.sent('GET', /\/reports\/kyc/)[0].path;
        expect(sent).toContain('/reports/kyc');
        await expect(page.locator('tbody tr')).toHaveCount(2);
        await expect(page.locator('thead th')).toHaveCount(25);                 // every output property of the entity
        await expect(page.locator('.ws-result-bar__count')).toHaveText('2 rows');
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power Ltd');
        await expect(page.locator('.ws-status').first()).toContainText('Compliant');

        // sort by Borrower Name: ascending, then descending
        const name = page.locator('thead th').filter({ hasText: 'Borrower Name' });
        await name.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Himalaya Hydro');
        await name.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power');

        // hide two columns; the Excel carries the visible ones
        await page.locator('.ws-cols > button').click();
        await page.locator('.ws-cols__panel').getByLabel('PAN', { exact: true }).uncheck();
        await page.locator('.ws-cols__panel').getByLabel('CIN', { exact: true }).uncheck();
        await expect(page.locator('thead th')).toHaveCount(23);
        await page.locator('.ws-header__title').click();
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => api.sent('POST', /\/reports\/kyc\/export/).length).toBe(1);
        const exported = api.sent('POST', /\/reports\/kyc\/export/)[0].body;
        expect(exported.columns).toHaveLength(23);
        expect(exported.columns).not.toContain('PAN');
    });

    test('selection criteria section collapses and expands, and collapses after a successful run', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/kyc');
        const toggle = page.locator('.ws-panel__toggle');
        const groups = page.locator('.ws-filter__groups');
        await expect(toggle).toHaveAttribute('aria-expanded', 'true');
        await expect(groups).toBeVisible();
        await toggle.click();
        await expect(toggle).toHaveAttribute('aria-expanded', 'false');
        await expect(groups).toBeHidden();
        await expect(page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' })).toBeVisible();
        await toggle.click();
        await expect(groups).toBeVisible();
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect.poll(() => api.sent('GET', /\/reports\/kyc/).length).toBe(1);
        await expect(groups).toBeHidden();
        await toggle.click();
        await expect(groups).toBeVisible();
    });

    test('select-options work like SAP: from / to, multiple selection with single values, ranges and exclusions', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/kyc');
        const execute = page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' });

        await page.locator('#kyc-ranl-from').fill('0000010003814');
        await page.locator('#kyc-ranl-to').fill('0000010003900');
        await expect(page.locator('#kyc-ranl-multi')).not.toHaveClass(/so-multi--on/);
        await execute.click();
        await expect.poll(() => api.sent('GET', /\/reports\/kyc/).length).toBe(1);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc/)[0].query!.contract))
            .toEqual([{ sign: 'I', option: 'BT', low: '0000010003814', high: '0000010003900' }]);
        await expect(page.locator('tbody tr')).toHaveCount(2);

        // multiple selection: exclude one contract in the dialog
        await page.locator('.ws-panel__toggle').click();
        await page.locator('#kyc-ranl-multi').click();
        const dialog = page.getByRole('dialog');
        await expect(dialog).toContainText('Multiple Selection for Loan Contract');
        await dialog.getByRole('tab', { name: /Select Ranges/ }).click();
        await expect(dialog.locator('input').first()).toHaveValue('0000010003814');   // the range typed in From / To
        await dialog.getByRole('tab', { name: /Exclude Single Values/ }).click();
        await dialog.locator('input').first().fill('0000010003900');
        await dialog.getByRole('button', { name: 'Copy' }).click();
        await expect(dialog).toBeHidden();
        await expect(page.locator('#kyc-ranl-multi')).toHaveClass(/so-multi--on/);
        await expect(page.locator('.so-multi__count')).toHaveText('2');
        await expect(page.locator('.ws-chip').filter({ hasText: 'Contract' })).toContainText('excl. 0000010003900');

        await execute.click();
        await expect.poll(() => api.sent('GET', /\/reports\/kyc/).length).toBe(2);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc/)[1].query!.contract)).toEqual([
            { sign: 'I', option: 'BT', low: '0000010003814', high: '0000010003900' },
            { sign: 'E', option: 'EQ', low: '0000010003900', high: '' }
        ]);
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power Ltd');
    });

    test('select-option dialog: wildcard, operators, validation, delete all', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/kyc');
        await page.locator('#kyc-ranl-multi').click();
        const dialog = page.getByRole('dialog');
        await dialog.locator('input').first().fill('0000010003*');                 // single value with a wildcard
        await dialog.getByRole('tab', { name: /Select Ranges/ }).click();
        await dialog.locator('input').first().fill('0000010009000');               // Between with only one limit
        await dialog.getByRole('button', { name: 'Copy' }).click();
        await expect(dialog.getByRole('alert')).toContainText('both values');
        await dialog.locator('select').first().selectOption('GE');                  // ≥ needs one value
        await dialog.getByRole('button', { name: 'Copy' }).click();
        await expect(dialog).toBeHidden();

        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect.poll(() => api.sent('GET', /\/reports\/kyc/).length).toBe(1);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc/)[0].query!.contract)).toEqual([
            { sign: 'I', option: 'CP', low: '0000010003*', high: '' },
            { sign: 'I', option: 'GE', low: '0000010009000', high: '' }
        ]);
        await expect(page.locator('tbody tr')).toHaveCount(2);

        // Delete All empties the parameter
        await page.locator('.ws-panel__toggle').click();
        await page.locator('#kyc-ranl-multi').click();
        await dialog.getByRole('button', { name: 'Delete All' }).click();
        await dialog.getByRole('button', { name: 'Copy' }).click();
        await expect(page.locator('#kyc-ranl-from')).toHaveValue('');
        await expect(page.locator('#kyc-ranl-multi')).not.toHaveClass(/so-multi--on/);

        // Cancel keeps the parameter as it was
        await page.locator('#kyc-ranl-from').fill('0000010003814');
        await page.locator('#kyc-ranl-multi').click();
        await dialog.getByRole('tab', { name: /Select Single Values/ }).click();
        await dialog.locator('input').nth(1).fill('0000010003900');
        await dialog.getByRole('button', { name: 'Cancel' }).click();
        await expect(page.locator('#kyc-ranl-multi')).not.toHaveClass(/so-multi--on/);
        await expect(page.locator('#kyc-ranl-from')).toHaveValue('0000010003814');
    });

    test('Loans KYC Report is listed under Loans Management and launches', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        await expect(page.locator('.rp-list__item')).toHaveCount(2);
        await page.locator('.rp-list__item').filter({ hasText: 'Loans KYC Report' }).getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/kyc$/);
    });
});
