# Collateral Management – end-to-end tests

UI tests of the Collateral Management screens with [Playwright](https://playwright.dev). The Java backend is
replaced by an in-memory mock (`tests/mock-api.ts`): the tests need only the Angular app, no database or SAP.

| File | Covers |
|---|---|
| `tests/collateral-list.spec.ts` | list, display-only access, Send for Approval, rejection reason, opening from a workflow task, delete |
| `tests/collateral-detail.spec.ts` | 14 tabs, change and save, new collateral with required fields, no-edit-access message, lock during approval |
| `tests/collateral-child-tables.spec.ts` | Coverage dialog, Documents with upload, Securities with INR default |
| `tests/checklist-id-configuration.spec.ts` | Configuration > Checklist ID Number Range |

## Run

```bash
cd src/main/ng/e2e
npm install
npx playwright install chromium     # once
npm test                            # starts `ng serve --port 4300` from src/main/ng and runs the tests
```

To test an app that is already running: `E2E_BASE_URL=http://localhost:4200 npm test`.
Failed tests keep a screenshot and a trace; `npm run report` opens the HTML report.
