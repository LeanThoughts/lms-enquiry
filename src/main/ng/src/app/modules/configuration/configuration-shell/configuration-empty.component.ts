import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CONFIGURATION_MODULES } from './configuration-apps';

/** Content of a module tab that has no configuration apps yet */
@Component({
    selector: 'app-configuration-empty',
    standalone: true,
    template: `
        <div class="cfg-empty">
            <span class="sap-icon--action-settings cfg-empty__icon" aria-hidden="true"></span>
            <div class="cfg-empty__title">{{ title }}</div>
            <div class="cfg-empty__text">No configuration apps in this module yet.</div>
        </div>
    `
})
export class ConfigurationEmptyComponent {
    private route = inject(ActivatedRoute);
    readonly title = CONFIGURATION_MODULES.find(m => m.id === this.route.snapshot.paramMap.get('module'))?.title ?? 'Configuration';
}
