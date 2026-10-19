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

        await expect(modules.nth(0)).toContainText('4 reports');
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

        await expect.poll(() => api.sent('GET', /\/reports\/workflow-status$/).length).toBe(1);
        const query = api.sent('GET', /\/reports\/workflow-status$/)[0].query!;
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

        await expect.poll(() => api.sent('GET', /\/reports\/icc-approval$/).length).toBe(1);
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

        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(1);
        const sent = api.sent('GET', /\/reports\/kyc$/)[0].path;
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

    test('Loans : Balance List: SAP-like selection screen, Execute sends every parameter, amounts, columns, Excel', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        await page.locator('.rp-list__item').filter({ hasText: 'Loans : Balance List' }).getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/balance-list$/);
        await expect(page.getByRole('heading', { name: 'Loans : Balance List' })).toBeVisible();

        // the defaults of the SAP screen
        await expect(page.locator('#bl-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#bl-bukrs')).toBeDisabled();
        await expect(page.locator('#bl-completed')).toBeChecked();
        await expect(page.locator('#bl-key-date')).not.toHaveValue('');
        await expect(page.locator('#bl-nonzero')).not.toBeChecked();
        await expect(page.getByText('Evaluate completed contracts?')).toBeVisible();
        await expect(page.getByText('ALV List')).toHaveCount(0);
        await expect(page.getByText('Classic List')).toHaveCount(0);
        await expect(page.locator('.rv__menu > .rv__btn')).toContainText('Variant');

        await page.locator('#bl-key-date').fill('2026-10-17');
        await page.locator('#bl-display-currency').fill('inr');
        await page.locator('#bl-currency-from').fill('INR');
        await page.locator('#bl-nonzero').check();
        await page.locator('#bl-totals-only').check();
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect.poll(() => api.sent('GET', /\/reports\/balance-list$/).length).toBe(1);
        const query = api.sent('GET', /\/reports\/balance-list$/)[0].query!;
        expect(query.keyDate).toBe('2026-10-17');
        expect(query.displayCurrency).toBe('inr');
        expect(query.nonZeroOnly).toBe('true');
        expect(query.totalsOnly).toBe('true');
        expect(query.evaluateCompleted).toBe('true');
        expect(JSON.parse(query.currency)[0]).toMatchObject({ option: 'EQ', low: 'INR' });
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await expect(page.locator('thead th')).toHaveCount(22);
        await expect(page.locator('.ws-result-bar__count')).toHaveText('1 row');
        await expect(page.locator('tbody tr').first()).toContainText('Solar Park Phase 2');
        await expect(page.locator('tbody tr').first()).toContainText('3,25,00,000.00');   // grouped amount
        await expect(page.locator('.ws-filter')).toHaveClass(/ws-filter--collapsed/);

        // no restriction on the currency: both contracts; sort by Nominal Capital
        await page.getByRole('button', { name: /Selection Criteria/ }).click();
        await page.locator('#bl-currency-from').fill('');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.locator('tbody tr')).toHaveCount(2);
        const nominal = page.locator('thead th').filter({ hasText: 'Nominal Capital' });
        await nominal.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Run-of-river');
        await nominal.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Solar Park');

        // hide a column; the Excel carries the visible ones
        await page.locator('.ws-cols > button').click();
        await page.locator('.ws-cols__panel').getByLabel('Versch.', { exact: true }).uncheck();
        await expect(page.locator('thead th')).toHaveCount(21);
        await page.locator('.ws-header__title').click();
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => api.sent('POST', /\/reports\/balance-list\/export/).length).toBe(1);
        const exported = api.sent('POST', /\/reports\/balance-list\/export/)[0].body;
        expect(exported.columns).toHaveLength(21);
        expect(exported.columns).not.toContain('Versch.');
    });

    test('Loans : Balance List: variants save the selection (not the key date) and load it again', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/balance-list');
        await page.locator('#bl-ranl-from').fill('0000010003814');
        await page.locator('#bl-nonzero').check();
        await page.locator('#bl-display-currency').fill('USD');
        await page.getByRole('button', { name: 'Save as variant' }).click();
        const dialog = page.getByRole('dialog', { name: 'Save as Variant' });
        await dialog.getByLabel('Variant name').fill('Non-zero USD');
        await dialog.getByRole('button', { name: 'Save', exact: true }).click();
        await expect(page.getByText('Variant Non-zero USD saved.')).toBeVisible();
        expect(api.variants).toHaveLength(1);
        const saved = api.variants[0].parameters;
        expect(saved.contract).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);
        expect(saved.nonZeroOnly).toBe(true);
        expect(saved.displayCurrency).toBe('USD');
        expect(saved.keyDate).toBeUndefined();
        expect(saved.companyCode).toBeUndefined();

        await page.locator('#bl-ranl-from').fill('');
        await page.locator('#bl-nonzero').uncheck();
        await page.locator('#bl-display-currency').fill('');
        await page.locator('.rv__menu > .rv__btn').click();
        await page.getByRole('menuitem').filter({ hasText: 'Non-zero USD' }).getByRole('button').first().click();
        await expect(page.locator('#bl-ranl-from')).toHaveValue('0000010003814');
        await expect(page.locator('#bl-nonzero')).toBeChecked();
        await expect(page.locator('#bl-display-currency')).toHaveValue('USD');
        await expect(page.locator('#bl-key-date')).not.toHaveValue('');
    });

    test('Loans : Disbursement report: selection screen, Execute sends every parameter, SAP cells shown as a table', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        await page.locator('.rp-list__item').filter({ hasText: 'Disbursement' }).getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/disbursement$/);
        await expect(page.getByRole('heading', { name: /Disbursement/ })).toBeVisible();

        // the defaults of the SAP screen
        await expect(page.locator('#db-bukrs')).toHaveValue('PFS');
        await expect(page.locator('#db-mode-report')).toBeChecked();
        await expect(page.locator('#db-mode-comparative')).not.toBeChecked();
        await expect(page.locator('#db-fy-from')).toHaveValue(/^\d{4}$/);
        await expect(page.locator('#db-fy-to')).toHaveValue('');
        await expect(page.locator('#db-period')).toHaveValue('M');
        await expect(page.locator('#db-all-loans')).toBeChecked();
        await expect(page.locator('#db-suppress-zero')).toBeChecked();
        await expect(page.locator('.rv__menu > .rv__btn')).toContainText('Variant');

        // validation of the year
        await page.locator('#db-fy-from').fill('26');
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();
        await expect(page.getByText('Fiscal Year: 26 is not a year (yyyy).')).toBeVisible();
        expect(api.sent('GET', /\/reports\/disbursement$/)).toHaveLength(0);

        await page.locator('#db-fy-from').fill('2025');
        await page.locator('#db-fy-to').fill('2026');
        await page.locator('#db-mode-comparative').check();
        await page.locator('#db-period').selectOption('Q');
        await page.locator('#db-project-type').fill('Solar');
        await page.locator('#db-suppress-zero').uncheck();
        await page.locator('.ws-filter__buttons').getByRole('button', { name: 'Execute' }).click();

        await expect.poll(() => api.sent('GET', /\/reports\/disbursement$/).length).toBe(1);
        const query = api.sent('GET', /\/reports\/disbursement$/)[0].query!;
        expect(query).toMatchObject({ companyCode: 'PFS', mode: 'C', fiscalYearFrom: '2025', fiscalYearTo: '2026', periodType: 'Q',
            projectType: 'Solar', allLoans: 'true', suppressZero: 'false' });

        // one column per SAP Fieldlabel, one row per RowNo
        await expect(page.locator('thead th')).toHaveCount(5);
        await expect(page.locator('thead th').nth(2)).toContainText('Apr-2026');
        await expect(page.locator('tbody tr')).toHaveCount(3);
        await expect(page.locator('.ws-result-bar__count')).toHaveText('3 rows');
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power Ltd');
        await expect(page.locator('tbody tr').first().locator('td').nth(0)).toHaveText('0000010003814');   // ids keep their zeros
        await expect(page.locator('tbody tr').nth(1).locator('td').nth(2)).toHaveText('');                 // no Value for that cell
        await expect(page.locator('tbody tr').first().locator('td').nth(2)).toHaveClass(/ws-td--num/);
        await expect(page.locator('tbody tr').first().locator('td').nth(0)).not.toHaveClass(/ws-td--num/);
        await expect(page.locator('.ws-filter')).toHaveClass(/ws-filter--collapsed/);

        // amounts sort as numbers: 125.25- < 250.50 < 1,000.00 < 9,500.00 in the May-2026 column
        const may = page.locator('thead th').filter({ hasText: 'May-2026' });
        await may.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Coastal Wind');
        await may.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Sunrise Power');

        // hide a column; the Excel carries the visible ones with their keys
        await page.locator('.ws-cols > button').click();
        await page.locator('.ws-cols__panel').getByLabel('Borrower', { exact: true }).uncheck();
        await expect(page.locator('thead th')).toHaveCount(4);
        await page.locator('.ws-header__title').click();
        await page.getByRole('button', { name: 'Download Excel' }).click();
        await expect.poll(() => api.sent('POST', /\/reports\/disbursement\/export/).length).toBe(1);
        const exported = api.sent('POST', /\/reports\/disbursement\/export/)[0].body;
        expect(exported.columns.map((c: any) => c.label)).toEqual(['Loan Contract', 'Apr-2026', 'May-2026', 'Total']);
        expect(exported.columns[1].key).toBe('c2');
        expect(exported.rows).toHaveLength(3);
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
        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(1);
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
        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(1);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc$/)[0].query!.contract))
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
        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(2);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc$/)[1].query!.contract)).toEqual([
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
        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(1);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc$/)[0].query!.contract)).toEqual([
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

    test('variants: save the selection under a name, load it again, default on open, run, delete', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports/kyc');
        await expect(page.locator('.rv__menu > .rv__btn')).toContainText('Variant');

        // fill in the selection and save it as the default variant
        await page.locator('#kyc-ranl-from').fill('0000010003814');
        await page.getByRole('button', { name: 'Save as variant' }).click();
        const dialog = page.getByRole('dialog', { name: 'Save as Variant' });
        await dialog.getByLabel('Variant name').fill('Sunrise only');
        await dialog.getByLabel('Use as my default variant').check();
        await dialog.getByRole('button', { name: 'Save', exact: true }).click();
        await expect(page.getByText('Variant Sunrise only saved.')).toBeVisible();
        expect(api.variants).toHaveLength(1);
        expect(api.variants[0].parameters.contract).toEqual([{ sign: 'I', option: 'EQ', low: '0000010003814', high: '' }]);
        expect(api.variants[0].parameters.status).toEqual([{ sign: 'I', option: 'EQ', low: '60', high: '' }]);
        expect(api.variants[0].parameters.runDate).toBeUndefined();          // today's date and the fixed company code are not saved
        expect(api.variants[0].parameters.companyCode).toBeUndefined();
        await expect(page.locator('.rv__menu > .rv__btn')).toContainText('Sunrise only');
        await expect(page.locator('.rv__changed')).toHaveCount(0);

        // changing the screen shows the variant as modified; loading it restores the saved values
        await page.locator('#kyc-ranl-from').fill('0000010003900');
        await expect(page.locator('.rv__changed')).toBeVisible();
        await page.locator('.rv__menu > .rv__btn').click();
        await page.getByRole('menuitem').filter({ hasText: 'Sunrise only' }).getByRole('button').first().click();
        await expect(page.locator('#kyc-ranl-from')).toHaveValue('0000010003814');
        await expect(page.locator('.rv__changed')).toHaveCount(0);

        // a second variant with another selection, then reopen the report: the default variant is loaded
        await page.locator('#kyc-ranl-from').fill('0000010003900');
        await page.getByRole('button', { name: 'Save as variant' }).click();
        await dialog.getByLabel('Variant name').fill('Himalaya only');
        await dialog.getByLabel('Use as my default variant').uncheck();          // the dialog starts from the loaded variant's settings
        await dialog.getByRole('button', { name: 'Save', exact: true }).click();
        expect(api.variants).toHaveLength(2);
        await page.goto('/reports/kyc');
        await expect(page.locator('#kyc-ranl-from')).toHaveValue('0000010003814');
        await expect(page.locator('.rv__menu > .rv__btn')).toContainText('Sunrise only');

        // saving under an existing name overwrites it
        await page.locator('#kyc-ranl-from').fill('0000010003999');
        await page.getByRole('button', { name: 'Save as variant' }).click();
        await expect(dialog.getByText('replaces it')).toBeVisible();
        await dialog.getByRole('button', { name: 'Overwrite' }).click();
        expect(api.variants).toHaveLength(2);
        expect(api.variants[0].parameters.contract[0].low).toBe('0000010003999');

        // load and run a variant: the report runs with the saved parameters
        await page.locator('.rv__menu > .rv__btn').click();
        await page.getByRole('button', { name: 'Load Himalaya only and run' }).click();
        await expect.poll(() => api.sent('GET', /\/reports\/kyc$/).length).toBe(1);
        expect(JSON.parse(api.sent('GET', /\/reports\/kyc$/)[0].query!.contract)[0].low).toBe('0000010003900');
        await expect(page.locator('tbody tr')).toHaveCount(1);
        await expect(page.locator('tbody tr').first()).toContainText('Himalaya Hydro');

        // delete asks twice
        await page.locator('.rv__menu > .rv__btn').click();
        await page.getByRole('button', { name: 'Delete Himalaya only' }).click();
        expect(api.variants).toHaveLength(2);
        await page.getByRole('button', { name: 'Confirm delete Himalaya only' }).click();
        await expect.poll(() => api.variants.length).toBe(1);
    });

    test('Loans KYC Report is listed under Loans Management and launches', async ({ page }) => {
        const api = new MockApi();
        await api.install(page);
        await page.goto('/reports');
        await page.locator('.rp-module').nth(0).getByRole('button', { name: 'Show reports' }).click();
        await expect(page.locator('.rp-list__item')).toHaveCount(4);
        await page.locator('.rp-list__item').filter({ hasText: 'Loans KYC Report' }).getByRole('button', { name: 'Launch' }).click();
        await expect(page).toHaveURL(/\/reports\/kyc$/);
    });
});
