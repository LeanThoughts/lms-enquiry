import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { BusinessPartnerSearchService } from '../../business-partner-search.service';
import { 
    ButtonComponent, 
    DatePickerComponent, 
    FormModule, 
    LayoutGridModule, 
    SelectModule, 
    TitleComponent,
} from '@fundamental-ngx/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MOBILE_NUMBER_REGEX, NUMERIC_ONLY_REGEX, PHONE_NUMBER_REGEX } from '../../../../common/common.regex';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from '../../../../message.service';
import { statesOfIndia } from '../../../../app.constants';
import { DatePipe, JsonPipe } from '@angular/common';
import { ComponentNgxComponent } from '../../../../common/component-ngx/component-ngx.component';
import { MultiComboboxModule } from '@fundamental-ngx/core';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-partner-details-update',
    imports: [
        ButtonComponent,
        LayoutGridModule,
        ReactiveFormsModule,
        FormModule,
        SelectModule,
        DatePickerComponent,
        TitleComponent,
        ComponentNgxComponent,
        MultiComboboxModule,
        NgIf
    ],
    templateUrl: './partner-details-update.component.html'
})
export class PartnerDetailsUpdateComponent implements OnInit, OnDestroy {

    operation: string = '';
    selectedBusinessPartner: any;
    partnerDetailsForm!: FormGroup;

    businessPartnerRoleTypes: any[] = [];
    legalForms: any[] = [];
    legalEntities: any[] = [];
    houseBanks: any[] = [];
    partnerTitles: any[] = [];
    statesOfIndia: any[] = statesOfIndia;

    // disableSendForApproval: boolean = false;

    private destroy$ = new Subject<void>();

    title: string = '';

    previousDefaultPartnerRole: string = '';
    formFieldsConfig: any = {}

    /**
     * Constructor
     */
    constructor(
        private businessPartnerService: BusinessPartnerSearchService, 
        private route: ActivatedRoute,
        public router: Router,
        private messageService: MessageService) 
    {
        // Determine selected business partner and also subscribe to changes
        // this.selectedBusinessPartner = this.businessPartnerService.selectedEntity$.getValue();
        this.businessPartnerService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe(selectedBusinessPartner => {
            console.log('selectedBusinessPartner', selectedBusinessPartner);
            this.selectedBusinessPartner = selectedBusinessPartner;
        });

        // Fetch business partner titles if operation is not create
        this.operation = this.router.url.includes('create') ? 'create' : 'update';
        if (this.operation !== 'create') {
            this.businessPartnerService.getTitles(this.selectedBusinessPartner.partnerCategory).subscribe(response => {
                this.partnerTitles = response._embedded.titles;
            });
        }

        // Get business partner role types, legal forms, legal entities, house banks and titles from resolved data
        const resolvedData = this.route.snapshot.data['routeResolvedData'];
        if (resolvedData) {
            this.legalForms = resolvedData['legalForms'] ? resolvedData['legalForms']._embedded.legalForms : [];
            this.legalEntities = resolvedData['legalEntities'] ? resolvedData['legalEntities']._embedded.legalEntities : [];
            this.houseBanks = resolvedData['houseBanks'] ? resolvedData['houseBanks']._embedded.houseBanks : [];
            this.businessPartnerRoleTypes = resolvedData['businessPartnerRoleTypes'];
        }

        // Get title
        this.title = this.getTitle();

        // // Disable send for approval if operation is create
        // this.disableSendForApproval = this.operation === 'create';
    }

    /**
     * Get title
     */
    getTitle(): string {
        var title = '';
        if (this.operation === 'create') {
            title = 'Create Business Partner';
        }
        else {
            title = 'Update Business Partner (';
            if (this.selectedBusinessPartner.partyNumber)
                title += 'BP Number: ' + this.selectedBusinessPartner.partyNumber;
            if (title.includes('BP Number:'))
                title += ' / ';
            if (this.selectedBusinessPartner.partyName.trim())
                title += 'Name: ' + this.selectedBusinessPartner.partyName;
            title += ')';
        }
        return title;
    }

