import { Component } from '@angular/core';
import { ClassicLayoutComponent } from './layouts/classic/classic.component';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-layout',
    standalone: true,
    templateUrl: './layout.component.html',
    // styleUrl: './layout.component.scss',
    imports: [ClassicLayoutComponent, CommonModule]
})
export class LayoutComponent {

    layout = 'classic';
}
