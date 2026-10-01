import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ButtonComponent, DialogService, FormModule, LayoutGridModule } from '@fundamental-ngx/core';
import { catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { ApplicationFeeService } from '../application-fee.service';
import { BusinessPartnerSearchService } from '../../../../business-partner-search/business-partner-search.service';
import { MessageService } from '../../../../../message.service';
import { SearchPartnersDialogComponent } from '../search-partners-dialog/search-partners-dialog.component';

const MAIN_LOAN_PARTNER_ROLE = 'TR0100';

// Identification category code -> invoicing detail field
const IDENTIFICATION_FIELDS: { [code: string]: string } = {
    'Z00004': 'cinNumber',
    'Z00011': 'gstNumber',
    'Z00002': 'pan',
    'Z00009': 'msmeRegistrationNumber',
    'Z00012': 'leiNumber',
    'Z00013': 'cKYCRefNumber'
};

@Component({
    selector: 'app-invoicing-details',
    imports: [
        ButtonComponent,
        FormModule,
        LayoutGridModule
    ],
    templateUrl: './invoicing-details.component.html'
})
export class InvoicingDetailsComponent implements OnInit {

    @Input() loanApplication: any;
    @Input() applicationFeeId: string = '';

    readonly fields = [
        { name: 'companyName', label: 'Company Name' },
        { name: 'businessPartnerNumber', label: 'Business Partner Number' },
        { name: 'cinNumber', label: 'CIN Number' },
        { name: 'gstNumber', label: 'GST Number' },
        { name: 'pan', label: 'PAN' },
        { name: 'msmeRegistrationNumber', label: 'MSME Registration Number' },
        { name: 'leiNumber', label: 'LEI Number' },
        { name: 'cKYCRefNumber', label: 'CKYC Reference Number' },
        { name: 'doorNumber', label: 'Door Number' },
        { name: 'address', label: 'Address' },
        { name: 'street', label: 'Street' },
        { name: 'city', label: 'City' },
        { name: 'state', label: 'State' },
        { name: 'postalCode', label: 'Postal Code' },
        { name: 'landline', label: 'Landline' },
        { name: 'mobile', label: 'Mobile' },
        { name: 'email', label: 'Email' }
    ];

    details: any = {};
    invoicingDetail: any = null;
    saving: boolean = false;

    private states: any[] = [];

    /**
     * Constructor
     */
    constructor(
        route: ActivatedRoute,
        private dialogService: DialogService,
        private applicationFeeService: ApplicationFeeService,
        private businessPartnerSearchService: BusinessPartnerSearchService,
        private messageService: MessageService
    ) {
        this.states = route.snapshot.data['routeResolvedData']?.state ?? [];
    }

    /**
     * On init
     */
    ngOnInit(): void {
        if (!this.applicationFeeId) return;
        this.applicationFeeService.getInvoicingDetails(this.applicationFeeId).pipe(
            tap((invoicingDetail: any) => this.invoicingDetail = invoicingDetail),
            switchMap((invoicingDetail: any) => invoicingDetail 
                ? this.applicationFeeService.getInvoicingDetailPartner(invoicingDetail.id) 
                : of(null)),
            switchMap((partner: any) => partner ? this.loadPartner(partner) : of(null))
        ).subscribe({
            error: () => this.messageService.showError('Unable to load the customer and invoicing details.')
        });
    }

    /**
     * Open the partner search dialog and save the selected partner as the invoicing partner
     */
    searchPartners(): void {
        const dialogRef = this.dialogService.open(SearchPartnersDialogComponent, { width: '60rem' });
        dialogRef.afterClosed.subscribe({
            next: (partner: any) => {
                if (!partner?.id) return;
                this.saving = true;
                this.loadPartner(partner).pipe(
                    switchMap(() => this.saveInvoicingDetails(partner))
                ).subscribe({
                    next: () => {
                        this.saving = false;
                        this.messageService.showSuccess('Customer/ Invoicing details saved successfully.');
                    },
                    error: (error: any) => {
                        this.saving = false;
                        this.messageService.showError(error?.error?.message || 'An error occurred while saving the customer/ invoicing details.');
                    }
                });
            },
            // Dismissing the dialog (Cancel or close) errors the afterClosed stream
            error: () => {}
        });
    }

    /**
     * Show the partner's details, including the identification numbers maintained for the partner
     */
    private loadPartner(partner: any): Observable<void> {
        this.details = {
            companyName: partner.partyName1,
            businessPartnerNumber: partner.partyNumber,
            cinNumber: partner.cinNumber,
            gstNumber: partner.gstNumber,
            pan: partner.pan,
            doorNumber: partner.addressLine1,
            address: partner.addressLine2,
            street: partner.street,
            city: partner.city,
            state: this.states.find((state: any) => state.code === partner.state)?.value ?? partner.state,
            postalCode: partner.postalCode,
            landline: partner.contactNumber,
            mobile: partner.mobileNumber,
            email: partner.email
        };
        return this.businessPartnerSearchService.getBusinessPartnerIdentificationDetails(partner.id).pipe(
            catchError(() => of([])),
            map((identifications: any[]) => {
                (identifications || []).forEach((identification: any) => {
                    const field = IDENTIFICATION_FIELDS[identification.identificationCategoryCode];
                    if (field) {
                        this.details[field] = identification.identificationNumber;
                    }
                });
            })
        );
    }

    /**
     * Save the invoicing details, link the partner to the loan application as its main loan partner
     * and refresh the application fee
     */
    private saveInvoicingDetails(partner: any): Observable<any> {
        const loanApplicationId = this.loanApplication.id;
        const invoicingDetail = {
            ...this.details,
            id: this.invoicingDetail?.id,
            loanApplicationId,
            partnerId: partner.id
        };
        const save$ = this.invoicingDetail
            ? this.applicationFeeService.updateInvoicingDetail(invoicingDetail)
            : this.applicationFeeService.createInvoicingDetail(invoicingDetail);

        return save$.pipe(
            tap((saved: any) => this.invoicingDetail = saved),
            switchMap(() => this.applicationFeeService.updateLoanApplication(loanApplicationId, partner.id)),
            switchMap(() => this.saveMainLoanPartner(partner)),
            switchMap(() => this.applicationFeeService.getApplicationFee(loanApplicationId)),
            tap((applicationFee: any) => this.applicationFeeService.selectedEntity$.next(applicationFee))
        );
    }

    /**
     * Create or update the main loan partner of the loan application
     */
    private saveMainLoanPartner(partner: any): Observable<any> {
        const loanApplicationId = this.loanApplication.id;
        const businessPartnerName = [partner.partyName1, partner.partyName2].filter(Boolean).join(' ');
        return this.applicationFeeService.getLoanPartnersByRoleType(loanApplicationId, MAIN_LOAN_PARTNER_ROLE).pipe(
            catchError(() => of([])),
            switchMap((loanPartners: any[]) => {
                if (loanPartners.length > 0) {
                    return this.applicationFeeService.updateLoanPartner({
                        ...loanPartners[0],
                        businessPartnerId: partner.partyNumber,
                        businessPartnerName
                    });
                }
                return this.applicationFeeService.createLoanPartner({
                    businessPartnerId: partner.partyNumber,
                    businessPartnerName,
                    roleType: MAIN_LOAN_PARTNER_ROLE,
                    roleDescription: 'Main Loan Partner',
                    kycStatus: 'Not Started',
                    loanApplicationId,
                    startDate: this.loanApplication.loanEnquiryDate
                });
            }),
            catchError(() => {
                this.messageService.showWarning('Invoicing details are saved, but the main loan partner of the loan contract could not be updated.');
                return of(null);
            })
        );
    }
}