    /**
     * On partner category change event
     */
    onPartnerCategoryChange(event: any) {
        if (event) {
            this.businessPartnerService.getTitles(event).subscribe(response => {
                this.partnerTitles = response._embedded.titles;
            });
        }
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Initialize partner details form
        this.partnerDetailsForm = new FormGroup({
            partnerCategory: new FormControl(this.selectedBusinessPartner?.partnerCategory?.trim() || null),
            partnerGroup: new FormControl(this.selectedBusinessPartner?.partnerGroup?.trim() || null, [Validators.required]),
            defaultPartnerRole: new FormControl(this.selectedBusinessPartner?.defaultPartnerRole?.trim() || '', [Validators.required]),
            partyName1: new FormControl(this.selectedBusinessPartner?.partyName1?.trim() || null),
            partyName2: new FormControl(this.selectedBusinessPartner?.partyName2?.trim() || null),
            externalBPNumber: new FormControl(this.selectedBusinessPartner?.externalBPNumber?.trim() || null),
            title: new FormControl(this.selectedBusinessPartner?.title?.trim() || ''),
            searchTerm1: new FormControl(this.selectedBusinessPartner?.searchTerm1?.trim() || null),
            searchTerm2: new FormControl(this.selectedBusinessPartner?.searchTerm2?.trim() || null),
            addressLine1: new FormControl(this.selectedBusinessPartner?.addressLine1?.trim() || null),
            addressLine2: new FormControl(this.selectedBusinessPartner?.addressLine2?.trim() || null),
            addressLine3: new FormControl(this.selectedBusinessPartner?.addressLine3?.trim() || null),
            city: new FormControl(this.selectedBusinessPartner?.city?.trim() || null),
            state: new FormControl(this.selectedBusinessPartner?.state?.trim() || null),
            country: new FormControl(this.selectedBusinessPartner?.country?.trim() || 'India'),
            postalCode: new FormControl(this.selectedBusinessPartner?.postalCode?.trim() || null, [Validators.pattern(NUMERIC_ONLY_REGEX)]), 
            addressValidFromDate: new FormControl(this.selectedBusinessPartner?.addressValidFromDate || null),
            contactNumber: new FormControl(this.selectedBusinessPartner?.contactNumber?.trim() || null, [Validators.pattern(PHONE_NUMBER_REGEX)]),
            email: new FormControl(this.selectedBusinessPartner?.email?.trim() || null, [Validators.email]),
            mobileNumber: new FormControl(this.selectedBusinessPartner?.mobileNumber?.trim() || null, [Validators.pattern(MOBILE_NUMBER_REGEX)]),
            faxNumber: new FormControl(this.selectedBusinessPartner?.faxNumber?.trim() || null, [Validators.pattern(PHONE_NUMBER_REGEX)]),
            legalForm: new FormControl(this.selectedBusinessPartner?.legalForm?.trim() || null),
            legalEntity: new FormControl(this.selectedBusinessPartner?.legalEntity?.trim() || null),
            houseBank: new FormControl(this.selectedBusinessPartner?.houseBank?.trim() || null)
        });

        // Get previous default partner role and call onDefaultPartnerRoleChange to set form validators
        this.previousDefaultPartnerRole = this.selectedBusinessPartner?.defaultPartnerRole || '';
        if (this.operation === 'update') {
            this.onDefaultPartnerRoleChange(this.selectedBusinessPartner?.defaultPartnerRole);
        }
    }

    /**
     * Get partner category description
     */
    getPartnerCategoryDescription(partnerCategory: string): string {
        const categoryMap: { [key: string]: string } = {
            '1': 'Person',
            '2': 'Organization',
            '3': 'Group'
        };
        return categoryMap[partnerCategory] || '--';
    }

