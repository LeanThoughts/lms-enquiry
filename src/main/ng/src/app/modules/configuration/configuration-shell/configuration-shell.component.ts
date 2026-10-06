import { Component, DestroyRef, OnInit, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { ObjectStatusComponent } from '@fundamental-ngx/core';
import { filter } from 'rxjs';
import { CONFIGURATION_MODULES, ConfigurationApp, ConfigurationModule } from './configuration-apps';

/**
 * Configuration workspace (menu "Configuration"): module tabs on top, the apps of the selected module on the left and
 * the selected app on the right, below a bar "Display / Edit Configuration: <app>". Styled with the SAP theme of the
 * application only (theme variables), so it follows the theme the user selects.
 */
@Component({
    selector: 'app-configuration-shell',
    standalone: true,
    imports: [RouterOutlet, RouterLink, RouterLinkActive, ObjectStatusComponent],
    templateUrl: './configuration-shell.component.html',
    styleUrl: './configuration-shell.component.scss',
    // The bar of the workspace replaces the title of the hosted app (see the scss)
    encapsulation: ViewEncapsulation.None
})
export class ConfigurationShellComponent implements OnInit {

    readonly modules = CONFIGURATION_MODULES;
    module: ConfigurationModule = CONFIGURATION_MODULES[0];
    app: ConfigurationApp | undefined;

    /** The hosted app; its `edit` flag decides "Display" or "Edit" */
    private hosted: { edit?: boolean } | undefined;

    private router = inject(Router);
    private destroyRef = inject(DestroyRef);

    ngOnInit(): void {
        this.select(this.router.url);
        this.router.events
            .pipe(filter(e => e instanceof NavigationEnd), takeUntilDestroyed(this.destroyRef))
            .subscribe(e => this.select((e as NavigationEnd).urlAfterRedirects));
    }

    get mode(): 'Display' | 'Edit' {
        return this.hosted?.edit ? 'Edit' : 'Display';
    }

    onActivate(component: unknown): void {
        this.hosted = component as { edit?: boolean };
    }

    onDeactivate(): void {
        this.hosted = undefined;
    }

    /** Group heading before an app of the list (when its group differs from the previous app's) */
    groupBefore(index: number): string | undefined {
        const apps = this.module.apps;
        const group = apps[index]?.group;
        return group && (index === 0 || apps[index - 1].group !== group) ? group : undefined;
    }

    /** Tab: opens the first app of the module (or the module's empty page) */
    openModule(module: ConfigurationModule): void {
        if (module === this.module && this.app) {
            return;
        }
        const target = module.apps.length ? module.apps[0].path : module.id;
        this.router.navigateByUrl('/configuration/' + target);
    }

    private select(url: string): void {
        // path below /configuration, e.g. "bp-tables/titles"
        const segment = url.split('?')[0].split('#')[0].split('/').filter(s => !!s).slice(1).join('/');
        for (const module of this.modules) {
            const app = module.apps.find(a => a.path === segment);
            if (app || module.id === segment) {
                this.module = module;
                this.app = app;
                return;
            }
        }
        this.app = undefined;
    }
}
