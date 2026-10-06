import { Component } from '@angular/core';
import {
    BarModule,
    DialogModule,
    DialogRef,
    IconComponent,
    TitleComponent
} from '@fundamental-ngx/core';

/** Data passed to {@link ConfirmDialogComponent}. */
export interface ConfirmDialogData {
    title: string;
    message: string;
    confirmLabel: string;
    /** Use the negative (red) style for destructive actions. */
    destructive?: boolean;
}

/** Small yes/no dialog. Closes with true when confirmed; dismissed otherwise. */
@Component({
    selector: 'app-collateral-confirm-dialog',
    template: `
        <fd-dialog>
            <fd-dialog-header>
                <h1 fd-title class="fd-dialog-header-title">
                    <fd-icon [glyph]="data.destructive ? 'message-warning' : 'message-information'"></fd-icon>
                    {{ data.title }}
                </h1>
            </fd-dialog-header>
            <fd-dialog-body>
                <p class="cm-confirm__text">{{ data.message }}</p>
            </fd-dialog-body>
            <fd-dialog-footer>
                <fd-button-bar [fdType]="data.destructive ? 'negative' : 'emphasized'" [label]="data.confirmLabel"
                    (click)="dialogRef.close(true)"></fd-button-bar>
                <fd-button-bar fdType="transparent" label="Cancel" (click)="dialogRef.dismiss()"></fd-button-bar>
            </fd-dialog-footer>
        </fd-dialog>
    `,
    styles: [`.cm-confirm__text { margin: 0; max-width: 28rem; line-height: 1.5; }`],
    imports: [DialogModule, BarModule, IconComponent, TitleComponent]
})
export class ConfirmDialogComponent {

    readonly data: ConfirmDialogData;

    constructor(public dialogRef: DialogRef) {
        this.data = dialogRef.data as ConfirmDialogData;
    }
}