    /**
     * On default partner role change event
     */
    onDefaultPartnerRoleChange(event: any) {
        if (!event) {
            this.formFieldsConfig = {};
            return;
        }
    
        this.businessPartnerService.getBusinessPartnerRoleFieldConfig(event).subscribe({
            next: (result: any) => {
                // ← Create the object HERE, inside the subscription
                const formFieldsConfig: any = {};
                const fieldList = result?.bupaRoleEntityFieldStatusList || [];
                fieldList
                    .filter((fieldConfig: any) => fieldConfig.entity === 'Partner')
                    .forEach((fieldConfig: any) => {
                        const fieldKey = fieldConfig.fieldName ?? fieldConfig.field;
                        if (fieldKey.trim()) {
                            formFieldsConfig[fieldKey.trim()] = fieldConfig.fieldStatus;
                        } 
                        else {
                            console.warn('Field config missing fieldName/field:', fieldConfig);
                        }
                    });    
                this.formFieldsConfig = { ...formFieldsConfig };
                this.resetFormValidators();
                console.log('formFieldsConfig', this.formFieldsConfig);
            },
            error: (err) => {
                console.error('Failed to load field config', err);
                this.formFieldsConfig = {}; // optional fallback
            }
        });
    }

    /**
     * Reset form validators
     */
    resetFormValidators() {
        // Partner Category
        this.formFieldsConfig.partyCategory === 2 ? this.partnerDetailsForm.get('partnerCategory')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('partnerCategory')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('partnerCategory')?.updateValueAndValidity();
        // Party Name 1
        this.formFieldsConfig.partyName1 === 2 ? this.partnerDetailsForm.get('partyName1')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('partyName1')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('partyName1')?.updateValueAndValidity();
        // Party Name 2
        this.formFieldsConfig.partyName2 === 2 ? this.partnerDetailsForm.get('partyName2')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('partyName2')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('partyName2')?.updateValueAndValidity();
        // Address Line 1
        this.formFieldsConfig.addressLine1 === 2 ? this.partnerDetailsForm.get('addressLine1')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('addressLine1')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('addressLine1')?.updateValueAndValidity();
        // Address Line 2
        this.formFieldsConfig.addressLine2 === 2 ? this.partnerDetailsForm.get('addressLine2')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('addressLine2')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('addressLine2')?.updateValueAndValidity();
        // City
        this.formFieldsConfig.city === 2 ? this.partnerDetailsForm.get('city')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('city')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('city')?.updateValueAndValidity();
        // State
        this.formFieldsConfig.state === 2 ? this.partnerDetailsForm.get('state')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('state')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('state')?.updateValueAndValidity();
        // Email
        this.formFieldsConfig.email === 2 ? this.partnerDetailsForm.get('email')?.setValidators([Validators.required, Validators.email]) 
            : this.partnerDetailsForm.get('email')?.removeValidators([Validators.required, Validators.email]);
        this.partnerDetailsForm.get('email')?.updateValueAndValidity();
        // Title
        this.formFieldsConfig.title === 2 ? this.partnerDetailsForm.get('title')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('title')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('title')?.updateValueAndValidity();
        // Postal Code
        this.formFieldsConfig.postalCode === 2 ? this.partnerDetailsForm.get('postalCode')?.setValidators([Validators.required, Validators.pattern(NUMERIC_ONLY_REGEX)]) 
            : this.partnerDetailsForm.get('postalCode')?.removeValidators([Validators.required, Validators.pattern(NUMERIC_ONLY_REGEX)]);
        this.partnerDetailsForm.get('postalCode')?.updateValueAndValidity();
        // Country
        this.formFieldsConfig.country === 2 ? this.partnerDetailsForm.get('country')?.setValidators([Validators.required]) 
            : this.partnerDetailsForm.get('country')?.removeValidators([Validators.required]);
        this.partnerDetailsForm.get('country')?.updateValueAndValidity();
        // Contact Number
        this.formFieldsConfig.contactNumber === 2 ? this.partnerDetailsForm.get('contactNumber')?.setValidators([Validators.required, Validators.pattern(PHONE_NUMBER_REGEX)]) 
            : this.partnerDetailsForm.get('contactNumber')?.removeValidators([Validators.required, Validators.pattern(PHONE_NUMBER_REGEX)]);
        this.partnerDetailsForm.get('contactNumber')?.updateValueAndValidity();
    }

