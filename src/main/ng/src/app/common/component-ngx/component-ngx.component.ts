import { ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'component-ngx',
    template: `
        <div class="docs-tile docs-component" [class.docs-tile-example-background]="hasBackground">
            <div class="docs-tile__content docs-tile-content-example">
                <div class="fd-doc-component">
                    <ng-content></ng-content>
                </div>
            </div>
        </div>
    `,
    styleUrls: ['./component-ngx.component.scss'],
    providers: [
    ],
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        // ContentDensityDirective
    ]
})
export class ComponentNgxComponent implements OnInit {

    hasBackground: boolean = true;
    
    ngOnInit(): void {
    }
}