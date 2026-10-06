import { defineConfig, devices } from '@playwright/test';

/**
 * End-to-end tests of Collateral Management. The Java backend is replaced by an in-memory mock (tests/mock-api.ts),
 * so the tests need the Angular app only:
 *  - by default the app is started with `ng serve --port 4300` from the Angular project (one folder up);
 *  - set E2E_BASE_URL to test an app that is already running (e.g. http://localhost:4200).
 */
const baseURL = process.env['E2E_BASE_URL'] ?? 'http://localhost:4300';

export default defineConfig({
    testDir: './tests',
    timeout: 60_000,
    expect: { timeout: 10_000 },
    fullyParallel: false,
    retries: process.env['CI'] ? 1 : 0,
    reporter: [['list'], ['html', { open: 'never' }]],
    use: {
        baseURL,
        viewport: { width: 1440, height: 900 },
        screenshot: 'only-on-failure',
        trace: 'retain-on-failure',
        launchOptions: process.env['CHROME_PATH'] ? { executablePath: process.env['CHROME_PATH'] } : {}
    },
    projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'], viewport: { width: 1440, height: 900 } } }],
    webServer: process.env['E2E_BASE_URL'] ? undefined : {
        command: 'npx ng serve --port 4300',
        cwd: '..',
        url: 'http://localhost:4300',
        timeout: 240_000,
        reuseExistingServer: true
    }
});