    /**
     * Submit
     */
    submit() {
        if (this.partnerDetailsForm.valid) {
            var partnerDetails = this.partnerDetailsForm.value;
            
            if (partnerDetails.addressValidFromDate) {
                const dt = new Date(partnerDetails.addressValidFromDate);
                partnerDetails.addressValidFromDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            const handleError = (operation: string) => (error: any) => {
                this.messageService.showError(
                    `${error.message}!! Error ${operation} business partner. Please try again. If the problem persists, please contact `
                    + `the administrator.`
                );
            };
    
            // There is a server side error when country is passed to the server, hence pass empty string for country
            partnerDetails.country = '';
            if (this.operation === 'create') {
                this.businessPartnerService.createPartner(partnerDetails).subscribe({
                    next: (result) => {
                        this.createDefaultBusinessPartnerRole(result.id, true, () => {
                            this.messageService.showSuccess('Business partner created successfully');
                            // Navigate to the update page for the newly created business partner with the id
                            this.businessPartnerService.selectedEntity$.next(result);
                            this.router.navigate(['/business-partners/update', result.id, result.defaultPartnerRole]);
                        });
                    },
                    error: handleError('creating')
                });
            }
            else {
                Object.assign(this.selectedBusinessPartner, partnerDetails);
                this.businessPartnerService.updateBusinessPartner(this.selectedBusinessPartner).subscribe({
                    next: (result) => {
                        this.createDefaultBusinessPartnerRole(this.selectedBusinessPartner.id, true, () => {
                            this.messageService.showSuccess('Business partner details updated successfully');
                            this.businessPartnerService.selectedEntity$.next(result);
                            this.router.navigate(['/business-partners/update', result.id, result.defaultPartnerRole]);
                        });
                    },
                    error: handleError('updating')
                });
            }
        } 
        else {
            // console.log('partnerDetailsForm', this.partnerDetailsForm.valid);
            // // Log all form errors in a detailed manner
            // const collectErrors = (formGroup: FormGroup, parentKey: string = ''): any => {
            //     let errors: any = {};
            //     Object.keys(formGroup.controls).forEach(key => {
            //         const control = formGroup.get(key);
            //         const controlPath = parentKey ? `${parentKey}.${key}` : key;
            //         if (control instanceof FormGroup) {
            //             const childErrors = collectErrors(control, controlPath);
            //             if (Object.keys(childErrors).length > 0) {
            //                 errors[key] = childErrors;
            //             }
            //         } else {
            //             if (control && control.errors) {
            //                 errors[controlPath] = control.errors;
            //             }
            //         }
            //     });
            //     return errors;
            // };
            // const allErrors = collectErrors(this.partnerDetailsForm);
            // console.log('partnerDetailsForm errors', allErrors);
            this.partnerDetailsForm.markAllAsTouched();
            this.partnerDetailsForm.updateValueAndValidity();
        }
    }

    /**
     * Create default business partner role
     */
    createDefaultBusinessPartnerRole(businessPartnerId: string, defaultRole: boolean, onSuccess: () => void): void {
        const defaultPartnerRole = this.businessPartnerRoleTypes.find(role => role.code === this.partnerDetailsForm.get('defaultPartnerRole')?.value);
        if (defaultPartnerRole.code === this.previousDefaultPartnerRole) {
            onSuccess();
            return;
        }
        this.businessPartnerService.createBusinessPartnerRole({
            businessPartnerId: businessPartnerId,
            roleTypeId: defaultPartnerRole.id,
            defaultRole: defaultRole
        }).subscribe({
            next: () => {
                onSuccess();
            }
        });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
