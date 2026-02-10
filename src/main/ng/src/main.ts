import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { ApplicationRef } from '@angular/core';
import { ThemingService } from '@fundamental-ngx/core/theming';

async function main() {
    // Now bootstrap Angular app after MSAL is fully initialized
    await bootstrapApplication(AppComponent, {
      providers: [
        ...appConfig.providers, 
      ]
    }).then((appRef: ApplicationRef) => appRef.injector.get(ThemingService).init());}

main().catch((err) => console.error('Error during bootstrap:', err));
