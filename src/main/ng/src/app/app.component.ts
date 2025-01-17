import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Platform } from '@angular/cdk/platform';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { FuseConfigService } from '@fuse/services/config.service';
import { FuseNavigationService } from '@fuse/components/navigation/navigation.service';
import { FuseSidebarService } from '@fuse/components/sidebar/sidebar.service';
import { FuseSplashScreenService } from '@fuse/services/splash-screen.service';
import { FuseTranslationLoaderService } from '@fuse/services/translation-loader.service';

import { navigation } from 'app/navigation/navigation';
import { locale as navigationEnglish } from 'app/navigation/i18n/en';
import { locale as navigationTurkish } from 'app/navigation/i18n/tr';
import { AppService } from './app.service';
import { Keepalive } from '@ng-idle/keepalive';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { MatSnackBar } from '@angular/material';

@Component({
    selector: 'app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
    fuseConfig: any;
    navigation: any;

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @param {DOCUMENT} document
     * @param {FuseConfigService} _fuseConfigService
     * @param {FuseNavigationService} _fuseNavigationService
     * @param {FuseSidebarService} _fuseSidebarService
     * @param {FuseSplashScreenService} _fuseSplashScreenService
     * @param {FuseTranslationLoaderService} _fuseTranslationLoaderService
     * @param {Platform} _platform
     * @param {TranslateService} _translateService
     */
    constructor(
        @Inject(DOCUMENT) private document: any,
        private _fuseConfigService: FuseConfigService,
        private _fuseNavigationService: FuseNavigationService,
        private _fuseSidebarService: FuseSidebarService,
        private _fuseSplashScreenService: FuseSplashScreenService,
        private _fuseTranslationLoaderService: FuseTranslationLoaderService,
        private _translateService: TranslateService,
        private _platform: Platform,
        private _appService: AppService,
        private idle: Idle, 
        private keepalive: Keepalive,
        private _matSnackBar: MatSnackBar
    ) {

        if (_appService.currentUser === undefined) {
            _appService.me().subscribe((response) => {
                // Set the currently logged in user.
                _appService.currentUser = response;

                // Get user menu.
                _appService.getUserMenu().subscribe(response => {
                    response.menuHeaderDTOS.forEach(element => {
                        navigation.push(element);
                    });
                });

                // Get default navigation
                this.navigation = navigation;
                // Register the navigation to the service
                this._fuseNavigationService.register('main', this.navigation);
                // Set the main navigation as our current navigation
                this._fuseNavigationService.setCurrentNavigation('main');

                // if (_appService.currentUser.role === 'TR0100') {
                //     // Get default navigation
                //     this.navigation = navigation;
                //     // Register the navigation to the service
                //     this._fuseNavigationService.register('main', this.navigation);
                //     // Set the main navigation as our current navigation
                //     this._fuseNavigationService.setCurrentNavigation('main');
                // }
                // else if (_appService.currentUser.role === 'ZLM023' || _appService.currentUser.role === 'ZLM024' || _appService.currentUser.role === 'ZLM040') {
                //     // Get admin navigation
                //     this.navigation = adminNavigation;
                //     // Register the navigation to the service
                //     this._fuseNavigationService.register('admin', this.navigation);
                //     // Set the main navigation as our current navigation
                //     this._fuseNavigationService.setCurrentNavigation('admin');
                // }
                // else // if (_appService.currentUser.role === 'ZLM013')
                // {
                //     // Get officer navigation
                //     this.navigation = officerNavigation;
                //     // Register the navigation to the service
                //     this._fuseNavigationService.register('officer', this.navigation);
                //     // Set the main navigation as our current navigation
                //     this._fuseNavigationService.setCurrentNavigation('officer');
                // }
            });
        }


        // Add languages
        this._translateService.addLangs(['en', 'tr']);

        // Set the default language
        this._translateService.setDefaultLang('en');

        // Set the navigation translations
        this._fuseTranslationLoaderService.loadTranslations(navigationEnglish, navigationTurkish);

        // Use a language
        this._translateService.use('en');

        // Add is-mobile class to the body if the platform is mobile
        if (this._platform.ANDROID || this._platform.IOS) {
            this.document.body.classList.add('is-mobile');
        }

        // Set the private defaults
        this._unsubscribeAll = new Subject();


        // sets an idle timeout of 5 seconds, for testing purposes.
        idle.setIdle(970);
        // sets a timeout period of 5 seconds. after 10 seconds of inactivity, the user will be considered timed out.
        idle.setTimeout(30);
        // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
        idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

        // If there is user action after the idle period.
        idle.onIdleEnd.subscribe(() => {
            this._matSnackBar.dismiss();
        });

        // If there is no user action even after idle time + timeout time. Perform final actions like logout
        idle.onTimeout.subscribe(() => {
            console.log('timed out after 30 seconds');
            window.location.href = '/enquiry/logout';
        });

        // On stat of idle time.
        idle.onIdleStart.subscribe(() => console.log('You are idle.'));
        
        // On start of timeout (after idle time).
        idle.onTimeoutWarning.subscribe((countdown) => console.log('You will timeout in 30 seconds.'));
    
        // sets the ping interval to 15 seconds
        keepalive.interval(15);
        keepalive.onPing.subscribe(() => console.log('Ping.'));
    
        this.idle.watch();       
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void {
        // Subscribe to config changes
        this._fuseConfigService.config
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe((config) => {
                this.fuseConfig = config;

                if (this.fuseConfig.layout.width === 'boxed') {
                    this.document.body.classList.add('boxed');
                }
                else {
                    this.document.body.classList.remove('boxed');
                }
            });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        // Unsubscribe from all subscriptions
        this._unsubscribeAll.next();
        this._unsubscribeAll.complete();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Toggle sidebar open
     *
     * @param key
     */
    toggleSidebarOpen(key): void {
        this._fuseSidebarService.getSidebar(key).toggleOpen();
    }
}
