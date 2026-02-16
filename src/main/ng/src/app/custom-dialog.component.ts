import { CdkScrollable } from '@angular/cdk/overlay';

import { NgStyle } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormModule } from '@fundamental-ngx/core';
import { BarModule } from '@fundamental-ngx/core/bar';
import {
    DialogBodyComponent,
    DialogComponent,
    DialogFooterComponent,
    DialogHeaderComponent,
    DialogRef
} from '@fundamental-ngx/core/dialog';
import { ScrollbarDirective } from '@fundamental-ngx/core/scrollbar';
import { TitleComponent } from '@fundamental-ngx/core/title';
import { MessageService } from './message.service';

@Component({
    template: `
        <fd-dialog>
            <fd-dialog-header>
                <h1 id="fd-dialog-header-1" fd-title>{{ dialogRef.data.title }}</h1>
            </fd-dialog-header>
            <fd-dialog-body>
                <p id="fd-dialog-body-1" [ngStyle]="{ 'text-align': 'justify', margin: 0, 'font-size': '1rem' }">
                    {{ dialogRef.data.description }}
                </p>
                <ul [style.margin-bottom]="0">
                    @for (fact of dialogRef.data.facts; track fact) {
                        <li>
                            {{ fact }}
                        </li>
                    }
                </ul>
                @if (dialogRef.data.displayInput) {
                    <div fd-form-item>
                        <!-- <label fd-form-label for="input-1" [colon]="true">Reason</label> -->
                        <input fd-form-control type="text" id="input-1" [(ngModel)]="inputValue" [required]="dialogRef.data.inputRequired"/>
                    </div>
                }
            </fd-dialog-body>
            <fd-dialog-footer>
                <fd-button-bar
                    label="Ok"
                    fdType="emphasized"
                    (click)="ok()"
                    ariaLabel="Ok"
                >
                </fd-button-bar>
                <fd-button-bar
                    label="Cancel"
                    fdType="transparent"
                    (click)="dialogRef.dismiss('Cancel')"
                    ariaLabel="Cancel"
                >
                </fd-button-bar>
            </fd-dialog-footer>
        </fd-dialog>
    `,
    imports: [
        TitleComponent,
        CdkScrollable,
        ScrollbarDirective,
        BarModule,
        NgStyle,
        DialogFooterComponent,
        DialogBodyComponent,
        DialogHeaderComponent,
        DialogComponent,
        FormModule,
        FormsModule
    ]
})
export class CustomDialogComponent {

    inputValue: string = '';

    /**
     * Constructor
     */
    constructor(public dialogRef: DialogRef, private messageService: MessageService) {
    }

    /**
     * Close dialog
     */
    ok() {
        if (this.dialogRef.data.inputRequired) {
            if (this.inputValue.trim() === '') {
                this.messageService.showError('Please provide valid input');
                return;
            }
            else {
                this.dialogRef.close({
                    continue: true,
                    inputValue: this.inputValue 
                });
            }
        }
        else {
            this.dialogRef.close({
                continue: true,
                inputValue: this.inputValue 
            });
        }
    }
}