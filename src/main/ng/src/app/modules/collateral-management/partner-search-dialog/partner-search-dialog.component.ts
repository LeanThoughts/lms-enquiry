import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import {
    BarModule,
    DialogCloseButtonComponent,
    DialogModule,
    DialogRef,
    FormModule,
    IconComponent,
    MessageStripComponent,
    SelectModule,
    TableModule,
    TitleComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CollateralService } from '../collateral.service';
import { PartnerSearchResult, ValueEntry } from '../collateral.model';

/** Data passed to {@link PartnerSearchDialogComponent}. */
export interface PartnerSearchDialogData {
    /** Name of the field the partner is chosen for, e.g. "Security Trustee". */
    fieldLabel: string;
}

/** Most partners a search returns (see the backend). */
const MAX_RESULTS = 200;

/**
 * Search business partners by name 1, name 2, default partner role and search terms 1 and 2.
 * Closes with the chosen {@link PartnerSearchResult}.
 */
@Component({
    selector: 'app-collateral-partner-search-dialog',
    templateUrl: './partner-search-dialog.component.html',
    styleUrl: './partner-search-dialog.component.scss',
    imports: [ReactiveFormsModule, DialogModule, DialogCloseButtonComponent, BarModule, FormModule, SelectModule,
        TableModule, IconComponent, MessageStripComponent, TitleComponent]
})
export class PartnerSearchDialogComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    readonly data: PartnerSearchDialogData;
    readonly maxResults = MAX_RESULTS;

    readonly form = new FormGroup({
        name1: new FormControl('', { nonNullable: true }),
        name2: new FormControl('', { nonNullable: true }),
        defaultPartnerRole: new FormControl<string | null>(null),
        searchTerm1: new FormControl('', { nonNullable: true }),
        searchTerm2: new FormControl('', { nonNullable: true })
    });

    roles: ValueEntry[] = [];
    results: PartnerSearchResult[] = [];
    selected: PartnerSearchResult | null = null;
    searching = false;
    searched = false;
    errorMessage = '';

    constructor(public dialogRef: DialogRef, private collateralService: CollateralService) {
        this.data = dialogRef.data as PartnerSearchDialogData;
    }

    ngOnInit(): void {
        this.collateralService.getPartnerRoles().pipe(takeUntil(this.destroy$))
            .subscribe(roles => this.roles = roles);
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    search(): void {
        const criteria = this.form.getRawValue();
        const texts = [criteria.name1, criteria.name2, criteria.searchTerm1, criteria.searchTerm2].map(text => text.trim());
        if (!texts.some(Boolean) && !criteria.defaultPartnerRole) {
            this.errorMessage = 'Enter at least one search criterion.';
            return;
        }
        if (texts.some(text => text.length === 1)) {
            this.errorMessage = 'Enter at least 2 characters per search criterion.';
            return;
        }
        this.errorMessage = '';
        this.searching = true;
        this.selected = null;
        this.collateralService.searchPartners(criteria).pipe(takeUntil(this.destroy$)).subscribe({
            next: results => {
                this.searching = false;
                this.searched = true;
                this.results = results ?? [];
            },
            error: error => {
                this.searching = false;
                this.errorMessage = this.collateralService.errorMessage(error, 'The partners could not be searched.');
            }
        });
    }

    clear(): void {
        this.form.reset({ name1: '', name2: '', defaultPartnerRole: null, searchTerm1: '', searchTerm2: '' });
        this.results = [];
        this.selected = null;
        this.searched = false;
        this.errorMessage = '';
    }

    select(partner: PartnerSearchResult): void {
        this.selected = partner;
    }

    choose(partner: PartnerSearchResult | null = this.selected): void {
        if (partner) {
            this.dialogRef.close(partner);
        }
    }

    cancel(): void {
        this.dialogRef.dismiss();
    }

    roleText(partner: PartnerSearchResult): string {
        if (!partner.defaultPartnerRole) {
            return '';
        }
        return partner.defaultPartnerRoleText
            ? `${partner.defaultPartnerRole} ${partner.defaultPartnerRoleText}`
            : partner.defaultPartnerRole;
    }
}
