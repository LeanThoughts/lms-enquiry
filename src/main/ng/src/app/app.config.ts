import { ApplicationConfig, LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideTheming, themingInitializer } from '@fundamental-ngx/core/theming';
// import { provideHateoas } from './hateoas.provider';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpInterceptor } from './http.interceptor';
import { MOBILE_MODE_CONFIG, MobileModeConfigToken, MobileModeControl } from '@fundamental-ngx/core/mobile-mode';
import { DATE_TIME_FORMATS, DatetimeAdapter, FD_DATETIME_FORMATS, FdDatetimeAdapter } from '@fundamental-ngx/core/datetime';
import { RtlService } from '@fundamental-ngx/core';

const SELECT_MOBILE_CONFIG: MobileModeConfigToken = {
    target: MobileModeControl.SELECT,
    config: { hasCloseButton: true, dialogConfig: { mobileOuterSpacing: true } }
};

export const appConfig: ApplicationConfig = {
    providers: [
        { 
            provide: MOBILE_MODE_CONFIG, 
            useValue: SELECT_MOBILE_CONFIG, 
            multi: true 
        },
        {
            provide: DatetimeAdapter,
            useClass: FdDatetimeAdapter
        },
        {
            provide: DATE_TIME_FORMATS,
            useValue: FD_DATETIME_FORMATS
        },
        {
            provide: LOCALE_ID,
            useValue: 'en-IN'
        },
        provideHttpClient(
            withInterceptors([httpInterceptor])
        ),
        provideAnimations(),
        provideRouter(routes),
        RtlService,
        provideTheming({ defaultTheme: 'sap_fiori_3', changeThemeOnQueryParamChange: true }),
        themingInitializer(),
        // provideTheming({ excludeDefaultThemes: false, defaultTheme: 'sap_fiori_3', changeThemeOnQueryParamChange: false }),
        // provideTheming({ defaultTheme: 'sap_horizon', changeThemeOnQueryParamChange: false }),
        // provideHateoas() // Hateoas configuration,
    ]
};
