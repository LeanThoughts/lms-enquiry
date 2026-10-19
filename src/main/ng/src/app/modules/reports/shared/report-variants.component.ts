import { Component, DestroyRef, ElementRef, HostListener, OnInit, inject, input, output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ButtonComponent, IconComponent } from '@fundamental-ngx/core';
import { ReportVariant, VariantLoad } from './report-variant.model';
import { ReportVariantService } from './report-variant.service';

/**
 * Variants of a report (as in SAP): the selection screen can be saved under a name, loaded again at any time (and run
 * right away), kept private or shared with all users, and one own variant can be the default that is loaded when the
 * report opens. Used as {@code <app-report-variants report="kyc" [current]="…" (variantLoaded)="…">}; {@code current}
 * returns the parameters to save.
 */
@Component({
    selector: 'app-report-variants',
    templateUrl: './report-variants.component.html',
    styleUrls: ['./report-variants.component.scss'],
    imports: [FormsModule, ButtonComponent, IconComponent]
})
export class ReportVariantsComponent implements OnInit {

    private readonly service = inject(ReportVariantService);
    private readonly destroyRef = inject(DestroyRef);
    private readonly host = inject<ElementRef<HTMLElement>>(ElementRef);

    /** Report key: workflow-status, icc-approval or kyc */
    readonly report = input.required<string>();
    /** The parameters of the selection screen as they are now (without the ones not saved in a variant) */
    readonly current = input.required<() => Record<string, unknown>>();
    readonly variantLoaded = output<VariantLoad>();

    variants: ReportVariant[] = [];
    loaded = false;
    menuOpen = false;
    active: ReportVariant | null = null;
    pendingDelete = '';
    message = '';

    // save dialog
    saving = false;
    form = { name: '', description: '', shared: false, defaultVariant: false };
    formError = '';
    busy = false;

    ngOnInit(): void {
        this.refresh(true);
    }

    get mine(): ReportVariant[] {
        return this.variants.filter(v => v.mine);
    }

    get others(): ReportVariant[] {
        return this.variants.filter(v => !v.mine);
    }

    /** The screen differs from the loaded variant */
    get changed(): boolean {
        return !!this.active && JSON.stringify(this.active.parameters) !== JSON.stringify(this.current()());
    }

    /** The name typed in the save dialog belongs to an own variant that would be overwritten */
    get overwrites(): boolean {
        const name = this.form.name.trim().toLowerCase();
        return !!name && this.mine.some(v => v.name.toLowerCase() === name);
    }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: Event): void {
        if (this.menuOpen && !this.host.nativeElement.contains(event.target as Node)) {
            this.menuOpen = false;
            this.pendingDelete = '';
        }
    }

    @HostListener('document:keydown.escape')
    onEscape(): void {
        this.menuOpen = false;
        this.saving = false;
        this.pendingDelete = '';
    }

    toggleMenu(event: Event): void {
        event.stopPropagation();
        this.menuOpen = !this.menuOpen;
        this.pendingDelete = '';
        this.message = '';
    }

    /** Loads the variant into the selection screen; run = and executes the report. */
    load(variant: ReportVariant, run = false): void {
        this.active = variant;
        this.menuOpen = false;
        this.pendingDelete = '';
        this.variantLoaded.emit({ name: variant.name, parameters: variant.parameters, run });
    }

    openSave(): void {
        this.menuOpen = false;
        const active = this.active?.mine ? this.active : null;
        this.form = {
            name: active?.name ?? '',
            description: active?.description ?? '',
            shared: active?.shared ?? false,
            defaultVariant: active?.defaultVariant ?? false
        };
        this.formError = '';
        this.saving = true;
        setTimeout(() => this.host.nativeElement.querySelector<HTMLElement>('.rv-dialog input')?.focus());
    }

    save(): void {
        if (!this.form.name.trim()) {
            this.formError = 'Enter a name for the variant.';
            return;
        }
        this.busy = true;
        this.service.save(this.report(), { ...this.form, name: this.form.name.trim(), parameters: this.current()() })
            .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
                next: saved => {
                    this.busy = false;
                    this.saving = false;
                    this.message = `Variant ${saved.name} saved.`;
                    this.refresh(false, saved.id);
                },
                error: error => {
                    this.busy = false;
                    this.formError = this.service.errorMessage(error, 'The variant could not be saved.');
                }
            });
    }

    /** Two steps: the first click asks, the second deletes. */
    remove(variant: ReportVariant, event: Event): void {
        event.stopPropagation();
        if (this.pendingDelete !== variant.id) {
            this.pendingDelete = variant.id;
            return;
        }
        this.service.delete(this.report(), variant.id).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
            next: () => {
                this.pendingDelete = '';
                if (this.active?.id === variant.id) {
                    this.active = null;
                }
                this.message = `Variant ${variant.name} deleted.`;
                this.refresh(false);
            },
            error: error => {
                this.pendingDelete = '';
                this.message = this.service.errorMessage(error, 'The variant could not be deleted.');
            }
        });
    }

    private refresh(applyDefault: boolean, activateId = ''): void {
        this.service.list(this.report()).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
            next: list => {
                this.variants = list;
                this.loaded = true;
                if (activateId) {
                    this.active = list.find(v => v.id === activateId) ?? this.active;
                } else if (this.active) {
                    this.active = list.find(v => v.id === this.active?.id) ?? null;
                }
                const initial = applyDefault ? list.find(v => v.mine && v.defaultVariant) : undefined;
                if (initial) {
                    this.load(initial);
                }
            },
            error: () => {
                this.loaded = true;       // the report works without variants
            }
        });
    }
}
