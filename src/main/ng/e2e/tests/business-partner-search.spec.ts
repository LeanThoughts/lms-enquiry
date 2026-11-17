import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Master Data > Business Partners: search term, partner role and server-side paging (GET /business-partners/search). */
const ROLES = [
    { code: 'TR0151', value: 'Security Trustee' },
    { code: 'FLVN00', value: 'Vendor' },
    { code: 'TR0100', value: 'Borrower' }
];

function partner(i: number, role = 'FLVN00'): Record<string, unknown> {
    return { id: 'p' + i, partyNumber: 50000000 + i, partyName: 'Partner ' + i, partyName1: 'Partner ' + i, partyName2: null,
        defaultPartnerRole: role, defaultPartnerRoleText: ROLES.find(r => r.code === role)?.value ?? null,
        partnerGroup: '0050', partnerCategory: '2', pan: 'PAN' + i, searchTerm1: 'TERM' + i, searchTerm2: null,
        contactPersonName: null, email: null, addressLine1: 'Street ' + i, addressLine2: null, city: 'Delhi',
        workFlowStatusDescription: null };
}

async function mockBusinessPartners(page: Page, searches: URLSearchParams[]): Promise<void> {
    await page.route(/\/businessPartnerRoleTypes(\?|$)/, route => route.fulfill({ json: { _embedded: { businessPartnerRoleTypes: ROLES } } }));
    for (const path of ['legalForms', 'legalEntities', 'houseBanks', 'partnerGroups']) {
        await page.route(new RegExp('/' + path + '(\\?|$)'), route => route.fulfill({ json: { _embedded: { [path]: [] } } }));
    }
    await page.route(/\/business-partners\/search\?/, route => {
        const params = new URL(route.request().url()).searchParams;
        searches.push(params);
        const size = Number(params.get('size'));
        const pageIndex = Number(params.get('page'));
        const total = params.get('partnerRole') === 'TR0151' ? 2 : 60;
        const rows = Array.from({ length: Math.min(size, total - pageIndex * size) }, (_, k) =>
            partner(pageIndex * size + k + 1, params.get('partnerRole') || 'FLVN00'));
        return route.fulfill({ json: { rows, page: pageIndex, size, total, sort: 'name' } });
    });
}

test.describe('Business Partners: search', () => {

    test('search term and partner role are sent to the paged search; pages are loaded from the server', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const searches: URLSearchParams[] = [];
        await mockBusinessPartners(page, searches);
        await page.goto('/business-partners');

        // the dropdown has the roles of businessPartnerRoleTypes
        const role = page.locator('#partnerRole');
        await expect(role.locator('option')).toHaveText(['All roles', 'Borrower (TR0100)', 'Security Trustee (TR0151)', 'Vendor (FLVN00)']);

        await page.locator('#searchTerm').fill('zeta');
        await page.getByRole('button', { name: 'Search', exact: true }).click();
        await expect(page.locator('.bp-list__summary')).toContainText('60 business partners found');
        await expect(page.locator('.bp-list__summary')).toContainText('showing 1–25 of 60');
        expect(searches[0].get('searchTerm')).toBe('zeta');
        expect(searches[0].get('page')).toBe('0');
        expect(searches[0].get('size')).toBe('25');
        expect(searches[0].has('partnerRole')).toBe(false);

        // page 3 comes from the server
        await page.locator('.bp-list__pagination').getByRole('button', { name: /3/ }).first().click();
        await expect(page.locator('.bp-list__pagination .bp-list__range')).toHaveText('51–60 of 60');
        expect(searches.at(-1)!.get('page')).toBe('2');

        // partner role: a new search starts at page 1
        await role.selectOption('TR0151');
        await page.getByRole('button', { name: 'Search', exact: true }).click();
        await expect(page.locator('.bp-list__summary')).toContainText('2 business partners found');
        expect(searches.at(-1)!.get('partnerRole')).toBe('TR0151');
        expect(searches.at(-1)!.get('page')).toBe('0');
        await expect(page.getByRole('cell', { name: /Security Trustee/ }).first()).toBeVisible();
        await expect(page.locator('.bp-list__pagination')).toHaveCount(0);

        // Clear empties the form and the list without searching
        const count = searches.length;
        await page.getByRole('button', { name: 'Clear', exact: true }).click();
        await expect(page.locator('#partnerRole')).toHaveValue('');
        await expect(page.getByText('No business partners to show')).toBeVisible();
        expect(searches.length).toBe(count);
    });

    test('a search without any value is not sent', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const searches: URLSearchParams[] = [];
        await mockBusinessPartners(page, searches);
        await page.goto('/business-partners');
        await page.getByRole('button', { name: 'Search', exact: true }).click();
        await expect(page.getByText('Please provide a value for at least one search parameter.')).toBeVisible();
        expect(searches).toHaveLength(0);
    });
});
